package org.light.serialize.core.util;

import org.junit.Assert;
import org.junit.Test;
import org.light.serialize.core.buffer.LinkedByteBuffer;

import java.util.ArrayList;
import java.util.List;

import static org.light.serialize.core.util.BufferUtil.*;

/**
 * BufferUtil test
 *
 * @author alex
 */
public class BufferUtilTest {

    @Test
    public void testWriteReadBool() {
        LinkedByteBuffer buffer = LinkedByteBuffer.alloc();
        writeBool(buffer, true);
        writeBool(buffer, false);
        Assert.assertTrue(readBool(buffer));
        Assert.assertFalse(readBool(buffer));
    }

    @Test
    public void testWriteReadChar() {
        LinkedByteBuffer buffer = LinkedByteBuffer.alloc();
        for (int i = Byte.MIN_VALUE; i < Byte.MAX_VALUE; i++) {
            writeChar(buffer, (char) i);
            Assert.assertEquals(readChar(buffer), (char) i);
        }
    }

    @Test
    public void testWriteReadShort() {
        LinkedByteBuffer buffer = LinkedByteBuffer.alloc();
        for (int i = Short.MIN_VALUE; i < Short.MAX_VALUE; i++) {
            writeShort(buffer, (short) i);
            Assert.assertEquals(readShort(buffer), (short) i);
            writeVarShort(buffer, (short) i);
            Assert.assertEquals(readVarShort(buffer), (short) i);
            writeReverseVarShort(buffer, (short) i);
            Assert.assertEquals(readReverseVarShort(buffer), (short) i);
        }
    }

    @Test
    public void testWriteReadInt() {
        LinkedByteBuffer buffer = LinkedByteBuffer.alloc();
        for (int i = Short.MIN_VALUE; i < Short.MAX_VALUE; i++) {
            writeInt(buffer, i);
            Assert.assertEquals(readInt(buffer), i);
            writeVarInt(buffer, i);
            Assert.assertEquals(readVarInt(buffer), i);
            writeReverseVarInt(buffer, i);
            Assert.assertEquals(readReverseVarInt(buffer), i);
            writeZigzagVarInt(buffer, i);
            Assert.assertEquals(readZigzagVarInt(buffer), i);
        }

        List<Integer> bounds = new ArrayList<>(256);
        bounds.add(Integer.MIN_VALUE);
        bounds.add(Integer.MIN_VALUE + 1);
        bounds.add(Integer.MAX_VALUE);
        bounds.add(Integer.MAX_VALUE - 1);
        for (int i = 0; i < 64; i++) {
            bounds.add(1 << i);
            bounds.add((1 << i) + 1);
            bounds.add((1 << i) - 1);
        }

        for (int val : bounds) {
            writeInt(buffer, val);
            Assert.assertEquals(readInt(buffer), val);
            writeVarInt(buffer, val);
            Assert.assertEquals(readVarInt(buffer), val);
            writeReverseVarInt(buffer, val);
            Assert.assertEquals(readReverseVarInt(buffer), val);
            writeZigzagVarInt(buffer, val);
            Assert.assertEquals(readZigzagVarInt(buffer), val);
        }
    }

    @Test
    public void testWriteReadLong() {
        LinkedByteBuffer buffer = LinkedByteBuffer.alloc();
        for (int i = Short.MIN_VALUE; i < Short.MAX_VALUE; i++) {
            writeLong(buffer, i);
            Assert.assertEquals(readLong(buffer), i);
            writeVarLong(buffer, i);
            Assert.assertEquals(readVarLong(buffer), i);
            writeReverseVarLong(buffer, i);
            Assert.assertEquals(readReverseVarLong(buffer), i);
            writeZigzagVarLong(buffer, i);
            Assert.assertEquals(readZigzagVarLong(buffer), i);
        }

        List<Long> bounds = new ArrayList<>(256);
        bounds.add((long) Integer.MIN_VALUE);
        bounds.add((long) Integer.MIN_VALUE + 1);
        bounds.add((long) Integer.MAX_VALUE);
        bounds.add((long) Integer.MAX_VALUE - 1);
        for (int i = 0; i < 64; i++) {
            bounds.add(1L << i);
            bounds.add((1L << i) + 1);
            bounds.add((1L << i) - 1);
        }

        for (long val : bounds) {
            writeLong(buffer, val);
            Assert.assertEquals(readLong(buffer), val);
            writeVarLong(buffer, val);
            Assert.assertEquals(readVarLong(buffer), val);
            writeReverseVarLong(buffer, val);
            Assert.assertEquals(readReverseVarLong(buffer), val);
            writeZigzagVarLong(buffer, val);
            Assert.assertEquals(readZigzagVarLong(buffer), val);
        }
    }

    @Test
    public void testWriteReadFloat() {
        LinkedByteBuffer buffer = LinkedByteBuffer.alloc();
        for (int i = Short.MIN_VALUE; i < Short.MAX_VALUE; i++) {
            float floatVal = Float.valueOf(i + "." + Math.abs(i));
            writeFloat(buffer, floatVal);
            float expected = readFloat(buffer);
            Assert.assertEquals(expected, floatVal, 0);
        }

        float[] bounds = {Float.MIN_VALUE, Float.MIN_VALUE + 1, Float.MAX_VALUE, Float.MAX_VALUE - 1, Float.NaN,
                Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY};
        for (float val : bounds) {
            writeFloat(buffer, val);
            Assert.assertEquals(readFloat(buffer), val, 0);
        }
    }

