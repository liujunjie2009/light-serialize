package org.light.serialize.core.serializer.java;

import org.light.serialize.core.io.ObjectInput;
import org.light.serialize.core.io.ObjectOutput;
import org.light.serialize.core.serializer.Serializer;
import org.light.serialize.core.serializer.SerializerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * BigNumberSerializers
 *
 * @author alex
 */
public class BigNumberSerializers {

    public static final BigDecimalSerializer BIG_DECIMAL_SERIALIZER = new BigDecimalSerializer(BigDecimal.class);
    public static final BigIntegerSerializer BIG_INTEGER_SERIALIZER = new BigIntegerSerializer(BigInteger.class);

    /**
     * The serializer for {@code java.math.BigInteger}.
     */
    public static class BigIntegerSerializer extends Serializer<BigInteger> {

        public BigIntegerSerializer(Class<? extends BigInteger> type) {
            super(type);
        }

        @Override
        public void write(ObjectOutput output, BigInteger value) throws IOException {
            doWrite(output, value);
        }

        @Override
        public BigInteger read(ObjectInput input) throws IOException {
            return doRead(input);
        }

        static void doWrite(ObjectOutput output, BigInteger value) throws IOException {
            if (value == BigInteger.ZERO) {
                output.writeByte((byte) 1);
                output.writeByte((byte) 0);
                return;
            }

            byte[] bytes = value.toByteArray();
            output.writeVarInt(bytes.length);
            output.writeBytes(bytes);
        }

        static BigInteger doRead(ObjectInput input) throws IOException {
            int length = input.readVarInt();
            byte[] bytes = input.readBytes(length);

            if (length == 1) {
                switch (bytes[0]) {
                    case 0:
                        return BigInteger.ZERO;
                    case 1:
                        return BigInteger.ONE;
                    case 10:
                        return BigInteger.TEN;
                }
            }

            return new BigInteger(bytes);
        }
    }

    /**
     * The serializer for {@code java.math.BigDecimal}.
     */
    public static class BigDecimalSerializer extends Serializer<BigDecimal> {

        public BigDecimalSerializer(Class<? extends BigDecimal> type) {
            super(type);
        }

        @Override
        public void write(ObjectOutput output, BigDecimal value) throws IOException {
            if (value == BigDecimal.ZERO) {
                BigIntegerSerializer.doWrite(output, BigInteger.ZERO);
                output.writeVarInt(0);
                return;
            }

            BigIntegerSerializer.doWrite(output, value.unscaledValue());
            output.writeVarInt(value.scale());
        }

        @Override
        public BigDecimal read(ObjectInput input) throws IOException {
            BigInteger unscaledValue = BigIntegerSerializer.doRead(input);
            int scale = input.readVarInt();
            if (unscaledValue == BigInteger.ZERO && scale == 0) {
                return BigDecimal.ZERO;
            }

            return new BigDecimal(unscaledValue, scale);
        }
    }

    public static void register(SerializerFactory factory) {
        factory.register(BIG_DECIMAL_SERIALIZER);
        factory.register(BIG_INTEGER_SERIALIZER);
    }

}
