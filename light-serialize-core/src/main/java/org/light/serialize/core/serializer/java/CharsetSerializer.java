package org.light.serialize.core.serializer.java;

import org.light.serialize.core.io.ObjectInput;
import org.light.serialize.core.io.ObjectOutput;
import org.light.serialize.core.serializer.Serializer;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * CharsetSerializer
 */
public class CharsetSerializer extends Serializer<Charset> {

    public CharsetSerializer(Class<? extends Charset> type) {
        super(type);
    }

    @Override
    public void write(ObjectOutput output, Charset value) throws IOException {
        output.writeString(value.name());
    }

    @Override
    public Charset read(ObjectInput input) throws IOException {
        return Charset.forName(input.readString());

    }
}
