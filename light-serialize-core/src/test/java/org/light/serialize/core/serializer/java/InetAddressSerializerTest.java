package org.light.serialize.core.serializer.java;

import org.junit.Assert;
import org.junit.Test;
import org.light.serialize.core.io.ObjectInput;
import org.light.serialize.core.io.ObjectOutput;

import java.io.IOException;
import java.net.InetAddress;

/**
 * InetAddressSerializerTest
 *
 * @author alex
 */
public class InetAddressSerializerTest {

    @Test
    public void test() throws IOException {
        ObjectOutput output = new ObjectOutput();
        ObjectInput input = new ObjectInput(output.buffer());
        InetAddress address = InetAddress.getByName("www.google.com");

        output.writeObject(address);
        Object actual = input.readObject();
        Assert.assertEquals(address, actual);
        Assert.assertEquals(0, output.buffer().readableBytes());

    }

}