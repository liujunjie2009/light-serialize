package org.light.serialize.core.io;

import org.junit.Assert;
import org.junit.Test;
import org.light.serialize.core.buffer.Buffer;
import org.light.serialize.core.buffer.LinkedByteBuffer;

import java.io.IOException;
import java.util.Objects;

import static org.light.serialize.core.serializer.DefaultSerializerFactory.*;

/**
 * ObjectOutput test
 *
 * @author alex
 */
public class ObjectOutputInputTest {

    @Test
    public void testBuffer() {
        LinkedByteBuffer buffer = LinkedByteBuffer.alloc();
        Assert.assertEquals(buffer, new ObjectOutput(buffer).buffer());
        Assert.assertEquals(buffer, new ObjectInput(buffer).buffer());
    }

    @Test
    public void testBool() throws IOException {
        ObjectOutput output = new ObjectOutput();
        ObjectInput input = new ObjectInput(output.buffer());

        output.writeBool(true);
        Assert.assertEquals(true, input.readBool());

        output.writeBool(false);
        Assert.assertEquals(false, input.readBool());
    }

    @Test
    public void testByte() throws IOException {
        ObjectOutput output = new ObjectOutput();
        ObjectInput input = new ObjectInput(output.buffer());

        for (int i = Byte.MIN_VALUE; i < Byte.MAX_VALUE; i++) {
            output.writeByte(i);
            Assert.assertEquals(i, input.readByte());
        }
    }

    @Test
    public void testChar() throws IOException {
        ObjectOutput output = new ObjectOutput();
        ObjectInput input = new ObjectInput(output.buffer());

        for (int i = Character.MIN_VALUE; i < Character.MAX_VALUE; i++) {
            output.writeChar((char) i);
            Assert.assertEquals(i, input.readChar());
        }

        output.writeChar('龙');
        Assert.assertEquals('龙', input.readChar());
    }

    @Test
    public void testShort() throws IOException {
        ObjectOutput output = new ObjectOutput();
        ObjectInput input = new ObjectInput(output.buffer());

        for (int i = Short.MIN_VALUE; i < Short.MAX_VALUE; i++) {
            output.writeShort(i);
            Assert.assertEquals(i, input.readShort());
        }
    }

    @Test
    public void testInt() throws IOException {
        ObjectOutput output = new ObjectOutput();
        Buffer buffer = output.buffer();
        ObjectInput input = new ObjectInput(buffer);

        output.writeInt(0);
        Assert.assertEquals(0, input.readInt());
        output.writeInt(-1);
        Assert.assertEquals(-1, input.readInt());
        output.writeInt(1);
        Assert.assertEquals(1, input.readInt());
        output.writeInt(Short.MIN_VALUE);
        Assert.assertEquals(Short.MIN_VALUE, input.readInt());
        output.writeInt(Short.MAX_VALUE);
        Assert.assertEquals(Short.MAX_VALUE, input.readInt());
        output.writeInt(Integer.MIN_VALUE);
        Assert.assertEquals(Integer.MIN_VALUE, input.readInt());
        output.writeInt(Integer.MAX_VALUE);
        Assert.assertEquals(Integer.MAX_VALUE, input.readInt());

        Assert.assertEquals(0, buffer.readableBytes());
    }

    @Test
    public void testVarInt() throws IOException {
        ObjectOutput output = new ObjectOutput();
        Buffer buffer = output.buffer();
        ObjectInput input = new ObjectInput(buffer);

        output.writeVarInt(0);
        Assert.assertEquals(0, input.readVarInt());
        output.writeVarInt(-1);
        Assert.assertEquals(-1, input.readVarInt());
        output.writeVarInt(1);
        Assert.assertEquals(1, input.readVarInt());
        output.writeVarInt(Short.MIN_VALUE);
        Assert.assertEquals(Short.MIN_VALUE, input.readVarInt());
        output.writeVarInt(Short.MAX_VALUE);
        Assert.assertEquals(Short.MAX_VALUE, input.readVarInt());
        output.writeVarInt(Integer.MIN_VALUE);
        Assert.assertEquals(Integer.MIN_VALUE, input.readVarInt());
        output.writeVarInt(Integer.MAX_VALUE);
        Assert.assertEquals(Integer.MAX_VALUE, input.readVarInt());

        // output.writeVarInt(VAR_INT_ADVANTAGE_UPPER_BOUND);
        // Assert.assertEquals(4, buffer.readableBytes());
        // Assert.assertEquals(VAR_INT_ADVANTAGE_UPPER_BOUND, input.readVarInt());
        //
        // output.writeVarInt(VAR_INT_ADVANTAGE_UPPER_BOUND - 1);
        // Assert.assertEquals(3, buffer.readableBytes());
        // Assert.assertEquals(VAR_INT_ADVANTAGE_UPPER_BOUND - 1, input.readVarInt());
        //
        // output.writeVarInt(VAR_INT_ADVANTAGE_LOWER_BOUND);
        // Assert.assertEquals(5, buffer.readableBytes());
        // Assert.assertEquals(VAR_INT_ADVANTAGE_LOWER_BOUND, input.readVarInt());

        Assert.assertEquals(0, buffer.readableBytes());
    }

