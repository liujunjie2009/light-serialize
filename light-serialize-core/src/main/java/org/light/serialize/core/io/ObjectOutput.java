package org.light.serialize.core.io;

import org.light.serialize.core.buffer.Buffer;
import org.light.serialize.core.buffer.ByteBuffer;
import org.light.serialize.core.buffer.LinkedByteBuffer;
import org.light.serialize.core.constants.Constants;
import org.light.serialize.core.constants.TagId;
import org.light.serialize.core.serializer.Serializer;
import org.light.serialize.core.serializer.SerializerFactory;
import org.light.serialize.core.util.BufferUtil;

import java.io.IOException;

import static org.light.serialize.core.constants.TagId.*;

/**
 * The writing of objects.
 *
 * @author alex
 */
public class ObjectOutput {

    protected final Buffer buffer;

    public ObjectOutput() {
        this.buffer = ByteBuffer.alloc();
    }

    public ObjectOutput(int initBufferCapacity) {
        this.buffer = ByteBuffer.alloc(initBufferCapacity);
    }

    public ObjectOutput(Buffer buffer) {
        this.buffer = buffer;
    }

    public Buffer buffer() {
        return buffer;
    }

    public void writeBool(boolean val) {
        BufferUtil.writeBool(buffer, val);
    }

    public void writeByte(int val) {
        buffer.writeByte(val);
    }

    public void writeChar(char val) {
        BufferUtil.writeChar(buffer, val);
    }

    public void writeUtf8Char(char val) {
        BufferUtil.writeUtf8Char(buffer, val);
    }

    public void writeShort(int val) {
        BufferUtil.writeShort(buffer, val);
    }

    public void writeVarShort(short val) {
        BufferUtil.writeVarShort(buffer, val);
    }

    public void writeReverseVarShort(int val) {
        BufferUtil.writeReverseVarShort(buffer, val);
    }

    public void writeInt(int val) {
        BufferUtil.writeInt(buffer, val);
    }

    public int writeVarInt(int val) {
        return BufferUtil.writeVarInt(buffer, val);
    }

    public int writeReverseVarInt(int val) {
        return BufferUtil.writeReverseVarInt(buffer, val);
    }

    public int writeZigzagVarInt(int val) {
        return BufferUtil.writeZigzagVarInt(buffer, val);
    }

    public void writeLong(long val) {
        BufferUtil.writeLong(buffer, val);
    }

    public int writeVarLong(long val) {
        return BufferUtil.writeVarLong(buffer, val);
    }

    public int writeReverseVarLong(long val) {
        return BufferUtil.writeReverseVarLong(buffer, val);
    }

    public int writeZigzagVarLong(long val) {
        return BufferUtil.writeZigzagVarLong(buffer, val);
    }

    public void writeFloat(float val) {
        BufferUtil.writeFloat(buffer, val);
    }

    public void writeDouble(double val) {
        BufferUtil.writeDouble(buffer, val);
    }

    public void writeBytes(byte[] val) {
        buffer.writeBytes(val);
    }

    public void writeBytes(byte[] val, int offset, int length) {
        buffer.writeBytes(val, offset, length);
    }

    public void writeString(String val) {
        BufferUtil.writeString(buffer, val);
    }

    /**
     * write an object.
     */
    public void writeObject(Object obj) throws IOException {
        WriteContext writeContext = WriteContext.get();
        try {
            int depth = writeContext.increaseDepth();
            if (depth == Integer.MAX_VALUE) {
                throw new IOException("max depth exceeded: " + depth);
            }

            // write null
            if (obj == null) {
                writeByte(TagId.NULL);
                return;
            }

            // write int
            if (obj instanceof Integer) {
                writeIntObject((int) obj);
                return;
            }

            // write true or false
            if (obj instanceof Boolean) {
                writeByte((boolean) obj ? TagId.BOOL_TRUE : TagId.BOOL_FALSE);
                return;
            }

            // write long
            if (obj instanceof Long) {
                writeLongObject((long) obj);
                return;
            }

            // write float
            if (obj instanceof Float) {
                writeFloatObject((float) obj);
                return;
            }

            // write double
            if (obj instanceof Double) {
                writeDoubleObject((double) obj);
                return;
            }

            // write short
            if (obj instanceof Short) {
                writeShortObject((short) obj);
                return;
            }

            // write byte
            if (obj instanceof Byte) {
                writeByteObject(((Byte) obj).intValue());
                return;
            }

            // write char
            if (obj instanceof Character) {
                writeCharObject((char) obj);
                return;
            }

            // write reference
            if (writeReference(obj)) {
                return;
            }

            // write string
            if (obj instanceof String) {
                writeStringObject((String) obj);
                return;
            }

            // write by registered type
            if (writeByRegisteredType(obj)) {
                return;
            }

            // write by reference type
            if (writeByReferenceType(obj)) {
                return;
            }

            // write by class name
            writeByTypeName(obj);
        } catch (IOException e) {
            throw e;
        } catch (Throwable e) {
            throw new IOException(e);
        } finally {
            if (writeContext.decreaseDepth() == 0) {
                // reset read context
                writeContext.reset();
            }
        }
    }

