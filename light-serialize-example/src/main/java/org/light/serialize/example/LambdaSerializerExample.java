package org.light.serialize.example;

import org.light.serialize.core.io.ObjectInput;
import org.light.serialize.core.io.ObjectOutput;

import java.io.IOException;
import java.util.function.BiFunction;

public class LambdaSerializerExample {

    public static void main(String[] args) throws IOException {
        ObjectOutput output = new ObjectOutput();
        ObjectInput input = new ObjectInput(output.buffer());

        BiFunction<Integer, Integer, Integer> sourceFunction = (BiFunction<Integer, Integer, Integer> & java.io.Serializable)
                ( (arg1, arg2) -> arg1 + arg2);
        output.writeObject(sourceFunction);

        BiFunction<Integer, Integer, Integer> readFunction = (BiFunction<Integer, Integer, Integer>)input.readObject();

        assert readFunction.equals(sourceFunction);
    }
}
