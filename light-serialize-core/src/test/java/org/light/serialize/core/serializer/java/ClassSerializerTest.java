package org.light.serialize.core.serializer.java;

import org.junit.Assert;
import org.junit.Test;
import org.light.serialize.core.io.ObjectInput;
import org.light.serialize.core.io.ObjectOutput;
import org.light.serialize.core.serializer.DefaultSerializerFactory;
import org.light.serialize.core.serializer.Serializer;

import java.io.IOException;
import java.util.Map;

/**
 * ClassSerializer test
 * @author alex
 */
public class ClassSerializerTest {

    @Test
    public void test() throws IOException {
        ObjectOutput output = new ObjectOutput();
        ObjectInput input = new ObjectInput(output.buffer());

        output.writeObject(ClassItem.class);
        Assert.assertEquals(ClassItem.class, input.readObject());

        DefaultSerializerFactory.getSharedInstance().register(new ObjectSerializer<ClassItem>(ClassItem.class));


        Map<Class<?>, Serializer<?>> serializers = DefaultSerializerFactory.getSharedInstance().getSerializers();

        long totalBytes = 0;
        for (Class<?> clazz : serializers.keySet()) {
            output.writeObject(clazz);
            totalBytes += output.buffer().readableBytes();
            Assert.assertEquals(clazz, input.readObject());
        }
        System.out.println(totalBytes);// 2402
        Assert.assertEquals(0, output.buffer().readableBytes());

    }

}

class ClassItem {

}

