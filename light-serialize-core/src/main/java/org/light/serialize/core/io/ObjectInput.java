package org.light.serialize.core.io;

import org.light.serialize.core.buffer.Buffer;
import org.light.serialize.core.constants.Constants;
import org.light.serialize.core.constants.TagId;
import org.light.serialize.core.serializer.Serializer;
import org.light.serialize.core.serializer.java.*;
import org.light.serialize.core.util.BufferUtil;

import java.io.IOException;

import static org.light.serialize.core.constants.Constants.*;

/**
 * The reading of objects.
 *
 * @author alex
 */
public class ObjectInput {

    protected final Buffer buffer;

    public ObjectInput(Buffer buffer) {
        this.buffer = buffer;
    }

    public Buffer buffer() {
        return buffer;
    }

    public boolean readBool() {
        return BufferUtil.readBool(buffer);
    }

    public byte readByte() {
        return buffer.readByte();
    }

    public short readShort() {
        return BufferUtil.readVarShort(buffer);
    }

    public short readVarShort() {
        return BufferUtil.readVarShort(buffer);
    }

    public short readReverseVarShort() {
        return BufferUtil.readReverseVarShort(buffer);
    }

    public char readChar() {
        return BufferUtil.readChar(buffer);
    }

    public int readInt() {
        return BufferUtil.readInt(buffer);
    }

    public int readVarInt() {
        return BufferUtil.readVarInt(buffer);
    }

    public int readReverseVarInt() {
        return BufferUtil.readReverseVarInt(buffer);
    }

    public int readZigzagVarInt() {
        return BufferUtil.readZigzagVarInt(buffer);
    }

    public long readLong() {
        return BufferUtil.readLong(buffer);
    }

    public long readVarLong() {
        return BufferUtil.readVarLong(buffer);
    }

    public long readReverseVarLong() {
        return BufferUtil.readReverseVarLong(buffer);
    }

    public long readZigzagVarLong() {
        return BufferUtil.readZigzagVarLong(buffer);
    }

    public float readFloat() {
        return BufferUtil.readFloat(buffer);
    }

    public double readDouble() {
        return BufferUtil.readDouble(buffer);
    }

    public String readString() {
        return BufferUtil.readString(buffer);
    }

    public void readBytes(byte[] bytes, int offset, int length) {
        buffer.readBytes(bytes, offset, length);
    }

    public byte[] readBytes(int length) {
        return buffer.readBytes(length);
    }