    /**
     * write int object.
     */
    public void writeIntObject(int val) {
        // [16, 47]: [TagId.INT_N_16(-103), TagId.INT_47(-40)], length:64
        if (val >= -16 && val <= 47) {
            writeByte(val - 87);
            return;
        }

        // [-2048, 2047]: [TagId.INT_N_2048(-39), TagId.INT_2047(-24)], length: 16
        if (val >= -2048 && val <= 2047) {
            writeByte((val >> 8) - 31);
            writeByte(val);
            return;
        }

        // [-262144, 262143]: [TagId.INT_N_262144(-23), TagId.INT_262143(-16)], length: 8
        if (val >= -0x40000 && val <= 0x3ffff) {
            writeByte((val >> 16) - 19);
            writeByte(val >> 8);
            writeByte(val);
            return;
        }

        // The trailing 18 bits are all 0, can compressed to up to 3 bytes by {@link TagId.INT_REVERSE_VAR}
        if ((val & 0x3ffff) == 0) {
            writeByte(TagId.INT_REVERSE_VAR);
            writeReverseVarInt(val);
            return;
        }

        // The trailing 18 bits are all 1, can compressed to up to 3 bytes by {@link TagId.INT_COMPLEMENT_REVERSE_VAR}
        if ((val & 0x3ffff) == 0x3ffff) {
            writeByte(TagId.INT_COMPLEMENT_REVERSE_VAR);
            writeReverseVarInt(~val);
            return;
        }

        // [-33554432, 33554433]: [TagId.INT_N_33554432(-15), TagId.INT_33554433(-12)], length: 4
        if (val >= -0x2000000 && val <= 0x2000001) {
            writeByte((val >> 24) + 13);
            writeByte(val >> 16);
            writeByte(val >> 8);
            writeByte(val);
            return;
        }

        writeByte(INT);
        writeInt(val);
    }

    /**
     * write long object
     */
    public void writeLongObject(long val) {
        // [-4, 11]: [TagId.LONG_N_4(-7), TagId.LONG_11(8)], length: 16
        if (val >= -4 && val <= 11) {
            writeByte((int) val - 3);
            return;
        }

        // [-1024, 1023]: [TagId.LONG_N_1024(9), TagId.LONG_1023(16)], length: 8
        if (val >= -1024 && val <= 1023) {
            int intVal = (int) val;
            writeByte((intVal >> 8) + 13);
            writeByte(intVal);
            return;
        }

        // [-131072, 131071]: [TagId.LONG_N_131072(17), TagId.LONG_131071(20)], length: 4
        if (val >= -0x20000 && val <= 0x1ffff) {
            int intVal = (int) val;
            writeByte((intVal >> 16) - 15);
            writeByte(intVal >> 8);
            writeByte(intVal);
            return;
        }

        // [-16777216, 16777215]: [TagId.LONG_N_16777216(21), TagId.LONG_16777215(22)], length: 2
        if (val >= -0x1000000 && val <= 0xffffff) {
            int intVal = (int) val;
            writeByte((intVal >> 24) - 22);
            writeByte(intVal >> 16);
            writeByte(intVal >> 8);
            writeByte(intVal);
            return;
        }

        // write as int
        if (val >= Integer.MIN_VALUE && val <= Integer.MAX_VALUE) {
            writeByte(TagId.LONG_INT);
            writeInt((int) val);
            return;
        }

        // The trailing 36 bits are all 0, can compressed to up to 5 bytes by {@link TagId.LONG_REVERSE_VAR}
        if ((val & 0xfffffffffL) == 0) {
            writeByte(TagId.LONG_REVERSE_VAR);
            writeReverseVarLong(val);
            return;
        }

        // The trailing 36 bits are all 1, can compressed to up to 5 bytes by {@link TagId.LONG_COMPLEMENT_REVERSE_VAR}
        if ((val & 0xfffffffffL) == 0xfffffffffL) {
            writeByte(LONG_COMPLEMENT_REVERSE_VAR);
            writeReverseVarLong(~val);
            return;
        }

        // 5 bytes int
        if (val >= -0x8000000000L && val <= 0x7fffffffffL) {
            writeByte(LONG_5_BYTES);
            writeByte((int) (val >> 32));
            writeInt((int) val);
            return;
        }


        // 6 bytes int
        if (val >= -0x800000000000L && val <= 0x7fffffffffffL) {
            writeByte(LONG_6_BYTES);
            writeByte((int) (val >> 40));
            writeByte((int) (val >> 32));
            writeInt((int) val);
            return;
        }


        // 7 bytes int
        if (val >= -0x80000000000000L && val <= 0x7fffffffffffffL) {
            writeByte(LONG_7_BYTES);
            writeByte((int) (val >> 48));
            writeByte((int) (val >> 40));
            writeByte((int) (val >> 32));
            writeInt((int) val);
            return;
        }

        // 8 bytes int
        writeByte(LONG);
        writeLong(val);
    }

