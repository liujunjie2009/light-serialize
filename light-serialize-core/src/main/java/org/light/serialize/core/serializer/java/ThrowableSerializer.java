package org.light.serialize.core.serializer.java;

import org.light.serialize.core.io.ObjectOutput;
import org.light.serialize.core.util.TypeResolver;

import java.io.IOException;
import java.util.regex.Pattern;

/**
 * ThrowableSerializer.
 *
 * @author alex
 */
public class ThrowableSerializer<T extends Throwable> extends ObjectSerializer<T> {

    public ThrowableSerializer(Class<T> type) {
        super(type);
    }

    @Override
    public void write(ObjectOutput output, T value) throws IOException {
        value.getStackTrace();
        super.write(output, value);
    }

}
