package org.light.serialize.core.util;

import org.junit.Assert;
import org.junit.Test;
import org.light.serialize.core.buffer.LinkedByteBuffer;

import static org.light.serialize.core.util.BufferUtil.*;

/**
 * BufferUtil test
 *
 * @author alex
 */
public class BufferUtilTest {

    private static final long[] longArray = {-1, 6305512404777178521L, -149250664738802268L, -7512732061384969157L, -6890220891978031564L, -718887517233981794L, -9145578169762338939L, -8299652195991305653L, -3678064849912537449L, 123598630025901706L, -6353381134012744599L, -7833830815400630381L, 7716687469028253537L, 8145128924466159385L, 3068366191465575669L, 5454602903149636570L, 7818991702724107164L, 2841864352755611310L, -8812001961640259700L, -7632512846822695675L, -6927353102227825787L, -5994445847729675952L, 7598423622942254562L, 3039731498643049434L, 912257043042009016L};

    @Test
    public void testWriteChar() {
        LinkedByteBuffer buffer = LinkedByteBuffer.alloc();
        for (int i = Byte.MIN_VALUE; i < Byte.MAX_VALUE; i++) {
            writeChar(buffer, (char) i);
            Assert.assertEquals(readChar(buffer), (char) i);
        }
    }

    @Test
    public void testWriteShort() {
        LinkedByteBuffer buffer = LinkedByteBuffer.alloc();
        for (int i = Short.MIN_VALUE; i < Short.MAX_VALUE; i++) {
            writeShort(buffer, (short) i);
            Assert.assertEquals(readShort(buffer), (short) i);
        }
    }

    @Test
    public void testWriteVarShort() {
        LinkedByteBuffer buffer = LinkedByteBuffer.alloc();
        for (int i = Short.MIN_VALUE; i < Short.MAX_VALUE; i++) {
            writeVarShort(buffer, (short) i);
            Assert.assertEquals(readVarShort(buffer), (short) i);
        }
    }

    @Test
    public void testWriteInt() {
        LinkedByteBuffer buffer = LinkedByteBuffer.alloc();
        for (int i = Short.MIN_VALUE; i < Short.MAX_VALUE; i++) {
            writeInt(buffer, i);
            Assert.assertEquals(readInt(buffer), i);
        }

        writeInt(buffer, Integer.MIN_VALUE);
        Assert.assertEquals(readInt(buffer), Integer.MIN_VALUE);

        writeInt(buffer, Integer.MAX_VALUE);
        Assert.assertEquals(readInt(buffer), Integer.MAX_VALUE);
    }

    @Test
    public void testWriteReadVarInt() {
        LinkedByteBuffer buffer = LinkedByteBuffer.alloc();
        for (int i = Short.MIN_VALUE; i < Short.MAX_VALUE; i++) {
            writeVarInt(buffer, i);
            Assert.assertEquals(readVarInt(buffer), i);
        }

        writeVarInt(buffer, Integer.MIN_VALUE);
        Assert.assertEquals(readVarInt(buffer), Integer.MIN_VALUE);

        writeVarInt(buffer, Integer.MAX_VALUE);
        Assert.assertEquals(readVarInt(buffer), Integer.MAX_VALUE);
    }

    @Test
    public void testWriteReadComplementVarInt() {
        // LinkedByteBuffer buffer = LinkedByteBuffer.alloc();
        // for (int i = Short.MIN_VALUE; i < Short.MAX_VALUE; i++) {
        //     writeComplementVarInt(buffer, i);
        //     Assert.assertEquals(readComplementVarInt(buffer), i);
        // }
        //
        // writeComplementVarInt(buffer, Integer.MIN_VALUE);
        // Assert.assertEquals(readComplementVarInt(buffer), Integer.MIN_VALUE);
        //
        // writeComplementVarInt(buffer, Integer.MAX_VALUE);
        // Assert.assertEquals(readComplementVarInt(buffer), Integer.MAX_VALUE);
    }

