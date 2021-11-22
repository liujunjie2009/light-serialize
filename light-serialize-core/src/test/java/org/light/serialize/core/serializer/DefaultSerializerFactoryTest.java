package org.light.serialize.core.serializer;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;
import org.light.serialize.core.constants.TagId;
import org.light.serialize.core.serializer.java.BasicSerializers;
import org.light.serialize.core.serializer.java.ProxySerializer;
import org.light.serialize.core.serializer.java.ObjectSerializer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

import static org.light.serialize.core.serializer.DefaultSerializerFactory.*;

/**
 * DefaultSerializerFactory test
 *
 * @author alex
 */
public class DefaultSerializerFactoryTest extends TestCase {

    @Test
    public void testGetSharedInstance() {
        DefaultSerializerFactory serializerFactory = DefaultSerializerFactory.getSharedInstance();
        Assert.assertNotNull(serializerFactory);
    }

    @Test
    public void testInitRegisteredSerializers() {
//        DefaultSerializerFactory serializerFactory = DefaultSerializerFactory.getSharedInstance();
//        Assert.assertNotNull(serializerFactory.serializers.size() > 0);
//        Assert.assertNotNull(serializerFactory.registeredSerializers.size() > 0);
    }

    @Test
    public void testRegisterSerializer() {
        DefaultSerializerFactory serializerFactory = DefaultSerializerFactory.getSharedInstance();
        Assert.assertThrows(IllegalArgumentException.class, () -> {
            serializerFactory.register(null);
        });

        Assert.assertThrows(IllegalArgumentException.class, () -> {
            serializerFactory.register(null);
        });

        Assert.assertThrows(IllegalStateException.class, () -> {
            serializerFactory.register(BasicSerializers.BOOL_SERIALIZER);
        });

        Assert.assertThrows(IllegalArgumentException.class, () -> {
            Long typeId = null;
            serializerFactory.getRegisteredSerializer(typeId);
        });

        Assert.assertThrows(IllegalArgumentException.class, () -> {
            Class<?> clazz = null;
            serializerFactory.getRegisteredSerializer(clazz);
        });

        serializerFactory.register(new ObjectSerializer<MyRegister>(MyRegister.class));
        Assert.assertNotNull(serializerFactory.getSerializer(MyRegister.class));
        Assert.assertNotNull(serializerFactory.getRegisteredSerializer(MyRegister.class));
    }

