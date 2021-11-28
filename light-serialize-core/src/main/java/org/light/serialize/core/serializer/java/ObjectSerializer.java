package org.light.serialize.core.serializer.java;

import org.light.serialize.core.constants.Constants;
import org.light.serialize.core.constants.Strategy;
import org.light.serialize.core.instantiator.ObjectInstantiator;
import org.light.serialize.core.instantiator.sun.UnSafeInstantiator;
import org.light.serialize.core.io.ObjectInput;
import org.light.serialize.core.io.ObjectOutput;
import org.light.serialize.core.io.ReadContext;
import org.light.serialize.core.io.WriteContext;
import org.light.serialize.core.serializer.Serializer;
import org.light.serialize.core.util.UnsafeUtil;
import sun.misc.Unsafe;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.security.AccessControlException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * ObjectSerializer.
 *
 * @author alex
 */
public class ObjectSerializer<T> extends Serializer<T> {

    protected static final Unsafe unsafe = UnsafeUtil.getUnsafe();

    private final ObjectInstantiator<T> instantiator;

    private FieldSerializer[] exactFieldSerializers;
    private FieldSerializer[][] orderHierarchyFieldSerializers;
    private HashMap<String, FieldSerializer>[] nameHierarchyFieldSerializers;

    public ObjectSerializer(Class<?> type) {
        super(type);
        this.instantiator = new UnSafeInstantiator(type);
        initFieldSerializers();
    }

    private void initFieldSerializers() {
        List<FieldSerializer> exactFieldSerializerList = new ArrayList<>(64);
        List<FieldSerializer[]> orderFieldSerializersList = new ArrayList<>(8);
        List<HashMap<String, FieldSerializer>> nameFieldSerializersList = new ArrayList<>(8);

        Class<?> nextClass = this.type;

        while (nextClass != Object.class && nextClass != null) {
            List<FieldSerializer> orderFieldSerializers = new ArrayList<>(16);
            HashMap<String, FieldSerializer> nameFieldSerializers = new HashMap<>(16);

            for (Field field : nextClass.getDeclaredFields()) {
                int modifiers = field.getModifiers();
                if (Modifier.isStatic(modifiers) || Modifier.isTransient(modifiers)) {
                    continue;
                }

                if (!field.isAccessible()) {
                    try {
                        field.setAccessible(true);
                    } catch (AccessControlException e) {
                        throw new RuntimeException(e);
                    }
                }

                FieldSerializer fieldSerializer = newFieldSerializer(field);
                exactFieldSerializerList.add(fieldSerializer);
                orderFieldSerializers.add(fieldSerializer);
                nameFieldSerializers.put(fieldSerializer.name, fieldSerializer);
            }

            orderFieldSerializersList.add(orderFieldSerializers.toArray(new FieldSerializer[0]));
            nameFieldSerializersList.add(nameFieldSerializers);
            nextClass = nextClass.getSuperclass();
        }

        this.exactFieldSerializers = exactFieldSerializerList.toArray(new FieldSerializer[0]);
        this.orderHierarchyFieldSerializers = orderFieldSerializersList.toArray(new FieldSerializer[0][]);
        this.nameHierarchyFieldSerializers = nameFieldSerializersList.toArray(new HashMap[0]);
    }

    /**
     * new field serializer
     */
    private static FieldSerializer newFieldSerializer(Field field) {
        Class<?> type = field.getType();

        if (type.equals(Boolean.TYPE)) {
            return new BooleanFieldSerializer(field);
        } else if (type.equals(Byte.TYPE)) {
            return new ByteFieldSerializer(field);
        } else if (type.equals(Short.TYPE)) {
            return new ShortFieldSerializer(field);
        } else if (type.equals(Character.TYPE)) {
            return new CharFieldSerializer(field);
        } else if (type.equals(Integer.TYPE)) {
            return new IntFieldSerializer(field);
        } else if (type.equals(Long.TYPE)) {
            return new LongFieldSerializer(field);
        } else if (type.equals(Float.TYPE)) {
            return new FloatFieldSerializer(field);
        } else if (type.equals(Double.TYPE)) {
            return new DoubleFieldSerializer(field);
        } else if (type.equals(String.class)) {
            return new StringFieldSerializer(field);
        }

        return new ObjectFieldSerializer(field);
    }

