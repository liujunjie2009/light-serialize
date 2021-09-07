package org.light.serialize.example;

import org.light.serialize.core.io.ObjectInput;
import org.light.serialize.core.io.ObjectOutput;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Objects;

public class ProxySerializerExample {

    public static void main(String[] args) throws IOException {
        ObjectOutput output = new ObjectOutput();
        ObjectInput input = new ObjectInput(output.buffer());

        Object proxy = Proxy.newProxyInstance(ProxySerializerExample.class.getClassLoader(), new Class<?>[]{MyInterface.class}, new MyInvocationHandler());

        output.writeObject(proxy);
        Object actual = input.readObject();

        assert Objects.equals(Proxy.getInvocationHandler(proxy), Proxy.getInvocationHandler(actual));
    }
}

class MyInvocationHandler implements InvocationHandler {

    private String hello = "hello";

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return hello;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyInvocationHandler that = (MyInvocationHandler) o;
        return Objects.equals(hello, that.hello);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hello);
    }
}

interface MyInterface {

    String hello();

}