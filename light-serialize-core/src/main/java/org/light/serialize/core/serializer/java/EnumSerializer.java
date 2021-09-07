package org.light.serialize.core.serializer.java;

import org.light.serialize.core.constants.Strategy;
import org.light.serialize.core.io.ObjectInput;
import org.light.serialize.core.io.ObjectOutput;
import org.light.serialize.core.io.WriteContext;
import org.light.serialize.core.serializer.Serializer;

import java.io.IOException;

/**
 * EnumSerializer.
 *
 * @author alex
 */
public class EnumSerializer<T extends Enum> extends Serializer<T> {

    private Object[] enumConstants;

    public EnumSerializer(Class<T> enumType) {
        super(enumType);
        enumConstants = type.getEnumConstants();
        if (enumConstants == null && !Enum.class.equals(type)) {
            throw new IllegalArgumentException("The type must be an enum: " + type);
        }
    }

    @Override
    public void write(ObjectOutput output, T value) throws IOException {
        Strategy strategy = WriteContext.get().getStrategy();
        if (strategy == Strategy.ORDER) {
            output.writeVarInt(value.ordinal());
        } else {
            output.writeString(value.name());
        }
    }

    @Override
    public T read(ObjectInput input) throws IOException {
        Strategy strategy = WriteContext.get().getStrategy();
        if (strategy == Strategy.ORDER) {
            return (T) enumConstants[input.readVarInt()];
        } else {
            String name = input.readString();
            try {
                return (T) Enum.valueOf((Class<T>) type, name);
            } catch (IllegalArgumentException e) {
                throw new IOException("Enum value not found with name: " + name, e);
            }
        }
    }
}

