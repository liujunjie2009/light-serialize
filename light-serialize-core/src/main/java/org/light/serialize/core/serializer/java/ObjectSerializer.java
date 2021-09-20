package org.light.serialize.core.serializer.java;

import org.light.serialize.core.constants.Strategy;
import org.light.serialize.core.instantiator.ObjectInstantiator;
import org.light.serialize.core.instantiator.sun.UnSafeInstantiator;
import org.light.serialize.core.io.ObjectInput;
import org.light.serialize.core.io.ObjectOutput;
import org.light.serialize.core.io.ReadContext;
import org.light.serialize.core.io.WriteContext;
import org.light.serialize.core.serializer.Serializer;
import org.light.serialize.core.util.BufferUtil;
import org.light.serialize.core.util.UnsafeUtil;
import sun.misc.Unsafe;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.security.AccessControlException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ObjectSerializer.
 *
 * @author alex
 */
public class ObjectSerializer<T> extends Serializer<T> {

    private final ObjectInstantiator<T> instantiator;

    private final List<FieldSerializer> exactFieldSerializers = new ArrayList<>(32);
    private final List<List<FieldSerializer>> orderFieldSerializersList = new ArrayList<>(8);
    private final List<Map<String, FieldSerializer>> nameFieldSerializersList = new ArrayList<>(8);

    public ObjectSerializer(Class<?> type) {
        super(type);
        this.instantiator = new UnSafeInstantiator(type);
        initFieldSerializers();
    }

