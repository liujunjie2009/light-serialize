package org.light.serialize.example;

import org.light.serialize.core.constants.Strategy;
import org.light.serialize.core.io.ObjectInput;
import org.light.serialize.core.io.ObjectOutput;
import org.light.serialize.core.serializer.java.BasicSerializers;

import java.io.IOException;

public class DirectSerializerExample {

    public static void main(String[] args) throws IOException {
        ObjectOutput output = new ObjectOutput(512);
        BasicSerializers.LONG_SERIALIZER.write(output, 100L, Strategy.ORDER);
        Long read = BasicSerializers.LONG_SERIALIZER.read(new ObjectInput(output.buffer()));
        assert read == 100L;
    }
}
