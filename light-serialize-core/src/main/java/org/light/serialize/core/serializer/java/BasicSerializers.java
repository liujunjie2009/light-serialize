package org.light.serialize.core.serializer.java;

import org.light.serialize.core.io.ObjectInput;
import org.light.serialize.core.io.ObjectOutput;
import org.light.serialize.core.serializer.Serializer;
import org.light.serialize.core.serializer.SerializerFactory;

import java.io.IOException;

/**
 * Basic serializers.
 *
 * @author alex
 */
public class BasicSerializers {

    /**
     * The serializer for byte.
     */
    public static final Serializer<Byte> BYTE_SERIALIZER = new Serializer<Byte>(Byte.TYPE) {

        @Override
        public void write(ObjectOutput output, Byte value) throws IOException {
            output.writeByte(value);
        }

        @Override
        public Byte read(ObjectInput input) throws IOException {
            return input.readByte();
        }
    };

    /**
     * The serializer for byte wrapper.
     */
    public static final Serializer<Byte> BYTE_WRAPPER_SERIALIZER = new Serializer<Byte>(Byte.class) {

        @Override
        public void write(ObjectOutput output, Byte value) throws IOException {
            output.writeIntObject(value == null ? null : value.intValue());
        }

        @Override
        public Byte read(ObjectInput input) throws IOException {
            Integer value = (Integer) input.readObject();
            return value == null ? null : value.byteValue();
        }
    };

    /**
     * The serializer for boolean.
     */
    public static final Serializer<Boolean> BOOL_SERIALIZER = new  Serializer<Boolean>(Boolean.TYPE) {

        @Override
        public void write(ObjectOutput output, Boolean value) throws IOException {
            output.writeByte(value ? 1 : 0);
        }

        @Override
        public Boolean read(ObjectInput input) throws IOException {
            return input.readByte() == 1;
        }
    };

    /**
     * The serializer for boolean wrapper.
     */
    public static final Serializer<Boolean> BOOL_WRAPPER_SERIALIZER = new Serializer<Boolean>(Boolean.class) {

        @Override
        public void write(ObjectOutput output, Boolean value) throws IOException {
            if (value == null) {
                output.writeByte(0);
            } else {
                output.writeByte(value ? 2 : 1);
            }
        }

        @Override
        public Boolean read(ObjectInput input) throws IOException {
            int value = input.readByte();
            return value == 0 ? null : value == 2;
        }
    };

    /**
     * The serializer for char.
     */
    public static final Serializer<Character> CHAR_SERIALIZER = new Serializer<Character>(Character.TYPE) {

        @Override
        public void write(ObjectOutput output, Character value) throws IOException {
            output.writeVarInt(value);
        }

        @Override
        public Character read(ObjectInput input) throws IOException {
            return (char) input.readVarInt();
        }
    };

    /**
     * The serializer for char wrapper.
     */
    public static final Serializer<Character> CHAR_WRAPPER_SERIALIZER = new Serializer<Character>(Character.class) {

        @Override
        public void write(ObjectOutput output, Character value) throws IOException {
            if (value == null) {
                output.writeObject(null);
            } else {
                output.writeIntObject((int) value);
            }
        }

        @Override
        public Character read(ObjectInput input) throws IOException {
            Integer value = (Integer) input.readObject();
            return value == null ? null : (char) value.intValue();
        }
    };

    /**
     * The serializer for short.
     */
    public static final Serializer<Short> SHORT_SERIALIZER = new Serializer<Short>(Short.TYPE) {

        @Override
        public void write(ObjectOutput output, Short value) throws IOException {
            output.writeZigzagVarInt(value);
        }

        @Override
        public Short read(ObjectInput input) throws IOException {
            return (short) input.readZigzagVarInt();
        }
    };

    /**
     * The serializer for short wrapper.
     */
    public static final Serializer<Short> SHORT_WRAPPER_SERIALIZER = new Serializer<Short>(Short.class) {

        @Override
        public void write(ObjectOutput output, Short value) throws IOException {
            if (value == null) {
                output.writeObject(null);
            } else {
                output.writeIntObject(value.intValue());
            }
        }

        @Override
        public Short read(ObjectInput input) throws IOException {
            Integer value = (Integer) input.readObject();
            return value == null ? null : value.shortValue();
        }
    };

