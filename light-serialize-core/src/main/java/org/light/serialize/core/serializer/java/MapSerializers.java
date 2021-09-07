package org.light.serialize.core.serializer.java;

import org.light.serialize.core.instantiator.ObjectInstantiator;
import org.light.serialize.core.instantiator.sun.UnSafeInstantiator;
import org.light.serialize.core.io.ObjectInput;
import org.light.serialize.core.io.ObjectOutput;
import org.light.serialize.core.io.ReadContext;
import org.light.serialize.core.serializer.Serializer;
import org.light.serialize.core.serializer.SerializerFactory;
import org.light.serialize.core.util.ReflectUtil;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.function.Supplier;

import static org.light.serialize.core.serializer.java.MapSerializers.CheckedMapSerializer.CHECKED_MAP_TYPE;
import static org.light.serialize.core.serializer.java.MapSerializers.CheckedNavigableMapSerializer.CHECKED_NAVIGABLE_MAP_TYPE;
import static org.light.serialize.core.serializer.java.MapSerializers.CheckedSortedMapSerializer.CHECKED_SORTED_MAP_TYPE;
import static org.light.serialize.core.serializer.java.MapSerializers.SingletonMapSerializer.SINGLETON_MAP_TYPE;
import static org.light.serialize.core.serializer.java.MapSerializers.SynchronizedMapSerializer.SYNCHRONIZED_MAP_TYPE;
import static org.light.serialize.core.serializer.java.MapSerializers.SynchronizedNavigableMapSerializer.SYNCHRONIZED_NAVIGABLE_MAP_TYPE;
import static org.light.serialize.core.serializer.java.MapSerializers.SynchronizedSortedMapSerializer.SYNCHRONIZED_SORTED_MAP_TYPE;
import static org.light.serialize.core.serializer.java.MapSerializers.UnmodifiableMapSerializer.UNMODIFIABLE_MAP_TYPE;
import static org.light.serialize.core.serializer.java.MapSerializers.UnmodifiableSortedMapSerializer.UNMODIFIABLE_SORTED_MAP_TYPE;

/**
 * MapSerializers.
 * TODO:ConcurrentSkipListMap   ConcurrentHashMap
 * @author alex
 */
public class MapSerializers {

    public static final MapSerializer<HashMap> HASH_MAP_SERIALIZER = new MapSerializer<>(HashMap.class);
    public static final MapSerializer<LinkedHashMap> LINKED_HASH_MAP_SERIALIZER = new MapSerializer<>(LinkedHashMap.class);
    public static final MapSerializer<Hashtable> HASH_TABLE_SERIALIZER = new MapSerializer<>(Hashtable.class);
    public static final MapSerializer<ConcurrentHashMap> CONCURRENT_HASH_MAP_SERIALIZER = new MapSerializer<>(ConcurrentHashMap.class);
    public static final TreeMapSerializer TREE_MAP_SERIALIZER = new TreeMapSerializer(TreeMap.class);

    /**
     * The serializer for {@code java.util.Map}.
     */
    public static class MapSerializer<T extends Map> extends Serializer<T> {

        public MapSerializer(Class<T> type) {
            super(type);
        }

        @Override
        public void write(ObjectOutput output, T value) throws IOException {
            Object[] items = value.entrySet().toArray();
            int size = items.length;

            output.writeVarInt(size);
            writeHeader(output, value);

            for (int i = 0; i < size; i++) {
                Map.Entry<?, ?> item = (Map.Entry<?, ?>) items[i];
                output.writeObject(item.getKey());
                output.writeObject(item.getValue());
            }
        }

        @Override
        public T read(ObjectInput input) throws IOException {
            ReadContext readContext = ReadContext.get();
            int refObjectsSize = readContext.getReferenceObjectsSize();
            int size = input.readVarInt();
            T map = create(input, size);

            if (refObjectsSize == readContext.getReferenceObjectsSize()) {
                ReadContext.get().putReferenceObject(map);
            }

            for (int i = 0; i < size; i++) {
                map.put(input.readObject(), input.readObject());
            }

            return map;
        }

