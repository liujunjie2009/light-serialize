package org.light.serialize.benchmark.light;

import org.light.serialize.benchmark.model.Simple;
import org.light.serialize.core.buffer.Buffer;
import org.light.serialize.core.constants.Strategy;
import org.light.serialize.core.util.SerializeUtil;
import org.openjdk.jmh.annotations.*;

import java.io.IOException;

/**
 * @date 2021/05/16
 */
@State(Scope.Benchmark)
public class LightSimpleExactBenchmark {

    private static Simple testSimple = new Simple();

    @Setup(Level.Trial)
    public void start() throws IOException {
        int size = SerializeUtil.serialize(testSimple, Strategy.EXACT).readableBytes();
        System.out.print("Simple bytes:" + size + "\t");
    }

    @Benchmark
    public Object testSimple() throws IOException {
        Buffer buffer = SerializeUtil.serialize(testSimple, Strategy.EXACT);
        return SerializeUtil.deserialize(buffer, Strategy.EXACT);
    }

}
