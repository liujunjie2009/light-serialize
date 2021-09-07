package org.light.serialize.core.serializer.java;

import org.light.serialize.core.instantiator.ObjectInstantiator;
import org.light.serialize.core.instantiator.sun.UnSafeInstantiator;
import org.light.serialize.core.io.ObjectInput;
import org.light.serialize.core.io.ObjectOutput;
import org.light.serialize.core.io.ReadContext;
import org.light.serialize.core.serializer.LightSerializable;
import org.light.serialize.core.serializer.Serializer;

import java.io.IOException;

/**
 * LightSerializableSerializer
 *
 * @author alex
 */
public class LightSerializableSerializer<T extends LightSerializable> extends Serializer<T> {

    private final ObjectInstantiator instantiator;

    public LightSerializableSerializer(Class<?> type) {
        super(type);
        this.instantiator = new UnSafeInstantiator(getType());
    }

    @Override
    public void write(ObjectOutput output, T value) throws IOException {
        value.write(output);
    }

    @Override
    public T read(ObjectInput input) throws IOException {
        T object = (T) instantiator.newInstance();
        ReadContext.get().putReferenceObject(object);
        object.read(input);
        return object;
    }
}
