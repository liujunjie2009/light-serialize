package org.light.serialize.core.serializer.java;

import org.light.serialize.core.io.ObjectInput;
import org.light.serialize.core.io.ObjectOutput;
import org.light.serialize.core.io.ReadContext;
import org.light.serialize.core.serializer.Serializer;
import org.light.serialize.core.serializer.SerializerFactory;

import java.io.IOException;
import java.lang.reflect.Array;

/**
 * ArraySerializers
 *
 * @author alex
 */
public class ArraySerializers {

    public static final Serializer<boolean[]> BOOL_ARRAY_SERIALIZER = new Serializer<boolean[]>(boolean[].class) {

        @Override
        public void write(ObjectOutput output, boolean[] value) throws IOException {
            int length = value.length;
            output.writeVarInt(length);
            byte[] bytes = new byte[length / 8 + (length % 8 > 0 ? 1 : 0)];

            for (int i = 0; i < length; i++) {
                int byteIndex = i / 8;
                if (value[i]) {
                    bytes[byteIndex] = (byte) (bytes[byteIndex] | (1 << (i % 8)));
                }
            }

            output.writeBytes(bytes);
        }

        @Override
        public boolean[] read(ObjectInput input) throws IOException {
            int length = input.readVarInt();
            byte[] bytes = input.readBytes(length / 8 + (length % 8 > 0 ? 1 : 0));
            boolean[] value = new boolean[length];

            for (int i = 0; i < length; i++) {
                byte b = bytes[i / 8];
                if ((b & (1 << (i % 8))) != 0) {
                    value[i] = true;
                }
            }

            return value;
        }
    };

    public static final Serializer<byte[]> BYTE_ARRAY_SERIALIZER = new Serializer<byte[]>(byte[].class) {

        @Override
        public void write(ObjectOutput output, byte[] value) throws IOException {
            output.writeVarInt(value.length);
            output.writeBytes(value);
        }

        @Override
        public byte[] read(ObjectInput input) throws IOException {
            return input.readBytes(input.readVarInt());
        }
    };

    public static final Serializer<char[]> CHAR_ARRAY_SERIALIZER = new Serializer<char[]>(char[].class) {

        @Override
        public void write(ObjectOutput output, char[] value) throws IOException {
            int length = value.length;
            output.writeVarInt(length);
            for (int i = 0; i < length; i++) {
                output.writeVarInt(value[i]);
            }
        }

        @Override
        public char[] read(ObjectInput input) throws IOException {
            int length = input.readVarInt();
            char[] value = new char[length];
            for (int i = 0; i < length; i++) {
                value[i] = (char) input.readVarInt();
            }

            return value;
        }
    };

    public static final Serializer<short[]> SHORT_ARRAY_SERIALIZER = new Serializer<short[]>(short[].class) {

        @Override
        public void write(ObjectOutput output, short[] value) throws IOException {
            int length = value.length;
            output.writeVarInt(length);
            for (int i = 0; i < length; i++) {
                output.writeZigzagVarInt(value[i]);
            }
        }

        @Override
        public short[] read(ObjectInput input) throws IOException {
            int length = input.readVarInt();
            short[] value = new short[length];
            for (int i = 0; i < length; i++) {
                value[i] = (short) input.readZigzagVarInt();
            }

            return value;
        }
    };

    public static final Serializer<int[]> INT_ARRAY_SERIALIZER = new Serializer<int[]>(int[].class) {

        @Override
        public void write(ObjectOutput output, int[] value) throws IOException {
            int length = value.length;
            output.writeVarInt(length);
            for (int i = 0; i < length; i++) {
                output.writeZigzagVarInt(value[i]);
            }
        }

        @Override
        public int[] read(ObjectInput input) throws IOException {
            int length = input.readVarInt();
            int[] value = new int[length];
            for (int i = 0; i < length; i++) {
                value[i] = input.readZigzagVarInt();
            }

            return value;
        }
    };

    public static final Serializer<long[]> LONG_ARRAY_SERIALIZER = new Serializer<long[]>(long[].class) {

        @Override
        public void write(ObjectOutput output, long[] value) throws IOException {
            int length = value.length;
            output.writeVarInt(length);
            for (int i = 0; i < length; i++) {
                output.writeZigzagVarLong(value[i]);
            }
        }

        @Override
        public long[] read(ObjectInput input) throws IOException {
            int length = input.readVarInt();
            long[] value = new long[length];
            for (int i = 0; i < length; i++) {
                value[i] = input.readZigzagVarLong();
            }

            return value;
        }
    };

