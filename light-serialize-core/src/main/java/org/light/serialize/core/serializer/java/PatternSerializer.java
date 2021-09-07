package org.light.serialize.core.serializer.java;

import org.light.serialize.core.io.ObjectInput;
import org.light.serialize.core.io.ObjectOutput;
import org.light.serialize.core.serializer.Serializer;
import org.light.serialize.core.util.TypeResolver;

import java.io.IOException;
import java.util.regex.Pattern;

/**
 * The serializer for {@link java.util.regex.Pattern}.
 *
 * @author alex
 */
public class PatternSerializer extends Serializer<Pattern> {

    public PatternSerializer() {
        super(Pattern.class);
    }

    @Override
    public void write(ObjectOutput output, Pattern value) throws IOException {
        output.writeString(value.pattern());
        output.writeVarInt(value.flags());
    }

    @Override
    public Pattern read(ObjectInput input) throws IOException {
        String regex = input.readString();
        int flags = input.readVarInt();
        return Pattern.compile(regex, flags);
    }

}
