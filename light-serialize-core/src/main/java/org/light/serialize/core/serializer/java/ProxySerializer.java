package org.light.serialize.core.serializer.java;

import org.light.serialize.core.io.ObjectInput;
import org.light.serialize.core.io.ObjectOutput;
import org.light.serialize.core.io.ReadContext;
import org.light.serialize.core.serializer.Serializer;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * The serializer for {@link java.lang.reflect.Proxy}.
 *
 * @author alex
 */
public class ProxySerializer extends Serializer<Object> {

    private static final Object PROXY_FAKE_REFERENCE = new Object();

    public ProxySerializer() {
        super(Proxy.class);
    }

    @Override
    public Object read(ObjectInput input) throws IOException {
        ReadContext readContext = ReadContext.get();
        int refObjectsIndex = readContext.getReferenceObjectsSize();
        readContext.putReferenceObject(PROXY_FAKE_REFERENCE);

        InvocationHandler invocationHandler = (InvocationHandler) input.readObject();
        Class<?>[] interfaces = (Class<?>[])input.readObject();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        try {
            Object proxy = Proxy.newProxyInstance(loader, interfaces, invocationHandler);
            readContext.putReferenceObject(refObjectsIndex, proxy);
            return proxy;
        } catch (Throwable e) {
            throw new IOException(String.format("Could not create proxy with classLoader[%s]", loader), e);
        }
    }

    @Override
    public void write(ObjectOutput output, Object value) throws IOException {
        output.writeObject(Proxy.getInvocationHandler(value));
        output.writeObject(value.getClass().getInterfaces());
    }
}
