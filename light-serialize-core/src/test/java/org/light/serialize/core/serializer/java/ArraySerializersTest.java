package org.light.serialize.core.serializer.java;

import org.junit.Assert;
import org.junit.Test;
import org.light.serialize.core.io.ObjectInput;
import org.light.serialize.core.io.ObjectOutput;

import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * ArraySerializers test
 *
 * @author alex
 */
public class ArraySerializersTest {

    @Test
    public void testArraySerializers() throws IOException {
        ObjectOutput output = new ObjectOutput();
        ObjectInput input = new ObjectInput(output.buffer());

        MyArrayClass myArrayClass = new MyArrayClass();
        output.writeObject(myArrayClass);

        Assert.assertEquals(myArrayClass, input.readObject());
        Assert.assertEquals(0, output.buffer().readableBytes());
    }

}

class MyArrayClass {

    private byte[] bytes1 = null;
    private byte[] bytes2 = {-1, 0, 1, Byte.MIN_VALUE, Byte.MAX_VALUE};
    private Byte[] bytes3 = null;
    private Byte[] bytes4 = {-1, 0, 1, Byte.MIN_VALUE, Byte.MAX_VALUE};

    private boolean[] booleans1 = null;
    private boolean[] booleans2 = {false, false, true, true};
    private Boolean[] booleans3 = null;
    private Boolean[] booleans4 = {null, null, false, false, null, null, true, true, null, null, true};

    private char[] chars1 = null;
    private char[] chars2 = {'q', 'w', '<', '>', '龙'};
    private Character[] chars3 = null;
    private Character[] chars4 = {'q', 'w', '<', '>', '龙'};

    private short[] shorts1 = null;
    private short[] shorts2 = {-1, 0, 1, Short.MIN_VALUE, Short.MAX_VALUE};
    private Short[] shorts3 = null;
    private Short[] shorts4 = {-1, 0, 1, Short.MIN_VALUE, Short.MAX_VALUE};

    private int[] ints1 = null;
    private int[] ints2 = {-1, 0, 1, Integer.MIN_VALUE, Integer.MAX_VALUE};
    private Integer[] ints3 = null;
    private Integer[] ints4 = {-1, 0, 1, Integer.MIN_VALUE, Integer.MAX_VALUE};

    private long[] longs1 = null;
    private long[] longs2 = {-1, 0, 1, Long.MIN_VALUE, Long.MAX_VALUE};
    private Long[] longs3 = null;
    private Long[] longs4 = {-1L, 0L, 1L, Long.MIN_VALUE, Long.MAX_VALUE};

    private float[] floats1 = null;
    private float[] floats2 = {-1, 0, 1, Float.MIN_VALUE, Float.MAX_VALUE, Float.NaN};
    private Float[] floats3 = null;
    private Float[] floats4 = {-1F, 0F, 1F, Float.MIN_VALUE, Float.MAX_VALUE, Float.NaN};

    private double[] doubles1 = null;
    private double[] doubles2 = {-1, 0, 1, Double.MIN_VALUE, Double.MAX_VALUE, Double.NaN};
    private Double[] doubles3 = null;
    private Double[] doubles4 = {-1D, 0D, 1D, Double.MIN_VALUE, Double.MAX_VALUE, Double.NaN};

    private String[] strs1 = null;
    private String[] strs2 = {null, "", "null", "My name is alex."};

    private Object[] objs1 = null;
    private Object[] objs2 = {null, Integer.MAX_VALUE, Long.MAX_VALUE, Float.NaN, Double.MAX_VALUE, -1, 0, 1, 'q', "My name is alex.",
    Byte.MAX_VALUE, Short.MAX_VALUE};

    private Class<?>[] clazzs1 = null;
    private Class<?>[] clazzs2 = {Integer.class, Integer.TYPE, Long.class, Long.TYPE, new Object(){}.getClass(),
            String.class, Set.class, List.class, Map.class};

    public byte[] getBytes1() {
        return bytes1;
    }

    public void setBytes1(byte[] bytes1) {
        this.bytes1 = bytes1;
    }

    public byte[] getBytes2() {
        return bytes2;
    }

    public void setBytes2(byte[] bytes2) {
        this.bytes2 = bytes2;
    }

    public Byte[] getBytes3() {
        return bytes3;
    }

    public void setBytes3(Byte[] bytes3) {
        this.bytes3 = bytes3;
    }

    public Byte[] getBytes4() {
        return bytes4;
    }

    public void setBytes4(Byte[] bytes4) {
        this.bytes4 = bytes4;
    }

    public boolean[] getBooleans1() {
        return booleans1;
    }

    public void setBooleans1(boolean[] booleans1) {
        this.booleans1 = booleans1;
    }

    public boolean[] getBooleans2() {
        return booleans2;
    }