    @Override
    public void write(ObjectOutput output, T value) throws IOException {
        Strategy strategy = WriteContext.get().getStrategy();
        switch (strategy) {
            case NAME:
                writeForName(output, value);
                break;
            case ORDER:
                writeForOrder(output, value);
                break;
            case EXACT:
                writeForExact(output, value);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + strategy);
        }

    }

    /**
     * write for {@link Strategy#NAME}
     */
    private void writeForName(ObjectOutput output, T value) throws IOException {
        WriteContext writeContext = WriteContext.get();
        int fieldNameTypeIndex = writeContext.getFieldNameTypeIndex(type);

        /*
         * write field name
         */
        if (fieldNameTypeIndex == Constants.NULL_INDEX) {
            // 0: field names and values
            output.writeByte(0);

            FieldSerializer[][] orderHierarchyFieldSerializers = this.orderHierarchyFieldSerializers;
            int depth = orderHierarchyFieldSerializers.length;

            output.writeVarInt(depth);

            for (int i = 0; i < depth; i++) {
                FieldSerializer[] fieldSerializers = orderHierarchyFieldSerializers[i];
                int fieldSize = fieldSerializers.length;
                output.writeVarInt(fieldSize);

                for (int j = 0; j < fieldSize; j++) {
                    output.writeString(fieldSerializers[j].name);
                }
            }
        } else {
            // field names type index and values
            output.writeVarInt(fieldNameTypeIndex);
        }

        /*
         * write field value
         */
        FieldSerializer[] serializers = this.exactFieldSerializers;
        int fieldSize = serializers.length;

        if (fieldSize == 0) {
            return;
        }

        for (int i = 0; i < fieldSize; i++) {
            // write field value
            serializers[i].write(output, value);
        }
    }

    /**
     * write for {@link Strategy#ORDER}
     */
    private void writeForOrder(ObjectOutput output, T value) throws IOException {
        FieldSerializer[][] orderHierarchyFieldSerializers = this.orderHierarchyFieldSerializers;
        int depth = orderHierarchyFieldSerializers.length;
        // write class depth
        output.writeVarInt(depth);

        for (int i = 0; i < depth; i++) {
            FieldSerializer[] fieldSerializers = orderHierarchyFieldSerializers[i];
            int fieldSize = fieldSerializers.length;
            // write field size
            output.writeVarInt(fieldSize);

            if (fieldSize == 0) {
                continue;
            }

            for (int j = 0; j < fieldSize; j++) {
                // write field value
                FieldSerializer serializer = fieldSerializers[j];
                Class<?> type = serializer.field.getType();
                long offset = serializer.offset;

                if (type.equals(Boolean.TYPE)) {
                    output.writeBool(unsafe.getBoolean(value, offset));
                } else if (type.equals(Byte.TYPE)) {
                    output.writeByte(unsafe.getByte(value, offset));
                } else if (type.equals(Short.TYPE)) {
                    output.writeZigzagVarInt(unsafe.getShort(value, offset));
                } else if (type.equals(Character.TYPE)) {
                    output.writeChar(unsafe.getChar(value, offset));
                } else if (type.equals(Integer.TYPE)) {
                    output.writeZigzagVarInt(unsafe.getInt(value, offset));
                } else if (type.equals(Long.TYPE)) {
                    output.writeZigzagVarLong(unsafe.getLong(value, offset));
                } else if (type.equals(Float.TYPE)) {
                    output.writeFloat(unsafe.getFloat(value, offset));
                } else if (type.equals(Double.TYPE)) {
                    output.writeDouble(unsafe.getDouble(value, offset));
                } else if (type.equals(String.class)) {
                    output.writeString((String) unsafe.getObject(value, offset));
                } else {
                    output.writeObject(unsafe.getObject(value, offset));
                }
            }
        }

    }

    /**
     * write for {@link Strategy#EXACT}
     */
    private void writeForExact(ObjectOutput output, T value) throws IOException {
        FieldSerializer[] serializers = this.exactFieldSerializers;
        int fieldSize = serializers.length;

        if (fieldSize == 0) {
            return;
        }

        for (int i = 0; i < fieldSize; i++) {
            // write field value
            FieldSerializer serializer = serializers[i];
            Class<?> type = serializer.field.getType();
//            serializer.write(output, value);

            long offset = serializer.offset;
            if (type.equals(Boolean.TYPE)) {
                output.writeBool(unsafe.getBoolean(value, offset));
            } else if (type.equals(Byte.TYPE)) {
                output.writeByte(unsafe.getByte(value, offset));
            } else if (type.equals(Short.TYPE)) {
                output.writeZigzagVarInt(unsafe.getShort(value, offset));
            } else if (type.equals(Character.TYPE)) {
                output.writeChar(unsafe.getChar(value, offset));
            } else if (type.equals(Integer.TYPE)) {
                output.writeZigzagVarInt(unsafe.getInt(value, offset));
            } else if (type.equals(Long.TYPE)) {
                output.writeZigzagVarLong(unsafe.getLong(value, offset));
            } else if (type.equals(Float.TYPE)) {
                output.writeFloat(unsafe.getFloat(value, offset));
            } else if (type.equals(Double.TYPE)) {
                output.writeDouble(unsafe.getDouble(value, offset));
            } else if (type.equals(String.class)) {
                output.writeString((String) unsafe.getObject(value, offset));
            } else {
                output.writeObject(unsafe.getObject(value, offset));
//                Object fieldVal = unsafe.getObject(value, offset);
//                Serializer s = WriteContext.get().getSerializerFactory().getSerializer(fieldVal.getClass());
//                s.write(output, fieldVal);
            }
        }


    }

    @Override
    public T read(ObjectInput input) throws IOException {
        ReadContext readContext = ReadContext.get();
        Strategy strategy = readContext.getStrategy();
        T target;

        try {
            target = instantiator.newInstance();
            readContext.putReferenceObject(target);

            switch (strategy) {
                case NAME:
                    readForName(input, target);
                    break;
                case ORDER:
                    readForOrder(input, target);
                    break;
                case EXACT:
                    readForExact(input, target);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + strategy);
            }

            return target;
        } catch (IOException e) {
            throw e;
        } catch (Throwable e) {
            throw new IOException(e);
        }
    }

    /**
     * readForName
     */
    private void readForName(ObjectInput input, Object target) throws IOException {
        HashMap<String, FieldSerializer>[] nameFieldSerializersList = this.nameHierarchyFieldSerializers;
        ReadContext readContext = ReadContext.get();
        int localDepth = nameFieldSerializersList.length;
        FieldSerializer[] targetFieldSerializers;
        int typeIndex = input.readVarInt();

        /*
         * read field names and get target field serializers
         */
        if (typeIndex == 0) {
            targetFieldSerializers = new FieldSerializer[exactFieldSerializers.length];
            int readDepth = input.readVarInt();

            for (int j = 0; j < readDepth; j++) {
                int fieldSize = input.readVarInt();
                for (int i = 0; i < fieldSize; i++) {
                    String fieldName = input.readString();
                    FieldSerializer serializer = null;
                    HashMap<String, FieldSerializer> entries = nameFieldSerializersList[j];
//                    FieldSerializer serializer = entries.get(fieldName);

                    // find field serializer from local field serializers by field name
                    for (int localHierarchy = j; localHierarchy < localDepth + j; localHierarchy++) {
                        HashMap<String, FieldSerializer> fieldSerializers = nameFieldSerializersList[localHierarchy % localDepth];
                        if ((serializer = fieldSerializers.get(fieldName)) != null) {
                            break;
                        }
                    }

//                    targetFieldSerializers.add(serializer == null ? NoneFieldSerializer.instance : serializer);
                    targetFieldSerializers[j * entries.size() + i] = (serializer == null ? NoneFieldSerializer.instance : serializer);
                }
            }

            readContext.addReferenceFieldSerializers(targetFieldSerializers);
        } else {
            targetFieldSerializers = (FieldSerializer[]) readContext.getReferenceFieldSerializers(typeIndex);
        }

        /*
         * read field value
         */
        for (int i = 0; i < targetFieldSerializers.length; i++) {
//            System.out.printf(Arrays.toString(targetFieldSerializers));
            FieldSerializer targetFieldSerializer = targetFieldSerializers[i];
//            System.out.println(targetFieldSerializer + "");
            if (targetFieldSerializer != null) {
                targetFieldSerializer.read(input, target);
            }

        }
    }

    /**
     * readForOrder
     */
    private void readForOrder(ObjectInput input, Object target) throws IOException {
        FieldSerializer[][] orderHierarchyFieldSerializers = this.orderHierarchyFieldSerializers;
        int localDepth = orderHierarchyFieldSerializers.length;
        int readDepth = input.readVarInt();

        for (int readHierarchy = 0; readHierarchy < readDepth; readHierarchy++) {
            int readFieldSize = input.readVarInt();
            for (int i = 0; i < readFieldSize; i++) {
                if (readHierarchy >= localDepth) {
                    input.readObject();
                    continue;
                }

                FieldSerializer[] orderFieldSerializers = orderHierarchyFieldSerializers[readHierarchy];
                int fieldSize = orderFieldSerializers.length;

                if(i >= fieldSize) {
                    input.readObject();
                    continue;
                }

//                orderFieldSerializers[i].read(input, target);
                FieldSerializer serializer = orderFieldSerializers[i];
                Class<?> type = serializer.field.getType();
                long offset = serializer.offset;

                if (type.equals(Boolean.TYPE)) {
                    unsafe.putBoolean(target, offset, input.readBool());
                } else if (type.equals(Byte.TYPE)) {
                    unsafe.putByte(target, offset, input.readByte());
                } else if (type.equals(Short.TYPE)) {
                    unsafe.putShort(target, offset, (short) input.readZigzagVarInt());
                } else if (type.equals(Character.TYPE)) {
                    unsafe.putChar(target, offset, input.readChar());
                } else if (type.equals(Integer.TYPE)) {
                    unsafe.putInt(target, offset, input.readZigzagVarInt());
                } else if (type.equals(Long.TYPE)) {
                    unsafe.putLong(target, offset, input.readZigzagVarLong());
                } else if (type.equals(Float.TYPE)) {
                    unsafe.putFloat(target, offset, input.readFloat());
                } else if (type.equals(Double.TYPE)) {
                    unsafe.putDouble(target, offset, input.readDouble());
                } else if (type.equals(String.class)) {
                    unsafe.putObject(target, offset, input.readString());
                } else {
                    Object o1 = input.readObject();
                    unsafe.putObject(target, offset, o1);
                }

            }
        }

    }

    /**
     * readForExact
     */
    private void readForExact(ObjectInput input, Object target) throws IOException {
        FieldSerializer[] exactFieldSerializers = this.exactFieldSerializers;
        int length = exactFieldSerializers.length;

        for (int i = 0; i < length; i++) {
            exactFieldSerializers[i].read(input, target);
        }
    }

}

