package org.light.serialize.core.serializer.java;

import org.junit.Assert;
import org.junit.Test;
import org.light.serialize.core.io.ObjectInput;
import org.light.serialize.core.io.ObjectOutput;

import java.io.IOException;
import java.util.Currency;

/**
 * TimeZoneSerializerTest
 *
 * @author alex
 */
public class CurrencySerializerTest {

    @Test
    public void test() throws IOException {
        ObjectOutput output = new ObjectOutput();
        ObjectInput input = new ObjectInput(output.buffer());

        for (Currency currency : Currency.getAvailableCurrencies()) {
            output.writeObject(currency);

            Object actual = input.readObject();
            Assert.assertEquals(currency, actual);
            Assert.assertEquals(0, output.buffer().readableBytes());
        }

    }

}