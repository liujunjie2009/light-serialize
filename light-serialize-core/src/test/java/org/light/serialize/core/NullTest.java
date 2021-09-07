package org.light.serialize.core;

import org.junit.Assert;
import org.junit.Test;
import org.light.serialize.core.util.SerializeUtil;

import java.io.IOException;
import java.math.BigInteger;

/**
 * @date 2020/12/12
 */
public class NullTest {

    @Test
    public void testNull() throws IOException {
        Object x = 1;
        byte[] nullData = SerializeUtil.serializeToBytes(null);
        Object result = SerializeUtil.deserialize(nullData);
        Assert.assertEquals(result, null);

        BigInteger[] ar = new BigInteger[3];
        System.out.println(ar.getClass().getSuperclass());

        System.out.println(Number[].class.isAssignableFrom(ar.getClass()));

        System.out.println(Object.class.getInterfaces().length);
        System.out.println(ar.getClass().getName());
        System.out.println(ar.getClass().getSuperclass().getName());

        System.out.println(null == null);
    }
}