    @Test
    public void testGetSerializer() {
        DefaultSerializerFactory serializerFactory = DefaultSerializerFactory.getSharedInstance();

        Assert.assertThrows(IllegalArgumentException.class, () -> {
            Class<?> clazz = null;
            serializerFactory.getSerializer(clazz);
        });


        Serializer<Object> serializer = null;

        // serializer = serializerFactory.getSerializer((short) VAR_SHORT_ADVANTAGE_UPPER_BOUND);
        // Assert.assertEquals(TagId.SHORT, serializer.getTypeId());
        // serializer = serializerFactory.getSerializer((short) VAR_SHORT_ADVANTAGE_LOWER_BOUND);
        // Assert.assertEquals(TagId.SHORT_REVERSE_VAR, serializer.getTypeId());

        // serializer = serializerFactory.getSerializer((short) (VAR_SHORT_ADVANTAGE_UPPER_BOUND - 1));
        // Assert.assertEquals(TagId.VAR_SHORT, serializer.getTypeId());
        // serializer = serializerFactory.getSerializer((short) (VAR_SHORT_ADVANTAGE_LOWER_BOUND + 1));
        // Assert.assertEquals(TagId.VAR_SHORT, serializer.getTypeId());

        // serializer = serializerFactory.getSerializer((short) COMPLEMENT_VAR_SHORT_ADVANTAGE_UPPER_BOUND);
        // Assert.assertEquals(TagId.VAR_SHORT, serializer.getTypeId());
        // serializer = serializerFactory.getSerializer((short) COMPLEMENT_VAR_SHORT_ADVANTAGE_LOWER_BOUND);
        // Assert.assertEquals(TagId.SHORT, serializer.getTypeId());
        //
        // serializer = serializerFactory.getSerializer((short) (COMPLEMENT_VAR_SHORT_ADVANTAGE_UPPER_BOUND - 1));
        // Assert.assertEquals(TagId.SHORT_REVERSE_VAR, serializer.getTypeId());
        // serializer = serializerFactory.getSerializer((short) (COMPLEMENT_VAR_SHORT_ADVANTAGE_LOWER_BOUND + 1));
        // Assert.assertEquals(TagId.SHORT_REVERSE_VAR, serializer.getTypeId());
        //
        //
        // serializer = serializerFactory.getSerializer(VAR_INT_ADVANTAGE_UPPER_BOUND);
        // Assert.assertEquals(TagId.INT, serializer.getTypeId());
        // serializer = serializerFactory.getSerializer(VAR_INT_ADVANTAGE_LOWER_BOUND);
        // Assert.assertEquals(TagId.INT_REVERSE_VAR, serializer.getTypeId());

        // serializer = serializerFactory.getSerializer(VAR_INT_ADVANTAGE_UPPER_BOUND - 1);
        // Assert.assertEquals(TagId.VAR_INT, serializer.getTypeId());
        // serializer = serializerFactory.getSerializer(VAR_INT_ADVANTAGE_LOWER_BOUND + 1);
        // Assert.assertEquals(TagId.VAR_INT, serializer.getTypeId());

        // serializer = serializerFactory.getSerializer(COMPLEMENT_VAR_INT_ADVANTAGE_UPPER_BOUND);
        // Assert.assertEquals(TagId.VAR_INT, serializer.getTypeId());
        // serializer = serializerFactory.getSerializer(COMPLEMENT_VAR_INT_ADVANTAGE_LOWER_BOUND);
        // Assert.assertEquals(TagId.INT, serializer.getTypeId());
        //
        // serializer = serializerFactory.getSerializer(COMPLEMENT_VAR_INT_ADVANTAGE_UPPER_BOUND - 1);
        // Assert.assertEquals(TagId.INT_REVERSE_VAR, serializer.getTypeId());
        // serializer = serializerFactory.getSerializer(COMPLEMENT_VAR_INT_ADVANTAGE_LOWER_BOUND + 1);
        // Assert.assertEquals(TagId.INT_REVERSE_VAR, serializer.getTypeId());
        //
        //
        // serializer = serializerFactory.getSerializer(VAR_LONG_ADVANTAGE_UPPER_BOUND);
        // Assert.assertEquals(TagId.LONG, serializer.getTypeId());
        // serializer = serializerFactory.getSerializer(VAR_LONG_ADVANTAGE_LOWER_BOUND);
        // Assert.assertEquals(TagId.LONG_COMPLEMENT_REVERSE_VAR, serializer.getTypeId());

        // serializer = serializerFactory.getSerializer(VAR_LONG_ADVANTAGE_UPPER_BOUND - 1);
        // Assert.assertEquals(TagId.VAR_LONG, serializer.getTypeId());
        // serializer = serializerFactory.getSerializer(VAR_LONG_ADVANTAGE_LOWER_BOUND + 1);
        // Assert.assertEquals(TagId.VAR_LONG, serializer.getTypeId());


        // serializer = serializerFactory.getSerializer(NEGATIVE_VAR_LONG_ADVANTAGE_UPPER_BOUND);
        // Assert.assertEquals(TagId.VAR_LONG, serializer.getTypeId());
        // serializer = serializerFactory.getSerializer(NEGATIVE_VAR_LONG_ADVANTAGE_LOWER_BOUND);
        // Assert.assertEquals(TagId.LONG, serializer.getTypeId());
        //
        // serializer = serializerFactory.getSerializer(NEGATIVE_VAR_LONG_ADVANTAGE_UPPER_BOUND - 1);
        // Assert.assertEquals(TagId.LONG_COMPLEMENT_REVERSE_VAR, serializer.getTypeId());
        // serializer = serializerFactory.getSerializer(NEGATIVE_VAR_LONG_ADVANTAGE_LOWER_BOUND + 1);
        // Assert.assertEquals(TagId.LONG_COMPLEMENT_REVERSE_VAR, serializer.getTypeId());
        //
        //
        // serializer = serializerFactory.getSerializer(Float.intBitsToFloat(Integer.reverse(VAR_INT_ADVANTAGE_UPPER_BOUND)));
        // Assert.assertEquals(TagId.FLOAT, serializer.getTypeId());
        // serializer = serializerFactory.getSerializer(Float.intBitsToFloat(Integer.reverse(VAR_INT_ADVANTAGE_LOWER_BOUND)));
        // Assert.assertEquals(TagId.FLOAT, serializer.getTypeId());

        // serializer = serializerFactory.getSerializer(Float.intBitsToFloat(Integer.reverse(VAR_INT_ADVANTAGE_UPPER_BOUND - 1)));
        // Assert.assertEquals(TagId.FLOAT_REVERSE_VAR, serializer.getTypeId());
        // serializer = serializerFactory.getSerializer(Float.intBitsToFloat(Integer.reverse(VAR_INT_ADVANTAGE_LOWER_BOUND + 1)));
        // Assert.assertEquals(TagId.FLOAT_REVERSE_VAR, serializer.getTypeId());


        // serializer = serializerFactory.getSerializer(Double.longBitsToDouble(Long.reverse(VAR_LONG_ADVANTAGE_UPPER_BOUND)));
        // Assert.assertEquals(TagId.DOUBLE, serializer.getTypeId());
        // serializer = serializerFactory.getSerializer(Double.longBitsToDouble(Long.reverse(VAR_LONG_ADVANTAGE_LOWER_BOUND)));
        // Assert.assertEquals(TagId.DOUBLE, serializer.getTypeId());

        // serializer = serializerFactory.getSerializer(Double.longBitsToDouble(Long.reverse(VAR_LONG_ADVANTAGE_UPPER_BOUND - 1)));
        // Assert.assertEquals(TagId.DOUBLE_REVERSE_VAR, serializer.getTypeId());
        // serializer = serializerFactory.getSerializer(Double.longBitsToDouble(Long.reverse(VAR_LONG_ADVANTAGE_LOWER_BOUND + 1)));
        // Assert.assertEquals(TagId.DOUBLE_REVERSE_VAR, serializer.getTypeId());


        // serializer = serializerFactory.getSerializer(true);
        // Assert.assertEquals(TagId.BOOL_TRUE, serializer.getTypeId());
        // serializer = serializerFactory.getSerializer(false);
        // Assert.assertEquals(TagId.BOOL_FALSE, serializer.getTypeId());


        // MyInterface myProxy = (MyInterface) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
        //         new Class[]{MyInterface.class}, new MyInvocationHandler());
        // Assert.assertEquals(serializerFactory.getSerializer(myProxy).type, Proxy.class);

        Assert.assertNotNull(serializerFactory.getSerializer(MyInterface[].class));
        Assert.assertNotNull(serializerFactory.getSerializer(MySuperInterface.class));
        Assert.assertNotNull(serializerFactory.getSerializer(MyInterface.class));

        Assert.assertNotNull(serializerFactory.getSerializer(MySuperClass.class));
        Assert.assertNotNull(serializerFactory.getSerializer(MyClass.class));
        Assert.assertNotNull(serializerFactory.getSerializer(MyClass2.class));
    }

