package org.light.serialize.core;

import org.junit.Assert;
import org.junit.Test;
import org.light.serialize.core.constants.Strategy;
import org.light.serialize.core.io.ObjectInput;
import org.light.serialize.core.io.ObjectOutput;
import org.light.serialize.core.io.WriteContext;
import org.light.serialize.core.model.Person;

import java.io.IOException;

/**
 * @date 2020/11/22
 */
public class ObjectSerializerTest {

    @Test
    public void testBean() throws IOException {
        ObjectOutput output = new ObjectOutput();
        ObjectInput input = new ObjectInput(output.buffer());

        Person person = new Person("lsl", 30);
        output.writeObject(person);
        Person actual = (Person)input.readObject();

        Assert.assertEquals(person, actual);
        Assert.assertEquals(0, output.buffer().readableBytes());


        WriteContext.get().setStrategy(Strategy.NAME);
        Object[] persons = {new Person("lsl1", 30), new Person("lsl2", 30)};
        output.writeObject(persons);
        Object[] actualPersons = (Object[])input.readObject();

        Assert.assertEquals(persons, actualPersons);
        Assert.assertEquals(0, output.buffer().readableBytes());
    }

}