abstract class FieldSerializer {

    protected static final Unsafe unsafe = UnsafeUtil.getUnsafe();

    public final String name;
    public final Field field;
    public final long offset;

    public FieldSerializer(Field field) {
        this.name = field.getName();
        this.field = field;
        this.offset = unsafe.objectFieldOffset(field);
    }

    public FieldSerializer() {
        this.name = null;
        this.field = null;
        this.offset = -1;
    }

    public abstract void write(ObjectOutput output, Object target) throws IOException;

    public abstract void read(ObjectInput input, Object target) throws IOException;

}

class BooleanFieldSerializer extends FieldSerializer {

    public BooleanFieldSerializer(Field field) {
        super(field);
    }

    @Override
    public void write(ObjectOutput output, Object target) throws IOException {
        output.writeObject(unsafe.getBoolean(target, offset));
    }

    @Override
    public void read(ObjectInput input, Object target) throws IOException {
        unsafe.putBoolean(target, offset, (boolean) input.readObject());
    }
}

class ByteFieldSerializer extends FieldSerializer {

    public ByteFieldSerializer(Field field) {
        super(field);
    }

    @Override
    public void write(ObjectOutput output, Object target) throws IOException {
        int val = unsafe.getByte(target, offset);
        output.writeObject(val);
    }