    @Test
    public void testNewSerializer() {
        Assert.assertThrows(IllegalArgumentException.class, () -> {
            DefaultSerializerFactory.newSerializer(MyRegister.class, DefaultSerializerFactory.getSharedInstance().getSerializer(Map.class));
        });

        RuntimeException runtimeException = Assert.assertThrows(RuntimeException.class, () -> {
            MyInterface myProxy = (MyInterface) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                    new Class[]{MyInterface.class}, new MyInvocationHandler());
            newSerializer(myProxy.getClass(), new ProxySerializer());
        });

        Assert.assertTrue(runtimeException.getMessage().contains("Unable to create serializer"));
    }

}

class MyRegister {
    private String strVal;
    private int intVal;

    public String getStrVal() {
        return strVal;
    }

    public void setStrVal(String strVal) {
        this.strVal = strVal;
    }

    public int getIntVal() {
        return intVal;
    }

    public void setIntVal(int intVal) {
        this.intVal = intVal;
    }
}


class MyInvocationHandler implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return "MyInvocationHandler";
    }
}

interface MyInterface extends MySuperInterface {

}


interface MySuperInterface {

}

class MyClass extends MySuperClass {

}

class MySuperClass implements MySuperInterface {

}

class MyClass2 extends MySuperClass2 {

}

class MySuperClass2 implements MySuperInterface {

}