        /**
         * Write map header.
         */
        protected void writeHeader(ObjectOutput output, T value) throws IOException {

        }

        protected T create(ObjectInput input, int size) throws IOException {
            try {
                if (type.equals(HashMap.class)) {
                    // 1073741824(1<<30): maximum capacity which is  power of two.
                    return (T) new HashMap(size < 1073741824 ? (int) (size / 0.75f + 1) : size);
                }

                // 1610612736: Integer.MAX_VALUE * 0.75
                if (type.equals(Hashtable.class)) {
                    return (T) new Hashtable(size < 1610612736 ? (int) (size / 0.75f + 1) : size);
                }

                if (type.equals(ConcurrentHashMap.class)) {
                    // 1073741824(1<<30): maximum capacity which is  power of two.
                    return (T) new ConcurrentHashMap(size < 1073741824 ? (int) (size / 0.75f + 1) : size);
                }

                return (T) type.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new IOException(e);
            }
        }
    }


    /**
     * The serializer for {@code java.util.Collections$EmptyMap}.
     */
    public static class EmptyMapSerializer<T extends Map> extends Serializer<T> {

        private final Supplier<T> supplier;

        public EmptyMapSerializer(Class<? extends T> type, Supplier<T> supplier) {
            super(type);
            this.supplier = supplier;
        }

        @Override
        public void write(ObjectOutput output, T value) throws IOException {

        }

        @Override
        public T read(ObjectInput input) throws IOException {
            return supplier.get();
        }

    }


    /**
     * The serializer for {@code java.util.Collections$SingletonMap}.
     */
    public static class SingletonMapSerializer extends Serializer<Map> {

        static final Class<?> SINGLETON_MAP_TYPE = ReflectUtil.loadClass("java.util.Collections$SingletonMap");
        static final Field SINGLETON_MAP_K_FIELD = ReflectUtil.getAccessibleField(SINGLETON_MAP_TYPE, "k");
        static final Field SINGLETON_MAP_V_FIELD = ReflectUtil.getAccessibleField(SINGLETON_MAP_TYPE, "v");

        private final ObjectInstantiator<Map> instantiator = new UnSafeInstantiator(type);

        public SingletonMapSerializer(Class<?> type) {
            super(type);
        }

        @Override
        public void write(ObjectOutput output, Map value) throws IOException {
            Map.Entry<?, ?> entry = (Map.Entry<?, ?>) value.entrySet().iterator().next();
            output.writeObject(entry.getKey());
            output.writeObject(entry.getValue());
        }

        @Override
        public Map read(ObjectInput input) throws IOException {
            try {
                Map map = instantiator.newInstance();
                ReadContext.get().putReferenceObject(map);
                SINGLETON_MAP_K_FIELD.set(map, input.readObject());
                SINGLETON_MAP_V_FIELD.set(map, input.readObject());
                return map;
            } catch (IllegalAccessException e) {
                throw new IOException(e);
            }
        }
    }


    /**
     * The serializer for {@code java.util.Collections$UnmodifiableMap}.
     */
    public static class UnmodifiableMapSerializer<T extends Map> extends Serializer<T> {

        static final Class<?> UNMODIFIABLE_MAP_TYPE = ReflectUtil.loadClass("java.util.Collections$UnmodifiableMap");
        static final Field UNMODIFIABLE_MAP_M_FIELD = ReflectUtil.getAccessibleField(UNMODIFIABLE_MAP_TYPE, "m");

        private final ObjectInstantiator<T> instantiator = new UnSafeInstantiator(type);

        public UnmodifiableMapSerializer(Class<? extends T> type) {
            super(type);
        }

