package org.light.serialize.core.serializer.java;

import org.junit.Assert;
import org.junit.Test;
import org.light.serialize.core.io.ObjectInput;
import org.light.serialize.core.io.ObjectOutput;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * CalendarSerializerTest
 *
 * @author alex
 */
public class CalendarSerializerTest {

    @Test
    public void test() throws IOException {
        ObjectOutput output = new ObjectOutput();
        ObjectInput input = new ObjectInput(output.buffer());

        Object calendar = Calendar.getInstance();
        output.writeObject(calendar);

        Object actual = input.readObject();
        Assert.assertEquals(calendar, actual);
        Assert.assertEquals(0, output.buffer().readableBytes());


        calendar = new GregorianCalendar();
        output.writeObject(calendar);

        actual = input.readObject();
        Assert.assertEquals(calendar, actual);
        Assert.assertEquals(0, output.buffer().readableBytes());
    }

}