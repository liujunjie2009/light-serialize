package org.light.serialize.core.serializer.java;

import org.junit.Test;
import org.light.serialize.core.util.SerializeUtil;

import java.io.IOException;

/**
 * ThrowableSerializer test
 *
 * @author alex
 */
public class ThrowableSerializerTest {

    @Test
    public void test() throws IOException, NoSuchMethodException {

        IOException ioException = new IOException("test");
        RuntimeException runtimeException = new RuntimeException(ioException);
        // runtimeException.getStackTrace()
        byte[] data = SerializeUtil.serializeToBytes(runtimeException);
        RuntimeException runtimeExceptionCopy = (RuntimeException)SerializeUtil.deserialize(data);
        System.out.println(runtimeExceptionCopy.toString());
    }
}
