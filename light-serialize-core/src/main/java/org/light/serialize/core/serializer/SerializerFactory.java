package org.light.serialize.core.serializer;

/**
 * Creates, register, get serializer.
 *
 * @author alex
 */
public interface SerializerFactory {

    /**
     * Register serializer.
     */
    void register(Serializer<?> serializer);

    /**
     * Get registered serializer by typeId.
     */
    Serializer<?> getRegisteredSerializer(Long typeId);

    /**
     * Get registered Serializer by class.
     */
    Serializer<?> getRegisteredSerializer(Class<?> type);

    /**
     * Get serializer by class.
     */
    Serializer<?> getSerializer(Class<?> type);

}
