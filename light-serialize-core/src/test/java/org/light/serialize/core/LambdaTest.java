package org.light.serialize.core;

import org.junit.Test;
import org.light.serialize.core.util.SerializeUtil;

import java.util.concurrent.Callable;

/**
 * @date 2021/01/10
 */
public class LambdaTest {

    @Test
    public void testLambda() throws Exception {
        Callable<Integer> lambda1 = (Callable<Integer> & java.io.Serializable)( () -> 111222);
        System.out.println(lambda1.getClass().getName());
        System.out.println(Callable.class.getName());
        byte[] serializeData = SerializeUtil.serializeToBytes(lambda1);
        Callable<Integer> deserializeLambda1 = (Callable<Integer>)SerializeUtil.deserialize(serializeData);

        System.out.println(deserializeLambda1.call());
    }

}