    /**
     * The serializer for int.
     */
    public static final Serializer<Integer> INT_SERIALIZER = new Serializer<Integer>(Integer.TYPE) {

        @Override
        public void write(ObjectOutput output, Integer value) throws IOException {
            output.writeIntObject(value);
        }

        @Override
        public Integer read(ObjectInput input) throws IOException {
            return (Integer) input.readObject();
        }
    };

    /**
     * The serializer for int wrapper.
     */
    public static final Serializer<Integer> INT_WRAPPER_SERIALIZER = new Serializer<Integer>(Integer.class) {

        @Override
        public void write(ObjectOutput output, Integer value) throws IOException {
            if (value == null) {
                output.writeObject(null);
            } else {
                output.writeIntObject(value.intValue());
            }
        }

        @Override
        public Integer read(ObjectInput input) throws IOException {
            Integer value = (Integer) input.readObject();
            return value == null ? null : value;
        }
    };

    /**
     * The serializer for long.
     */
    public static final Serializer<Long> LONG_SERIALIZER = new Serializer<Long>(Long.TYPE) {

        @Override
        public void write(ObjectOutput output, Long value) throws IOException {
            if (value >= Integer.MIN_VALUE && value <= Integer.MAX_VALUE) {
                output.writeIntObject(value.intValue());
            } else {
                output.writeLongObject(value);
            }
        }

        @Override
        public Long read(ObjectInput input) throws IOException {
            return ((Number) input.readObject()).longValue();
        }
    };

    /**
     * The serializer for long wrapper.
     */
    public static final Serializer<Long> LONG_WRAPPER_SERIALIZER = new Serializer<Long>(Long.class) {

        @Override
        public void write(ObjectOutput output, Long value) throws IOException {
            if (value == null) {
                output.writeObject(null);
            } else if (value >= Integer.MIN_VALUE && value <= Integer.MAX_VALUE) {
                output.writeIntObject(value.intValue());
            } else {
                output.writeLongObject(value.longValue());
            }
        }

        @Override
        public Long read(ObjectInput input) throws IOException {
            Number readObject = (Number) input.readObject();
            return readObject == null ? null : readObject.longValue();
        }

    };

    /**
     * The serializer for float.
     */
    public static final Serializer<Float> FLOAT_SERIALIZER = new Serializer<Float>(Float.TYPE) {

        @Override
        public void write(ObjectOutput output, Float value) throws IOException {
            output.writeFloatObject(value);
        }

        @Override
        public Float read(ObjectInput input) throws IOException {
            return (Float) input.readObject();
        }
    };

    /**
     * The serializer for float wrapper.
     */
    public static final Serializer<Float> FLOAT_WRAPPER_SERIALIZER = new Serializer<Float>(Float.class) {

        @Override
        public void write(ObjectOutput output, Float value) throws IOException {
            if (value == null) {
                output.writeObject(null);
            } else {
                output.writeFloatObject(value.floatValue());
            }
        }

        @Override
        public Float read(ObjectInput input) throws IOException {
            Float readObject = (Float) input.readObject();
            return readObject == null ? null : readObject.floatValue();
        }
    };

    /**
     * The serializer for double.
     */
    public static final Serializer<Double> DOUBLE_SERIALIZER = new Serializer<Double>(Double.TYPE) {

        @Override
        public void write(ObjectOutput output, Double value) throws IOException {
            output.writeDoubleObject(value.doubleValue());
        }

        @Override
        public Double read(ObjectInput input) throws IOException {
            return (Double) input.readObject();
        }
    };

