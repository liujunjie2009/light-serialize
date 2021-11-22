package org.light.serialize.core.io;

import org.light.serialize.core.constants.Constants;
import org.light.serialize.core.constants.Strategy;
import org.light.serialize.core.serializer.DefaultSerializerFactory;
import org.light.serialize.core.serializer.Serializer;
import org.light.serialize.core.serializer.SerializerFactory;
import org.light.serialize.core.util.IdentityIntMap;
import org.light.serialize.core.util.ObjectIntMap;

import java.util.HashMap;
import java.util.Map;

/**
 * The write context.
 *
 * @author alex
 */
public class WriteContext {

    private static final ThreadLocal<WriteContext> writeContexts = new ThreadLocal() {
        @Override
        protected Object initialValue() {
            return new WriteContext();
        }
    };

    private SerializerFactory serializerFactory = DefaultSerializerFactory.getSharedInstance();

    /**
     * The object reference indexes, index start from 0,key: serialized object.
     */
    private final IdentityIntMap<Object> objectIndexes = new IdentityIntMap<>();

    /**
     * The class reference indexes, index start from 0,key: class.
     */
    private final IdentityIntMap<Class<?>> typeIndexes = new IdentityIntMap<>();

    /**
     * The field name indexes, index start from 0,key: field name.
     */
//    private ObjectIntMap<String> fieldNameIndexes = new ObjectIntMap<>(128);

    /**
     * The field name type indexes, index start from 0,key: field name class.
     */
    private ObjectIntMap<Class<?>> fieldNameTypeIndexes = new ObjectIntMap<>(128);

    /**
     * The serialize object recursion depth.
     */
    private int depth = 0;

    /**
     * The serializing global strategy.
     * TODO: 只有内部才可以设置值
     */
    private Strategy strategy = Strategy.getDefault();

    public static WriteContext get() {
        return writeContexts.get();
    }

    public Strategy getStrategy() {
        return strategy;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    public int getObjectIndex(Object obj) {
        if (obj == null) {
            return Constants.NULL_INDEX;
        } else {
            return objectIndexes.get(obj, Constants.NULL_INDEX);
        }
    }

    /**
     * Put object, if object is not exist, the object reference index will be increased by 1.
     */
    public int putObject(Object obj) {
        IdentityIntMap<Object> objectIndexes = this.objectIndexes;
        int index = objectIndexes.size();
        objectIndexes.put(obj, index);
        return index;
    }

    public int objectIndexSize() {
        return this.objectIndexes.size();
    }

    /**
     * Get the class reference index.
     */
    public int getTypeIndex(Class<?> type) {
        return typeIndexes.get(type, Constants.NULL_INDEX);
    }

    public int typeIndexSize() {
        return typeIndexes.size();
    }

    /**
     * Put class, if class is not exist, the class reference index will be {@code classIndexSeed} increased by 1.
     */
    public int putClass(Class<?> type) {
        IdentityIntMap<Class<?>> typeIndexes = this.typeIndexes;
        int index = typeIndexes.get(type, Constants.NULL_INDEX);
        if (index == Constants.NULL_INDEX) {
            index = typeIndexSize();
            typeIndexes.put(type, index);
        }

        return index;
    }

//    public Integer putFieldName(String fieldName) {
//        ObjectIntMap<String> fieldNameIndexes = this.fieldNameIndexes;
//        Integer index = fieldNameIndexes.get(fieldName, -1);
//        if (index == null) {
//            index = fieldNameIndexes.size();
//            fieldNameIndexes.put(fieldName, index);
//        }
//
//        return index;
//    }

//    public Integer getFieldNameIndex(String fieldName) {
//        return fieldNameIndexes.get(fieldName, -1);
//    }

    public void putFieldNameClass(Class<?> type) {
        ObjectIntMap<Class<?>> indexes = this.fieldNameTypeIndexes;
        int index = indexes.size();
        indexes.put(type, index);
    }

    public int getFieldNameTypeIndex(Class<?> type) {
        return this.fieldNameTypeIndexes.get(type, Constants.NULL_INDEX);
    }

    public int increaseDepth() {
        return ++depth;
    }

    public int decreaseDepth() {
        return --depth;
    }

    public SerializerFactory getSerializerFactory() {
        return serializerFactory;
    }

    public void reset() {
        this.serializerFactory = DefaultSerializerFactory.getSharedInstance();
        this.objectIndexes.clear();
        this.typeIndexes.clear();
//        this.fieldNameIndexes.clear();
        this.fieldNameTypeIndexes.clear();
        this.depth = 0;
        this.strategy = Strategy.getDefault();
    }

}
