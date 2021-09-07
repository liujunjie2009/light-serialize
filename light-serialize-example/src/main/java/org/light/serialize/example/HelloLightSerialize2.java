package org.light.serialize.example;

import org.light.serialize.core.io.ObjectInput;
import org.light.serialize.core.io.ObjectOutput;

import java.io.IOException;

public class HelloLightSerialize2 {

    public static void main(String[] args) throws IOException {
        String hello = "hello light serialize";

        ObjectOutput output = new ObjectOutput(64);
        output.writeObject(hello);

        ObjectInput input = new ObjectInput(output.buffer());
        String target = (String) input.readObject();

        assert hello.equals(target);
    }

}
