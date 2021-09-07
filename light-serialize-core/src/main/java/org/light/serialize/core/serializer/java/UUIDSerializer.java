package org.light.serialize.core.serializer.java;

import org.light.serialize.core.io.ObjectInput;
import org.light.serialize.core.io.ObjectOutput;
import org.light.serialize.core.serializer.Serializer;
import org.light.serialize.core.util.TypeResolver;

import java.io.IOException;
import java.util.UUID;

/**
 * The serializer for {@link java.util.UUID}.
 *
 * @author alex
 */
public class UUIDSerializer extends Serializer<UUID> {

    public UUIDSerializer() {
        super(UUID.class);
    }

    @Override
    public void write(ObjectOutput output, UUID value) throws IOException {
        output.writeLong(value.getMostSignificantBits());
        output.writeLong(value.getLeastSignificantBits());
    }

    @Override
    public UUID read(ObjectInput input) throws IOException {
        return new UUID(input.readLong(), input.readLong());
    }
}