    /**
     * The serializer for double wrapper.
     */
    public static final Serializer<Double> DOUBLE_WRAPPER_SERIALIZER = new Serializer<Double>(Double.class) {

        @Override
        public void write(ObjectOutput output, Double value) throws IOException {
            if (value == null) {
                output.writeObject(null);
            } else {
                output.writeDoubleObject(value.doubleValue());
            }
        }

        @Override
        public Double read(ObjectInput input) throws IOException {
            Double readObject = (Double) input.readObject();
            return readObject == null ? null : readObject.doubleValue();
        }
    };

    /**
     * The serializer for String.
     */
    public static final Serializer<String> STRING_SERIALIZER = new Serializer<String>(String.class) {

        @Override
        public void write(ObjectOutput output, String value) throws IOException {
            output.writeString(value);
        }

        @Override
        public String read(ObjectInput input) throws IOException {
            return input.readString();
        }
    };

    /**
     * The serializer for StringBuilder.
     */
    public static final Serializer<StringBuilder> STRING_BUILDER_SERIALIZER = new Serializer<StringBuilder>(StringBuilder.class) {

        @Override
        public void write(ObjectOutput output, StringBuilder value) throws IOException {
            output.writeString(value.toString());
        }

        @Override
        public StringBuilder read(ObjectInput input) throws IOException {
            return new StringBuilder(input.readString());
        }
    };

    /**
     * The serializer for StringBuffer.
     */
    public static final Serializer<StringBuffer> STRING_BUFFER_SERIALIZER = new Serializer<StringBuffer>(StringBuffer.class) {

        @Override
        public void write(ObjectOutput output, StringBuffer value) throws IOException {
            output.writeString(value.toString());
        }

        @Override
        public StringBuffer read(ObjectInput input) throws IOException {
            return new StringBuffer(input.readString());
        }
    };

    /**
     * The serializer for void.
     */
    public static final Serializer<Void> VOID_SERIALIZER = new Serializer<Void>(Void.TYPE) {

        @Override
        public void write(ObjectOutput output, Void value) throws IOException {
        }

        @Override
        public Void read(ObjectInput input) throws IOException {
            return null;
        }
    };

    /**
     * The serializer for void wrapper.
     */
    public static final Serializer<Void> VOID_WRAPPER_SERIALIZER = new Serializer<Void>(Void.class) {

        @Override
        public void write(ObjectOutput output, Void value) throws IOException {
        }

        @Override
        public Void read(ObjectInput input) throws IOException {
            return null;
        }
    };

    /**
     * The serializer for Object.
     */
    public static final Serializer<Object> OBJECT_SERIALIZER = new Serializer(Object.class) {

        @Override
        public void write(ObjectOutput output, Object value) throws IOException {
            output.writeByte(value == null ? 0 : 1);
        }

        @Override
        public Object read(ObjectInput input) throws IOException {
            return input.readByte() == 0 ? null : new Object();
        }
    };

    public static void register(SerializerFactory factory) {
        factory.register(BYTE_SERIALIZER);
        factory.register(BYTE_WRAPPER_SERIALIZER);
        factory.register(BOOL_SERIALIZER);
        factory.register(BOOL_WRAPPER_SERIALIZER);
        factory.register(CHAR_SERIALIZER);
        factory.register(CHAR_WRAPPER_SERIALIZER);
        factory.register(SHORT_SERIALIZER);
        factory.register(SHORT_WRAPPER_SERIALIZER);
        factory.register(INT_SERIALIZER);
        factory.register(INT_WRAPPER_SERIALIZER);
        factory.register(LONG_SERIALIZER);
        factory.register(LONG_WRAPPER_SERIALIZER);
        factory.register(FLOAT_SERIALIZER);
        factory.register(FLOAT_WRAPPER_SERIALIZER);
        factory.register(DOUBLE_SERIALIZER);
        factory.register(DOUBLE_WRAPPER_SERIALIZER);
        factory.register(STRING_SERIALIZER);
        factory.register(STRING_BUILDER_SERIALIZER);
        factory.register(STRING_BUFFER_SERIALIZER);
        factory.register(VOID_SERIALIZER);
        factory.register(VOID_WRAPPER_SERIALIZER);
        factory.register(OBJECT_SERIALIZER);
    }
}