    /**
     * Read and return an object.
     */
    public Object readObject() throws IOException {
        ReadContext readContext = ReadContext.get();
        int depth = readContext.increaseDepth();

        if (depth == Integer.MAX_VALUE) {
            throw new IOException("max depth exceeded: " + depth);
        }

        try {
            int tag = readByte();

            switch (tag) {
                case TagId.NULL:
                    return null;

                /*
                 * Bool
                 */
                case TagId.BOOL:
                    return BasicSerializers.BOOL_SERIALIZER.read(this);
                case TagId.BOOL_WRAPPER:
                    return BasicSerializers.BOOL_WRAPPER_SERIALIZER.read(this);
                case TagId.BOOL_TRUE:
                    return true;
                case TagId.BOOL_FALSE:
                    return false;

                /*
                 * Byte
                 */
                case TagId.BYTE:
                    return BasicSerializers.BYTE_SERIALIZER.read(this);
                case TagId.BYTE_WRAPPER:
                    return BasicSerializers.BYTE_WRAPPER_SERIALIZER.read(this);
                case TagId.BYTE_0:
                    return BYTE_0_VAL;
                case TagId.BYTE_1:
                    return BYTE_1_VAL;
                case TagId.BYTE_N_1:
                    return BYTE_N_1_VAL;

                /*
                 * Char
                 */
                case TagId.CHAR:
                    return BasicSerializers.CHAR_SERIALIZER.read(this);
                case TagId.CHAR_WRAPPER:
                    return BasicSerializers.CHAR_WRAPPER_SERIALIZER.read(this);
                case TagId.CHAR_ASCII:
                    return (char) readByte();

                /*
                 * Short
                 */
                case TagId.SHORT:
                    return BasicSerializers.SHORT_SERIALIZER.read(this);
                case TagId.SHORT_WRAPPER:
                    return BasicSerializers.SHORT_WRAPPER_SERIALIZER.read(this);
                case TagId.SHORT_BYTE:
                    return (short) readByte();
                case TagId.SHORT_REVERSE_VAR:
                    return readReverseVarShort();
                case TagId.SHORT_COMPLEMENT_REVERSE_VAR:
                    return ~readReverseVarShort();
                case TagId.SHORT_0:
                    return SHORT_0_VAL;
                case TagId.SHORT_1:
                    return SHORT_1_VAL;
                case TagId.SHORT_N_1:
                    return SHORT_N_1_VAL;

                /*
                 * Int
                 */
                case TagId.INT:
                case TagId.INT_WRAPPER:
                    return readInt();
                case TagId.INT_REVERSE_VAR:
                    return readReverseVarInt();
                case TagId.INT_COMPLEMENT_REVERSE_VAR:
                    return ~readReverseVarInt();
                case TagId.INT_N_16:
                    return INT_N_16_VAL;
                case TagId.INT_N_16 + 1:
                    return INT_N_15_VAL;
                case TagId.INT_N_16 + 2:
                    return INT_N_14_VAL;
                case TagId.INT_N_16 + 3:
                    return INT_N_13_VAL;
                case TagId.INT_N_16 + 4:
                    return INT_N_12_VAL;
                case TagId.INT_N_16 + 5:
                    return INT_N_11_VAL;
                case TagId.INT_N_16 + 6:
                    return INT_N_10_VAL;
                case TagId.INT_N_16 + 7:
                    return INT_N_9_VAL;
                case TagId.INT_N_16 + 8:
                    return INT_N_8_VAL;
                case TagId.INT_N_16 + 9:
                    return INT_N_7_VAL;
                case TagId.INT_N_16 + 10:
                    return INT_N_6_VAL;
                case TagId.INT_N_16 + 11:
                    return INT_N_5_VAL;
                case TagId.INT_N_16 + 12:
                    return INT_N_4_VAL;
                case TagId.INT_N_16 + 13:
                    return INT_N_3_VAL;
                case TagId.INT_N_16 + 14:
                    return INT_N_2_VAL;
                case TagId.INT_N_16 + 15:
                    return INT_N_1_VAL;
                case TagId.INT_N_16 + 16:
                    return INT_0_VAL;
                case TagId.INT_N_16 + 17:
                    return INT_1_VAL;
                case TagId.INT_N_16 + 18:
                    return INT_2_VAL;
                case TagId.INT_N_16 + 19:
                    return INT_3_VAL;
                case TagId.INT_N_16 + 20:
                    return INT_4_VAL;
                case TagId.INT_N_16 + 21:
                    return INT_5_VAL;
                case TagId.INT_N_16 + 22:
                    return INT_6_VAL;
                case TagId.INT_N_16 + 23:
                    return INT_7_VAL;
                case TagId.INT_N_16 + 24:
                    return INT_8_VAL;
                case TagId.INT_N_16 + 25:
                    return INT_9_VAL;
                case TagId.INT_N_16 + 26:
                    return INT_10_VAL;
                case TagId.INT_N_16 + 27:
                    return INT_11_VAL;
                case TagId.INT_N_16 + 28:
                    return INT_12_VAL;
                case TagId.INT_N_16 + 29:
                    return INT_13_VAL;
                case TagId.INT_N_16 + 30:
                    return INT_14_VAL;
                case TagId.INT_N_16 + 31:
                    return INT_15_VAL;
                case TagId.INT_N_16 + 32:
                    return INT_16_VAL;
                case TagId.INT_N_16 + 33:
                    return INT_17_VAL;
                case TagId.INT_N_16 + 34:
                    return INT_18_VAL;
                case TagId.INT_N_16 + 35:
                    return INT_19_VAL;
                case TagId.INT_N_16 + 36:
                    return INT_20_VAL;
                case TagId.INT_N_16 + 37:
                    return INT_21_VAL;
                case TagId.INT_N_16 + 38:
                    return INT_22_VAL;
                case TagId.INT_N_16 + 39:
                    return INT_23_VAL;
                case TagId.INT_N_16 + 40:
                    return INT_24_VAL;
                case TagId.INT_N_16 + 41:
                    return INT_25_VAL;
                case TagId.INT_N_16 + 42:
                    return INT_26_VAL;
                case TagId.INT_N_16 + 43:
                    return INT_27_VAL;
                case TagId.INT_N_16 + 44:
                    return INT_28_VAL;
                case TagId.INT_N_16 + 45:
                    return INT_29_VAL;
                case TagId.INT_N_16 + 46:
                    return INT_30_VAL;
                case TagId.INT_N_16 + 47:
                    return INT_31_VAL;
                case TagId.INT_N_16 + 48:
                    return INT_32_VAL;
                case TagId.INT_N_16 + 49:
                    return INT_33_VAL;
                case TagId.INT_N_16 + 50:
                    return INT_34_VAL;
                case TagId.INT_N_16 + 51:
                    return INT_35_VAL;
                case TagId.INT_N_16 + 52:
                    return INT_36_VAL;
                case TagId.INT_N_16 + 53:
                    return INT_37_VAL;
                case TagId.INT_N_16 + 54:
                    return INT_38_VAL;
                case TagId.INT_N_16 + 55:
                    return INT_39_VAL;
                case TagId.INT_N_16 + 56:
                    return INT_40_VAL;
                case TagId.INT_N_16 + 57:
                    return INT_41_VAL;
                case TagId.INT_N_16 + 58:
                    return INT_42_VAL;
                case TagId.INT_N_16 + 59:
                    return INT_43_VAL;
                case TagId.INT_N_16 + 60:
                    return INT_44_VAL;
                case TagId.INT_N_16 + 61:
                    return INT_45_VAL;
                case TagId.INT_N_16 + 62:
                    return INT_46_VAL;
                case TagId.INT_47:
                    return INT_47_VAL;

                case TagId.INT_N_2048:
                case TagId.INT_N_2048 + 1:
                case TagId.INT_N_2048 + 2:
                case TagId.INT_N_2048 + 3:
                case TagId.INT_N_2048 + 4:
                case TagId.INT_N_2048 + 5:
                case TagId.INT_N_2048 + 6:
                case TagId.INT_N_2048 + 7:
                case TagId.INT_N_2048 + 8:
                case TagId.INT_N_2048 + 9:
                case TagId.INT_N_2048 + 10:
                case TagId.INT_N_2048 + 11:
                case TagId.INT_N_2048 + 12:
                case TagId.INT_N_2048 + 13:
                case TagId.INT_N_2048 + 14:
                case TagId.INT_2047:
                    return ((tag + 31) << 8) + readByte();

                case TagId.INT_N_262144:
                case TagId.INT_N_262144 + 1:
                case TagId.INT_N_262144 + 2:
                case TagId.INT_N_262144 + 3:
                case TagId.INT_N_262144 + 4:
                case TagId.INT_N_262144 + 5:
                case TagId.INT_N_262144 + 6:
                case TagId.INT_262143:
                    return ((tag + 19) << 16) + (readByte() << 8) + readByte();

                case TagId.INT_N_33554432:
                case TagId.INT_N_33554432 + 1:
                case TagId.INT_N_33554432 + 2:
                case TagId.INT_33554433:
                    return ((tag + 13) << 24) + (readByte() << 16) + (readByte() << 8) + readByte();

                /*
                 * Long
                 */
                case TagId.LONG:
                case TagId.LONG_WRAPPER:
                    return readLong();
                case TagId.LONG_REVERSE_VAR:
                    return readReverseVarLong();
                case TagId.LONG_COMPLEMENT_REVERSE_VAR:
                    return ~readReverseVarLong();

                case TagId.LONG_N_4:
                    return LONG_N_4_VAL;
                case TagId.LONG_N_4 + 1:
                    return LONG_N_3_VAL;
                case TagId.LONG_N_4 + 2:
                    return LONG_N_2_VAL;
                case TagId.LONG_N_4 + 3:
                    return LONG_N_1_VAL;
                case TagId.LONG_N_4 + 4:
                    return LONG_0_VAL;
                case TagId.LONG_N_4 + 5:
                    return LONG_1_VAL;
                case TagId.LONG_N_4 + 6:
                    return LONG_2_VAL;
                case TagId.LONG_N_4 + 7:
                    return LONG_3_VAL;
                case TagId.LONG_N_4 + 8:
                    return LONG_4_VAL;
                case TagId.LONG_N_4 + 9:
                    return LONG_5_VAL;
                case TagId.LONG_N_4 + 10:
                    return LONG_6_VAL;
                case TagId.LONG_N_4 + 11:
                    return LONG_7_VAL;
                case TagId.LONG_N_4 + 12:
                    return LONG_8_VAL;
                case TagId.LONG_N_4 + 13:
                    return LONG_9_VAL;
                case TagId.LONG_N_4 + 14:
                    return LONG_10_VAL;
                case TagId.LONG_11:
                    return LONG_11_VAL;

                case TagId.LONG_N_1024:
                case TagId.LONG_N_1024 + 1:
                case TagId.LONG_N_1024 + 2:
                case TagId.LONG_N_1024 + 3:
                case TagId.LONG_N_1024 + 4:
                case TagId.LONG_N_1024 + 5:
                case TagId.LONG_N_1024 + 6:
                case TagId.LONG_1023:
                    return ((tag - 13) << 8) + readByte();

                case TagId.LONG_N_131072:
                case TagId.LONG_N_131072 + 1:
                case TagId.LONG_N_131072 + 2:
                case TagId.LONG_131071:
                    return ((tag + 15) << 16) + (readByte() << 8) + readByte();

                case TagId.LONG_N_16777216:
                case TagId.LONG_16777215:
                    return ((tag + 22) << 24) + (readByte() << 16) + (readByte() << 8) + readByte();

                case TagId.LONG_INT:
                    return (long) readInt();
                case TagId.LONG_5_BYTES:
                    return (long) (buffer.readByte() & 0xFF) << 32
                            | (long) (buffer.readByte() & 0xFF) << 24
                            | (buffer.readByte() & 0xFF) << 16
                            | (buffer.readByte() & 0xFF) << 8
                            | buffer.readByte() & 0xFF;
                case TagId.LONG_6_BYTES:
                    return (long) (buffer.readByte() & 0xFF) << 40
                            | (long) (buffer.readByte() & 0xFF) << 32
                            | (long) (buffer.readByte() & 0xFF) << 24
                            | (buffer.readByte() & 0xFF) << 16
                            | (buffer.readByte() & 0xFF) << 8
                            | buffer.readByte() & 0xFF;
                case TagId.LONG_7_BYTES:
                    return (long) (buffer.readByte() & 0xFF) << 48
                            | (long) (buffer.readByte() & 0xFF) << 40
                            | (long) (buffer.readByte() & 0xFF) << 32
                            | (long) (buffer.readByte() & 0xFF) << 24
                            | (buffer.readByte() & 0xFF) << 16
                            | (buffer.readByte() & 0xFF) << 8
                            | buffer.readByte() & 0xFF;

                /*
                 * Float
                 */
                case TagId.FLOAT:
                case TagId.FLOAT_WRAPPER:
                    return readFloat();
                case TagId.FLOAT_0:
                    return FLOAT_0_VAL;
                case TagId.FLOAT_1:
                    return FLOAT_1_VAL;
                case TagId.FLOAT_N_1:
                    return FLOAT_N_1_VAL;
                case TagId.FLOAT_BYTE:
                    return (float) readByte();
                case TagId.FLOAT_SHORT:
                    return (float) readShort();
                case TagId.FLOAT_MILLI_VAR:
                    return readVarInt() * 0.001f;
                case TagId.FLOAT_MILLI_COMPLEMENT_VAR:
                    return ~readVarInt() * 0.001f;

                /*
                 * Double
                 */
                case TagId.DOUBLE:
                case TagId.DOUBLE_WRAPPER:
                    return readDouble();
                case TagId.DOUBLE_0:
                    return DOUBLE_0_VAL;
                case TagId.DOUBLE_1:
                    return DOUBLE_1_VAL;
                case TagId.DOUBLE_N_1:
                    return DOUBLE_N_1_VAL;
                case TagId.DOUBLE_BYTE:
                    return (double) readByte();
                case TagId.DOUBLE_SHORT:
                    return (double) readShort();
                case TagId.DOUBLE_MILLI_VAR:
                    return readVarLong() * 0.001d;
                case TagId.DOUBLE_MILLI_COMPLEMENT_VAR:
                    return ~readVarLong() * 0.001d;

                /*
                 * String
                 */
                case TagId.STRING:
                    String str = readString();
                    readContext.putReferenceObjectIfAbsent(str, readContext.getReferenceObjectsSize());
                    return str;
                case TagId.STRING_EMPTY:
                    return EMPTY_STRING_VAL;

                /*
                 * Object Reference
                 */
                case TagId.OBJECT_REFERENCE:
                    return readContext.getReferenceObject(readVarInt());
                case TagId.OBJECT_REFERENCE_0:
                case TagId.OBJECT_REFERENCE_1:
                case TagId.OBJECT_REFERENCE_2:
                case TagId.OBJECT_REFERENCE_3:
                case TagId.OBJECT_REFERENCE_4:
                case TagId.OBJECT_REFERENCE_5:
                case TagId.OBJECT_REFERENCE_6:
                case TagId.OBJECT_REFERENCE_7:
                    return readContext.getReferenceObject(tag - TagId.TYPE_REFERENCE_0);

                case TagId.OBJECT_REFERENCE_LAST_0:
                case TagId.OBJECT_REFERENCE_LAST_1:
                case TagId.OBJECT_REFERENCE_LAST_2:
                case TagId.OBJECT_REFERENCE_LAST_3:
                case TagId.OBJECT_REFERENCE_LAST_4:
                case TagId.OBJECT_REFERENCE_LAST_5:
                case TagId.OBJECT_REFERENCE_LAST_6:
                case TagId.OBJECT_REFERENCE_LAST_7:
                    int referenceObjectsSize = readContext.getReferenceObjectsSize();
                    return readContext.getReferenceObject(referenceObjectsSize - (tag - TagId.OBJECT_REFERENCE_LAST_0) - 1);

                /*
                 * Type Reference
                 */
                case TagId.TYPE_REFERENCE:
                    return readByReferenceType(readVarInt());
                case TagId.TYPE_REFERENCE_0:
                case TagId.TYPE_REFERENCE_1:
                case TagId.TYPE_REFERENCE_2:
                case TagId.TYPE_REFERENCE_3:
                case TagId.TYPE_REFERENCE_4:
                case TagId.TYPE_REFERENCE_5:
                case TagId.TYPE_REFERENCE_6:
                case TagId.TYPE_REFERENCE_7:
                    return readByReferenceType(tag - TagId.TYPE_REFERENCE_0);

                case TagId.TYPE_REFERENCE_LAST_0:
                case TagId.TYPE_REFERENCE_LAST_1:
                case TagId.TYPE_REFERENCE_LAST_2:
                case TagId.TYPE_REFERENCE_LAST_3:
                case TagId.TYPE_REFERENCE_LAST_4:
                case TagId.TYPE_REFERENCE_LAST_5:
                case TagId.TYPE_REFERENCE_LAST_6:
                case TagId.TYPE_REFERENCE_LAST_7:
                    int refTypeSize = readContext.getReferenceTypeSize();
                    return readByReferenceType(refTypeSize - (tag - TagId.TYPE_REFERENCE_LAST_0) - 1);

                /*
                 * TYPE_REGISTERED
                 */
                case TagId.TYPE_REGISTERED:
                    return readByRegisteredType(readZigzagVarLong());

                /*
                 * TYPE_NAME
                 */
                case TagId.TYPE_NAME:
                    return readByTypeName();

                case TagId.VOID:
                    return BasicSerializers.VOID_SERIALIZER.read(this);
                case TagId.VOID_WRAPPER:
                    return BasicSerializers.VOID_WRAPPER_SERIALIZER.read(this);
                case TagId.CLASS:
                    return readByTagSerializer(Constants.CLASS_SERIALIZER);
                case TagId.OBJECT:
                    return readByTagSerializer(BasicSerializers.OBJECT_SERIALIZER);
                case TagId.BOOL_ARRAY:
                    return readByTagSerializer(ArraySerializers.BOOL_ARRAY_SERIALIZER);
                case TagId.BOOL_WRAPPER_ARRAY:
                    return readByTagSerializer(ArraySerializers.BOOL_WRAPPER_ARRAY_SERIALIZER);
                case TagId.BYTE_ARRAY:
                    return readByTagSerializer(ArraySerializers.BYTE_ARRAY_SERIALIZER);
                case TagId.BYTE_WRAPPER_ARRAY:
                    return readByTagSerializer(ArraySerializers.BYTE_WRAPPER_ARRAY_SERIALIZER);
                case TagId.CHAR_ARRAY:
                    return readByTagSerializer(ArraySerializers.CHAR_ARRAY_SERIALIZER);
                case TagId.CHAR_WRAPPER_ARRAY:
                    return readByTagSerializer(ArraySerializers.CHAR_ARRAY_WRAPPER_SERIALIZER);
                case TagId.SHORT_ARRAY:
                    return readByTagSerializer(ArraySerializers.SHORT_ARRAY_SERIALIZER);
                case TagId.SHORT_WRAPPER_ARRAY:
                    return readByTagSerializer(ArraySerializers.SHORT_WRAPPER_ARRAY_SERIALIZER);
                case TagId.INT_ARRAY:
                    return readByTagSerializer(ArraySerializers.INT_ARRAY_SERIALIZER);
                case TagId.INT_WRAPPER_ARRAY:
                    return readByTagSerializer(ArraySerializers.INT_WRAPPER_ARRAY_SERIALIZER);
                case TagId.LONG_ARRAY:
                    return readByTagSerializer(ArraySerializers.LONG_ARRAY_SERIALIZER);
                case TagId.LONG_WRAPPER_ARRAY:
                    return readByTagSerializer(ArraySerializers.LONG_WRAPPER_ARRAY_SERIALIZER);
                case TagId.FLOAT_ARRAY:
                    return readByTagSerializer(ArraySerializers.FLOAT_ARRAY_SERIALIZER);
                case TagId.FLOAT_WRAPPER_ARRAY:
                    return readByTagSerializer(ArraySerializers.FLOAT_WRAPPER_ARRAY_SERIALIZER);
                case TagId.DOUBLE_ARRAY:
                    return readByTagSerializer(ArraySerializers.DOUBLE_ARRAY_SERIALIZER);
                case TagId.DOUBLE_WRAPPER_ARRAY:
                    return readByTagSerializer(ArraySerializers.DOUBLE_WRAPPER_ARRAY_SERIALIZER);
                case TagId.STRING_ARRAY:
                    return readByTagSerializer(ArraySerializers.STRING_ARRAY_SERIALIZER);
                case TagId.CLASS_ARRAY:
                    return readByTagSerializer(ArraySerializers.CLASS_ARRAY_SERIALIZER);
                case TagId.OBJECT_ARRAY:
                    return readByTagSerializer(ArraySerializers.OBJECT_ARRAY_SERIALIZER);
                case TagId.HASH_SET:
                    return readByTagSerializer(CollectionSerializers.HASH_SET_SERIALIZER);
                case TagId.LINKED_HASH_SET:
                    return readByTagSerializer(CollectionSerializers.LINKED_HASH_SET_SERIALIZER);
                case TagId.TREE_SET:
                    return readByTagSerializer(CollectionSerializers.TREE_SET_SERIALIZER);
                case TagId.ARRAY_LIST:
                    return readByTagSerializer(CollectionSerializers.ARRAY_LIST_SERIALIZER);
                case TagId.LINKED_LIST:
                    return readByTagSerializer(CollectionSerializers.LINKED_LIST_SERIALIZER);
                case TagId.VECTOR:
                    return readByTagSerializer(CollectionSerializers.VECTOR_SERIALIZER);
                case TagId.HASH_MAP:
                    return readByTagSerializer(MapSerializers.HASH_MAP_SERIALIZER);
                case TagId.HASH_TABLE:
                    return readByTagSerializer(MapSerializers.HASH_TABLE_SERIALIZER);
                case TagId.LINKED_HASH_MAP:
                    return readByTagSerializer(MapSerializers.LINKED_HASH_MAP_SERIALIZER);
                case TagId.CONCURRENT_HASH_MAP:
                    return readByTagSerializer(MapSerializers.CONCURRENT_HASH_MAP_SERIALIZER);
                case TagId.TREE_MAP:
                    return readByTagSerializer(MapSerializers.TREE_MAP_SERIALIZER);
                case TagId.OPTIONAL:
                    return readByTagSerializer(OptionalSerializers.OPTIONAL_SERIALIZER);
                case TagId.OPTIONAL_DOUBLE:
                    return readByTagSerializer(OptionalSerializers.OPTIONAL_DOUBLE_SERIALIZER);
                case TagId.OPTIONAL_INT:
                    return readByTagSerializer(OptionalSerializers.OPTIONAL_INT_SERIALIZER);
                case TagId.OPTIONAL_LONG:
                    return readByTagSerializer(OptionalSerializers.OPTIONAL_LONG_SERIALIZER);
                case TagId.DATE:
                    return readByTagSerializer(TimeSerializers.DATE_SERIALIZER);
                case TagId.GREGORIAN_CALENDAR:
                    return readByTagSerializer(Constants.GREGORIAN_CALENDAR_SERIALIZER);
                case TagId.BIG_DECIMAL:
                    return readByTagSerializer(BigNumberSerializers.BIG_DECIMAL_SERIALIZER);
                case TagId.BIG_INTEGER:
                    return readByTagSerializer(BigNumberSerializers.BIG_INTEGER_SERIALIZER);
                case TagId.PROXY:
                    return readByTagSerializer(PROXY_SERIALIZER);
                case TagId.LAMBDA:
                    return readByTagSerializer(LAMBDA_SERIALIZER);
                default:
                    throw new IllegalStateException("Unexpected value: " + tag);
            }

        } catch (IOException e) {
            throw e;
        } catch (Throwable e) {
            throw new IOException(e);
        } finally {
            if (readContext.decreaseDepth() == 0) {
                readContext.reset();
                // TODO: discard some bytes when complete reading an object.??
                buffer.discardSomeReadBytes();
            }
        }
    }