    /**
     * write float object
     */
    public void writeFloatObject(float val) {
        int intVal = (int) val;

        if (intVal == val) {

            if (intVal == 0) {
                writeByte(FLOAT_0);
                return;
            } else if (intVal == 1) {
                writeByte(FLOAT_1);
                return;
            } else if (intVal == -1) {
                writeByte(FLOAT_N_1);
                return;
            } else if (Byte.MIN_VALUE <= intVal && intVal < Byte.MAX_VALUE) {
                writeByte(FLOAT_BYTE);
                writeByte(intVal);
                return;
            } else if (Short.MIN_VALUE <= intVal && intVal < Short.MAX_VALUE) {
                writeByte(FLOAT_SHORT);
                writeByte(intVal);
                return;
            }
        }

        int mills = (int) (val * 1000);
        if (0.001 * mills == val) {

            if (mills >> 21 == 0) {
                writeByte(FLOAT_MILLI_VAR);
                writeVarInt(mills);
                return;
            }

            if (mills >> 21 == -1) {
                writeByte(FLOAT_MILLI_COMPLEMENT_VAR);
                writeVarInt(~mills);
                return;
            }
        }

        writeByte(FLOAT);
        writeFloat(val);
    }

    /**
     * write double object
     */
    public void writeDoubleObject(double val) {
        long longVal = (long) val;

        if (longVal == val) {
            if (longVal == 0) {
                writeByte(DOUBLE_0);
                return;
            } else if (longVal == 1) {
                writeByte(DOUBLE_1);
                return;
            } else if (longVal == -1) {
                writeByte(DOUBLE_N_1);
                return;
            } else if (Byte.MIN_VALUE <= longVal && longVal < Byte.MAX_VALUE) {
                writeByte(DOUBLE_BYTE);
                writeByte((int) longVal);
                return;
            } else if (Short.MIN_VALUE <= longVal && longVal < Short.MAX_VALUE) {
                writeByte(DOUBLE_SHORT);
                writeShort((int) longVal);
                return;
            }
        }

        long mills = (long) (val * 1000);
        if (0.001 * mills == val) {

            if (mills >> 49 == 0) {
                writeByte(DOUBLE_MILLI_VAR);
                writeVarLong(mills);
                return;
            }

            if (mills >> 49 == -1) {
                writeByte(DOUBLE_MILLI_COMPLEMENT_VAR);
                writeVarLong(~mills);
                return;
            }
        }

        writeByte(DOUBLE);
        writeDouble(val);
    }

    /**
     * write short object
     */
    public void writeShortObject(short val) {
        if (val == 0) {
            writeByte(SHORT_0);
            return;
        }

        if (val == 1) {
            writeByte(SHORT_1);
            return;
        }

        if (val == -1) {
            writeByte(SHORT_N_1);
            return;
        }

        if (val >= Byte.MIN_VALUE && val <= Byte.MAX_VALUE) {
            writeByte(SHORT_BYTE);
            writeByte(val);
            return;
        }

        // The trailing 9 bits are all 0, can compressed to up to 1 bytes by {@link TagId.SHORT_REVERSE_VAR}
        if ((val & 0x1ff) == 0) {
            writeByte(TagId.SHORT_REVERSE_VAR);
            writeReverseVarShort(val);
            return;
        }

        // The trailing 9 bits are all 1, can compressed to up to 1 bytes by {@link TagId.SHORT_COMPLEMENT_REVERSE_VAR}
        if ((val & 0x1ff) == 0x1ff) {
            writeByte(TagId.SHORT_COMPLEMENT_REVERSE_VAR);
            writeReverseVarShort(~val);
            return;
        }

        writeByte(SHORT);
        writeShort(val);
    }

