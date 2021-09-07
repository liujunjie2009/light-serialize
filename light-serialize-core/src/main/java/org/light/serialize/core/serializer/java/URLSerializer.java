package org.light.serialize.core.serializer.java;

import org.light.serialize.core.io.ObjectInput;
import org.light.serialize.core.io.ObjectOutput;
import org.light.serialize.core.serializer.Serializer;
import org.light.serialize.core.util.TypeResolver;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * URLSerializer
 */
public class URLSerializer extends Serializer<URL> {

    public URLSerializer() {
        super(URL.class);
    }

    @Override
    public void write(ObjectOutput output, URL object) throws IOException {
        output.writeString(object.toExternalForm());
    }

    @Override
    public URL read(ObjectInput input) throws IOException {
        try {
            return new URL(input.readString());
        } catch (MalformedURLException ex) {
            throw new IOException(ex);
        }
    }

}
