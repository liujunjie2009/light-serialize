package org.light.serialize.core.util;

import org.light.serialize.core.buffer.Buffer;

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
        int result = (b & 0XFE) << 8;

        if ((b & 1) != 0) {
            b = buffer.readByte();
            result |= ((b & 0XFE) << 1);

            if ((b & 1) != 0) {
                b = buffer.readByte();
                result |=  b;
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
     * Read char, 1-3 byte.
     */
    public static char readUtf8Char(Buffer buffer) {
        int c = buffer.readByte();
        if ((c & 0X80) == 0) {
            return (char) c;
        } else if ((c & 0XE0) == 0XE0) {
            return (char) (((c & 0X0F) << 12) + ((buffer.readByte() & 0X3F) << 6) + (buffer.readByte() & 0X3F));
        } /*else if ((c & 0XF0) == 0XE0) {
                chars[i] = (char) (((c & 0X1F) << 6) + (buffer.readByte() & 0X3F));
            } */else {
            // throw error("bad utf-8 encoding at " + codeName(ch));
            return (char) (((c & 0X1F) << 6) + (buffer.readByte() & 0X3F));
        }
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
        int result = (b & 0XFE) << 24;

        if ((b & 1) != 0) {
            b = buffer.readByte();
            result |= ((b & 0XFE) << 17);

            if ((b & 1) != 0) {
                b = buffer.readByte();
                result |= ((b & 0XFE) << 10);

                if ((b & 1) != 0) {
                    b = buffer.readByte();
                    result |= ((b & 0XFE) << 3);

                    if ((b & 1) != 0) {
                        b = buffer.readByte();
                        result |= b;
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
        long b = buffer.readByte();
        long result = (b & 0XFE) << 56;

        if ((b & 1) != 0) {
            b = buffer.readByte();
            result |= ((b & 0XFE) << 49);

            if ((b & 1) != 0) {
                b = buffer.readByte();
                result |= ((b & 0XFE) << 42);

                if ((b & 1) != 0) {
                    b = buffer.readByte();
                    result |= ((b & 0XFE) << 35);

                    if ((b & 1) != 0) {
                        b = buffer.readByte();
                        result |= ((b & 0XFE) << 28);

                        if ((b & 1) != 0) {
                            b = buffer.readByte();
                            result |= ((b & 0XFE) << 21);

                            if ((b & 1) != 0) {
                                b = buffer.readByte();
                                result |= ((b & 0XFE) << 14);

                                if ((b & 1) != 0) {
                                    b = buffer.readByte();
                                    result |= ((b & 0XFE) << 7);

                                    if ((b & 1) != 0) {
                                        b = buffer.readByte();
                                        result |= (b & 0XFF);
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
        return Float.intBitsToFloat(readInt(buffer));
    }

    /**
     * Read fixed-length double, 8 byte.
     */
    public static double readDouble(Buffer buffer) {
        return Double.longBitsToDouble(readLong(buffer));
    }

    /**
     * Read UTF_8 string.
     */
    public static String readString(Buffer buffer) {
        int lengthTag = readVarInt(buffer);

        if (lengthTag == 0) {
            return null;
        }

        if (lengthTag == 1) {
            return "";
        }

        int length = lengthTag - 1;
        char[] chars = new char[length];
        for (int i = 0; i < length; i++) {
            chars[i] = readUtf8Char(buffer);
        }

        return new String(chars);
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
     * Write char, 1-3 byte.
     */
    public static void writeUtf8Char(Buffer buffer, char val) {
        if (val < 0X80) {
            buffer.writeByte(val);
        } else if (val < 0X800) {
            buffer.writeByte(0xC0 | ((val >> 6) & 0x1F));
            buffer.writeByte(0x80 | (val & 0x3F));
        } else {
            buffer.writeByte(0xE0 | ((val >> 12) & 0x0F));
            buffer.writeByte(0x80 | ((val >> 6) & 0x3F));
            buffer.writeByte(0x80 | (val & 0x3F));
        }
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
        // last two bit.
        buffer.writeByte(((byte) (val & 3)));
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
        // last four bit.
        buffer.writeByte((byte) (val & 15));
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
     * Write UTF_8 string.
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
            writeVarInt(buffer, 1);
            return;
        }

        writeVarInt(buffer, length + 1);

        for (int i = 0; i < length; i++) {
            writeUtf8Char(buffer, val.charAt(i));
        }
    }

}
