package org.light.serialize.benchmark.light;

import org.light.serialize.benchmark.model.Simple;
import org.light.serialize.core.buffer.Buffer;
import org.light.serialize.core.constants.Strategy;
import org.light.serialize.core.util.SerializeUtil;
import org.openjdk.jmh.annotations.*;

import java.io.IOException;

/**
 * @author alex
 */
@State(Scope.Benchmark)
public class LightSimpleNameBenchmark {

    private static Simple testSimple = new Simple();

    @Setup(Level.Trial)
    public void start() throws IOException {
        Buffer buffer = SerializeUtil.serialize(testSimple, Strategy.NAME);
        int size = buffer.readableBytes();
        System.out.print("Simple bytes:" + size + "\t");
    }

    @Benchmark
    public Object testSimple() throws IOException {
        Buffer buffer = SerializeUtil.serialize(testSimple, Strategy.NAME);
//        return SerializeUtil.deserialize(buffer, Strategy.NAME);
        return null;
    }

}