    @Test
    public void testWriteReadZigzagVarInt() {
        LinkedByteBuffer buffer = LinkedByteBuffer.alloc();
        for (int i = Short.MIN_VALUE; i < Short.MAX_VALUE; i++) {
            writeZigzagVarInt(buffer, i);
            Assert.assertEquals(readZigzagVarInt(buffer), i);
        }

        writeZigzagVarInt(buffer, Integer.MIN_VALUE);
        Assert.assertEquals(readZigzagVarInt(buffer), Integer.MIN_VALUE);

        writeZigzagVarInt(buffer, Integer.MAX_VALUE);
        Assert.assertEquals(readZigzagVarInt(buffer), Integer.MAX_VALUE);
    }

    @Test
    public void testWriteReadLong() {
        LinkedByteBuffer buffer = LinkedByteBuffer.alloc();
        for (int i = Short.MIN_VALUE; i < Short.MAX_VALUE; i++) {
            writeLong(buffer, i);
            Assert.assertEquals(readLong(buffer), i);
        }

        writeLong(buffer, Long.MIN_VALUE);
        Assert.assertEquals(readLong(buffer), Long.MIN_VALUE);

        writeLong(buffer, Long.MAX_VALUE);
        Assert.assertEquals(readLong(buffer), Long.MAX_VALUE);
    }

    @Test
    public void testWriteReadVarLong() {
        LinkedByteBuffer buffer = LinkedByteBuffer.alloc();
        for (int i = Short.MIN_VALUE; i < Short.MAX_VALUE; i++) {
            writeVarLong(buffer, i);
            Assert.assertEquals(readVarLong(buffer), i);
        }

        writeVarLong(buffer, Long.MIN_VALUE);
        Assert.assertEquals(readVarLong(buffer), Long.MIN_VALUE);

        writeVarLong(buffer, Long.MAX_VALUE);
        Assert.assertEquals(readVarLong(buffer), Long.MAX_VALUE);

        long testLongVal1 = (1l << 41);
        writeVarLong(buffer, testLongVal1);
        Assert.assertEquals(readVarLong(buffer), testLongVal1);

        long testLongVal2 = (1l << 48);
        writeVarLong(buffer, testLongVal2);
        Assert.assertEquals(readVarLong(buffer), testLongVal2);

        for (int i = 0; i < longArray.length; i++) {
            long l = longArray[i];
            writeVarLong(buffer, l);
            Assert.assertEquals(readVarLong(buffer), l);
        }

    }

    @Test
    public void testWriteReadComplementVarLong() {
        // LinkedByteBuffer buffer = LinkedByteBuffer.alloc();
        // for (int i = Short.MIN_VALUE; i < Short.MAX_VALUE; i++) {
        //     writeComplementVarLong(buffer, i);
        //     Assert.assertEquals(readComplementVarLong(buffer), i);
        // }
        //
        // writeComplementVarLong(buffer, Long.MIN_VALUE);
        // Assert.assertEquals(readComplementVarLong(buffer), Long.MIN_VALUE);
        //
        // writeComplementVarLong(buffer, Long.MAX_VALUE);
        // Assert.assertEquals(readComplementVarLong(buffer), Long.MAX_VALUE);
    }

    @Test
    public void testWriteReadZigzagVarLong() {
        LinkedByteBuffer buffer = LinkedByteBuffer.alloc();
        for (int i = Short.MIN_VALUE; i < Short.MAX_VALUE; i++) {
            writeZigzagVarLong(buffer, i);
            Assert.assertEquals(readZigzagVarLong(buffer), i);
        }

        writeZigzagVarLong(buffer, Long.MIN_VALUE);
        Assert.assertEquals(readZigzagVarLong(buffer), Long.MIN_VALUE);

        writeZigzagVarLong(buffer, Long.MAX_VALUE);
        Assert.assertEquals(readZigzagVarLong(buffer), Long.MAX_VALUE);

        for (int i = 0; i < longArray.length; i++) {
            long l = longArray[i];
            writeZigzagVarLong(buffer, l);
            Assert.assertEquals(readZigzagVarLong(buffer), l);
        }

        System.out.println(buffer.readableBytes());
    }