    public void setBooleans2(boolean[] booleans2) {
        this.booleans2 = booleans2;
    }

    public Boolean[] getBooleans3() {
        return booleans3;
    }

    public void setBooleans3(Boolean[] booleans3) {
        this.booleans3 = booleans3;
    }

    public Boolean[] getBooleans4() {
        return booleans4;
    }

    public void setBooleans4(Boolean[] booleans4) {
        this.booleans4 = booleans4;
    }

    public char[] getChars1() {
        return chars1;
    }

    public void setChars1(char[] chars1) {
        this.chars1 = chars1;
    }

    public char[] getChars2() {
        return chars2;
    }

    public void setChars2(char[] chars2) {
        this.chars2 = chars2;
    }

    public Character[] getChars3() {
        return chars3;
    }

    public void setChars3(Character[] chars3) {
        this.chars3 = chars3;
    }

    public Character[] getChars4() {
        return chars4;
    }

    public void setChars4(Character[] chars4) {
        this.chars4 = chars4;
    }

    public short[] getShorts1() {
        return shorts1;
    }

    public void setShorts1(short[] shorts1) {
        this.shorts1 = shorts1;
    }

    public short[] getShorts2() {
        return shorts2;
    }

    public void setShorts2(short[] shorts2) {
        this.shorts2 = shorts2;
    }

    public Short[] getShorts3() {
        return shorts3;
    }

    public void setShorts3(Short[] shorts3) {
        this.shorts3 = shorts3;
    }

    public Short[] getShorts4() {
        return shorts4;
    }

    public void setShorts4(Short[] shorts4) {
        this.shorts4 = shorts4;
    }

    public int[] getInts1() {
        return ints1;
    }

    public void setInts1(int[] ints1) {
        this.ints1 = ints1;
    }

    public int[] getInts2() {
        return ints2;
    }

    public void setInts2(int[] ints2) {
        this.ints2 = ints2;
    }

    public Integer[] getInts3() {
        return ints3;
    }

    public void setInts3(Integer[] ints3) {
        this.ints3 = ints3;
    }

    public Integer[] getInts4() {
        return ints4;
    }

    public void setInts4(Integer[] ints4) {
        this.ints4 = ints4;
    }

    public long[] getLongs1() {
        return longs1;
    }

    public void setLongs1(long[] longs1) {
        this.longs1 = longs1;
    }

    public long[] getLongs2() {
        return longs2;
    }

    public void setLongs2(long[] longs2) {
        this.longs2 = longs2;
    }

    public Long[] getLongs3() {
        return longs3;
    }

    public void setLongs3(Long[] longs3) {
        this.longs3 = longs3;
    }

    public Long[] getLongs4() {
        return longs4;
    }

    public void setLongs4(Long[] longs4) {
        this.longs4 = longs4;
    }

    public float[] getFloats1() {
        return floats1;
    }

    public void setFloats1(float[] floats1) {
        this.floats1 = floats1;
    }

    public float[] getFloats2() {
        return floats2;
    }

    public void setFloats2(float[] floats2) {
        this.floats2 = floats2;
    }

    public Float[] getFloats3() {
        return floats3;
    }

    public void setFloats3(Float[] floats3) {
        this.floats3 = floats3;
    }

    public Float[] getFloats4() {
        return floats4;
    }

    public void setFloats4(Float[] floats4) {
        this.floats4 = floats4;
    }

    public double[] getDoubles1() {
        return doubles1;
    }

    public void setDoubles1(double[] doubles1) {
        this.doubles1 = doubles1;
    }

    public double[] getDoubles2() {
        return doubles2;
    }

    public void setDoubles2(double[] doubles2) {
        this.doubles2 = doubles2;
    }

    public Double[] getDoubles3() {
        return doubles3;
    }

    public void setDoubles3(Double[] doubles3) {
        this.doubles3 = doubles3;
    }

    public Double[] getDoubles4() {
        return doubles4;
    }

    public void setDoubles4(Double[] doubles4) {
        this.doubles4 = doubles4;
    }

    public String[] getStrs1() {
        return strs1;
    }

    public void setStrs1(String[] strs1) {
        this.strs1 = strs1;
    }

    public String[] getStrs2() {
        return strs2;
    }

    public void setStrs2(String[] strs2) {
        this.strs2 = strs2;
    }

    public Object[] getObjs1() {
        return objs1;
    }

    public void setObjs1(Object[] objs1) {
        this.objs1 = objs1;
    }

    public Object[] getObjs2() {
        return objs2;
    }

    public void setObjs2(Object[] objs2) {
        this.objs2 = objs2;
    }

    public Class<?>[] getClazzs1() {
        return clazzs1;
    }

    public void setClazzs1(Class<?>[] clazzs1) {
        this.clazzs1 = clazzs1;
    }