    /**
     * Read by reference type.
     */
    private Object readByReferenceType(int typeIndex) throws IOException {
        ReadContext readContext = ReadContext.get();
        Class<?> referenceType = readContext.getReferenceType(typeIndex);
        Serializer<?> serializer = readContext.getSerializerFactory().getSerializer(referenceType);
        Object readObject = serializer.read(this);

        readContext.putReferenceObjectIfAbsent(readObject, readContext.getReferenceObjectsSize());
        return readObject;
    }

    /**
     * read object by registered type
     */
    private Object readByRegisteredType(long typeId) throws IOException {
        ReadContext readContext = ReadContext.get();
        Serializer<?> serializer = readContext.getSerializerFactory().getRegisteredSerializer(typeId);

        if (serializer == null) {
            throw new NullPointerException("The serializer which typeId is '" + typeId + "' is unregistered");
        }

        readContext.putReferenceType(serializer.getType());

        Object readObject = serializer.read(this);
        readContext.putReferenceObjectIfAbsent(readObject, readContext.getReferenceObjectsSize());
        return readObject;
    }

    /**
     * read object by class name
     */
    private Object readByTypeName() throws ClassNotFoundException, IOException {
        ReadContext readContext = ReadContext.get();
        Class<?> clazz = Class.forName(readString());
        Serializer<?> serializer = readContext.getSerializerFactory().getSerializer(clazz);

        readContext.putReferenceType(serializer.getType());

        Object readObject = serializer.read(this);
        readContext.putReferenceObjectIfAbsent(readObject, readContext.getReferenceObjectsSize());
        return readObject;
    }

    /**
     * read object by TagSerializer
     */
    private Object readByTagSerializer(Serializer<?> serializer) throws IOException {
        ReadContext readContext = ReadContext.get();
        Object readObject = serializer.read(this);

        readContext.putReferenceObjectIfAbsent(readObject, readContext.getReferenceObjectsSize());
        return readObject;
    }

}
