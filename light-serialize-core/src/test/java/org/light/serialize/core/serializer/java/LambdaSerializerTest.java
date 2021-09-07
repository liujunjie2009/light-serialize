package org.light.serialize.core.serializer.java;

import org.junit.Assert;
import org.junit.Test;
import org.light.serialize.core.io.ObjectInput;
import org.light.serialize.core.io.ObjectOutput;

import java.util.concurrent.Callable;
import java.util.function.BiFunction;

/**
 * LambdaSerializer test
 * @author alex
 */
public class LambdaSerializerTest {

    @Test
    public void testLambdaSerializers() throws Exception {
        ObjectOutput output = new ObjectOutput();
        ObjectInput input = new ObjectInput(output.buffer());

        LambdaItem lambdaItem1 = new LambdaItem(null, 100);
        LambdaItem lambdaItem2 = new LambdaItem(lambdaItem1, 100);

        BiFunction<LambdaItem, LambdaItem, Integer> lambda1 = (BiFunction<LambdaItem, LambdaItem, Integer> & java.io.Serializable)
                ( (arg1, arg2) -> arg1.intVal + arg2.intVal);
        output.writeObject(lambda1);

        BiFunction<LambdaItem, LambdaItem, Integer> actual = (BiFunction<LambdaItem, LambdaItem, Integer>)input.readObject();
        Assert.assertEquals(lambda1.apply(lambdaItem1, lambdaItem2), actual.apply(lambdaItem1, lambdaItem2));

        BiFunction<LambdaItem, LambdaItem, Integer> lambda2 = (BiFunction<LambdaItem, LambdaItem, Integer> & java.io.Serializable)
                ( (arg1, arg2) -> arg1.intVal + arg2.intVal);
        output.writeObject(lambda1);

        BiFunction<LambdaItem, LambdaItem, Integer> actual2 = (BiFunction<LambdaItem, LambdaItem, Integer>)input.readObject();
        Assert.assertEquals(lambda2.apply(lambdaItem1, lambdaItem2), actual2.apply(lambdaItem1, lambdaItem2));

        Assert.assertEquals(0, output.buffer().readableBytes());
    }
}

class LambdaItem {
    Object pre;
    Integer intVal;

    public LambdaItem(Object pre, Integer intVal) {
        this.pre = pre;
        this.intVal = intVal;
    }
}