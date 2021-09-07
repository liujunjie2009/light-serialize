package org.light.serialize.core.serializer.java;

import org.light.serialize.core.io.ObjectInput;
import org.light.serialize.core.io.ObjectOutput;
import org.light.serialize.core.io.ReadContext;
import org.light.serialize.core.io.WriteContext;
import org.light.serialize.core.serializer.Serializer;
import org.light.serialize.core.serializer.SerializerFactory;
import org.light.serialize.core.util.ReflectUtil;

import java.io.IOException;

/**
 * ClassSerializer.
 *
 * @author alex
 */
public class ClassSerializer extends Serializer<Class<?>> {

    public ClassSerializer() {
        super(Class.class);
    }

    @Override
    public void write(ObjectOutput output, Class<?> value) throws IOException {
        Serializer<?> registeredSerializer = WriteContext.get().getSerializerFactory().getRegisteredSerializer(value);

        if (registeredSerializer == null) {
            output.writeBool(false);
            output.writeString(value.getName());
        } else {
            output.writeBool(true);
            output.writeZigzagVarLong(registeredSerializer.getTypeId());
        }
    }

    @Override
    public Class<?> read(ObjectInput input) throws IOException {
        SerializerFactory serializerFactory = ReadContext.get().getSerializerFactory();
        boolean writeTypeId = input.readBool();

        if (writeTypeId) {
            long typeId = input.readZigzagVarLong();
            Serializer<?> registeredSerializer = serializerFactory.getRegisteredSerializer(typeId);
            if (registeredSerializer == null) {
                throw new IOException(String.format("can not find registered serializer by typeId[%d]", typeId));
            }

            return registeredSerializer.getType();
        }

        return ReflectUtil.loadClass(input.readString());
    }
}
