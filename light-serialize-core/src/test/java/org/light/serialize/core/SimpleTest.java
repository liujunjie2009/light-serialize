package org.light.serialize.core;

import org.junit.Test;
import org.light.serialize.core.buffer.Buffer;
import org.light.serialize.core.constants.Strategy;
import org.light.serialize.core.model.Simple;
import org.light.serialize.core.util.SerializeUtil;

import java.io.IOException;

/**
 */
public class SimpleTest {


    private static Simple testSimple = new Simple();

    @Test
    public void start() throws IOException {
        Buffer buffer = SerializeUtil.serialize(testSimple, Strategy.NAME);
        int size = buffer.readableBytes();
        System.out.print("Simple bytes:" + size + "\t");

        Simple deserialize = (Simple)SerializeUtil.deserialize(buffer);
        deserialize.outStr();
        System.out.println(deserialize);
    }
}