    private void initFieldSerializers() {
        Class<?> nextClass = this.type;

        while (nextClass != Object.class && nextClass != null) {
            List<FieldSerializer> orderFieldSerializers = new ArrayList<>(16);
            Map<String, FieldSerializer> nameFieldSerializers = new HashMap<>(16);

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

                FieldSerializer fieldSerializer = fieldSerializer(field);
                exactFieldSerializers.add(fieldSerializer);
                orderFieldSerializers.add(fieldSerializer);
                nameFieldSerializers.put(fieldSerializer.name, fieldSerializer);
            }

            orderFieldSerializersList.add(orderFieldSerializers);
            nameFieldSerializersList.add(nameFieldSerializers);
            nextClass = nextClass.getSuperclass();
        }
    }

    /**
     * get field serializer
     */
    private static FieldSerializer fieldSerializer(Field field) {
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

    /**
     * write field name
     */
    private static void writeFieldName(String fieldName, ObjectOutput output) {
        WriteContext writeContext = WriteContext.get();
        Integer fieldNameIndex = writeContext.getFieldNameIndex(fieldName);

        /*
         * The last bit mask of tag represent field index(bit:1) or name(bit:0).
         */
        if (fieldNameIndex == null) {
            int length = fieldName.length();
            output.writeVarInt(length << 1);
            for (int i = 0; i < length; i++) {
                output.writeUtf8Char(fieldName.charAt(i));
            }
            writeContext.putFieldName(fieldName);
        } else {
            output.writeVarInt((fieldNameIndex << 1) | 1);
        }
    }

    /**
     * read field name
     */
    private static String readFieldName(ObjectInput input) {
        ReadContext readContext = ReadContext.get();
        int tag = input.readVarInt();

        // field name index
        if ((tag & 1) == 1) {
            return readContext.getFieldName(tag >>> 1);
        } else {
            int length = tag >>> 1;
            char[] chars = new char[length];
            for (int i = 0; i < length; i++) {
                chars[i] = input.readUtf8Char();
            }
            return new String(chars);
        }
    }

    @Override
    public void write(ObjectOutput output, T value) throws IOException {
        WriteContext writeContext = WriteContext.get();

        try {
            Strategy strategy = writeContext.getStrategy();

            if (strategy == Strategy.NAME) {
                List<Map<String, FieldSerializer>> nameFieldSerializersList = this.nameFieldSerializersList;
                int endIndex = nameFieldSerializersList.size() - 1;
                for (int i = 0; i <= endIndex; i++) {
                    Map<String, FieldSerializer> fieldSerializers = nameFieldSerializersList.get(i);
                    int fieldSize = fieldSerializers.size();
                    output.writeVarInt(i == endIndex ? (fieldSize << 1) | 1 : (fieldSize << 1));

                    for (Map.Entry<String, FieldSerializer> fieldSerializerEtr : fieldSerializers.entrySet()) {
                        writeFieldName(fieldSerializerEtr.getKey(), output);
                        fieldSerializerEtr.getValue().write(output, value);
                    }
                }

                return;
            }

            if (strategy == Strategy.ORDER) {
                List<List<FieldSerializer>> orderFieldSerializersList = this.orderFieldSerializersList;
                int endIndex = orderFieldSerializersList.size() - 1;
                for (int i = 0; i <= endIndex; i++) {
                    List<FieldSerializer> fieldSerializers = orderFieldSerializersList.get(i);
                    int fieldSize = fieldSerializers.size();
                    output.writeVarInt(i == endIndex ? (fieldSize << 1) | 1 : (fieldSize << 1));
                    for (int j = 0; j < fieldSerializers.size(); j++) {
                        fieldSerializers.get(j).write(output, value);
                    }
                }

                return;
            }

            if (strategy == Strategy.EXACT) {
                List<FieldSerializer> fieldSerializerList = this.exactFieldSerializers;
                int fieldSize = fieldSerializerList.size();
                if (fieldSize == 0) {
                    return;
                }

                for (int index = 0; index < fieldSize; index++) {
                    fieldSerializerList.get(index).write(output, value);
                }
                return;
            }

        } catch (IOException e) {
            throw e;
        } catch (Throwable e) {
            throw new IOException(e);
        }
    }

    @Override
    public T read(ObjectInput input) throws IOException {
        ReadContext readContext = ReadContext.get();
        Strategy strategy = readContext.getStrategy();
        T object = null;

        try {
            object = instantiator.newInstance();
            readContext.putReferenceObject(object);

            if (strategy == Strategy.NAME) {
                List<Map<String, FieldSerializer>> nameFieldSerializersList = this.nameFieldSerializersList;
                int depth = nameFieldSerializersList.size();
                int from = 0;

                for (; ; ) {
                    int readFieldSizeTag = input.readVarInt();
                    int readFieldSize = readFieldSizeTag >>> 1;

                    // TODO: bug?
                    if (from >= depth) {
                        return object;
                    }

                    for (int i = 0; i < readFieldSize; i++) {
                        String fieldName = readFieldName(input);
                        scan: {
                            for (int j = from; j < depth + from; j++) {
                                Map<String, FieldSerializer> fieldSerializers = nameFieldSerializersList.get(j % depth);
                                FieldSerializer fieldSerializer = fieldSerializers.get(fieldName);

                                if (fieldSerializer != null) {
                                    fieldSerializer.read(input, object);
                                    break scan;
                                }
                            }

                            input.readObject();
                        }
                    }

                    // TODO: bug?
                    // end?
                    if ((readFieldSizeTag & 1) == 1) {
                        return object;
                    }

                    from++;
                }
            }

            if (strategy == Strategy.ORDER) {
                List<List<FieldSerializer>> orderFieldSerializersList = this.orderFieldSerializersList;
                int depth = orderFieldSerializersList.size();
                int from = 0;

                for (; ; ) {
                    int readFieldSizeTag = input.readVarInt();
                    int readFieldSize = readFieldSizeTag >>> 1;

                    if (from >= depth) {
                        return object;
                    }

                    List<FieldSerializer> orderFieldSerializers = orderFieldSerializersList.get(from);
                    int fieldSize = orderFieldSerializers.size();

                    for (int i = 0; i < readFieldSize; i++) {
                        if (i < fieldSize) {
                            orderFieldSerializers.get(i).read(input, object);
                        } else {
                            input.readObject();
                        }
                    }


                    // end?
                    if ((readFieldSizeTag & 1) == 1) {
                        return object;
                    }

                    from++;
                }
            }

            if (strategy == Strategy.EXACT) {
                List<FieldSerializer> exactFieldSerializers = this.exactFieldSerializers;
                for (int i = 0; i < exactFieldSerializers.size(); i++) {
                    exactFieldSerializers.get(i).read(input, object);
                }

                return object;
            }

            return object;
        } catch (IOException e) {
            throw e;
        } catch (Throwable e) {
            throw new IOException(e);
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
        unsafe.putBoolean(target, offset, input.readBool());
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


