package org.light.serialize.core.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ReflectUtil
 *
 * @author alex
 */
public class ReflectUtil {

    /**
     * Returns true if the specified type is a lambda. When true, Light-serialize uses {@link org.light.serialize.core.serializer.java.LambdaSerializer}
     * instead of the specified type to find the class {@link org.light.serialize.core.serializer.Serializer}.
     * <p>
     * This can be overridden to support alternative lambda implementations.
     * The default implementation returns true if the specified type's name contains '/' (to detect a Java 8+ lambda).
     */
    public static boolean isLambda(Class<?> type) {
        Objects.requireNonNull(type);
        return type.getName().indexOf('/') >= 0;
    }

    /**
     * Load class by name.
     */
    public static final ConcurrentHashMap<String, Class<?>> typeCache = new ConcurrentHashMap<>(64);
    public static Class<?> loadClass(String name) {
        try {
            Class<?> clazz = typeCache.get(name);
            if (clazz == null) {
                ClassLoader loader = Thread.currentThread().getContextClassLoader();
                if (loader != null) {
                    clazz = Class.forName(name, false, loader);
                } else {
                    clazz = Class.forName(name);
                }

                typeCache.put(name, clazz);
            }

            return clazz;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get accessible field
     */
    public static Field getAccessibleField(Class<?> clazz, String name) {
        Objects.requireNonNull(clazz);
        if (name == null || name.length() == 0) {
            throw new IllegalArgumentException("name must not be empty");
        }

        Class<?> searchType = clazz;
        while (Object.class != searchType && searchType != null) {
            Field[] fields = searchType.getDeclaredFields();

            for (int i = 0; i < fields.length; i++) {
                Field f = fields[i];
                if (f.getName().equals(name)) {
                    f.setAccessible(true);
                    return f;
                }
            }
            searchType = searchType.getSuperclass();
        }

        return null;
    }

    /**
     * Get accessible method.
     */
    public static Method getAccessibleMethod(Class<?> clazz, String name) {
        if (clazz == null) {
            throw new IllegalArgumentException("clazz must not be null");
        }

        if (name == null || name.length() == 0) {
            throw new IllegalArgumentException("name must not be empty");
        }

        Class<?> searchType = clazz;
        while (Object.class != searchType && searchType != null) {
            Method[] methods = searchType.getDeclaredMethods();

            for (int i = 0; i < methods.length; i++) {
                Method m = methods[i];
                if (m.getName().equals(name)) {
                    m.setAccessible(true);
                    return m;
                }
            }
            searchType = searchType.getSuperclass();
        }

        return null;
    }

    /**
     * Get Accessible Constructor.
     */
    public static Constructor<?> getAccessibleConstructor(Class<?> clazz, Class<?>... parameterTypes) {
        try {
            Constructor<?> constructor = clazz.getConstructor(parameterTypes);
            constructor.setAccessible(true);
            return constructor;
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }


}
