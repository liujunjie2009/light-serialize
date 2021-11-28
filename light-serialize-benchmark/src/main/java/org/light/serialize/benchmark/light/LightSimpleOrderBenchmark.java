package org.light.serialize.benchmark.light;

import org.light.serialize.benchmark.model.Simple;
import org.light.serialize.core.constants.Strategy;
import org.light.serialize.core.io.ObjectInput;
import org.light.serialize.core.io.ObjectOutput;
import org.light.serialize.core.serializer.java.ObjectSerializer;
import org.light.serialize.core.util.SerializeUtil;
import org.openjdk.jmh.annotations.*;

import java.io.IOException;

/**
 * @author alex
 */
@State(Scope.Benchmark)
public class LightSimpleOrderBenchmark {

    private static Simple testSimple = new Simple();

    ObjectSerializer<Simple> simpleSerializer = new ObjectSerializer<>(Simple.class);

    @Setup(Level.Trial)
    public void start() throws IOException {
        ObjectOutput output = new ObjectOutput(1024);
        ObjectSerializer<Simple> simpleSerializer = new ObjectSerializer<>(Simple.class);
        simpleSerializer.write(output, testSimple, Strategy.ORDER);
        int size = output.buffer().readableBytes();
        System.out.print("Simple bytes:" + size + "\t");
    }

    @Benchmark
    public Object testSimple() throws IOException {
        ObjectOutput output = new ObjectOutput(1024);
        simpleSerializer.write(output, testSimple, Strategy.ORDER);
        return simpleSerializer.read(new ObjectInput(output.buffer()), Strategy.ORDER);
//        return null;
    }

}
