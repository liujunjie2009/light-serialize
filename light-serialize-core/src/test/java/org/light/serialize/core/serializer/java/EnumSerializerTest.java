package org.light.serialize.core.serializer.java;

import org.junit.Assert;
import org.junit.Test;
import org.light.serialize.core.constants.Strategy;
import org.light.serialize.core.io.ObjectInput;
import org.light.serialize.core.io.ObjectOutput;
import org.light.serialize.core.io.WriteContext;

import java.io.IOException;

/**
 * EnumSerializer test
 *
 * @author alex
 */
public class EnumSerializerTest {

    @Test
    public void testEnumSerializers() throws IOException {
        ObjectOutput output = new ObjectOutput();
        ObjectInput input = new ObjectInput(output.buffer());

        output.writeObject(EnumItem.E1);
        Object actual = input.readObject();
        Assert.assertEquals(EnumItem.E1, actual);
        Assert.assertEquals(0, output.buffer().readableBytes());

        WriteContext.get().setStrategy(Strategy.ORDER);
        output.writeObject(EnumItem.E1);
        Object actual2 = input.readObject();
        Assert.assertEquals(EnumItem.E1, actual2);
        Assert.assertEquals(0, output.buffer().readableBytes());
    }

}

enum EnumItem {

    E1{},
    E2,
    E3,
    E4,
    E5;
}
