package org.light.serialize.example;

import org.light.serialize.core.util.SerializeUtil;

import java.io.IOException;

public class HelloLightSerialize {

    public static void main(String[] args) throws IOException {
        String hello = "hello light serialize";
        byte[] bytes = SerializeUtil.serializeToBytes(hello);
        String target = (String) SerializeUtil.deserialize(bytes);

        assert hello.equals(target);
    }

}
