package org.light.serialize.core.serializer.java;

import org.junit.Assert;
import org.junit.Test;
import org.light.serialize.core.io.ObjectInput;
import org.light.serialize.core.io.ObjectOutput;

import java.io.IOException;
import java.util.TimeZone;

/**
 * TimeZoneSerializerTest
 *
 * @author alex
 */
public class TimeZoneSerializerTest {

    @Test
    public void test() throws IOException {
        ObjectOutput output = new ObjectOutput();
        ObjectInput input = new ObjectInput(output.buffer());

        Object timeZone = TimeZone.getTimeZone("GMT+:08:00");
        output.writeObject(timeZone);

        Object actual = input.readObject();
        Assert.assertEquals(timeZone, actual);
        Assert.assertEquals(0, output.buffer().readableBytes());
    }

}