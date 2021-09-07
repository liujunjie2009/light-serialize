package org.light.serialize.core.serializer.java;

import org.junit.Assert;
import org.junit.Test;
import org.light.serialize.core.io.ObjectInput;
import org.light.serialize.core.io.ObjectOutput;

import java.io.IOException;
import java.net.URL;

/**
 * URLSerializerTest
 *
 * @author alex
 */
public class URLSerializerTest {

    @Test
    public void test() throws IOException {
        ObjectOutput output = new ObjectOutput();
        ObjectInput input = new ObjectInput(output.buffer());

        URL url = new URL("https://www.google.com/");

        output.writeObject(url);

        Object actual = input.readObject();
        Assert.assertEquals(url, actual);
        Assert.assertEquals(0, output.buffer().readableBytes());

    }

}