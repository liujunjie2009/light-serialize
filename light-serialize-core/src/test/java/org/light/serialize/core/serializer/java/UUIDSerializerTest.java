package org.light.serialize.core.serializer.java;

import org.junit.Assert;
import org.junit.Test;
import org.light.serialize.core.io.ObjectInput;
import org.light.serialize.core.io.ObjectOutput;

import java.io.IOException;
import java.util.Calendar;
import java.util.UUID;

/**
 * UUIDSerializerTest
 *
 * @author alex
 */
public class UUIDSerializerTest {

    @Test
    public void test() throws IOException {
        ObjectOutput output = new ObjectOutput();
        ObjectInput input = new ObjectInput(output.buffer());

        Object uuid = UUID.randomUUID();
        output.writeObject(uuid);

        Object actual = input.readObject();
        Assert.assertEquals(uuid, actual);
        Assert.assertEquals(0, output.buffer().readableBytes());

    }

}