    @Test
    public void testComplementVarInt() throws IOException {
        // ObjectOutput output = new ObjectOutput();
        // Buffer buffer = output.buffer();
        // ObjectInput input = new ObjectInput(buffer);
        //
        // output.writeComplementVarInt(0);
        // Assert.assertEquals(0, input.readComplementVarInt());
        // output.writeComplementVarInt(-1);
        // Assert.assertEquals(-1, input.readComplementVarInt());
        // output.writeComplementVarInt(1);
        // Assert.assertEquals(1, input.readComplementVarInt());
        // output.writeComplementVarInt(Short.MIN_VALUE);
        // Assert.assertEquals(Short.MIN_VALUE, input.readComplementVarInt());
        // output.writeComplementVarInt(Short.MAX_VALUE);
        // Assert.assertEquals(Short.MAX_VALUE, input.readComplementVarInt());
        // output.writeComplementVarInt(Integer.MIN_VALUE);
        // Assert.assertEquals(Integer.MIN_VALUE, input.readComplementVarInt());
        // output.writeComplementVarInt(Integer.MAX_VALUE);
        // Assert.assertEquals(Integer.MAX_VALUE, input.readComplementVarInt());
        //
        // output.writeComplementVarInt(COMPLEMENT_VAR_INT_ADVANTAGE_UPPER_BOUND);
        // Assert.assertEquals(1, buffer.readableBytes());
        // Assert.assertEquals(COMPLEMENT_VAR_INT_ADVANTAGE_UPPER_BOUND, input.readComplementVarInt());
        //
        // output.writeComplementVarInt(COMPLEMENT_VAR_INT_ADVANTAGE_LOWER_BOUND);
        // Assert.assertEquals(4, buffer.readableBytes());
        // Assert.assertEquals(COMPLEMENT_VAR_INT_ADVANTAGE_LOWER_BOUND, input.readComplementVarInt());
        //
        // output.writeComplementVarInt(COMPLEMENT_VAR_INT_ADVANTAGE_LOWER_BOUND + 1);
        // Assert.assertEquals(3, buffer.readableBytes());
        // Assert.assertEquals(COMPLEMENT_VAR_INT_ADVANTAGE_LOWER_BOUND + 1, input.readComplementVarInt());
        //
        // Assert.assertEquals(0, buffer.readableBytes());
    }

    @Test
    public void testLong() throws IOException {
        ObjectOutput output = new ObjectOutput();
        Buffer buffer = output.buffer();
        ObjectInput input = new ObjectInput(buffer);

        output.writeLong(0);
        Assert.assertEquals(0, input.readLong());
        output.writeLong(-1);
        Assert.assertEquals(-1, input.readLong());
        output.writeLong(1);
        Assert.assertEquals(1, input.readLong());
        output.writeLong(Short.MIN_VALUE);
        Assert.assertEquals(Short.MIN_VALUE, input.readLong());
        output.writeLong(Short.MAX_VALUE);
        Assert.assertEquals(Short.MAX_VALUE, input.readLong());
        output.writeLong(Integer.MIN_VALUE);
        Assert.assertEquals(Integer.MIN_VALUE, input.readLong());
        output.writeLong(Integer.MAX_VALUE);
        Assert.assertEquals(Integer.MAX_VALUE, input.readLong());
        output.writeLong(Long.MIN_VALUE);
        Assert.assertEquals(Long.MIN_VALUE, input.readLong());
        output.writeLong(Long.MAX_VALUE);
        Assert.assertEquals(Long.MAX_VALUE, input.readLong());

        Assert.assertEquals(0, buffer.readableBytes());
    }

