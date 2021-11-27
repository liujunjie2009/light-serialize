package org.light.serialize.core.io;

import org.light.serialize.core.constants.Strategy;
import org.light.serialize.core.serializer.DefaultSerializerFactory;
import org.light.serialize.core.serializer.SerializerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * The read context.
 *
 * @author alex
 */
public class ReadContext {

    private static final ThreadLocal<ReadContext> readContexts = new ThreadLocal() {
        @Override
        protected Object initialValue() {
            return new ReadContext();
        }
    };

    private SerializerFactory serializerFactory = DefaultSerializerFactory.getSharedInstance();

    /**
     * The reference objects, object reference index start from 0.
     */
    private final List<Object> referenceObjects = new ArrayList<>(256);

    /**
     * The reference classes, class reference index start from 0.
     */
    private final List<Class<?>> referenceTypes = new ArrayList<>(32);


    /**
     * The reference FieldSerializer, class reference index start from 0.
     */
    private final List<Object[]> referenceFieldSerializers = new ArrayList<>(16);

    public void addReferenceFieldSerializers(Object[] fieldSerializers) {
        referenceFieldSerializers.add(fieldSerializers);
    }

    public Object[] getReferenceFieldSerializers(int index) {
        List<Object[]> referenceFieldSerializers = this.referenceFieldSerializers;
        return referenceFieldSerializers.size() > index ? referenceFieldSerializers.get(index) : null;
    }


    /**
     * The serialize object recursion depth.
     */
    private int depth = 0;

    /**
     * The serializing global strategy.
     * TODO: 只有内部才可以设置值
     */
    private Strategy strategy = Strategy.getDefault();

    /**
     * Get ReadContext.
     */
    public static ReadContext get() {
        return readContexts.get();
    }

    /**
     * Get reference object.
     */
    public Object getReferenceObject(int index) {
        return referenceObjects.get(index);
    }

    /**
     * Get reference objects size.
     */
    public int getReferenceObjectsSize() {
        return referenceObjects.size();
    }

    /**
     * Get reference class.
     */
    public Class<?> getReferenceType(int index) {
        return referenceTypes.get(index);
    }

    public int getReferenceTypeSize() {
        return referenceTypes.size();
    }

    /**
     * Put reference object.
     */
    public void putReferenceObject(Object object) {
        referenceObjects.add(object);
    }

    /**
     * Put reference object if absent.
     */
    public void putReferenceObjectIfAbsent(Object object, int refIndex) {
        if (referenceObjects.size() == refIndex) {
            referenceObjects.add(object);
        }
    }

    public void putReferenceObject(int refIndex, Object object) {
        referenceObjects.set(refIndex, object);
    }

    /**
     * Put reference class.
     */
    public void putReferenceType(Class<?> type) {
        referenceTypes.add(type);
    }

    /**
     * Increase Depth.
     */
    public int increaseDepth() {
        return ++depth;
    }

    /**
     * Decrease depth
     */
    public int decreaseDepth() {
        return --depth;
    }

    public SerializerFactory getSerializerFactory() {
        return serializerFactory;
    }

    public Strategy getStrategy() {
        return strategy;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Reset context.
     */
    public void reset() {
//        this.serializerFactory = DefaultSerializerFactory.getSharedInstance();
        this.referenceObjects.clear();
        this.referenceTypes.clear();
        this.referenceFieldSerializers.clear();
        this.depth = 0;
    }
}