        @Override
        public void write(ObjectOutput output, T value) throws IOException {
            try {
                Object object = UNMODIFIABLE_MAP_M_FIELD.get(value);
                output.writeObject(object);
            } catch (Throwable e) {
                throw new IOException(e);
            }
        }

        @Override
        public T read(ObjectInput input) throws IOException {
            try {
                T map = instantiator.newInstance();
                ReadContext.get().putReferenceObject(map);
                UNMODIFIABLE_MAP_M_FIELD.set(map, input.readObject());
                return map;
            } catch (IllegalAccessException e) {
                throw new IOException(e);
            }
        }

    }


    /**
     * The serializer for {@code java.util.Collections$UnmodifiableSortedMap}.
     */
    public static class UnmodifiableSortedMapSerializer<T extends Map> extends Serializer<T> {

        static final Class<?> UNMODIFIABLE_SORTED_MAP_TYPE = ReflectUtil.loadClass("java.util.Collections$UnmodifiableSortedMap");
        static final Field UNMODIFIABLE_SORTED_MAP_M_FIELD = ReflectUtil.getAccessibleField(UNMODIFIABLE_SORTED_MAP_TYPE, "m");
        static final Field UNMODIFIABLE_SORTED_MAP_SM_FIELD = ReflectUtil.getAccessibleField(UNMODIFIABLE_SORTED_MAP_TYPE, "sm");

        private final ObjectInstantiator<T> instantiator = new UnSafeInstantiator(type);

        public UnmodifiableSortedMapSerializer(Class<? extends T> type) {
            super(type);
        }

        @Override
        public void write(ObjectOutput output, T value) throws IOException {
            try {
                Object object = UNMODIFIABLE_SORTED_MAP_M_FIELD.get(value);
                output.writeObject(object);
            } catch (Throwable e) {
                throw new IOException(e);
            }
        }

        @Override
        public T read(ObjectInput input) throws IOException {
            try {
                T map = instantiator.newInstance();
                ReadContext.get().putReferenceObject(map);
                Object m = input.readObject();
                UNMODIFIABLE_SORTED_MAP_M_FIELD.set(map, m);
                UNMODIFIABLE_SORTED_MAP_SM_FIELD.set(map, m);
                return map;
            } catch (IllegalAccessException e) {
                throw new IOException(e);
            }
        }

    }


    /**
     * The serializer for {@code java.util.Collections$CheckedMap}.
     */
    public static class CheckedMapSerializer<T extends Map> extends Serializer<T> {

        static final Class<?> CHECKED_MAP_TYPE = ReflectUtil.loadClass("java.util.Collections$CheckedMap");

        private static final Field CHECKED_MAP_M_FIELD = ReflectUtil.getAccessibleField(CHECKED_MAP_TYPE, "m");
        private static final Field CHECKED_MAP_KEY_TYPE_FIELD = ReflectUtil.getAccessibleField(CHECKED_MAP_TYPE, "keyType");
        private static final Field CHECKED_MAP_VALUE_TYPE_FIELD = ReflectUtil.getAccessibleField(CHECKED_MAP_TYPE, "valueType");

        private final ObjectInstantiator<T> instantiator = new UnSafeInstantiator(type);

        public CheckedMapSerializer(Class<? extends T> type) {
            super(type);
        }

        @Override
        public void write(ObjectOutput output, T value) throws IOException {
            try {
                output.writeObject(CHECKED_MAP_M_FIELD.get(value));
                output.writeObject(CHECKED_MAP_KEY_TYPE_FIELD.get(value));
                output.writeObject(CHECKED_MAP_VALUE_TYPE_FIELD.get(value));
            } catch (IllegalAccessException e) {
                throw new IOException(e);
            }
        }

