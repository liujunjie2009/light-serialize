package org.light.serialize.core.serializer.java;

import org.junit.Assert;
import org.junit.Test;
import org.light.serialize.core.io.ObjectInput;
import org.light.serialize.core.io.ObjectOutput;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import static org.junit.Assert.*;

/**
 * URISerializerTest
 *
 * @author alex
 */
public class URISerializerTest {

    @Test
    public void test() throws IOException, URISyntaxException {
        ObjectOutput output = new ObjectOutput();
        ObjectInput input = new ObjectInput(output.buffer());

        URI uri = new URI("https://www.google.com");

        output.writeObject(uri);

        Object actual = input.readObject();
        Assert.assertEquals(uri, actual);
        Assert.assertEquals(0, output.buffer().readableBytes());

    }
}