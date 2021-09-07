package org.light.serialize.core.util;

import org.light.serialize.core.constants.TagId;
import org.light.serialize.core.serializer.java.LambdaSerializer;

import java.lang.reflect.Proxy;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * TypeResolver
 *
 * @author alex
 */
public class TypeResolver {

    private static final Map<Class<?>, Long> typeTagIds = new HashMap<>(128);
    static {
        typeTagIds.put(Boolean.TYPE, Long.valueOf(TagId.BOOL));
        typeTagIds.put(Boolean.class, Long.valueOf(TagId.BOOL_WRAPPER));
        typeTagIds.put(Byte.TYPE, Long.valueOf(TagId.BYTE));
        typeTagIds.put(Byte.class, Long.valueOf(TagId.BYTE_WRAPPER));
        typeTagIds.put(Character.TYPE, Long.valueOf(TagId.CHAR));
        typeTagIds.put(Character.class, Long.valueOf(TagId.CHAR_WRAPPER));
        typeTagIds.put(Short.TYPE, Long.valueOf(TagId.SHORT));
        typeTagIds.put(Short.class, Long.valueOf(TagId.SHORT_WRAPPER));
        typeTagIds.put(Integer.TYPE, Long.valueOf(TagId.INT));
        typeTagIds.put(Integer.class, Long.valueOf(TagId.INT_WRAPPER));
        typeTagIds.put(Long.TYPE, Long.valueOf(TagId.LONG));
        typeTagIds.put(Long.class, Long.valueOf(TagId.LONG_WRAPPER));
        typeTagIds.put(Float.TYPE, Long.valueOf(TagId.FLOAT));
        typeTagIds.put(Float.class, Long.valueOf(TagId.FLOAT_WRAPPER));
        typeTagIds.put(Double.TYPE, Long.valueOf(TagId.DOUBLE));
        typeTagIds.put(Double.class, Long.valueOf(TagId.DOUBLE_WRAPPER));
        typeTagIds.put(String.class, Long.valueOf(TagId.STRING));
        typeTagIds.put(Class.class, Long.valueOf(TagId.CLASS));
        typeTagIds.put(Void.TYPE, Long.valueOf(TagId.VOID));
        typeTagIds.put(Void.class, Long.valueOf(TagId.VOID_WRAPPER));
        typeTagIds.put(Object.class, Long.valueOf(TagId.OBJECT));
        typeTagIds.put(boolean[].class, Long.valueOf(TagId.BOOL_ARRAY));
        typeTagIds.put(Boolean[].class, Long.valueOf(TagId.BOOL_WRAPPER_ARRAY));
        typeTagIds.put(byte[].class, Long.valueOf(TagId.BYTE_ARRAY));
        typeTagIds.put(Byte[].class, Long.valueOf(TagId.BYTE_WRAPPER_ARRAY));
        typeTagIds.put(char[].class, Long.valueOf(TagId.CHAR_ARRAY));
        typeTagIds.put(Character[].class, Long.valueOf(TagId.CHAR_WRAPPER_ARRAY));
        typeTagIds.put(short[].class, Long.valueOf(TagId.SHORT_ARRAY));
        typeTagIds.put(Short[].class, Long.valueOf(TagId.SHORT_WRAPPER_ARRAY));
        typeTagIds.put(int[].class, Long.valueOf(TagId.INT_ARRAY));
        typeTagIds.put(Integer[].class, Long.valueOf(TagId.INT_WRAPPER_ARRAY));
        typeTagIds.put(long[].class, Long.valueOf(TagId.LONG_ARRAY));
        typeTagIds.put(Long[].class, Long.valueOf(TagId.LONG_WRAPPER_ARRAY));
        typeTagIds.put(float[].class, Long.valueOf(TagId.FLOAT_ARRAY));
        typeTagIds.put(Float[].class, Long.valueOf(TagId.FLOAT_WRAPPER_ARRAY));
        typeTagIds.put(double[].class, Long.valueOf(TagId.DOUBLE_ARRAY));
        typeTagIds.put(Double[].class, Long.valueOf(TagId.DOUBLE_WRAPPER_ARRAY));
        typeTagIds.put(String[].class, Long.valueOf(TagId.STRING_ARRAY));
        typeTagIds.put(Class[].class, Long.valueOf(TagId.CLASS_ARRAY));
        typeTagIds.put(Object[].class, Long.valueOf(TagId.OBJECT_ARRAY));
        typeTagIds.put(HashSet.class, Long.valueOf(TagId.HASH_SET));
        typeTagIds.put(LinkedHashSet.class, Long.valueOf(TagId.LINKED_HASH_SET));
        typeTagIds.put(TreeSet.class, Long.valueOf(TagId.TREE_SET));
        typeTagIds.put(ArrayList.class, Long.valueOf(TagId.ARRAY_LIST));
        typeTagIds.put(LinkedList.class, Long.valueOf(TagId.LINKED_LIST));
        typeTagIds.put(Vector.class, Long.valueOf(TagId.VECTOR));
        typeTagIds.put(HashMap.class, Long.valueOf(TagId.HASH_MAP));
        typeTagIds.put(Hashtable.class, Long.valueOf(TagId.HASH_TABLE));
        typeTagIds.put(LinkedHashMap.class, Long.valueOf(TagId.LINKED_HASH_MAP));
        typeTagIds.put(ConcurrentHashMap.class, Long.valueOf(TagId.CONCURRENT_HASH_MAP));
        typeTagIds.put(TreeMap.class, Long.valueOf(TagId.TREE_MAP));
        typeTagIds.put(Optional.class, Long.valueOf(TagId.OPTIONAL));
        typeTagIds.put(OptionalDouble.class, Long.valueOf(TagId.OPTIONAL_DOUBLE));
        typeTagIds.put(OptionalInt.class, Long.valueOf(TagId.OPTIONAL_INT));
        typeTagIds.put(OptionalLong.class, Long.valueOf(TagId.OPTIONAL_LONG));
        typeTagIds.put(Date.class, Long.valueOf(TagId.DATE));
        typeTagIds.put(GregorianCalendar.class, Long.valueOf(TagId.GREGORIAN_CALENDAR));
        typeTagIds.put(BigDecimal.class, Long.valueOf(TagId.BIG_DECIMAL));
        typeTagIds.put(BigInteger.class, Long.valueOf(TagId.BIG_INTEGER));
        typeTagIds.put(Proxy.class, Long.valueOf(TagId.PROXY));
        typeTagIds.put(LambdaSerializer.Lambda.class, Long.valueOf(TagId.LAMBDA));
    }

    private TypeResolver() {
    }

    /**
     * resolve the type id of class.
     */
    public static long resolveTypeId(Class<?> type) {
        Objects.requireNonNull(type);
        Long typeId = typeTagIds.get(type);
        // tag type id: [Byte.MIN_VALUE, Byte.MAX_VALUE]
        if (typeId != null) {
            return typeId;
        }

        String typeName = type.getName();
        // resolve java class as int to save bytes
        if (type.getName().startsWith("java.")) {
            do {
                typeId = (long) XxHashUtil.hashInt(typeName);
            } while (typeId >= Byte.MIN_VALUE && typeId <= Byte.MAX_VALUE);

            return typeId;
        }

        // resolve non-java class as long
        do {
            typeId = XxHashUtil.hashLong(typeName);
        } while (typeId >= Byte.MIN_VALUE && typeId <= Byte.MAX_VALUE);

        return typeId;
    }

}
