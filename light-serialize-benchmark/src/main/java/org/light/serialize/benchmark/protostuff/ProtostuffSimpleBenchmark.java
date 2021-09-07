package org.light.serialize.benchmark.protostuff;

import io.protostuff.GraphIOUtil;
import io.protostuff.LinkedBuffer;
import io.protostuff.runtime.RuntimeSchema;
import org.light.serialize.benchmark.model.Simple;
import org.openjdk.jmh.annotations.*;

import java.io.IOException;

/**
 * @date 2021/05/16
 */
@State(Scope.Benchmark)
public class ProtostuffSimpleBenchmark {

    private static final int TEST_LIST_SIZE = 10000;

    private static Simple testSimple = new Simple();

    @Setup(Level.Trial)
    public void start() throws IOException {
        RuntimeSchema<Simple> schema = RuntimeSchema.createFrom(Simple.class);
        byte[] serializeBytes = GraphIOUtil.toByteArray(testSimple, schema, LinkedBuffer.allocate(512));
        System.out.print("Simple bytes:" + serializeBytes.length + "\t");
    }

    @Benchmark
    public Object testSimple() throws IOException, IllegalAccessException, InstantiationException {
        RuntimeSchema<Simple> schema = RuntimeSchema.createFrom(Simple.class);
        byte[] serializeBytes = GraphIOUtil.toByteArray(testSimple, schema, LinkedBuffer.allocate(512));
        Simple deserialize = Simple.class.newInstance();
        GraphIOUtil.mergeFrom(serializeBytes, testSimple, schema);
        return deserialize;
    }

}