        @Override
        public T read(ObjectInput input) throws IOException {
            try {
                T map = instantiator.newInstance();
                ReadContext.get().putReferenceObject(map);
                CHECKED_MAP_M_FIELD.set(map, input.readObject());
                CHECKED_MAP_KEY_TYPE_FIELD.set(map, input.readObject());
                CHECKED_MAP_VALUE_TYPE_FIELD.set(map, input.readObject());
                return map;
            } catch (IllegalAccessException e) {
                throw new IOException(e);
            }
        }
    }


    /**
     * The serializer for {@code java.util.Collections$CheckedSortedMap}.
     */
    public static class CheckedSortedMapSerializer<T extends Map> extends Serializer<T> {

        static final Class<?> CHECKED_SORTED_MAP_TYPE = ReflectUtil.loadClass("java.util.Collections$CheckedSortedMap");

        private static final Field CHECKED_SORTED_MAP_M_FIELD = ReflectUtil.getAccessibleField(CHECKED_SORTED_MAP_TYPE, "m");
        private static final Field CHECKED_SORTED_MAP_SM_FIELD = ReflectUtil.getAccessibleField(CHECKED_SORTED_MAP_TYPE, "sm");
        private static final Field CHECKED_SORTED_MAP_KEY_TYPE_FIELD = ReflectUtil.getAccessibleField(CHECKED_SORTED_MAP_TYPE, "keyType");
        private static final Field CHECKED_SORTED_MAP_VALUE_TYPE_FIELD = ReflectUtil.getAccessibleField(CHECKED_SORTED_MAP_TYPE, "valueType");

        private final ObjectInstantiator<T> instantiator = new UnSafeInstantiator(type);

        public CheckedSortedMapSerializer(Class<? extends T> type) {
            super(type);
        }

        @Override
        public void write(ObjectOutput output, T value) throws IOException {
            try {
                output.writeObject(CHECKED_SORTED_MAP_M_FIELD.get(value));
                output.writeObject(CHECKED_SORTED_MAP_KEY_TYPE_FIELD.get(value));
                output.writeObject(CHECKED_SORTED_MAP_VALUE_TYPE_FIELD.get(value));
            } catch (IllegalAccessException e) {
                throw new IOException(e);
            }
        }

        @Override
        public T read(ObjectInput input) throws IOException {
            try {
                T map = instantiator.newInstance();
                ReadContext.get().putReferenceObject(map);
                Object m = input.readObject();
                CHECKED_SORTED_MAP_M_FIELD.set(map, m);
                CHECKED_SORTED_MAP_SM_FIELD.set(map, m);
                CHECKED_SORTED_MAP_KEY_TYPE_FIELD.set(map, input.readObject());
                CHECKED_SORTED_MAP_VALUE_TYPE_FIELD.set(map, input.readObject());
                return map;
            } catch (IllegalAccessException e) {
                throw new IOException(e);
            }
        }
    }


    /**
     * The serializer for {@code java.util.Collections$CheckedNavigableMap}.
     */
    public static class CheckedNavigableMapSerializer<T extends Map> extends Serializer<T> {

        static final Class<?> CHECKED_NAVIGABLE_MAP_TYPE = ReflectUtil.loadClass("java.util.Collections$CheckedNavigableMap");

        private static final Field CHECKED_NAVIGABLE_MAP_M_FIELD = ReflectUtil.getAccessibleField(CHECKED_NAVIGABLE_MAP_TYPE, "m");
        private static final Field CHECKED_NAVIGABLE_MAP_SM_FIELD = ReflectUtil.getAccessibleField(CHECKED_NAVIGABLE_MAP_TYPE, "sm");
        private static final Field CHECKED_NAVIGABLE_MAP_NM_FIELD = ReflectUtil.getAccessibleField(CHECKED_NAVIGABLE_MAP_TYPE, "nm");
        private static final Field CHECKED_NAVIGABLE_MAP_KEY_TYPE_FIELD = ReflectUtil.getAccessibleField(CHECKED_NAVIGABLE_MAP_TYPE, "keyType");
        private static final Field CHECKED_NAVIGABLE_MAP_VALUE_TYPE_FIELD = ReflectUtil.getAccessibleField(CHECKED_NAVIGABLE_MAP_TYPE, "valueType");