    @Override
    public void read(ObjectInput input, Object target) throws IOException {
        unsafe.putByte(target, offset, ((Number) input.readObject()).byteValue());
    }
}

class CharFieldSerializer extends FieldSerializer {

    public CharFieldSerializer(Field field) {
        super(field);
    }

    @Override
    public void write(ObjectOutput output, Object target) throws IOException {
        int val = unsafe.getChar(target, offset);
        output.writeObject(val);
    }

    @Override
    public void read(ObjectInput input, Object target) throws IOException {
        int val = (int) input.readObject();
        unsafe.putChar(target, offset, (char) val);
    }
}

class ShortFieldSerializer extends FieldSerializer {

    public ShortFieldSerializer(Field field) {
        super(field);
    }

    @Override
    public void write(ObjectOutput output, Object target) throws IOException {
        int val = unsafe.getShort(target, offset);
        output.writeObject(val);
    }

    @Override
    public void read(ObjectInput input, Object target) throws IOException {
        unsafe.putShort(target, offset, ((Number) input.readObject()).shortValue());
    }
}

class IntFieldSerializer extends FieldSerializer {

    public IntFieldSerializer(Field field) {
        super(field);
    }

    @Override
    public void write(ObjectOutput output, Object target) throws IOException {
        output.writeObject(unsafe.getInt(target, offset));
    }

