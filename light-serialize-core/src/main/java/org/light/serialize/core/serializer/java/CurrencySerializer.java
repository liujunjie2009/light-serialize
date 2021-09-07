package org.light.serialize.core.serializer.java;

import org.light.serialize.core.io.ObjectInput;
import org.light.serialize.core.io.ObjectOutput;
import org.light.serialize.core.serializer.Serializer;

import java.io.IOException;
import java.util.Currency;

/**
 * The serializer for {@link java.util.Currency}.
 *
 * @author alex
 */
public class CurrencySerializer extends Serializer<Currency> {

    public CurrencySerializer() {
        super(Currency.class);
    }

    @Override
    public void write(ObjectOutput output, Currency value) throws IOException {
        output.writeString(value.getCurrencyCode());
    }

    @Override
    public Currency read(ObjectInput input) throws IOException {
        return Currency.getInstance(input.readString());
    }
}
