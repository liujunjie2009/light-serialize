package org.light.serialize.core.serializer;

import org.light.serialize.core.constants.Constants;
import org.light.serialize.core.serializer.java.*;
import org.light.serialize.core.util.ReflectUtil;

import java.io.IOException;
import java.lang.reflect.Proxy;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.Objects;
import java.util.SimpleTimeZone;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;

/**
 * DefaultSerializerFactory
 *
 * @author alex
 */
public class DefaultSerializerFactory implements SerializerFactory {

    private static final DefaultSerializerFactory sharedInstance = new DefaultSerializerFactory();

    protected final ConcurrentHashMap<Class<?>, Serializer<?>> serializers = new ConcurrentHashMap<>(1024);
    protected final ConcurrentHashMap<Long, Serializer<?>> registeredSerializers = new ConcurrentHashMap<>(258);

    public static DefaultSerializerFactory getSharedInstance() {
        return sharedInstance;
    }

    protected DefaultSerializerFactory() {
        initRegisteredSerializers();
    }

    protected void initRegisteredSerializers() {
        ArraySerializers.register(this);
        BasicSerializers.register(this);
        BigNumberSerializers.register(this);
        CollectionSerializers.register(this);
        MapSerializers.register(this);
        OptionalSerializers.register(this);
        TimeSerializers.register(this);

        register(new CalendarSerializer(Calendar.class));
        register(Constants.GREGORIAN_CALENDAR_SERIALIZER);
        register(new CharsetSerializer(Charset.class));
        register(Constants.CLASS_SERIALIZER);
        register(new CurrencySerializer());
        register(new EnumSerializer<>(Enum.class));
        register(new InetAddressSerializer(InetAddress.class));
        register(new InetAddressSerializer(Inet4Address.class));
        register(new InetAddressSerializer(Inet6Address.class));
        register(Constants.LAMBDA_SERIALIZER);
        register(new LightSerializableSerializer<>(LightSerializable.class));
        register(new LocaleSerializer());
        register(new PatternSerializer());
        register(Constants.PROXY_SERIALIZER);
        register(new ThrowableSerializer<>(Throwable.class));
        register(new ThrowableSerializer<>(Exception.class));
        register(new ThrowableSerializer<>(RuntimeException.class));
        register(new ThrowableSerializer<>(NullPointerException.class));
        register(new ThrowableSerializer<>(IOException.class));
        register(new TimeZoneSerializer(TimeZone.class));
        register(new TimeZoneSerializer(SimpleTimeZone.class));
        register(new UnicodeBlockSerializer());
        register(new URISerializer());
        register(new URLSerializer());
        register(new UUIDSerializer());
    }
    
    @Override
    public void register(Serializer<?> serializer) {
        Objects.requireNonNull(serializer);
        ConcurrentHashMap<Long, Serializer<?>> registeredSerializers = this.registeredSerializers;
        Long typeId = serializer.getTypeId();
        Serializer<?> preSerializer = registeredSerializers.get(typeId);

        if (preSerializer != null) {
            throw new IllegalStateException(String.format("typeId[%d] has been registered by [%s]", typeId, preSerializer));
        }

        registeredSerializers.put(typeId, serializer);
        serializers.put(serializer.getType(), serializer);
    }

    @Override
    public Serializer<?> getRegisteredSerializer(Long typeId) {
        return this.registeredSerializers.get(typeId);
    }

    @Override
    public Serializer<?> getRegisteredSerializer(Class<?> type) {
        Serializer<?> serializer = getSerializer(type);
        return getRegisteredSerializer(serializer.getTypeId());
    }

    @Override
    public Serializer<?> getSerializer(Class<?> type) {
        Objects.requireNonNull(type);

        ConcurrentHashMap<Class<?>, Serializer<?>> serializers = this.serializers;
        Serializer<?> serializer = serializers.get(type);

        if (serializer != null) {
            return serializer;
        }

        // ObjectArray
        if (type.isArray()) {
            serializer = new ArraySerializers.ObjectArraySerializer((Class<? extends Object[]>) type);
            serializers.put(type, serializer);
            return serializer;
        }

        // LightSerializable
        if (LightSerializable.class.isAssignableFrom(type)) {
            serializer = new LightSerializableSerializer<>(type);
            serializers.put(type, serializer);
            return serializer;
        }

        // If a Proxy class, treat it like an InvocationHandler because the concrete class for a proxy is generated.
        if (Proxy.isProxyClass(type)) {
            return Constants.PROXY_SERIALIZER;
        }

        // This handles an enum value that is an inner class, eg: enum A {b{}}, TimeUnit
        if (!type.isEnum() && Enum.class.isAssignableFrom(type) && type != Enum.class) {
            return getSerializer(type.getSuperclass());
        }

        // Lambda
        if (ReflectUtil.isLambda(type)) {
            return Constants.LAMBDA_SERIALIZER;
        }

        /*
         * create serializer from interfaces.
         */
        Class<?>[] interfaces = type.getInterfaces();
        for (int i = 0; i < interfaces.length; i++) {
            Serializer<?> interfaceSerializer = serializers.get(interfaces[i]);
            if (interfaceSerializer != null) {
                serializer = newSerializer(type, interfaceSerializer);
                serializers.put(type, serializer);
                return serializer;
            }
        }

        /*
         * create serializer from super class.
         */
        Class<?> superType = type.getSuperclass();
        while (superType != Object.class && superType != null) {
            Serializer<?> superSerializer = serializers.get(superType);
            if (superSerializer != null) {
                serializer = newSerializer(type, superSerializer);
                serializers.put(type, serializer);
                return serializer;
            }

            Class<?>[] superInterfaces = superType.getInterfaces();
            for (int i = 0; i < superInterfaces.length; i++) {
                Serializer<?> superInterfaceSerializer = serializers.get(superInterfaces[i]);
                if (superInterfaceSerializer != null) {
                    serializer = newSerializer(type, superInterfaceSerializer);
                    serializers.put(type, serializer);
                    return serializer;
                }
            }

            superType = superType.getSuperclass();
        }

        serializer = new ObjectSerializer(type);
        serializers.put(type, serializer);
        return serializer;
    }

    /**
     * new serializer
     */
    public static Serializer<?> newSerializer(Class<?> type, Serializer<?> serializer) {
        Class<?> serializerClass = serializer.getClass();
        if (!serializer.getType().isAssignableFrom(type)) {
            throw new IllegalArgumentException(String.format("serializer type[%s] is not assignable from type:%s",
                    serializer.getType().getName(), type.getName()));
        }

        try {
            return (Serializer<?>) serializerClass.getConstructor(Class.class).newInstance(type);
        } catch (Throwable e) {
            throw new RuntimeException(
                    String.format("Unable to create serializer for class[%s] by serializer[%s]", type.getName(), serializer),
                    e);
        }
    }

//    public Map<Class<?>, Serializer<?>> getSerializers() {
//        return serializers;
//    }

}
