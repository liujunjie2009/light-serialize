package org.light.serialize.core.serializer.java;

import org.junit.Assert;
import org.junit.Test;
import org.light.serialize.core.io.ObjectInput;
import org.light.serialize.core.io.ObjectOutput;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * CharsetSerializerTest
 *
 * @author alex
 */
public class CharsetSerializerTest {


    @Test
    public void test() throws IOException {
        ObjectOutput output = new ObjectOutput();
        ObjectInput input = new ObjectInput(output.buffer());

        Charset[] charsets = {StandardCharsets.ISO_8859_1, StandardCharsets.US_ASCII, StandardCharsets.UTF_8,
                StandardCharsets.UTF_16, StandardCharsets.US_ASCII};

        for (Charset charset : charsets) {
            output.writeObject(charset);

            Object actual = input.readObject();
            Assert.assertEquals(charset, actual);
            Assert.assertEquals(0, output.buffer().readableBytes());
        }

    }

}