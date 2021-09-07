package org.light.serialize.core.serializer.java;

import org.junit.Assert;
import org.junit.Test;
import org.light.serialize.core.io.ObjectInput;
import org.light.serialize.core.io.ObjectOutput;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.Assert.*;

/**
 * BigNumberSerializers test.
 *
 * @author alex
 */
public class BigNumberSerializersTest {

    @Test
    public void testCollectionSerializers() throws IOException {
        ObjectOutput output = new ObjectOutput();
        ObjectInput input = new ObjectInput(output.buffer());

        BigNumberClass myBigNum = new BigNumberClass();
        output.writeObject(myBigNum);

        Object actual = input.readObject();
        System.out.println(actual.equals(myBigNum));
        Assert.assertEquals(myBigNum, actual);
        Assert.assertEquals(0, output.buffer().readableBytes());
    }
}

class BigNumberClass {

    BigInteger o1 = BigInteger.ZERO;
    BigInteger o2 = BigInteger.ONE;
    BigInteger o3 = BigInteger.TEN;
    BigInteger o31 = new BigInteger("" + Integer.MAX_VALUE);
    BigInteger o32 = new BigInteger("" + Integer.MIN_VALUE);

    List<BigInteger> list0 = new ArrayList<>();

    BigDecimal o4 = BigDecimal.ZERO;
    BigDecimal o5 = BigDecimal.ONE;
    BigDecimal o6 = BigDecimal.TEN;
    BigDecimal o61 = new BigDecimal("" + Long.MAX_VALUE);
    BigDecimal o62 = new BigDecimal("" + Long.MIN_VALUE);

    List<BigDecimal> list1 = new ArrayList<>();

    public BigNumberClass() {
        for (int i = Byte.MIN_VALUE; i < Byte.MAX_VALUE; i++) {
            list0.add(new BigInteger("" + i));
            list1.add(new BigDecimal("" + i));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BigNumberClass that = (BigNumberClass) o;
        return Objects.equals(o1, that.o1) &&
                Objects.equals(o2, that.o2) &&
                Objects.equals(o3, that.o3) &&
                Objects.equals(o31, that.o31) &&
                Objects.equals(o32, that.o32) &&
                Objects.equals(list0, that.list0) &&
                Objects.equals(o4, that.o4) &&
                Objects.equals(o5, that.o5) &&
                Objects.equals(o6, that.o6) &&
                Objects.equals(o61, that.o61) &&
                Objects.equals(o62, that.o62) &&
                Objects.equals(list1, that.list1);
    }

    @Override
    public int hashCode() {
        return Objects.hash(o1, o2, o3, o31, o32, list0, o4, o5, o6, o61, o62, list1);
    }
}