    @Test
    public void testVarLong() throws IOException {
        ObjectOutput output = new ObjectOutput();
        Buffer buffer = output.buffer();
        ObjectInput input = new ObjectInput(buffer);

        output.writeVarLong(0);
        Assert.assertEquals(0, input.readVarLong());
        output.writeVarLong(-1);
        Assert.assertEquals(-1, input.readVarLong());
        output.writeVarLong(1);
        Assert.assertEquals(1, input.readVarLong());
        output.writeVarLong(Short.MIN_VALUE);
        Assert.assertEquals(Short.MIN_VALUE, input.readVarLong());
        output.writeVarLong(Short.MAX_VALUE);
        Assert.assertEquals(Short.MAX_VALUE, input.readVarLong());
        output.writeVarLong(Integer.MIN_VALUE);
        Assert.assertEquals(Integer.MIN_VALUE, input.readVarLong());
        output.writeVarLong(Integer.MAX_VALUE);
        Assert.assertEquals(Integer.MAX_VALUE, input.readVarLong());

        // output.writeVarLong(VAR_INT_ADVANTAGE_UPPER_BOUND);
        // Assert.assertEquals(4, buffer.readableBytes());
        // Assert.assertEquals(VAR_INT_ADVANTAGE_UPPER_BOUND, input.readVarLong());
        //
        // output.writeVarLong(VAR_INT_ADVANTAGE_UPPER_BOUND - 1);
        // Assert.assertEquals(3, buffer.readableBytes());
        // Assert.assertEquals(VAR_INT_ADVANTAGE_UPPER_BOUND - 1, input.readVarLong());
        //
        // output.writeVarLong(VAR_INT_ADVANTAGE_LOWER_BOUND);
        // Assert.assertEquals(9, buffer.readableBytes());
        // Assert.assertEquals(VAR_INT_ADVANTAGE_LOWER_BOUND, input.readVarLong());
        //
        // output.writeVarLong(VAR_LONG_ADVANTAGE_UPPER_BOUND);
        // Assert.assertEquals(8, buffer.readableBytes());
        // Assert.assertEquals(VAR_LONG_ADVANTAGE_UPPER_BOUND, input.readVarLong());
        //
        // output.writeVarLong(VAR_LONG_ADVANTAGE_UPPER_BOUND - 1);
        // Assert.assertEquals(7, buffer.readableBytes());
        // Assert.assertEquals(VAR_LONG_ADVANTAGE_UPPER_BOUND - 1, input.readVarLong());
        //
        // output.writeVarLong(VAR_LONG_ADVANTAGE_LOWER_BOUND);
        // Assert.assertEquals(9, buffer.readableBytes());
        // Assert.assertEquals(VAR_LONG_ADVANTAGE_LOWER_BOUND, input.readVarLong());

        Assert.assertEquals(0, buffer.readableBytes());
    }

    @Test
    public void testComplementVarLong() throws IOException {
        ObjectOutput output = new ObjectOutput();
        Buffer buffer = output.buffer();
        ObjectInput input = new ObjectInput(buffer);

        // output.writeComplementVarLong(0);
        // Assert.assertEquals(0, input.readComplementVarLong());
        // output.writeComplementVarLong(-1);
        // Assert.assertEquals(-1, input.readComplementVarLong());
        // output.writeComplementVarLong(1);
        // Assert.assertEquals(1, input.readComplementVarLong());
        // output.writeComplementVarLong(Short.MIN_VALUE);
        // Assert.assertEquals(Short.MIN_VALUE, input.readComplementVarLong());
        // output.writeComplementVarLong(Short.MAX_VALUE);
        // Assert.assertEquals(Short.MAX_VALUE, input.readComplementVarLong());
        // output.writeComplementVarLong(Integer.MIN_VALUE);
        // Assert.assertEquals(Integer.MIN_VALUE, input.readComplementVarLong());
        // output.writeComplementVarLong(Integer.MAX_VALUE);
        // Assert.assertEquals(Integer.MAX_VALUE, input.readComplementVarLong());
        //
        // output.writeComplementVarLong(COMPLEMENT_VAR_INT_ADVANTAGE_UPPER_BOUND);
        // Assert.assertEquals(1, buffer.readableBytes());
        // Assert.assertEquals(COMPLEMENT_VAR_INT_ADVANTAGE_UPPER_BOUND, input.readComplementVarLong());
        //
        // output.writeComplementVarLong(COMPLEMENT_VAR_INT_ADVANTAGE_LOWER_BOUND);
        // Assert.assertEquals(4, buffer.readableBytes());
        // Assert.assertEquals(COMPLEMENT_VAR_INT_ADVANTAGE_LOWER_BOUND, input.readComplementVarLong());
        //
        // output.writeComplementVarLong(COMPLEMENT_VAR_INT_ADVANTAGE_LOWER_BOUND + 1);
        // Assert.assertEquals(3, buffer.readableBytes());
        // Assert.assertEquals(COMPLEMENT_VAR_INT_ADVANTAGE_LOWER_BOUND + 1, input.readComplementVarLong());
        //
        //
        // output.writeComplementVarLong(NEGATIVE_VAR_LONG_ADVANTAGE_UPPER_BOUND);
        // Assert.assertEquals(1, buffer.readableBytes());
        // Assert.assertEquals(NEGATIVE_VAR_LONG_ADVANTAGE_UPPER_BOUND, input.readComplementVarLong());
        //
        // output.writeComplementVarLong(NEGATIVE_VAR_LONG_ADVANTAGE_LOWER_BOUND);
        // Assert.assertEquals(8, buffer.readableBytes());
        // Assert.assertEquals(NEGATIVE_VAR_LONG_ADVANTAGE_LOWER_BOUND, input.readComplementVarLong());
        //
        // output.writeComplementVarLong(NEGATIVE_VAR_LONG_ADVANTAGE_LOWER_BOUND + 1);
        // Assert.assertEquals(7, buffer.readableBytes());
        // Assert.assertEquals(NEGATIVE_VAR_LONG_ADVANTAGE_LOWER_BOUND + 1, input.readComplementVarLong());

        Assert.assertEquals(0, buffer.readableBytes());
    }

