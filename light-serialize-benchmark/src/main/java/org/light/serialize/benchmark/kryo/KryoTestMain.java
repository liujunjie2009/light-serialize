package org.light.serialize.benchmark.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import org.light.serialize.benchmark.model.Simple;

import java.io.IOException;

public class KryoTestMain {


    private static final int TEST_LIST_SIZE = 10000;

    private static Kryo kryo;
    private static Simple testSimple = new Simple();
    private static int simpleByteSize = 0;

    public static void start() throws IOException {
        kryo = new Kryo();
        kryo.setRegistrationRequired(false);
        kryo.setReferences(true);

        Output output = new Output(64, Integer.MAX_VALUE);
        kryo.writeClassAndObject(output, testSimple);
        simpleByteSize = output.toBytes().length;
        System.out.print("Simple bytes:" + simpleByteSize + "\t");
    }

    public static Object testSimple() throws IOException {
        Output output = new Output(64, Integer.MAX_VALUE);
        kryo.writeClassAndObject(output, testSimple);
//        Input input = new Input(output.toBytes());
//        return kryo.readClassAndObject(input);
        return null;
    }


    public static void main(String[] args) throws IOException {
        start();
        testSimple();
    }

}