    @Override
    public void read(ObjectInput input, Object target) throws IOException {
        unsafe.putInt(target, offset, (int) input.readObject());
    }
}

class LongFieldSerializer extends FieldSerializer {

    public LongFieldSerializer(Field field) {
        super(field);
    }

    @Override
    public void write(ObjectOutput output, Object target) throws IOException {
        output.writeObject(unsafe.getLong(target, offset));
    }

    @Override
    public void read(ObjectInput input, Object target) throws IOException {
        unsafe.putLong(target, offset, (long) input.readObject());
    }
}

class FloatFieldSerializer extends FieldSerializer {

    public FloatFieldSerializer(Field field) {
        super(field);
    }

    @Override
    public void write(ObjectOutput output, Object target) throws IOException {
        output.writeObject(unsafe.getFloat(target, offset));
    }

    @Override
    public void read(ObjectInput input, Object target) throws IOException {
        unsafe.putFloat(target, offset, (float) input.readObject());
    }
}

class DoubleFieldSerializer extends FieldSerializer {

    public DoubleFieldSerializer(Field field) {
        super(field);
    }

    @Override
    public void write(ObjectOutput output, Object target) throws IOException {
        output.writeObject(unsafe.getDouble(target, offset));
    }

    @Override
    public void read(ObjectInput input, Object target) throws IOException {
        unsafe.putDouble(target, offset, (double) input.readObject());
    }
}

class ObjectFieldSerializer extends FieldSerializer {

    public ObjectFieldSerializer(Field field) {
        super(field);
    }

    @Override
    public void write(ObjectOutput output, Object target) throws IOException {
        output.writeObject(unsafe.getObject(target, offset));
    }

    @Override
    public void read(ObjectInput input, Object target) throws IOException {
        unsafe.putObject(target, offset, input.readObject());
    }
}

class StringFieldSerializer extends ObjectFieldSerializer {

    public StringFieldSerializer(Field field) {
        super(field);
    }

}

class NoneFieldSerializer extends FieldSerializer {

    public static final NoneFieldSerializer instance = new NoneFieldSerializer();

    @Override
    public void write(ObjectOutput output, Object target) throws IOException {
        // do nothing
    }

    @Override
    public void read(ObjectInput input, Object target) throws IOException {
        input.readObject();
    }

}