    @Test
    public void testFloat() throws IOException {
        ObjectOutput output = new ObjectOutput();
        Buffer buffer = output.buffer();
        ObjectInput input = new ObjectInput(buffer);

        output.writeFloat(0);
        Assert.assertEquals(0, 0, input.readFloat());
        output.writeFloat(-1);
        Assert.assertEquals(-1, input.readFloat(), 0);
        output.writeFloat(1);
        Assert.assertEquals(1, input.readFloat(), 0);
        output.writeFloat(Short.MIN_VALUE);
        Assert.assertEquals(Short.MIN_VALUE, input.readFloat(), 0);
        output.writeFloat(Short.MAX_VALUE);
        Assert.assertEquals(Short.MAX_VALUE, input.readFloat(), 0);
        output.writeFloat(Integer.MIN_VALUE);
        Assert.assertEquals(Integer.MIN_VALUE, input.readFloat(), 0);
        output.writeFloat(Integer.MAX_VALUE);
        Assert.assertEquals(Integer.MAX_VALUE, input.readFloat(), 0);

        output.writeFloat(Float.MAX_VALUE);
        Assert.assertEquals(Float.MAX_VALUE, input.readFloat(), 0);
        output.writeFloat(Float.MIN_EXPONENT);
        Assert.assertEquals(Float.MIN_EXPONENT, input.readFloat(), 0);
        output.writeFloat(Float.NaN);
        Assert.assertEquals(Float.NaN, input.readFloat(), 0);

        for (int i = -100; i < 100; i++) {
            float floatVal = Float.valueOf(i + "." + Math.abs(i));
            output.writeFloat(floatVal);
            Assert.assertEquals(floatVal, input.readFloat(), 0);
        }

        Assert.assertEquals(0, buffer.readableBytes());
    }