    public Class<?>[] getClazzs2() {
        return clazzs2;
    }

    public void setClazzs2(Class<?>[] clazzs2) {
        this.clazzs2 = clazzs2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyArrayClass that = (MyArrayClass) o;
        return Arrays.equals(bytes1, that.bytes1) &&
                Arrays.equals(bytes2, that.bytes2) &&
                Arrays.equals(bytes3, that.bytes3) &&
                Arrays.equals(bytes4, that.bytes4) &&
                Arrays.equals(booleans1, that.booleans1) &&
                Arrays.equals(booleans2, that.booleans2) &&
                Arrays.equals(booleans3, that.booleans3) &&
                Arrays.equals(booleans4, that.booleans4) &&
                Arrays.equals(chars1, that.chars1) &&
                Arrays.equals(chars2, that.chars2) &&
                Arrays.equals(chars3, that.chars3) &&
                Arrays.equals(chars4, that.chars4) &&
                Arrays.equals(shorts1, that.shorts1) &&
                Arrays.equals(shorts2, that.shorts2) &&
                Arrays.equals(shorts3, that.shorts3) &&
                Arrays.equals(shorts4, that.shorts4) &&
                Arrays.equals(ints1, that.ints1) &&
                Arrays.equals(ints2, that.ints2) &&
                Arrays.equals(ints3, that.ints3) &&
                Arrays.equals(ints4, that.ints4) &&
                Arrays.equals(longs1, that.longs1) &&
                Arrays.equals(longs2, that.longs2) &&
                Arrays.equals(longs3, that.longs3) &&
                Arrays.equals(longs4, that.longs4) &&
                Arrays.equals(floats1, that.floats1) &&
                Arrays.equals(floats2, that.floats2) &&
                Arrays.equals(floats3, that.floats3) &&
                Arrays.equals(floats4, that.floats4) &&
                Arrays.equals(doubles1, that.doubles1) &&
                Arrays.equals(doubles2, that.doubles2) &&
                Arrays.equals(doubles3, that.doubles3) &&
                Arrays.equals(doubles4, that.doubles4) &&
                Arrays.equals(strs1, that.strs1) &&
                Arrays.equals(strs2, that.strs2) &&
                Arrays.equals(objs1, that.objs1) &&
                Arrays.equals(objs2, that.objs2) &&
                Arrays.equals(clazzs1, that.clazzs1) &&
                Arrays.equals(clazzs2, that.clazzs2);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(bytes1);
        result = 31 * result + Arrays.hashCode(bytes2);
        result = 31 * result + Arrays.hashCode(bytes3);
        result = 31 * result + Arrays.hashCode(bytes4);
        result = 31 * result + Arrays.hashCode(booleans1);
        result = 31 * result + Arrays.hashCode(booleans2);
        result = 31 * result + Arrays.hashCode(booleans3);
        result = 31 * result + Arrays.hashCode(booleans4);
        result = 31 * result + Arrays.hashCode(chars1);
        result = 31 * result + Arrays.hashCode(chars2);
        result = 31 * result + Arrays.hashCode(chars3);
        result = 31 * result + Arrays.hashCode(chars4);
        result = 31 * result + Arrays.hashCode(shorts1);
        result = 31 * result + Arrays.hashCode(shorts2);
        result = 31 * result + Arrays.hashCode(shorts3);
        result = 31 * result + Arrays.hashCode(shorts4);
        result = 31 * result + Arrays.hashCode(ints1);
        result = 31 * result + Arrays.hashCode(ints2);
        result = 31 * result + Arrays.hashCode(ints3);
        result = 31 * result + Arrays.hashCode(ints4);
        result = 31 * result + Arrays.hashCode(longs1);
        result = 31 * result + Arrays.hashCode(longs2);
        result = 31 * result + Arrays.hashCode(longs3);
        result = 31 * result + Arrays.hashCode(longs4);
        result = 31 * result + Arrays.hashCode(floats1);
        result = 31 * result + Arrays.hashCode(floats2);
        result = 31 * result + Arrays.hashCode(floats3);
        result = 31 * result + Arrays.hashCode(floats4);
        result = 31 * result + Arrays.hashCode(doubles1);
        result = 31 * result + Arrays.hashCode(doubles2);
        result = 31 * result + Arrays.hashCode(doubles3);
        result = 31 * result + Arrays.hashCode(doubles4);
        result = 31 * result + Arrays.hashCode(strs1);
        result = 31 * result + Arrays.hashCode(strs2);
        result = 31 * result + Arrays.hashCode(objs1);
        result = 31 * result + Arrays.hashCode(objs2);
        result = 31 * result + Arrays.hashCode(clazzs1);
        result = 31 * result + Arrays.hashCode(clazzs2);
        return result;
    }
}