    public static final Serializer<float[]> FLOAT_ARRAY_SERIALIZER = new Serializer<float[]>(float[].class) {
        @Override
        public void write(ObjectOutput output, float[] value) throws IOException {
            int length = value.length;
            output.writeVarInt(length);
            for (int i = 0; i < length; i++) {
                output.writeObject(value[i]);
            }
        }

        @Override
        public float[] read(ObjectInput input) throws IOException {
            int length = input.readVarInt();
            float[] value = new float[length];
            for (int i = 0; i < length; i++) {
                value[i] = (float) input.readObject();
            }

            return value;
        }
    };

    public static final Serializer<double[]> DOUBLE_ARRAY_SERIALIZER = new Serializer<double[]>(double[].class) {

        @Override
        public void write(ObjectOutput output, double[] value) throws IOException {
            int length = value.length;
            output.writeVarInt(length);
            for (int i = 0; i < length; i++) {
                output.writeObject(value[i]);
            }
        }

        @Override
        public double[] read(ObjectInput input) throws IOException {
            int length = input.readVarInt();
            double[] value = new double[length];
            for (int i = 0; i < length; i++) {
                value[i] = (double) input.readObject();
            }

            return value;
        }
    };

    /**
     * ObjectArraySerializer.
     */
    public static class ObjectArraySerializer extends Serializer<Object[]> {

        public ObjectArraySerializer(Class<? extends Object[]> type) {
            super(type);
        }

        @Override
        public void write(ObjectOutput output, Object[] value) throws IOException {
            int length = value.length;
            output.writeVarInt(length);
            for (int i = 0; i < length; i++) {
                output.writeObject(value[i]);

            }
        }

        @Override
        public Object[] read(ObjectInput input) throws IOException {
            int length = input.readVarInt();
            Object[] value = (Object[]) Array.newInstance(type.getComponentType(), length);
            ReadContext.get().putReferenceObject(value);

            for (int i = 0; i < length; i++) {
                value[i] = input.readObject();
            }

            return value;
        }
    }

    public static final ObjectArraySerializer OBJECT_ARRAY_SERIALIZER = new ObjectArraySerializer(Object[].class);
    public static final ObjectArraySerializer STRING_ARRAY_SERIALIZER =new ObjectArraySerializer(String[].class);
    public static final ObjectArraySerializer CLASS_ARRAY_SERIALIZER = new ObjectArraySerializer(Class[].class);
    public static final ObjectArraySerializer BYTE_WRAPPER_ARRAY_SERIALIZER = new ObjectArraySerializer(Byte[].class);
    public static final ObjectArraySerializer BOOL_WRAPPER_ARRAY_SERIALIZER = new ObjectArraySerializer(Boolean[].class);
    public static final ObjectArraySerializer CHAR_ARRAY_WRAPPER_SERIALIZER = new ObjectArraySerializer(Character[].class);
    public static final ObjectArraySerializer SHORT_WRAPPER_ARRAY_SERIALIZER = new ObjectArraySerializer(Short[].class);
    public static final ObjectArraySerializer INT_WRAPPER_ARRAY_SERIALIZER = new ObjectArraySerializer(Integer[].class);
    public static final ObjectArraySerializer LONG_WRAPPER_ARRAY_SERIALIZER = new ObjectArraySerializer(Long[].class);
    public static final ObjectArraySerializer FLOAT_WRAPPER_ARRAY_SERIALIZER = new ObjectArraySerializer(Float[].class);
    public static final ObjectArraySerializer DOUBLE_WRAPPER_ARRAY_SERIALIZER = new ObjectArraySerializer(Double[].class);

    public static void register(SerializerFactory factory) {
        factory.register(BYTE_ARRAY_SERIALIZER);
        factory.register(BYTE_WRAPPER_ARRAY_SERIALIZER);
        factory.register(BOOL_ARRAY_SERIALIZER);
        factory.register(BOOL_WRAPPER_ARRAY_SERIALIZER);
        factory.register(CHAR_ARRAY_SERIALIZER);
        factory.register(CHAR_ARRAY_WRAPPER_SERIALIZER);
        factory.register(SHORT_ARRAY_SERIALIZER);
        factory.register(SHORT_WRAPPER_ARRAY_SERIALIZER);
        factory.register(INT_ARRAY_SERIALIZER);
        factory.register(INT_WRAPPER_ARRAY_SERIALIZER);
        factory.register(LONG_ARRAY_SERIALIZER);
        factory.register(LONG_WRAPPER_ARRAY_SERIALIZER);
        factory.register(FLOAT_ARRAY_SERIALIZER);
        factory.register(FLOAT_WRAPPER_ARRAY_SERIALIZER);
        factory.register(DOUBLE_ARRAY_SERIALIZER);
        factory.register(DOUBLE_WRAPPER_ARRAY_SERIALIZER);
        factory.register(OBJECT_ARRAY_SERIALIZER);
        factory.register(STRING_ARRAY_SERIALIZER);
        factory.register(CLASS_ARRAY_SERIALIZER);
    }

}