    @Test
    public void testVarFloat() throws IOException {
        ObjectOutput output = new ObjectOutput();
        Buffer buffer = output.buffer();
        ObjectInput input = new ObjectInput(buffer);

        // output.writeVarFloat(0);
        // Assert.assertEquals(0, input.readVarFloat(), 0);
        // output.writeVarFloat(-1);
        // Assert.assertEquals(-1, input.readVarFloat(), 0);
        // output.writeVarFloat(1);
        // Assert.assertEquals(1, input.readVarFloat(), 0);
        // output.writeVarFloat(Short.MIN_VALUE);
        // Assert.assertEquals(Short.MIN_VALUE, input.readVarFloat(), 0);
        // output.writeVarFloat(Short.MAX_VALUE);
        // Assert.assertEquals(Short.MAX_VALUE, input.readVarFloat(), 0);
        // output.writeVarFloat(Integer.MIN_VALUE);
        // Assert.assertEquals(Integer.MIN_VALUE, input.readVarFloat(), 0);
        // output.writeVarFloat(Integer.MAX_VALUE);
        // Assert.assertEquals(Integer.MAX_VALUE, input.readVarFloat(), 0);

        // float testFloatUpper1 = Float.intBitsToFloat(Integer.reverse(VAR_INT_ADVANTAGE_UPPER_BOUND));
        // output.writeVarFloat(testFloatUpper1);
        // Assert.assertEquals(4, buffer.readableBytes());
        // Assert.assertEquals(testFloatUpper1, input.readVarFloat(), 0);
        //
        // float testFloatUpper2 = Float.intBitsToFloat(Integer.reverse(VAR_INT_ADVANTAGE_LOWER_BOUND));
        // output.writeVarFloat(testFloatUpper2);
        // Assert.assertEquals(5, buffer.readableBytes());
        // Assert.assertEquals(testFloatUpper2, input.readVarFloat(), 0);
        //
        // float testFloatUpper3 = Float.intBitsToFloat(Integer.reverse(VAR_INT_ADVANTAGE_UPPER_BOUND - 1));
        // output.writeVarFloat(testFloatUpper3);
        // Assert.assertEquals(3, buffer.readableBytes());
        // Assert.assertEquals(testFloatUpper3, input.readVarFloat(), 0);
        //
        // float testFloatUpper4 = Float.intBitsToFloat(Integer.reverse(VAR_INT_ADVANTAGE_LOWER_BOUND + 1));
        // output.writeVarFloat(testFloatUpper4);
        // Assert.assertEquals(1, buffer.readableBytes());
        // Assert.assertEquals(testFloatUpper4, input.readVarFloat(), 0);

        Assert.assertEquals(0, buffer.readableBytes());
    }

    @Test
    public void testDouble() throws IOException {
        ObjectOutput output = new ObjectOutput();
        Buffer buffer = output.buffer();
        ObjectInput input = new ObjectInput(buffer);

        output.writeDouble(0);
        Assert.assertEquals(0, 0, input.readDouble());
        output.writeDouble(-1);
        Assert.assertEquals(-1, input.readDouble(), 0);
        output.writeDouble(1);
        Assert.assertEquals(1, input.readDouble(), 0);
        output.writeDouble(Short.MIN_VALUE);
        Assert.assertEquals(Short.MIN_VALUE, input.readDouble(), 0);
        output.writeDouble(Short.MAX_VALUE);
        Assert.assertEquals(Short.MAX_VALUE, input.readDouble(), 0);
        output.writeDouble(Integer.MIN_VALUE);
        Assert.assertEquals(Integer.MIN_VALUE, input.readDouble(), 0);
        output.writeDouble(Integer.MAX_VALUE);
        Assert.assertEquals(Integer.MAX_VALUE, input.readDouble(), 0);

        output.writeDouble(Double.MAX_VALUE);
        Assert.assertEquals(Double.MAX_VALUE, input.readDouble(), 0);
        output.writeDouble(Float.NaN);
        Assert.assertEquals(Float.NaN, input.readDouble(), 0);
        output.writeDouble(Float.MAX_VALUE);
        Assert.assertEquals(Float.MAX_VALUE, input.readDouble(), 0);
        output.writeDouble(Double.NaN);
        Assert.assertEquals(Double.NaN, input.readDouble(), 0);

        for (int i = -100; i < 100; i++) {
            double doubleVal = Double.valueOf(i + "." + Math.abs(i));
            output.writeDouble(doubleVal);
            Assert.assertEquals(doubleVal, input.readDouble(), 0);
        }

        Assert.assertEquals(0, buffer.readableBytes());
    }