    @Test
    public void testWriteReadFloat() {
        LinkedByteBuffer buffer = LinkedByteBuffer.alloc();
        for (int i = Short.MIN_VALUE; i < Short.MAX_VALUE; i++) {
            float floatVal = Float.valueOf(i + "." + Math.abs(i));
            writeFloat(buffer, floatVal);
            Assert.assertEquals(readFloat(buffer), floatVal, 0);
        }

        writeFloat(buffer, Float.MIN_VALUE);
        Assert.assertEquals(0, readFloat(buffer), Float.MIN_VALUE);

        writeFloat(buffer, Float.MAX_VALUE);
        Assert.assertEquals(0, readFloat(buffer), Float.MAX_VALUE);

        writeFloat(buffer, Float.NaN);
        Assert.assertEquals(readFloat(buffer), Float.NaN, 0);
    }

    @Test
    public void testWriteReadVarFloat() {
        LinkedByteBuffer buffer = LinkedByteBuffer.alloc();
        // for (int i = Short.MIN_VALUE; i < Short.MAX_VALUE; i++) {
        //     float floateVal = Float.valueOf(i + "." + Math.abs(i));
        //     writeVarFloat(buffer, floateVal);
        //     Assert.assertEquals(readVarFloat(buffer), floateVal, 0);
        // }
        //
        // writeVarFloat(buffer, Float.MIN_VALUE);
        // Assert.assertEquals(0, readVarFloat(buffer), Float.MIN_VALUE);
        //
        // writeVarFloat(buffer, Float.MAX_VALUE);
        // Assert.assertEquals(0, readVarFloat(buffer), Float.MAX_VALUE);
        //
        // writeVarFloat(buffer, Float.NaN);
        // Assert.assertEquals(readVarFloat(buffer), Float.NaN, 0);
    }

    @Test
    public void testWriteReadDouble() {
        LinkedByteBuffer buffer = LinkedByteBuffer.alloc();
        for (int i = Short.MIN_VALUE; i < Short.MAX_VALUE; i++) {
            double doubleVal = Double.valueOf(i + "." + Math.abs(i));
            writeDouble(buffer, doubleVal);
            Assert.assertEquals(readDouble(buffer), doubleVal, 0);
        }

        writeDouble(buffer, Double.MIN_VALUE);
        Assert.assertEquals(0, readDouble(buffer), Double.MIN_VALUE);

        writeDouble(buffer, Double.MAX_VALUE);
        Assert.assertEquals(0, readDouble(buffer), Double.MAX_VALUE);

        writeDouble(buffer, Double.NaN);
        Assert.assertEquals(readDouble(buffer), Double.NaN, 0);
    }

    @Test
    public void testWriteReadVarDouble() {
        LinkedByteBuffer buffer = LinkedByteBuffer.alloc();
        // for (int i = 0; i < Short.MAX_VALUE; i++) {
        //     double doubleVal = Double.valueOf(i + "." + i);
        //     writeVarDouble(buffer, doubleVal);
        //     Assert.assertEquals(readVarDouble(buffer), doubleVal, 0);
        // }
        //
        // writeVarDouble(buffer, Double.MIN_VALUE);
        // Assert.assertEquals(0, readVarDouble(buffer), Double.MIN_VALUE);
        //
        // writeVarDouble(buffer, Double.MAX_VALUE);
        // Assert.assertEquals(0, readVarDouble(buffer), Double.MAX_VALUE);
        //
        // writeVarDouble(buffer, Double.NaN);
        // Assert.assertEquals(readVarDouble(buffer), Double.NaN, 0);
    }

    @Test
    public void testWriteReadString() {
        LinkedByteBuffer buffer = LinkedByteBuffer.alloc();

        writeString(buffer, null);
        String nullStr = readString(buffer);
        Assert.assertNull(nullStr);

        writeString(buffer, "");
        String emptyStr = readString(buffer);
        Assert.assertEquals(emptyStr, "");

        String asciiUtf8tr = "0123456789~!@#$%^&*()";
        writeString(buffer, asciiUtf8tr);
        Assert.assertEquals(asciiUtf8tr, readString(buffer));

        String testUtf8tr = asciiUtf8tr + "qwertyuiopasdfghjklzxcvbnm,.<>?mnbvcxzlkjhgfdsapoiuytrewq";
        writeString(buffer, testUtf8tr);
        Assert.assertEquals(testUtf8tr, readString(buffer));
    }
}