    /*
     * write byte object
     */
    public void writeByteObject(int val) {
        if (val == 0) {
            writeByte(BYTE_0);
            return;
        }

        if (val == 1) {
            writeByte(BYTE_1);
            return;
        }

        if (val == -1) {
            writeByte(BYTE_N_1);
            return;
        }

        writeByte(BYTE);
        writeByte(val);
    }

    /**
     * write char object
     */
    public void writeCharObject(char val) {
        if (val <= Byte.MAX_VALUE) {
            writeByte(CHAR_ASCII);
            writeByte(val);
            return;
        }

        writeByte(CHAR);
        writeChar(val);
    }

    /**
     * write String Object
     */
    public void writeStringObject(String val) {
        if (val.length() == 0) {
            writeByte(STRING_EMPTY);
        } else {
            WriteContext.get().putObject(val);
            writeByte(TagId.STRING);
            writeString(val);
        }
    }

    /**
     * write object Reference
     */
    private boolean writeReference(Object obj) {
        WriteContext writeContext = WriteContext.get();
        int refIndex = writeContext.getObjectIndex(obj);

        if (refIndex == Constants.NULL_INDEX) {
            return false;
        }

        if (refIndex < 8) {
            writeByte(TagId.OBJECT_REFERENCE_0 + refIndex);
            return true;
        }

        int lastRefIndex = writeContext.objectIndexSize() - refIndex - 1;
        if (lastRefIndex < 8) {
            writeByte(TagId.OBJECT_REFERENCE_LAST_0 + lastRefIndex);
            return true;
        }

        writeByte(TagId.OBJECT_REFERENCE);
        writeVarInt(refIndex);
        return true;
    }

    /**
     * write reference type
     */
    private boolean writeByReferenceType(Object obj) throws IOException {
        WriteContext writeContext = WriteContext.get();
        Class<?> type = obj.getClass();
        Serializer serializer = writeContext.getSerializerFactory().getSerializer(type);
        int typeIndex = writeContext.getTypeIndex(type);

        if (typeIndex == Constants.NULL_INDEX) {
            return false;
        }

        writeContext.putObject(obj);

        int lasTypeIndex;
        if (typeIndex < 8) {
            writeByte(TagId.TYPE_REFERENCE_0 + typeIndex);
        } else if ((lasTypeIndex = writeContext.typeIndexSize() - typeIndex - 1) < 8) {
            writeByte(TagId.TYPE_REFERENCE_LAST_0 + lasTypeIndex);
        } else {
            writeByte(TagId.TYPE_REFERENCE);
            writeVarInt(typeIndex);
        }

        serializer.write(this, obj);
        return true;
    }

    /**
     * write by registered type which is long type
     */
    private boolean writeByRegisteredType(Object obj) throws IOException {
        WriteContext writeContext = WriteContext.get();
        Class<?> type = obj.getClass();
        SerializerFactory serializerFactory = writeContext.getSerializerFactory();
        Serializer serializer = serializerFactory.getSerializer(type);
        long typeId = serializer.getTypeId();

        if (serializerFactory.getRegisteredSerializer(typeId) == null) {
            return false;
        }

        writeContext.putObject(obj);

        if (typeId >= Byte.MIN_VALUE && typeId <= Byte.MAX_VALUE) {
            // write registered type id
            writeByte((int) typeId);
            serializer.write(this, obj);
            return true;
        }

        writeContext.putClass(type);
        writeByte(TagId.TYPE_REGISTERED);
        // write registered type id by ZigzagVarLong.
        writeZigzagVarLong(typeId);
        serializer.write(this, obj);
        return true;
    }

    /**
     * write by class name.
     */
    private void writeByTypeName(Object obj) throws IOException {
        WriteContext writeContext = WriteContext.get();
        Class<?> type = obj.getClass();
        Serializer serializer = writeContext.getSerializerFactory().getSerializer(type);

        writeContext.putObject(obj);
        writeContext.putClass(type);

        writeByte(TagId.TYPE_NAME);
        writeString(type.getName());
        serializer.write(this, obj);
    }

}
