package org.light.serialize.core.util;

import org.light.serialize.core.buffer.Buffer;
import org.light.serialize.core.constants.TagId;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * {@code BufferUtil} provides many tool methods to reading or writing {@code Buffer}.
 *
 * @author alex
 */
public class BufferUtil {

    private BufferUtil() {
    }

    /**
     * Read boolean, 1 byte.
     */
    public static boolean readBool(Buffer buffer) {
        return buffer.readByte() != 0;
    }

    /**
     * Read fixed-length short, 2 byte.
     */
    public static short readShort(Buffer buffer) {
        return (short) (((buffer.readByte() & 0xFF) << 8) | (buffer.readByte() & 0xFF));
    }

    /**
     * Read variable-length short, 1-3 byte.
     */
    public static short readVarShort(Buffer buffer) {
        int b = buffer.readByte();
        int result = b & 0x7F;
        if ((b & 0x80) != 0) {
            b = buffer.readByte();
            result |= (b & 0x7F) << 7;
            if ((b & 0x80) != 0) {
                b = buffer.readByte();
                result |= (b & 0x7F) << 14;
            }
        }

        return (short) result;
    }

    /**
     * Read reverse variable-length short, 1-3 byte.
     */
    public static short readReverseVarShort(Buffer buffer) {
        int b = buffer.readByte();
        int result = b & 0XFE;

        if ((b & 1) != 0) {
            b = buffer.readByte();
            result = (result << 7) | ((b & 0XFE) << 1);

            if ((b & 1) != 0) {
                b = buffer.readByte();
                result |=  b >> 6;
            }
        }

        return (short) result;
    }

    /**
     * Read char, 2 byte.
     */
    public static char readChar(Buffer buffer) {
        return (char) readShort(buffer);
    }

    /**
     * Read fixed-length int, 4 byte.
     */
    public static int readInt(Buffer buffer) {
        return (buffer.readByte() & 0xFF) << 24
                | (buffer.readByte() & 0xFF) << 16
                | (buffer.readByte() & 0xFF) << 8
                | buffer.readByte() & 0xFF;
    }

    /**
     * Read variable-length int, 1-5 byte.
     */
    public static int readVarInt(Buffer buffer) {
        int b = buffer.readByte();
        int result = b & 0x7F;
        if ((b & 0x80) != 0) {
            b = buffer.readByte();
            result |= (b & 0x7F) << 7;
            if ((b & 0x80) != 0) {
                b = buffer.readByte();
                result |= (b & 0x7F) << 14;
                if ((b & 0x80) != 0) {
                    b = buffer.readByte();
                    result |= (b & 0x7F) << 21;
                    if ((b & 0x80) != 0) {
                        b = buffer.readByte();
                        result |= (b & 0x7F) << 28;
                    }
                }
            }
        }

        return result;
    }

    /**
     * Read reverse variable-length int, 1-5 byte.
     */
    public static int readReverseVarInt(Buffer buffer) {
        int b = buffer.readByte();
        int result = b & 0XFE;

        if ((b & 1) != 0) {
            b = buffer.readByte();
            result = (result << 7) | ((b & 0XFE) << 1);

            if ((b & 1) != 0) {
                b = buffer.readByte();
                result = (result << 7) | ((b & 0XFE) << 1);

                if ((b & 1) != 0) {
                    b = buffer.readByte();
                    result = (result << 7) | ((b & 0XFE) << 1);

                    if ((b & 1) != 0) {
                        b = buffer.readByte();
                        result = (result << 4) | ((b & 0XFE) >> 4);
                    }
                }
            }
        }

        return result;
    }


    /**
     * Read zigzag variable-length int, 1-5 byte.
     */
    public static int readZigzagVarInt(Buffer buffer) {
        int result = readVarInt(buffer);
        return (result >>> 1) ^ -(result & 1);
    }

    /**
     * Read fixed-length long, 8 byte.
     */
    public static long readLong(Buffer buffer) {
        return (long) buffer.readByte() << 56
                | (long) (buffer.readByte() & 0xFF) << 48
                | (long) (buffer.readByte() & 0xFF) << 40
                | (long) (buffer.readByte() & 0xFF) << 32
                | (long) (buffer.readByte() & 0xFF) << 24
                | (buffer.readByte() & 0xFF) << 16
                | (buffer.readByte() & 0xFF) << 8
                | buffer.readByte() & 0xFF;
    }

