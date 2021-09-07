package org.light.serialize.core.serializer.java;

import org.light.serialize.core.io.ObjectInput;
import org.light.serialize.core.io.ObjectOutput;
import org.light.serialize.core.serializer.Serializer;

import java.io.IOException;
import java.net.URI;

/**
 * URISerializer
 */
public class URISerializer extends Serializer<URI> {

    public URISerializer() {
        super(URI.class);
    }

    @Override
    public void write(ObjectOutput output, URI value) throws IOException {
        output.writeString(value.toString());
    }

    @Override
    public URI read(ObjectInput input) throws IOException {
        return URI.create(input.readString());
    }
}
