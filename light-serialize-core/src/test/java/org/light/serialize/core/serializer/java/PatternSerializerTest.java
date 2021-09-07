package org.light.serialize.core.serializer.java;

import org.junit.Assert;
import org.junit.Test;
import org.light.serialize.core.constants.Strategy;
import org.light.serialize.core.io.ObjectInput;
import org.light.serialize.core.io.ObjectOutput;
import org.light.serialize.core.io.WriteContext;
import org.light.serialize.core.util.SerializeUtil;

import java.io.IOException;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

/**
 * RegexSerializer test
 *
 * @author alex
 */
public class PatternSerializerTest {

    @Test
    public void test() throws IOException {
        ObjectOutput output = new ObjectOutput();
        ObjectInput input = new ObjectInput(output.buffer());
        Pattern regex = Pattern.compile("[0-9]+$");

        output.writeObject(regex);
        Pattern actual = (Pattern)input.readObject();
        Assert.assertEquals(regex.pattern(), actual.pattern());
        Assert.assertEquals(0, output.buffer().readableBytes());

        regex = Pattern.compile("([1][3][5]|[1][5][3])[0-9]{8}");
        output.writeObject(regex);
        actual = (Pattern)input.readObject();
        Assert.assertEquals(regex.pattern(), actual.pattern());
        Assert.assertEquals(0, output.buffer().readableBytes());

    }


}