        private final ObjectInstantiator<T> instantiator = new UnSafeInstantiator(type);

        public CheckedNavigableMapSerializer(Class<? extends T> type) {
            super(type);
        }

        @Override
        public void write(ObjectOutput output, T value) throws IOException {
            try {
                output.writeObject(CHECKED_NAVIGABLE_MAP_M_FIELD.get(value));
                output.writeObject(CHECKED_NAVIGABLE_MAP_KEY_TYPE_FIELD.get(value));
                output.writeObject(CHECKED_NAVIGABLE_MAP_VALUE_TYPE_FIELD.get(value));
            } catch (IllegalAccessException e) {
                throw new IOException(e);
            }
        }

        @Override
        public T read(ObjectInput input) throws IOException {
            try {
                T map = instantiator.newInstance();
                ReadContext.get().putReferenceObject(map);
                Object m = input.readObject();
                CHECKED_NAVIGABLE_MAP_M_FIELD.set(map, m);
                CHECKED_NAVIGABLE_MAP_SM_FIELD.set(map, m);
                CHECKED_NAVIGABLE_MAP_NM_FIELD.set(map, m);
                CHECKED_NAVIGABLE_MAP_KEY_TYPE_FIELD.set(map, input.readObject());
                CHECKED_NAVIGABLE_MAP_VALUE_TYPE_FIELD.set(map, input.readObject());
                return map;
            } catch (IllegalAccessException e) {
                throw new IOException(e);
            }
        }
    }


    /**
     * The serializer for {@code java.util.Collections$SynchronizedMap}.
     */
    public static class SynchronizedMapSerializer<T extends Map> extends Serializer<T> {
        static final Class<?> SYNCHRONIZED_MAP_TYPE = ReflectUtil.loadClass("java.util.Collections$SynchronizedMap");

        private static final Field SYNCHRONIZED_MAP_M_FIELD = ReflectUtil.getAccessibleField(SYNCHRONIZED_MAP_TYPE, "m");
        private static final Field SYNCHRONIZED_MAP_MUTEX_FIELD = ReflectUtil.getAccessibleField(SYNCHRONIZED_MAP_TYPE, "mutex");

        private final ObjectInstantiator<T> instantiator = new UnSafeInstantiator(type);

        public SynchronizedMapSerializer(Class<? extends T> type) {
            super(type);
        }

        @Override
        public void write(ObjectOutput output, T value) throws IOException {
            try {
                output.writeObject(SYNCHRONIZED_MAP_M_FIELD.get(value));
            } catch (IllegalAccessException e) {
                throw new IOException(e);
            }
        }

        @Override
        public T read(ObjectInput input) throws IOException {
            try {
                T map = instantiator.newInstance();
                ReadContext.get().putReferenceObject(map);
                Object m = input.readObject();
                SYNCHRONIZED_MAP_M_FIELD.set(map, m);
                SYNCHRONIZED_MAP_MUTEX_FIELD.set(map, map);
                return map;
            } catch (IllegalAccessException e) {
                throw new IOException(e);
            }
        }
    }


    /**
     * The serializer for {@code java.util.Collections$SynchronizedSortedMap}.
     */
    public static class SynchronizedSortedMapSerializer<T extends Map> extends Serializer<T> {
        static final Class<?> SYNCHRONIZED_SORTED_MAP_TYPE = ReflectUtil.loadClass("java.util.Collections$SynchronizedSortedMap");