    /**
     * Read variable-length long, 1-9 byte.
     */
    public static long readVarLong(Buffer buffer) {
        int b = buffer.readByte();
        long result = b & 0x7F;
        if ((b & 0x80) != 0) {
            b = buffer.readByte();
            result |= (b & 0x7F) << 7;
            if ((b & 0x80) != 0) {
                b = buffer.readByte();
                result |= (b & 0x7F) << 14;
                if ((b & 0x80) != 0) {
                    b = buffer.readByte();
                    result |= (b & 0x7F) << 21;
                    if ((b & 0x80) != 0) {
                        b = buffer.readByte();
                        result |= (long) (b & 0x7F) << 28;
                        if ((b & 0x80) != 0) {
                            b = buffer.readByte();
                            result |= (long) (b & 0x7F) << 35;
                            if ((b & 0x80) != 0) {
                                b = buffer.readByte();
                                result |= (long) (b & 0x7F) << 42;
                                if ((b & 0x80) != 0) {
                                    b = buffer.readByte();
                                    result |= (long) (b & 0x7F) << 49;
                                    if ((b & 0x80) != 0) {
                                        b = buffer.readByte();
                                        result |= (long) b << 56;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return result;
    }

    /**
     * Read variable-length int, 1-9 byte.
     */
    public static long readReverseVarLong(Buffer buffer) {
        int b = buffer.readByte();
        long result = b & 0XFE;

        if ((b & 1) != 0) {
            b = buffer.readByte();
            result = (result << 7) | ((b & 0XFE) << 1);

            if ((b & 1) != 0) {
                b = buffer.readByte();
                result = (result << 7) | ((b & 0XFE) << 1);

                if ((b & 1) != 0) {
                    b = buffer.readByte();
                    result = (result << 7) | ((b & 0XFE) << 1);

                    if ((b & 1) != 0) {
                        b = buffer.readByte();
                        result = (result << 7) | ((b & 0XFE) << 1);

                        if ((b & 1) != 0) {
                            b = buffer.readByte();
                            result = (result << 7) | ((b & 0XFE) << 1);

                            if ((b & 1) != 0) {
                                b = buffer.readByte();
                                result = (result << 7) | ((b & 0XFE) << 1);

                                if ((b & 1) != 0) {
                                    b = buffer.readByte();
                                    result = (result << 7) | ((b & 0XFE) << 1);

                                    if ((b & 1) != 0) {
                                        b = buffer.readByte();
                                        result = (result << 8) | b;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return result;
    }

    /**
     * Read zigzag variable-length long, 1-9 byte.
     */
    public static long readZigzagVarLong(Buffer buffer) {
        long result = readVarLong(buffer);
        return ((result >>> 1) ^ -(result & 1));
    }

    /**
     * Read fixed-length float, 4 byte.
     */
    public static float readFloat(Buffer buffer) {
        return Float.intBitsToFloat(buffer.readByte() & 0xFF
                | (buffer.readByte() & 0xFF) << 8
                | (buffer.readByte() & 0xFF) << 16
                | (buffer.readByte() & 0xFF) << 24);
    }

    /**
     * Read fixed-length double, 8 byte.
     */
    public static double readDouble(Buffer buffer) {
        return Double.longBitsToDouble(buffer.readByte() & 0xFF
                | (buffer.readByte() & 0xFF) << 8
                | (buffer.readByte() & 0xFF) << 16
                | (long) (buffer.readByte() & 0xFF) << 24
                | (long) (buffer.readByte() & 0xFF) << 32
                | (long) (buffer.readByte() & 0xFF) << 40
                | (long) (buffer.readByte() & 0xFF) << 48
                | (long) buffer.readByte() << 56);
    }

    /**
     * Read string, it is UTF_8 string when the first bit mask of length is 1, or it is ASCII string.
     */
    public static String readString(Buffer buffer) {
        int lengthTag = readVarInt(buffer);

        if (lengthTag == 0) {
            return null;
        }

        if (lengthTag == 2) {
            return "";
        }

        byte[] bytes = buffer.readBytes((lengthTag >>> 1) - 1);

        // ASCII
        if ((lengthTag & 1) == 0) {
            int length = bytes.length;
            char[] chars = new char[length];
            for (int i = 0; i < length; i++) {
                chars[i] = (char) bytes[i];
            }

            return new String(chars);
        }

        // UTF-8
        return new String(bytes, StandardCharsets.UTF_8);
    }

    /**
     * Write boolean, 1 byte.
     */
    public static void writeBool(Buffer buffer, boolean val) {
        buffer.writeByte(val ? 1 : 0);
    }

    /**
     * Write char, 2 byte.
     */
    public static void writeChar(Buffer buffer, char val) {
        writeShort(buffer, (short) val);
    }

    /**
     * Write fixed-length short, 2 byte.
     */
    public static void writeShort(Buffer buffer, int val) {
        buffer.writeByte((byte) (val >>> 8));
        buffer.writeByte((byte) val);
    }

    /**
     * Write variable-length short, 1-3 byte.
     */
    public static int writeVarShort(Buffer buffer, short val) {
        if (val >>> 7 == 0) {
            buffer.writeByte((byte) val);
            return 1;
        }
        if (val >>> 14 == 0) {
            buffer.writeByte((byte) ((val & 0x7F) | 0x80));
            buffer.writeByte((byte) (val >>> 7));
            return 2;
        }

        buffer.writeByte((byte) ((val & 0x7F) | 0x80));
        buffer.writeByte((byte) (val >>> 7 | 0x80));
        buffer.writeByte((byte) (val >>> 14));
        return 3;
    }

    /**
     * Write reverse variable-length short, 1-3 byte.
     */
    public static int writeReverseVarShort(Buffer buffer, int val) {
        if (val << 23 == 0) {
            buffer.writeByte((byte) (val >> 8));
            return 1;
        }

        if (val << 30 == 0) {
            buffer.writeByte((byte) (val >> 8) | 1);
            buffer.writeByte((byte) (val >> 1));
            return 2;
        }

        buffer.writeByte((byte) (val >> 8) | 1);
        buffer.writeByte((byte) (val >> 1) | 1);
        // last two bits.
        buffer.writeByte(((byte) val) << 6);
        return 3;
    }

    /**
     * Write fixed-length int, 4 byte.
     */
    public static void writeInt(Buffer buffer, int val) {
        buffer.writeByte((byte) (val >>> 24));
        buffer.writeByte((byte) (val >>> 16));
        buffer.writeByte((byte) (val >>> 8));
        buffer.writeByte((byte) val);
    }

    /**
     * Write variable-length int, 1-5 byte.
     */
    public static int writeVarInt(Buffer buffer, int val) {
        if (val >>> 7 == 0) {
            buffer.writeByte((byte) val);
            return 1;
        }
        if (val >>> 14 == 0) {
            buffer.writeByte((byte) ((val & 0x7F) | 0x80));
            buffer.writeByte((byte) (val >>> 7));
            return 2;
        }
        if (val >>> 21 == 0) {
            buffer.writeByte((byte) ((val & 0x7F) | 0x80));
            buffer.writeByte((byte) (val >>> 7 | 0x80));
            buffer.writeByte((byte) (val >>> 14));
            return 3;
        }
        if (val >>> 28 == 0) {
            buffer.writeByte((byte) ((val & 0x7F) | 0x80));
            buffer.writeByte((byte) (val >>> 7 | 0x80));
            buffer.writeByte((byte) (val >>> 14 | 0x80));
            buffer.writeByte((byte) (val >>> 21));
            return 4;
        }

        buffer.writeByte((byte) ((val & 0x7F) | 0x80));
        buffer.writeByte((byte) (val >>> 7 | 0x80));
        buffer.writeByte((byte) (val >>> 14 | 0x80));
        buffer.writeByte((byte) (val >>> 21 | 0x80));
        buffer.writeByte((byte) (val >>> 28));
        return 5;
    }

    /**
     * Write reverse variable-length int, 1-5 byte.
     */
    public static int writeReverseVarInt(Buffer buffer, int val) {
        if (val << 7 == 0) {
            buffer.writeByte((byte) (val >> 24));
            return 1;
        }

        if (val << 14 == 0) {
            buffer.writeByte((byte) (val >> 24) | 1);
            buffer.writeByte((byte) (val >> 17));
            return 2;
        }

        if (val << 21 == 0) {
            buffer.writeByte((byte) (val >> 24) | 1);
            buffer.writeByte((byte) (val >> 17) | 1);
            buffer.writeByte((byte) (val >> 10));
            return 3;
        }

        if (val << 28 == 0) {
            buffer.writeByte((byte) (val >> 24) | 1);
            buffer.writeByte((byte) (val >> 17) | 1);
            buffer.writeByte((byte) (val >> 10) | 1);
            buffer.writeByte((byte) (val >> 3));
            return 4;
        }

        buffer.writeByte((byte) (val >> 24) | 1);
        buffer.writeByte((byte) (val >> 17) | 1);
        buffer.writeByte((byte) (val >> 10) | 1);
        buffer.writeByte((byte) (val >> 3) | 1);
        buffer.writeByte((byte) (val << 4));
        return 5;
    }

    /**
     * Write zigzag variable-length int, 1-5 byte.
     */
    public static int writeZigzagVarInt(Buffer buffer, int val) {
        return writeVarInt(buffer, (val << 1) ^ (val >> 31));
    }

    /**
     * Write fixed-length long, 8 byte.
     */
    public static void writeLong(Buffer buffer, long val) {
        buffer.writeByte((byte) (val >>> 56));
        buffer.writeByte((byte) (val >>> 48));
        buffer.writeByte((byte) (val >>> 40));
        buffer.writeByte((byte) (val >>> 32));
        buffer.writeByte((byte) (val >>> 24));
        buffer.writeByte((byte) (val >>> 16));
        buffer.writeByte((byte) (val >>> 8));
        buffer.writeByte((byte) val);
    }

    /**
     * Write variable-length long, 1-9 byte.
     */
    public static int writeVarLong(Buffer buffer, long val) {
        if (val >>> 7 == 0) {
            buffer.writeByte((byte) val);
            return 1;
        }
        if (val >>> 14 == 0) {
            buffer.writeByte((byte) ((val & 0x7F) | 0x80));
            buffer.writeByte((byte) (val >>> 7));
            return 2;
        }
        if (val >>> 21 == 0) {
            buffer.writeByte((byte) ((val & 0x7F) | 0x80));
            buffer.writeByte((byte) (val >>> 7 | 0x80));
            buffer.writeByte((byte) (val >>> 14));
            return 3;
        }
        if (val >>> 28 == 0) {
            buffer.writeByte((byte) ((val & 0x7F) | 0x80));
            buffer.writeByte((byte) (val >>> 7 | 0x80));
            buffer.writeByte((byte) (val >>> 14 | 0x80));
            buffer.writeByte((byte) (val >>> 21));
            return 4;
        }
        if (val >>> 35 == 0) {
            buffer.writeByte((byte) ((val & 0x7F) | 0x80));
            buffer.writeByte((byte) (val >>> 7 | 0x80));
            buffer.writeByte((byte) (val >>> 14 | 0x80));
            buffer.writeByte((byte) (val >>> 21 | 0x80));
            buffer.writeByte((byte) (val >>> 28));
            return 5;
        }
        if (val >>> 42 == 0) {
            buffer.writeByte((byte) ((val & 0x7F) | 0x80));
            buffer.writeByte((byte) (val >>> 7 | 0x80));
            buffer.writeByte((byte) (val >>> 14 | 0x80));
            buffer.writeByte((byte) (val >>> 21 | 0x80));
            buffer.writeByte((byte) (val >>> 28 | 0x80));
            buffer.writeByte((byte) (val >>> 35));
            return 6;
        }
        if (val >>> 49 == 0) {
            buffer.writeByte((byte) ((val & 0x7F) | 0x80));
            buffer.writeByte((byte) (val >>> 7 | 0x80));
            buffer.writeByte((byte) (val >>> 14 | 0x80));
            buffer.writeByte((byte) (val >>> 21 | 0x80));
            buffer.writeByte((byte) (val >>> 28 | 0x80));
            buffer.writeByte((byte) (val >>> 35 | 0x80));
            buffer.writeByte((byte) (val >>> 42));
            return 7;
        }
        if (val >>> 56 == 0) {
            buffer.writeByte((byte) ((val & 0x7F) | 0x80));
            buffer.writeByte((byte) (val >>> 7 | 0x80));
            buffer.writeByte((byte) (val >>> 14 | 0x80));
            buffer.writeByte((byte) (val >>> 21 | 0x80));
            buffer.writeByte((byte) (val >>> 28 | 0x80));
            buffer.writeByte((byte) (val >>> 35 | 0x80));
            buffer.writeByte((byte) (val >>> 42 | 0x80));
            buffer.writeByte((byte) (val >>> 49));
            return 8;
        }

        buffer.writeByte((byte) ((val & 0x7F) | 0x80));
        buffer.writeByte((byte) (val >>> 7 | 0x80));
        buffer.writeByte((byte) (val >>> 14 | 0x80));
        buffer.writeByte((byte) (val >>> 21 | 0x80));
        buffer.writeByte((byte) (val >>> 28 | 0x80));
        buffer.writeByte((byte) (val >>> 35 | 0x80));
        buffer.writeByte((byte) (val >>> 42 | 0x80));
        buffer.writeByte((byte) (val >>> 49 | 0x80));
        buffer.writeByte((byte) (val >>> 56));
        return 9;
    }

    /**
     * Write reverse variable-length long, 1-9 byte.
     */
    public static int writeReverseVarLong(Buffer buffer, long val) {
        if (val << 7 == 0) {
            buffer.writeByte((byte) (val >> 56));
            return 1;
        }

        if (val << 14 == 0) {
            buffer.writeByte((byte) (val >> 56) | 1);
            buffer.writeByte((byte) (val >> 49));
            return 2;
        }

        if (val << 21 == 0) {
            buffer.writeByte((byte) (val >> 56) | 1);
            buffer.writeByte((byte) (val >> 49) | 1);
            buffer.writeByte((byte) (val >> 42));
            return 3;
        }

        if (val << 28 == 0) {
            buffer.writeByte((byte) (val >> 56) | 1);
            buffer.writeByte((byte) (val >> 49) | 1);
            buffer.writeByte((byte) (val >> 42) | 1);
            buffer.writeByte((byte) (val >> 35));
            return 4;
        }

        if (val << 35 == 0) {
            buffer.writeByte((byte) (val >> 56) | 1);
            buffer.writeByte((byte) (val >> 49) | 1);
            buffer.writeByte((byte) (val >> 42) | 1);
            buffer.writeByte((byte) (val >> 35) | 1);
            buffer.writeByte((byte) (val >> 28));
            return 5;
        }

        if (val << 42 == 0) {
            buffer.writeByte((byte) (val >> 56) | 1);
            buffer.writeByte((byte) (val >> 49) | 1);
            buffer.writeByte((byte) (val >> 42) | 1);
            buffer.writeByte((byte) (val >> 35) | 1);
            buffer.writeByte((byte) (val >> 28) | 1);
            buffer.writeByte((byte) (val >> 21));
            return 6;
        }

        if (val << 49 == 0) {
            buffer.writeByte((byte) (val >> 56) | 1);
            buffer.writeByte((byte) (val >> 49) | 1);
            buffer.writeByte((byte) (val >> 42) | 1);
            buffer.writeByte((byte) (val >> 35) | 1);
            buffer.writeByte((byte) (val >> 28) | 1);
            buffer.writeByte((byte) (val >> 21) | 1);
            buffer.writeByte((byte) (val >> 14));
            return 7;
        }

        if (val << 56 == 0) {
            buffer.writeByte((byte) (val >> 56) | 1);
            buffer.writeByte((byte) (val >> 49) | 1);
            buffer.writeByte((byte) (val >> 42) | 1);
            buffer.writeByte((byte) (val >> 35) | 1);
            buffer.writeByte((byte) (val >> 28) | 1);
            buffer.writeByte((byte) (val >> 21) | 1);
            buffer.writeByte((byte) (val >> 14) | 1);
            buffer.writeByte((byte) (val >> 7));
            return 8;
        }


        buffer.writeByte((byte) (val >> 56) | 1);
        buffer.writeByte((byte) (val >> 49) | 1);
        buffer.writeByte((byte) (val >> 42) | 1);
        buffer.writeByte((byte) (val >> 35) | 1);
        buffer.writeByte((byte) (val >> 28) | 1);
        buffer.writeByte((byte) (val >> 21) | 1);
        buffer.writeByte((byte) (val >> 14) | 1);
        buffer.writeByte((byte) (val >> 7) | 1);
        buffer.writeByte((byte) (val));
        return 9;
    }

    /**
     * Write zigzag variable-length long, 1-9 byte.
     */
    public static int writeZigzagVarLong(Buffer buffer, long val) {
        return writeVarLong(buffer, (val << 1) ^ (val >> 63));
    }

    /**
     * Write fixed-length float, 4 byte.
     */
    public static void writeFloat(Buffer buffer, float val) {
        writeInt(buffer, Float.floatToIntBits(val));
    }

    /**
     * Write fixed-length double, 8 byte.
     */
    public static void writeDouble(Buffer buffer, double val) {
        writeLong(buffer, Double.doubleToLongBits(val));
    }

    /**
     * Write string, it is UTF_8 string when the first bit mask of length is 1, or it is ASCII string.
     */
    public static void writeString(Buffer buffer, String val) {
        if (val == null) {
            // length 0 means null
            writeVarInt(buffer, 0);
            return;
        }

        int length = val.length();
        if (length == 0) {
            // length:1 means empty string
            writeVarInt(buffer, 2);
            return;
        }

        // Detect ASCII
        scan:
        if (length > 0 && length < 64) {
            for (int i = 0; i < length; i++) {
                if (val.charAt(i) > 127) break scan;
            }

            // ASCII flag
            buffer.writeByte((length + 1) << 1);
            for (int i = 0; i < length; i++) {
                buffer.writeByte(val.charAt(i));
            }
            return;
        }

        byte[] bytes = val.getBytes(StandardCharsets.UTF_8);
        // UTF-8 flag
        writeVarInt(buffer, ((bytes.length + 1) << 1) | 1);
        buffer.writeBytes(bytes);
    }

}
