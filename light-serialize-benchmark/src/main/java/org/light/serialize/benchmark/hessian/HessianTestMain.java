package org.light.serialize.benchmark.hessian;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import org.light.serialize.benchmark.model.Simple;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class HessianTestMain {

    private static Simple testSimple = new Simple();

    public static void main(String[] args) throws IOException {
        test();
    }

    public static Object test() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Hessian2Output output = new Hessian2Output(outputStream);
        output.writeObject(testSimple);
        output.flush();
        output.close();
        byte[] serializeBytes = outputStream.toByteArray();
        new Hessian2Input(new ByteArrayInputStream(serializeBytes)).readObject();

        return new Hessian2Input(new ByteArrayInputStream(serializeBytes)).readObject();
    }

}
