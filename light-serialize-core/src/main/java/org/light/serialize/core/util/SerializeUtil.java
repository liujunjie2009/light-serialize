package org.light.serialize.core.util;

import org.light.serialize.core.buffer.Buffer;
import org.light.serialize.core.buffer.LinkedByteBuffer;
import org.light.serialize.core.constants.Strategy;
import org.light.serialize.core.io.ObjectInput;
import org.light.serialize.core.io.ObjectOutput;
import org.light.serialize.core.io.ReadContext;
import org.light.serialize.core.io.WriteContext;

import java.io.IOException;
import java.util.Objects;

/**
 * SerializeUtil
 *
 * @author alex
 */
public class SerializeUtil {

    private SerializeUtil() {
    }

    /**
     * serialize an object to bytes.
     */
    public static byte[] serializeToBytes(Object obj) throws IOException {
        return serializeToBytes(obj, Strategy.getDefault());
    }

    /**
     * serialize an object to bytes.
     */
    public static byte[] serializeToBytes(Object obj, Strategy strategy) throws IOException {
        Buffer buffer = serialize(obj, strategy);
        return buffer.readBytes(buffer.readableBytes());
    }

    /**
     * serialize an object to buffer.
     */
    public static Buffer serialize(Object obj) throws IOException {
        return serialize(obj, Strategy.getDefault());
    }

    public static Buffer serialize(Object obj, Strategy strategy) throws IOException {
        Objects.requireNonNull(strategy);
        WriteContext.get().setStrategy(strategy);
        ObjectOutput output = new ObjectOutput();
        output.writeObject(obj);
        return output.buffer();
    }

    /**
     * deserialize an object byte bytes.
     */
    public static Object deserialize(byte[] data) throws IOException {
        return deserialize(data, Strategy.getDefault());
    }

    /**
     * deserialize an object byte bytes.
     */
    public static Object deserialize(byte[] data, Strategy strategy) throws IOException {
        return deserialize(LinkedByteBuffer.wrap(data, 0, data.length), strategy);
    }

    /**
     * deserialize an object byte buffer.
     */
    public static Object deserialize(Buffer buffer) throws IOException {
        return deserialize(buffer, Strategy.getDefault());
    }

    /**
     * deserialize an object byte buffer.
     */
    public static Object deserialize(Buffer buffer, Strategy strategy) throws IOException {
        Objects.requireNonNull(strategy);
        ReadContext.get().setStrategy(strategy);
        return new ObjectInput(buffer).readObject();
    }

}