        private static final Field SYNCHRONIZED_SORTED_MAP_M_FIELD = ReflectUtil.getAccessibleField(SYNCHRONIZED_SORTED_MAP_TYPE, "m");
        private static final Field SYNCHRONIZED_SORTED_MAP_SM_FIELD = ReflectUtil.getAccessibleField(SYNCHRONIZED_SORTED_MAP_TYPE, "sm");
        private static final Field SYNCHRONIZED_SORTED_MAP_MUTEX_FIELD = ReflectUtil.getAccessibleField(SYNCHRONIZED_SORTED_MAP_TYPE, "mutex");

        private final ObjectInstantiator<T> instantiator = new UnSafeInstantiator(type);

        public SynchronizedSortedMapSerializer(Class<? extends T> type) {
            super(type);
        }

        @Override
        public void write(ObjectOutput output, T value) throws IOException {
            try {
                output.writeObject(SYNCHRONIZED_SORTED_MAP_M_FIELD.get(value));
            } catch (IllegalAccessException e) {
                throw new IOException(e);
            }
        }

        @Override
        public T read(ObjectInput input) throws IOException {
            try {
                T map = instantiator.newInstance();
                ReadContext.get().putReferenceObject(map);
                Object m = input.readObject();
                SYNCHRONIZED_SORTED_MAP_M_FIELD.set(map, m);
                SYNCHRONIZED_SORTED_MAP_SM_FIELD.set(map, m);
                SYNCHRONIZED_SORTED_MAP_MUTEX_FIELD.set(map, map);
                return map;
            } catch (IllegalAccessException e) {
                throw new IOException(e);
            }
        }
    }


    /**
     * The serializer for {@code java.util.Collections$SynchronizedNavigableMap}.
     */
    public static class SynchronizedNavigableMapSerializer<T extends Map> extends Serializer<T> {
        static final Class<?> SYNCHRONIZED_NAVIGABLE_MAP_TYPE = ReflectUtil.loadClass("java.util.Collections$SynchronizedNavigableMap");

        private static final Field SYNCHRONIZED_NAVIGABLE_MAP_M_FIELD = ReflectUtil.getAccessibleField(SYNCHRONIZED_NAVIGABLE_MAP_TYPE, "m");
        private static final Field SYNCHRONIZED_NAVIGABLE_MAP_SM_FIELD = ReflectUtil.getAccessibleField(SYNCHRONIZED_NAVIGABLE_MAP_TYPE, "sm");
        private static final Field SYNCHRONIZED_NAVIGABLE_MAP_NM_FIELD = ReflectUtil.getAccessibleField(SYNCHRONIZED_NAVIGABLE_MAP_TYPE, "nm");
        private static final Field SYNCHRONIZED_NAVIGABLE_MAP_MUTEX_FIELD = ReflectUtil.getAccessibleField(SYNCHRONIZED_NAVIGABLE_MAP_TYPE, "mutex");


        private final ObjectInstantiator<T> instantiator = new UnSafeInstantiator(type);

        public SynchronizedNavigableMapSerializer(Class<? extends T> type) {
            super(type);
        }

        @Override
        public void write(ObjectOutput output, T value) throws IOException {
            try {
                output.writeObject(SYNCHRONIZED_NAVIGABLE_MAP_M_FIELD.get(value));
            } catch (IllegalAccessException e) {
                throw new IOException(e);
            }
        }

        @Override
        public T read(ObjectInput input) throws IOException {
            try {
                T map = instantiator.newInstance();
                ReadContext.get().putReferenceObject(map);
                Object m = input.readObject();
                SYNCHRONIZED_NAVIGABLE_MAP_M_FIELD.set(map, m);
                SYNCHRONIZED_NAVIGABLE_MAP_SM_FIELD.set(map, m);
                SYNCHRONIZED_NAVIGABLE_MAP_NM_FIELD.set(map, m);
                SYNCHRONIZED_NAVIGABLE_MAP_MUTEX_FIELD.set(map, map);
                return map;
            } catch (IllegalAccessException e) {
                throw new IOException(e);
            }
        }
    }


