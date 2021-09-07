package org.light.serialize.core.serializer.java;

import org.light.serialize.core.io.ObjectInput;
import org.light.serialize.core.io.ObjectOutput;
import org.light.serialize.core.serializer.Serializer;
import org.light.serialize.core.util.TypeResolver;

import java.io.IOException;
import java.util.TimeZone;

/**
 * The serializer for {@link java.util.TimeZone}.
 *
 * @author alex
 */
public class TimeZoneSerializer extends Serializer<TimeZone> {

    public TimeZoneSerializer(Class<? extends TimeZone> type) {
        super(type);
    }

    @Override
    public void write(ObjectOutput output, TimeZone value) throws IOException {
        output.writeString(value.getID());
    }

    @Override
    public TimeZone read(ObjectInput input) throws IOException {
        return TimeZone.getTimeZone(input.readString());
    }
}
