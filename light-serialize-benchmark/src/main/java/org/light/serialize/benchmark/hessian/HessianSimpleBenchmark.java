package org.light.serialize.benchmark.hessian;

import com.alibaba.fastjson.JSON;
import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import org.light.serialize.benchmark.model.Simple;
import org.openjdk.jmh.annotations.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @date 2021/05/16
 */
@State(Scope.Benchmark)
public class HessianSimpleBenchmark {

    private static final int TEST_LIST_SIZE = 10000;

    private static Simple testSimple;
    private static int simpleByteSize = 0;

    @Setup(Level.Trial)
    public void start() throws IOException {
        testSimple = new Simple();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Hessian2Output output = new Hessian2Output(outputStream);
        output.writeObject(testSimple);
        output.flush();
        output.close();
        simpleByteSize = outputStream.toByteArray().length;
        System.out.print("Simple bytes:" + simpleByteSize + "\t");
    }

    @Benchmark
    public Object testSimple() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Hessian2Output output = new Hessian2Output(outputStream);
        output.writeObject(testSimple);
        output.flush();
        output.close();
        byte[] serializeBytes = outputStream.toByteArray();
        Hessian2Input input = new Hessian2Input(new ByteArrayInputStream(serializeBytes));
        return input.readObject();
    }

}