    @Test
    public void testVarDouble() throws IOException {
        ObjectOutput output = new ObjectOutput();
        Buffer buffer = output.buffer();
        ObjectInput input = new ObjectInput(buffer);

        // output.writeVarDouble(0);
        // Assert.assertEquals(0, input.readVarDouble(), 0);
        // output.writeVarDouble(-1);
        // Assert.assertEquals(-1, input.readVarDouble(), 0);
        // output.writeVarDouble(1);
        // Assert.assertEquals(1, input.readVarDouble(), 0);
        // output.writeVarDouble(Short.MIN_VALUE);
        // Assert.assertEquals(Short.MIN_VALUE, input.readVarDouble(), 0);
        // output.writeVarDouble(Short.MAX_VALUE);
        // Assert.assertEquals(Short.MAX_VALUE, input.readVarDouble(), 0);
        // output.writeVarDouble(Integer.MIN_VALUE);
        // Assert.assertEquals(Integer.MIN_VALUE, input.readVarDouble(), 0);
        // output.writeVarDouble(Integer.MAX_VALUE);
        // Assert.assertEquals(Integer.MAX_VALUE, input.readVarDouble(), 0);

        // double testDoubleUpper1 = Double.longBitsToDouble(Long.reverse(VAR_LONG_ADVANTAGE_UPPER_BOUND));
        // output.writeVarDouble(testDoubleUpper1);
        // Assert.assertEquals(8, buffer.readableBytes());
        // Assert.assertEquals(testDoubleUpper1, input.readVarDouble(), 0);
        //
        // double testDoubleUpper2 = Double.longBitsToDouble(Long.reverse(VAR_LONG_ADVANTAGE_LOWER_BOUND));
        // output.writeVarDouble(testDoubleUpper2);
        // Assert.assertEquals(9, buffer.readableBytes());
        // Assert.assertEquals(testDoubleUpper2, input.readVarDouble(), 0);
        //
        // double testDoubleUpper3 = Double.longBitsToDouble(Long.reverse(VAR_LONG_ADVANTAGE_UPPER_BOUND - 1));
        // output.writeVarDouble(testDoubleUpper3);
        // Assert.assertEquals(7, buffer.readableBytes());
        // Assert.assertEquals(testDoubleUpper3, input.readVarDouble(), 0);
        //
        // double testDoubleUpper4 = Double.longBitsToDouble(Long.reverse(VAR_LONG_ADVANTAGE_LOWER_BOUND + 1));
        // output.writeVarDouble(testDoubleUpper4);
        // Assert.assertEquals(1, buffer.readableBytes());
        // Assert.assertEquals(testDoubleUpper4, input.readVarDouble(), 0);

        Assert.assertEquals(0, buffer.readableBytes());
    }

    @Test
    public void testBytes() throws IOException {
        ObjectOutput output = new ObjectOutput();
        Buffer buffer = output.buffer();
        ObjectInput input = new ObjectInput(buffer);

        byte[] bytes = null;

        Assert.assertThrows(IllegalArgumentException.class, () -> {
            output.writeBytes(null);
        });

        bytes = new byte[0];
        output.writeBytes(bytes);
        Assert.assertArrayEquals(bytes, input.readBytes(0));

        bytes = new byte[]{1, 2, 3, Byte.MAX_VALUE, Byte.MIN_VALUE, -1, 1, 0};
        output.writeBytes(bytes);
        Assert.assertArrayEquals(bytes, input.readBytes(bytes.length));
        output.writeBytes(bytes, 1, bytes.length - 1);

        byte[] destBytes = new byte[bytes.length - 1];
        input.readBytes(destBytes, 0, bytes.length - 1);
        for (int i = 0; i < bytes.length - 1; i++) {
            Assert.assertEquals(bytes[i + 1], destBytes[i]);
        }

        Assert.assertEquals(0, buffer.readableBytes());
    }

    @Test
    public void testString() throws IOException {
        ObjectOutput output = new ObjectOutput();
        Buffer buffer = output.buffer();
        ObjectInput input = new ObjectInput(buffer);

        String testStr = null;
        output.writeString(testStr);
        Assert.assertEquals(testStr, input.readString());

        testStr = "!@#$%^&*()_+1234567890`qwertyuiop[]{}asdfghjkl;':zxcvbnm,./<>?|";
        output.writeString(testStr);
        Assert.assertEquals(testStr, input.readString());

        testStr = "My name is alex";
        output.writeString(testStr);
        Assert.assertEquals(testStr, input.readString());

        testStr = "我的名字是alex";
        output.writeString(testStr);
        Assert.assertEquals(testStr, input.readString());

        Assert.assertEquals(0, buffer.readableBytes());
    }

    @Test
    public void testObject() throws IOException {
        ObjectOutput output = new ObjectOutput();
        Buffer buffer = output.buffer();
        ObjectInput input = new ObjectInput(buffer);

        Object testObj = new MyClass("alex", 28);

        output.writeObject(testObj);
        Assert.assertEquals(testObj, input.readObject());
    }

}

class MyClass {
    private String strVal;
    private int intVal;

    public MyClass(String strVal, int intVal) {
        this.strVal = strVal;
        this.intVal = intVal;
    }

    public String getStrVal() {
        return strVal;
    }

    public void setStrVal(String strVal) {
        this.strVal = strVal;
    }

    public int getIntVal() {
        return intVal;
    }

    public void setIntVal(int intVal) {
        this.intVal = intVal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyClass myClass = (MyClass) o;
        return intVal == myClass.intVal &&
                Objects.equals(strVal, myClass.strVal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(strVal, intVal);
    }
}