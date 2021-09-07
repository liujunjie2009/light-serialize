package org.light.serialize.core;

import org.junit.Test;
import org.light.serialize.core.util.TypeResolver;

import java.io.IOException;
import java.util.Arrays;

/**
 * @date 2020/11/16
 */
public class OtherTest {

    public static final int  BC_DOUBLE_ZERO           = 0x5b;
    public static final int  BC_DOUBLE_ONE            = 0x5c;
    public static final int  BC_DOUBLE_BYTE           = 0x5d;
    public static final int  BC_DOUBLE_SHORT          = 0x5e;
    public static final int  BC_DOUBLE_MILL           = 0x5f;

    @Test
    public void test(){
        System.out.println(int.class.getName());
        System.out.println(Integer.class.getName());
    }

    @Test
    public void testNoNameClass() throws IOException {
        // System.out.println(Float.intBitsToFloat(0xff800000));
        double d = 12.5000;
        long longValue = Double.doubleToLongBits(d);
        System.out.println(longValue);
        byte[] buffer = new byte[8];
        int p = 0;
        buffer[p] = (byte)longValue;
        buffer[p + 1] = (byte)(longValue >>> 8);
        buffer[p + 2] = (byte)(longValue >>> 16);
        buffer[p + 3] = (byte)(longValue >>> 24);
        buffer[p + 4] = (byte)(longValue >>> 32);
        buffer[p + 5] = (byte)(longValue >>> 40);
        buffer[p + 6] = (byte)(longValue >>> 48);
        buffer[p + 7] = (byte)(longValue >>> 56);


        System.out.println(Arrays.toString(buffer));
        System.out.println(Long.toBinaryString(longValue));
        System.out.println(Long.toBinaryString(longValue).length());

        System.out.println(readLong(buffer));

    }

    @Test
    public void testFloatClass() throws IOException {
        // System.out.println(Float.intBitsToFloat(0xff800000));
        float d = 12.5f;
        int intVal = Float.floatToIntBits(d);
        System.out.println(intVal);
        byte[] buffer = new byte[8];
        int p = 0;
        buffer[p] = (byte)intVal;
        buffer[p + 1] = (byte)(intVal >>> 8);
        buffer[p + 2] = (byte)(intVal >>> 16);
        buffer[p + 3] = (byte)(intVal >>> 24);


        System.out.println(Arrays.toString(buffer));
        System.out.println(Long.toBinaryString(intVal));
        System.out.println(Long.toBinaryString(intVal).length());

        System.out.println(readInt(buffer));

    }

    public static long readLong(byte[] buffer) {
        return buffer[0] & 0xFF //
                | (buffer[1] & 0xFF) << 8 //
                | (buffer[2] & 0xFF) << 16 //
                | (long)(buffer[3] & 0xFF) << 24 //
                | (long)(buffer[4] & 0xFF) << 32 //
                | (long)(buffer[5] & 0xFF) << 40 //
                | (long)(buffer[6] & 0xFF) << 48 //
                | (long)buffer[7] << 56;
    }

    public static long readInt(byte[] buffer) {
        return buffer[0] & 0xFF //
                | (buffer[1] & 0xFF) << 8 //
                | (buffer[2] & 0xFF) << 16 //
                | (long)(buffer[3] & 0xFF) << 24; //
    }

}