    @Test
    public void testWriteReadDouble() {
        LinkedByteBuffer buffer = LinkedByteBuffer.alloc();
        for (int i = Short.MIN_VALUE; i < Short.MAX_VALUE; i++) {
            double doubleVal = Double.valueOf(i + "." + Math.abs(i));
            writeDouble(buffer, doubleVal);
            Assert.assertEquals(readDouble(buffer), doubleVal, 0);
        }

        double[] bounds = {Float.MIN_VALUE, Float.MIN_VALUE + 1, Float.MAX_VALUE, Float.MAX_VALUE - 1, Float.NaN,
                Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, Double.MIN_VALUE, Double.MIN_VALUE + 1,
                Double.MAX_VALUE, Double.MAX_VALUE - 1, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY};
        for (double val : bounds) {
            writeDouble(buffer, val);
            Assert.assertEquals(readDouble(buffer), val, 0);
        }
    }

    @Test
    public void testWriteReadString() {
        LinkedByteBuffer buffer = LinkedByteBuffer.alloc();

        writeString(buffer, null);
        Assert.assertNull(readString(buffer));

        writeString(buffer, "");
        Assert.assertEquals(readString(buffer), "");

        writeString(buffer, "hello");
        Assert.assertEquals(readString(buffer), "hello");

        StringBuilder asciiStrBuilder = new StringBuilder(256);
        for (int i = 0; i < Byte.MAX_VALUE; i++) {
            asciiStrBuilder.append((char) i);
        }

        String asciiBuilder = asciiStrBuilder.toString();
        writeString(buffer, asciiBuilder);
        Assert.assertEquals(asciiBuilder, readString(buffer));

        String chineseStr = "你好";
        writeString(buffer, chineseStr);
        Assert.assertEquals(chineseStr, readString(buffer));

        String expressionStr = "😀😃😄😁😆😅😂🤣😇😉😊🙂🙃☺😋😌😍🥰😘😗😙😚🥲🤪😜😝😛🤑😎🤓🥸🧐🤠🥳" +
                "🤗🤡😏😶😐😑😒🙄🤨🤔🤫🤭🤥😳😞😟😠😡🤬😔😕🙁☹😬🥺😣😖😫😩🥱😤😮‍💨😮😱😨😰😯😦😧😢" +
                "😥😪🤤😓😭🤩😵😵‍💫🥴😲🤯🤐😷🤕🤒🤮🤢🤧🥵🥶😶‍🌫️😴💤😈👿👹👺💩👻💀☠👽🤖🎃😺😸😹😻😼😽" +
                "🙀😿😾👐🤲🙌👏🙏🤝👍👎👊✊🤛🤜🤞✌🤘🤟👌🤌🤏👈👉👆👇☝✋🤚🖐🖖👋🤙💪🦾🖕✍🤳💅🦵🦿🦶" +
                "👄🦷👅👂🦻👃👁👀🧠🫀🫁🦴👤👥🗣🫂👶👧🧒👦👩🧑👨👩‍🦱🧑‍🦱👨‍🦱👩‍🦰🧑‍🦰👨‍🦰👱‍♀️👱👱‍♂️👩‍🦳🧑‍🦳👨‍🦳👩‍🦲🧑‍🦲👨‍🦲🧔‍♀️🧔" +
                "🧔‍♂️👵🧓👴👲👳‍♀️👳👳‍♂️🧕👼👸🤴👰👰‍♀️👰‍♂️🤵‍♀️🤵🤵‍♂️🙇‍♀️🙇🙇‍♂️💁‍♀️💁💁‍♂️🙅‍♀️🙅🙅‍♂️🙆‍♀️🙆🙆‍♂️🤷‍♀️🤷" +
                "🤷‍♂️🙋‍♀️🙋🙋‍♂️🤦‍♀️🤦🤦‍♂️🧏‍♀️🧏🧏‍♂️🙎‍♀️🙎🙎‍♂️🙍‍♀️🙍🙍‍♂️💇‍♀️💇💇‍♂️💆‍♀️💆💆‍♂️🤰🤱👩‍🍼🧑‍🍼👨‍🍼🧎‍♀️🧎🧎‍♂️🧍‍♀️🧍" +
                "🧍‍♂️🚶‍♀️🚶🚶‍♂️👩‍🦯🧑‍🦯👨‍🦯🏃‍♀️🏃🏃‍♂️👩‍🦼🧑‍🦼👨‍🦼👩‍🦽🧑‍🦽👨‍🦽💃🕺👫👭👬🧑‍🤝‍🧑👩‍❤️‍👨👩‍❤️‍👩💑👨‍❤️‍👨👩‍❤️‍💋‍👨👩‍❤️‍💋‍👩💏👨‍❤️‍💋‍👨❤" +
                "🧡💛💚💙💜🤎🖤🤍💔❣💕💞💓💗💖💘💝❤️‍🔥❤️‍🩹💟";
        writeString(buffer, expressionStr);
        Assert.assertEquals(expressionStr, readString(buffer));
    }
}