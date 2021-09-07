package org.light.serialize.core.serializer.java;

import org.light.serialize.core.instantiator.ObjectInstantiator;
import org.light.serialize.core.instantiator.sun.UnSafeInstantiator;
import org.light.serialize.core.io.ObjectInput;
import org.light.serialize.core.io.ObjectOutput;
import org.light.serialize.core.io.ReadContext;
import org.light.serialize.core.serializer.Serializer;
import org.light.serialize.core.serializer.SerializerFactory;
import org.light.serialize.core.util.ReflectUtil;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;

/**
 * OptionalSerializers.
 *
 * @author alex
 */
public class OptionalSerializers {

    public static final OptionalDoubleSerializer OPTIONAL_DOUBLE_SERIALIZER = new OptionalDoubleSerializer();
    public static final OptionalIntSerializer OPTIONAL_INT_SERIALIZER = new OptionalIntSerializer();
    public static final OptionalLongSerializer OPTIONAL_LONG_SERIALIZER = new OptionalLongSerializer();
    public static final OptionalSerializer OPTIONAL_SERIALIZER = new OptionalSerializer();

    /**
     * The serializer for Optional.
     */
    public static class OptionalSerializer extends Serializer<Optional> {

        public static final Field OPTIONAL_VALUE_FIELD = ReflectUtil.getAccessibleField(Optional.class, "value");

        private ObjectInstantiator<Optional> instantiator = new UnSafeInstantiator(type);

        public OptionalSerializer() {
            super(Optional.class);
        }

        @Override
        public void write (ObjectOutput output, Optional value) throws IOException {
            output.writeObject(value.isPresent() ? value.get() : null);
        }

        @Override
        public Optional read(ObjectInput input) throws IOException {
            try {
                Optional optional = instantiator.newInstance();
                ReadContext.get().putReferenceObject(optional);
                OPTIONAL_VALUE_FIELD.set(optional, input.readObject());
                return optional;
            } catch (IllegalAccessException e) {
                throw new IOException(e);
            }
        }

    }

    /**
     * The serializer for OptionalInt.
     */
    public static class OptionalIntSerializer extends Serializer<OptionalInt> {

        public static final Field OPTIONAL_INT_VALUE_FIELD = ReflectUtil.getAccessibleField(OptionalInt.class, "value");
        public static final Field OPTIONAL_INT_IS_PRESENT_FIELD = ReflectUtil.getAccessibleField(OptionalInt.class, "isPresent");
        private ObjectInstantiator<OptionalInt> instantiator = new UnSafeInstantiator(type);

        public OptionalIntSerializer() {
            super(OptionalInt.class);
        }

        @Override
        public void write (ObjectOutput output, OptionalInt value) throws IOException {
            boolean present = value.isPresent();
            output.writeBool(present);
            if (present) {
                output.writeZigzagVarInt(value.getAsInt());
            }
        }

        @Override
        public OptionalInt read(ObjectInput input) throws IOException {
            try {
                OptionalInt optional = instantiator.newInstance();
                ReadContext.get().putReferenceObject(optional);
                if (input.readBool()) {
                    OPTIONAL_INT_VALUE_FIELD.set(optional, input.readZigzagVarInt());
                    OPTIONAL_INT_IS_PRESENT_FIELD.set(optional, true);
                }
                return optional;
            } catch (IllegalAccessException e) {
                throw new IOException(e);
            }
        }
    }

    /**
     * The serializer for OptionalLong.
     */
    public static class OptionalLongSerializer extends Serializer<OptionalLong> {

        public static final Field OPTIONAL_LONG_VALUE_FIELD = ReflectUtil.getAccessibleField(OptionalLong.class, "value");
        public static final Field OPTIONAL_LONG_IS_PRESENT_FIELD = ReflectUtil.getAccessibleField(OptionalLong.class, "isPresent");
        private ObjectInstantiator<OptionalLong> instantiator = new UnSafeInstantiator(type);

        public OptionalLongSerializer() {
            super(OptionalLong.class);
        }

        @Override
        public void write (ObjectOutput output, OptionalLong value) throws IOException {
            output.writeBool(value.isPresent());
            if (value.isPresent()) {
                output.writeZigzagVarLong(value.getAsLong());
            }
        }

        @Override
        public OptionalLong read(ObjectInput input) throws IOException {
            try {
                OptionalLong optional = instantiator.newInstance();
                ReadContext.get().putReferenceObject(optional);

                if (input.readBool()) {
                    OPTIONAL_LONG_VALUE_FIELD.set(optional, input.readZigzagVarLong());
                    OPTIONAL_LONG_IS_PRESENT_FIELD.set(optional, true);
                }
                return optional;
            } catch (IllegalAccessException e) {
                throw new IOException(e);
            }
        }
    }

    /**
     * The serializer for OptionalDouble.
     */
    public static class OptionalDoubleSerializer extends Serializer<OptionalDouble> {

        public static final Field OPTIONAL_DOUBLE_VALUE_FIELD = ReflectUtil.getAccessibleField(OptionalDouble.class, "value");
        public static final Field OPTIONAL_DOUBLE_IS_PRESENT_FIELD = ReflectUtil.getAccessibleField(OptionalDouble.class, "isPresent");
        private ObjectInstantiator<OptionalDouble> instantiator = new UnSafeInstantiator(type);

        public OptionalDoubleSerializer() {
            super(OptionalDouble.class);
        }

        @Override
        public void write (ObjectOutput output, OptionalDouble value) throws IOException {
            output.writeBool(value.isPresent());
            if (value.isPresent()) {
                output.writeObject(value.getAsDouble());
            }
        }

        @Override
        public OptionalDouble read(ObjectInput input) throws IOException {
            try {
                OptionalDouble optional = instantiator.newInstance();
                ReadContext.get().putReferenceObject(optional);
                if (input.readBool()) {
                    OPTIONAL_DOUBLE_VALUE_FIELD.set(optional, input.readObject());
                    OPTIONAL_DOUBLE_IS_PRESENT_FIELD.set(optional, true);
                }
                return optional;
            } catch (IllegalAccessException e) {
                throw new IOException(e);
            }
        }

    }

    public static void register(SerializerFactory serializerFactory) {
        serializerFactory.register(OPTIONAL_DOUBLE_SERIALIZER);
        serializerFactory.register(OPTIONAL_LONG_SERIALIZER);
        serializerFactory.register(OPTIONAL_INT_SERIALIZER);
        serializerFactory.register(OPTIONAL_SERIALIZER);
    }

}