    /**
     * The serializer for {@code java.util.TreeMap}.
     */
    public static class TreeMapSerializer extends MapSerializer<TreeMap> {

        static final Field TREE_MAP_COMPARATOR_FIELD = ReflectUtil.getAccessibleField(TreeMap.class, "comparator");

        public TreeMapSerializer(Class<TreeMap> type) {
            super(type);
        }

        @Override
        protected void writeHeader(ObjectOutput output, TreeMap map) throws IOException {
            super.writeHeader(output, map);
            output.writeObject(map.comparator());
        }

        @Override
        protected TreeMap create(ObjectInput input, int size) throws IOException {
            try {
                TreeMap treeMap = new TreeMap();
                ReadContext.get().putReferenceObject(treeMap);
                Object comparator = input.readObject();
                TREE_MAP_COMPARATOR_FIELD.set(treeMap, comparator);
                return treeMap;
            } catch (IllegalAccessException e) {
                throw new IOException(e);
            }
        }
    }


    /**
     * EnumMapSerializer
     */
    public static class EnumMapSerializer extends MapSerializer<EnumMap> {

        private static final Class<?> ENUM_MAP_TYPE = EnumMap.class;
        private static final Field KEY_TYPE_FIELD = ReflectUtil.getAccessibleField(ENUM_MAP_TYPE, "keyType");

        public EnumMapSerializer() {
            super(EnumMap.class);
        }

        @Override
        protected void writeHeader(ObjectOutput output, EnumMap map) throws IOException {
            try {
                super.writeHeader(output, map);
                output.writeObject(KEY_TYPE_FIELD.get(map));
            } catch (IllegalAccessException e) {
                throw new IOException(e);
            }
        }

        @Override
        protected EnumMap create(ObjectInput input, int size) throws IOException {
            return new EnumMap((Class<?>) input.readObject());
        }

    }

    /**
     * Register map serializers.
     */
    public static void register(SerializerFactory factory) {
        factory.register(new MapSerializer<>(Map.class));
        factory.register(new MapSerializer<>(ConcurrentSkipListMap.class));
        factory.register(HASH_MAP_SERIALIZER);
        factory.register(LINKED_HASH_MAP_SERIALIZER);
        factory.register(HASH_TABLE_SERIALIZER);
        factory.register(CONCURRENT_HASH_MAP_SERIALIZER);

        factory.register(new EmptyMapSerializer(Collections.emptyMap().getClass(),Collections::emptyMap));
        // Collections#emptySortedMap and Collections#emptyNavigableMap returns the same instance.
        factory.register(new EmptyMapSerializer(Collections.emptyNavigableMap().getClass(), Collections::emptyNavigableMap));

        factory.register(new SingletonMapSerializer(SINGLETON_MAP_TYPE));

        factory.register(new UnmodifiableMapSerializer(UNMODIFIABLE_MAP_TYPE));
        factory.register(new UnmodifiableSortedMapSerializer(UNMODIFIABLE_SORTED_MAP_TYPE));
        factory.register(new UnmodifiableSortedMapSerializer(ReflectUtil.loadClass("java.util.Collections$UnmodifiableNavigableMap")));

        factory.register(new CheckedMapSerializer(CHECKED_MAP_TYPE));
        factory.register(new CheckedSortedMapSerializer(CHECKED_SORTED_MAP_TYPE));
        factory.register(new CheckedNavigableMapSerializer(CHECKED_NAVIGABLE_MAP_TYPE));

        factory.register(new SynchronizedMapSerializer(SYNCHRONIZED_MAP_TYPE));
        factory.register(new SynchronizedSortedMapSerializer(SYNCHRONIZED_SORTED_MAP_TYPE));
        factory.register(new SynchronizedNavigableMapSerializer(SYNCHRONIZED_NAVIGABLE_MAP_TYPE));

        factory.register(TREE_MAP_SERIALIZER);
        factory.register(new EnumMapSerializer());
    }
}
