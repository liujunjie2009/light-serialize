package org.light.serialize.core.instantiator;

/**
 * Instantiates a new object.
 *
 * @author alex
 */
public interface ObjectInstantiator<T> {

    /**
     * Returns a new instance of an object. The returned object's class is defined by the
     * implementation.
     *
     * @return A new instance of an object.
     */
    T newInstance();
}
