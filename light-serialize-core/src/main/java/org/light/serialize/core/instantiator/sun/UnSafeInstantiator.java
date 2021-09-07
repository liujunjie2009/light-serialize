package org.light.serialize.core.instantiator.sun;

import org.light.serialize.core.instantiator.ObjectInstantiator;
import org.light.serialize.core.util.UnsafeUtil;
import sun.misc.Unsafe;

/**
 * Instantiator which is using {@link Unsafe} to create class instance.
 *
 * @author alex
 */
public class UnSafeInstantiator<T> implements ObjectInstantiator<T> {

    private final Unsafe unsafe;
    private final Class<T> type;

    public UnSafeInstantiator(Class<T> type) {
        this.unsafe = UnsafeUtil.getUnsafe();
        this.type = type;
    }

    @Override
    public T newInstance() {
        try {
            return (T) unsafe.allocateInstance(type);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        }
    }
}
