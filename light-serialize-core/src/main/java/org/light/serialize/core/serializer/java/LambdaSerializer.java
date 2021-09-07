package org.light.serialize.core.serializer.java;

import org.light.serialize.core.io.ObjectInput;
import org.light.serialize.core.io.ObjectOutput;
import org.light.serialize.core.io.ReadContext;
import org.light.serialize.core.serializer.Serializer;
import org.light.serialize.core.util.ReflectUtil;

import java.io.IOException;
import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * LambdaSerializer. Since java8
 *
 * @author alex
 */
public class LambdaSerializer extends Serializer {

    private static final Object LAMBDA_FAKE_REFERENCE = new Object();

    private static final Map<Class<?>, SerializedLambda> LAMBDA_CACHE = new ConcurrentHashMap<>(128);
    private static final Method SERIALIZED_LAMBDA_RESOLVE_METHOD = ReflectUtil.getAccessibleMethod(SerializedLambda.class, "readResolve");

    /**
     * Marker class used to find the class serializer for lambda.
     */
    public static abstract class Lambda {}

    public LambdaSerializer () {
        super(Lambda.class);
    }

    @Override
    public void write(ObjectOutput output, Object value) throws IOException {
        if (!(value instanceof Serializable)) {
            throw new IOException("Lambda must implement java.io.Serializable");
        }

        SerializedLambda serializedLambda = getSerializedLambda((Serializable) value);
        int count = serializedLambda.getCapturedArgCount();
        output.writeVarInt(count);
        for (int i = 0; i < count; i++) {
            output.writeObject(serializedLambda.getCapturedArg(i));
        }

        output.writeString(serializedLambda.getCapturingClass().replace('/', '.'));
        output.writeString(serializedLambda.getFunctionalInterfaceClass());
        output.writeString(serializedLambda.getFunctionalInterfaceMethodName());
        output.writeString(serializedLambda.getFunctionalInterfaceMethodSignature());
        output.writeVarInt(serializedLambda.getImplMethodKind());
        output.writeString(serializedLambda.getImplClass());
        output.writeString(serializedLambda.getImplMethodName());
        output.writeString(serializedLambda.getImplMethodSignature());
        output.writeString(serializedLambda.getInstantiatedMethodType());
    }

    @Override
    public Object read(ObjectInput input) throws IOException {
        ReadContext readContext = ReadContext.get();
        int refObjectsIndex = readContext.getReferenceObjectsSize();
        readContext.putReferenceObject(LAMBDA_FAKE_REFERENCE);

        int count = input.readVarInt();
        Object[] capturedArgs = new Object[count];
        for (int i = 0; i < count; i++) {
            capturedArgs[i] = input.readObject();
        }

        try {
            SerializedLambda lambda = new SerializedLambda(Class.forName(input.readString()), input.readString(),
                    input.readString(), input.readString(), input.readVarInt(), input.readString(), input.readString(),
                    input.readString(), input.readString(), capturedArgs);
            Object readResolveLambda = SERIALIZED_LAMBDA_RESOLVE_METHOD.invoke(lambda);
            // resume
            readContext.putReferenceObject(refObjectsIndex, readResolveLambda);
            return readResolveLambda;
        } catch (Exception ex) {
            throw new IOException("Error reading closure.", ex);
        }
    }

    private SerializedLambda getSerializedLambda(Serializable object) throws IOException {
        try {
            Class<? extends Serializable> clazz = object.getClass();
            SerializedLambda lambda = LAMBDA_CACHE.get(clazz);
            if (lambda == null) {
                Method writeReplace = clazz.getDeclaredMethod("writeReplace");
                writeReplace.setAccessible(true);
                lambda = (SerializedLambda) writeReplace.invoke(object);
                LAMBDA_CACHE.put(clazz, lambda);
            }

            return lambda;
        } catch (Exception e) {
            throw new IOException("Error serializing lambda", e);
        }
    }
}
