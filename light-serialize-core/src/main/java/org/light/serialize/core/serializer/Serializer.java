package org.light.serialize.core.serializer;

import org.light.serialize.core.constants.Strategy;
import org.light.serialize.core.io.ObjectInput;
import org.light.serialize.core.io.ObjectOutput;
import org.light.serialize.core.io.ReadContext;
import org.light.serialize.core.io.WriteContext;
import org.light.serialize.core.util.TypeResolver;

import java.io.IOException;
import java.util.Objects;

/**
 * Serializer
 *
 * @author alex
 */
public abstract class Serializer<T> {

    /**
     * serialized class
     */
    protected final Class<?> type;

    /**
     * long hash type id
     */
    protected final Long typeId;

    public Serializer(Class<?> type) {
        Objects.requireNonNull(type);
        this.type = type;
        this.typeId = TypeResolver.resolveTypeId(type);
    }

    public Class<?> getType() {
        return type;
    }

    public Long getTypeId() {
        return typeId;
    }

    /**
     * write object
     */
    public abstract void write(ObjectOutput output, T value) throws IOException;

    public final void write(ObjectOutput output, T value, Strategy strategy) throws IOException {
        Objects.requireNonNull(strategy);
        WriteContext.get().setStrategy(strategy);
        write(output, value);
    }

    /**
     * read object
     */
    public abstract T read(ObjectInput input) throws IOException;

    public final T read(ObjectInput input, Strategy strategy) throws IOException {
        Objects.requireNonNull(strategy);
        ReadContext.get().setStrategy(strategy);
        return read(input);
    }


    @Override
    public String toString() {
        return "Serializer{" +
                "type=" + type +
                ", typeId=" + typeId +
                '}';
    }
}
