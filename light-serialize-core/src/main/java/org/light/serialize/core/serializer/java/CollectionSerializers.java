package org.light.serialize.core.serializer.java;

import org.light.serialize.core.RuntimeEnv;
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
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.function.Supplier;

import static org.light.serialize.core.serializer.java.CollectionSerializers.ArraySubListSerializer.ARRAY_LIST_SUB_LIST_TYPE;
import static org.light.serialize.core.serializer.java.CollectionSerializers.ArraysAsListSerializer.ARRAY_AS_LIST_TYPE;
import static org.light.serialize.core.serializer.java.CollectionSerializers.CheckedCollectionSerializer.CHECKED_COLLECTION_TYPE;
import static org.light.serialize.core.serializer.java.CollectionSerializers.CheckedListSerializer.CHECKED_LIST_TYPE;
import static org.light.serialize.core.serializer.java.CollectionSerializers.CheckedNavigableSetSerializer.CHECKED_NAVIGABLE_SET_TYPE;
import static org.light.serialize.core.serializer.java.CollectionSerializers.CheckedQueueSerializer.CHECKED_QUEUE_TYPE;
import static org.light.serialize.core.serializer.java.CollectionSerializers.CheckedSortedSetSerializer.CHECKED_SORTED_SET_TYPE;
import static org.light.serialize.core.serializer.java.CollectionSerializers.CopiesListSerializer.COPIES_LIST_TYPE;
import static org.light.serialize.core.serializer.java.CollectionSerializers.EnumSetSerializer.ENUM_SET_TYPE;
import static org.light.serialize.core.serializer.java.CollectionSerializers.SetFromMapSerializer.SET_FROM_MAP_TYPE;
import static org.light.serialize.core.serializer.java.CollectionSerializers.SingletonListSerializer.SINGLETON_LIST_TYPE;
import static org.light.serialize.core.serializer.java.CollectionSerializers.SingletonSetSerializer.SINGLETON_SET_TYPE;
import static org.light.serialize.core.serializer.java.CollectionSerializers.SynchronizedCollectionSerializer.SYNCHRONIZED_COLLECTION_TYPE;
import static org.light.serialize.core.serializer.java.CollectionSerializers.SynchronizedListSerializer.SYNCHRONIZED_LIST_TYPE;
import static org.light.serialize.core.serializer.java.CollectionSerializers.SynchronizedNavigableSetSerializer.SYNCHRONIZED_NAVIGABLE_SET_TYPE;
import static org.light.serialize.core.serializer.java.CollectionSerializers.SynchronizedSortedSetSerializer.SYNCHRONIZED_SORTED_SET_TYPE;
import static org.light.serialize.core.serializer.java.CollectionSerializers.UnmodifiableCollectionSerializer.UNMODIFIABLE_COLLECTION_TYPE;
import static org.light.serialize.core.serializer.java.CollectionSerializers.UnmodifiableListSerializer.UNMODIFIABLE_LIST_TYPE;
import static org.light.serialize.core.serializer.java.CollectionSerializers.UnmodifiableSortedSetSerializer.UNMODIFIABLE_SORTED_SET_TYPE;
import static org.light.serialize.core.serializer.java.CollectionSerializers.UtilSubListSerializer.RANDOM_ACCESS_SUB_LIST_TYPE;
import static org.light.serialize.core.serializer.java.CollectionSerializers.UtilSubListSerializer.UTIL_SUB_LIST_TYPE;

/**
 * CollectionSerializers
 *
 * @author alex
 */
public class CollectionSerializers {

    public static  final CollectionSerializer<HashSet> HASH_SET_SERIALIZER = new CollectionSerializer<>(HashSet.class);
    public static  final CollectionSerializer<LinkedHashSet> LINKED_HASH_SET_SERIALIZER = new CollectionSerializer<>(LinkedHashSet.class);
    public static  final ArrayListSerializer ARRAY_LIST_SERIALIZER = new ArrayListSerializer(ArrayList.class);
    public static  final LinkedListSerializer LINKED_LIST_SERIALIZER = new LinkedListSerializer(LinkedList.class);
    public static  final VectorSerializer VECTOR_SERIALIZER = new VectorSerializer(Vector.class);
    public static  final TreeSetSerializer TREE_SET_SERIALIZER = new TreeSetSerializer(TreeSet.class);

    /**
     * The serializer for {@code java.util.Collection}.
     */
    public static class CollectionSerializer<T extends Collection> extends Serializer<T> {

        public CollectionSerializer(Class<?> type) {
            super(type);
        }

        @Override
        public void write(ObjectOutput output, T value) throws IOException {
            int size = value.size();
            output.writeVarInt(size);
            writeHeader(output, value);

            for (Object obj : value) {
                output.writeObject(obj);
            }
        }

        @Override
        public T read(ObjectInput input) throws IOException {
            ReadContext readContext = ReadContext.get();
            int refObjectsSize = readContext.getReferenceObjectsSize();
            int size = input.readVarInt();
            T collection = create(input, size);

            if (refObjectsSize == readContext.getReferenceObjectsSize()) {
                ReadContext.get().putReferenceObject(collection);
            }

            for (int i = 0; i < size; i++) {
                collection.add(input.readObject());
            }

            return collection;
        }

        /**
         * Write collection header.
         */
        protected void writeHeader(ObjectOutput output, T value) throws IOException {

        }

        /**
         * Create collection instance.
         */
        protected T create(ObjectInput input, int size) throws IOException {
            try {

                if (type.equals(ArrayList.class)) {
                    return (T) new ArrayList<>(size);
                }

                if (type.equals(HashSet.class)) {
                    return (T) new HashSet<>(Math.max((int) (size / 0.75f) + 1, 16));
                }

                if (type.equals(LinkedHashSet.class)) {
                    return (T) new LinkedHashSet<>(Math.max((int) (size / 0.75f) + 1, 16));
                }

                if (type.equals(Vector.class)) {
                    return (T) new Vector<>(size);
                }

                return (T) type.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new IOException(e);
            }
        }

    }


    /**
     * The serializer for {@code java.util.Collections$SetFromMap}.
     */
    public static class SetFromMapSerializer extends Serializer {
        static final Class<?> SET_FROM_MAP_TYPE = ReflectUtil.loadClass("java.util.Collections$SetFromMap");
        static final Field SET_FROM_MAP_M_FIELD = ReflectUtil.getAccessibleField(SET_FROM_MAP_TYPE, "m");
        static final Field SET_FROM_MAP_S_FIELD = ReflectUtil.getAccessibleField(SET_FROM_MAP_TYPE, "s");

        private final ObjectInstantiator<Set<?>> instantiator = new UnSafeInstantiator(type);

        public SetFromMapSerializer(Class<?> type) {
            super(type);
        }

        @Override
        public void write(ObjectOutput output, Object value) throws IOException {
            try {
                output.writeObject(SET_FROM_MAP_M_FIELD.get(value));
            } catch (IllegalAccessException e) {
                throw new IOException(e);
            }
        }

        @Override
        public Object read(ObjectInput input) throws IOException {
            try {
                Set<?> set = instantiator.newInstance();
                ReadContext.get().putReferenceObject(set);

                Map<?, Boolean> m = (Map<?, Boolean>) input.readObject();
                SET_FROM_MAP_M_FIELD.set(set, m);
                SET_FROM_MAP_S_FIELD.set(set, m.keySet());
                return set;
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

    }


    /**
     * The serializer for {@code java.util.List}.
     */
    public static class ListSerializer<T extends List> extends CollectionSerializer<T> {

        public ListSerializer(Class<?> type) {
            super(type);
        }

        @Override
        public void write(ObjectOutput output, T value) throws IOException {
            int size = value.size();
            output.writeVarInt(size);
            writeHeader(output, value);

            for (int i = 0; i < value.size(); i++) {
                output.writeObject(value.get(i));
            }
        }
    }


    /**
     * The serializer for {@code java.util.ArrayList}.
     */
    public static class ArrayListSerializer extends ListSerializer<ArrayList> {

        public ArrayListSerializer(Class<?> type) {
            super(type);
        }

        @Override
        protected ArrayList create(ObjectInput input, int size) throws IOException {
            return new ArrayList(size);
        }
    }


    /**
     * The serializer for {@code java.util.LinkedList}.
     */
    public static class LinkedListSerializer extends ListSerializer<LinkedList> {

        public LinkedListSerializer(Class<?> type) {
            super(type);
        }

        @Override
        protected LinkedList create(ObjectInput input, int size) throws IOException {
            return new LinkedList();
        }
    }


    /**
     * The serializer for {@code java.util.Vector}.
     */
    public static class VectorSerializer extends ListSerializer<Vector> {

        public VectorSerializer(Class<?> type) {
            super(type);
        }

        @Override
        protected Vector create(ObjectInput input, int size) throws IOException {
            return new Vector(size);
        }
    }


    /**
     * The serializer for {@code java.util.Arrays$ArrayList}.
     */
    public static class ArraysAsListSerializer extends Serializer {

        static final Class<?> ARRAY_AS_LIST_TYPE = ReflectUtil.loadClass("java.util.Arrays$ArrayList");
        static final Field ARRAY_AS_LIST_A_FIELD = ReflectUtil.getAccessibleField(ARRAY_AS_LIST_TYPE, "a");

        private final ObjectInstantiator<List<?>> instantiator = new UnSafeInstantiator(type);

        public ArraysAsListSerializer(Class<?> type) {
            super(type);
        }

        @Override
        public void write(ObjectOutput output, Object value) throws IOException {
            List<?> list = (List<?>) value;
            int size = list.size();
            output.writeVarInt(size);
            for (int i = 0; i < list.size(); i++) {
                output.writeObject(list.get(i));
            }
        }

        @Override
        public Object read(ObjectInput input) throws IOException {
            try {
                int size = input.readVarInt();
                List<?> list = instantiator.newInstance();
                ReadContext.get().putReferenceObject(list);
                Object[] items = new Object[size];

                for (int i = 0; i < size; i++) {
                    items[i] = input.readObject();
                }
                ARRAY_AS_LIST_A_FIELD.set(list, items);

                return list;
            } catch (IllegalAccessException e) {
                throw new IOException(e);
            }
        }
    }


    /**
     * The serializer for {@code java.util.Collections$CopiesList}.
     */
    public static class CopiesListSerializer extends Serializer {

        static final Class<?> COPIES_LIST_TYPE = ReflectUtil.loadClass("java.util.Collections$CopiesList");
        static final Field COPIES_LIST_N_FIELD = ReflectUtil.getAccessibleField(COPIES_LIST_TYPE, "n");
        static final Field COPIES_LIST_ELEMENT_FIELD = ReflectUtil.getAccessibleField(COPIES_LIST_TYPE, "element");

        private final ObjectInstantiator<List<?>> instantiator = new UnSafeInstantiator(type);

        public CopiesListSerializer(Class<?> type) {
            super(type);
        }

        @Override
        public void write(ObjectOutput output, Object value) throws IOException {
            try {
                output.writeObject(COPIES_LIST_N_FIELD.get(value));
                output.writeObject(COPIES_LIST_ELEMENT_FIELD.get(value));
            } catch (IllegalAccessException e) {
                throw new IOException(e);
            }
        }

        @Override
        public Object read(ObjectInput input) throws IOException {
            try {
                List<?> list = instantiator.newInstance();
                ReadContext.get().putReferenceObject(list);
                COPIES_LIST_N_FIELD.set(list, input.readObject());
                COPIES_LIST_ELEMENT_FIELD.set(list, input.readObject());
                return list;
            } catch (IllegalAccessException e) {
                throw new IOException(e);
            }
        }
    }


    /**
     * Supports {@code java.util.ArrayList$SubList} for java8.
     */
    public static class ArraySubListSerializer extends Serializer {

        static final Class<?> ARRAY_LIST_SUB_LIST_TYPE = ReflectUtil.loadClass("java.util.ArrayList$SubList");
        static final Field SUB_LIST_PARENT_FIELD = ReflectUtil.getAccessibleField(ARRAY_LIST_SUB_LIST_TYPE, "parent");
        static final Field SUB_LIST_PARENT_OFFSET_FIELD = ReflectUtil.getAccessibleField(ARRAY_LIST_SUB_LIST_TYPE, "parentOffset");
        static final Field SUB_LIST_OFFSET_FIELD = ReflectUtil.getAccessibleField(ARRAY_LIST_SUB_LIST_TYPE, "offset");
        static final Field SUB_LIST_SIZE_FIELD = ReflectUtil.getAccessibleField(ARRAY_LIST_SUB_LIST_TYPE, "size");
        static final Field SUB_LIST_THIS0_FIELD = ReflectUtil.getAccessibleField(ARRAY_LIST_SUB_LIST_TYPE, "this$0");

        private final ObjectInstantiator<List<?>> instantiator = new UnSafeInstantiator(type);

        public ArraySubListSerializer(Class<?> type) {
            super(type);
        }

        @Override
        public void write(ObjectOutput output, Object value) throws IOException {
            try {
                Object parent = SUB_LIST_PARENT_FIELD.get(value);
                int parentOffset = SUB_LIST_PARENT_OFFSET_FIELD.getInt(value);
                int offset = SUB_LIST_OFFSET_FIELD.getInt(value);
                int size = SUB_LIST_SIZE_FIELD.getInt(value);

                output.writeObject(parent);
                output.writeVarInt(parentOffset);
                output.writeVarInt(offset);
                output.writeVarInt(size);
            } catch (IllegalAccessException e) {
                throw new IOException(e);
            }
        }

        @Override
        public Object read(ObjectInput input) throws IOException {
            try {
                List<?> list = instantiator.newInstance();
                ReadContext.get().putReferenceObject(list);
                Object parent = input.readObject();
                int parentOffset = input.readVarInt();
                int offset = input.readVarInt();
                int size = input.readVarInt();

                // inner class "this$0" field set value.
                SUB_LIST_THIS0_FIELD.set(list, parent);
                SUB_LIST_PARENT_FIELD.set(list, parent);

                SUB_LIST_PARENT_OFFSET_FIELD.setInt(list, parentOffset);
                SUB_LIST_OFFSET_FIELD.setInt(list, offset);
                SUB_LIST_SIZE_FIELD.setInt(list, size);
                return list;
            } catch (IllegalAccessException e) {
                throw new IOException(e);
            }
        }
    }


    /**
     * The serializer for {@code java.util.SubList},{@code java.util.RandomAccessSubList}, Supports java8.
     */
    public static class UtilSubListSerializer extends Serializer<List<?>> {
        static final Class<?> UTIL_SUB_LIST_TYPE = ReflectUtil.loadClass("java.util.SubList");
        static final Class<?> RANDOM_ACCESS_SUB_LIST_TYPE = ReflectUtil.loadClass("java.util.RandomAccessSubList");
        static final Field L_Field = ReflectUtil.getAccessibleField(UTIL_SUB_LIST_TYPE, "l");
        static final Field OFFSET_FIELD = ReflectUtil.getAccessibleField(UTIL_SUB_LIST_TYPE, "offset");
        static final Field SIZE_FIELD = ReflectUtil.getAccessibleField(UTIL_SUB_LIST_TYPE, "size");

        private final ObjectInstantiator<List<?>> instantiator = new UnSafeInstantiator(type);

        public UtilSubListSerializer(Class<?> type) {
            super(type);
        }

        @Override
        public void write(ObjectOutput output, List<?> value) throws IOException {
            try {
                Object l = L_Field.get(value);
                int offset = OFFSET_FIELD.getInt(value);
                int size = SIZE_FIELD.getInt(value);

                output.writeObject(l);
                output.writeVarInt(offset);
                output.writeVarInt(size);
            } catch (IllegalAccessException e) {
                throw new IOException(e);
            }
        }

        @Override
        public List<?> read(ObjectInput input) throws IOException {
            try {
                List<?> list = instantiator.newInstance();
                ReadContext.get().putReferenceObject(list);
                List<?> parent = (List<?>) input.readObject();
                int offset = input.readVarInt();
                int size = input.readVarInt();

                L_Field.set(list, parent);
                OFFSET_FIELD.setInt(list, offset);
                SIZE_FIELD.setInt(list, size);

                return list;
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

    }


    /**
     * The serializer for {@code java.util.Collections$EmptySet}.
     */
    public static class EmptyCollectionSerializer<T extends Collection> extends Serializer<T> {

        private final Supplier<T> supplier;

        public EmptyCollectionSerializer(Class<T> type, Supplier<T> supplier) {
            super(type);
            this.supplier = supplier;
        }

        @Override
        public T read(ObjectInput input) throws IOException {
            return supplier.get();
        }

        @Override
        public void write(ObjectOutput output, T value) throws IOException {
            // do nothing
        }
    }


    /**
     * The serializer for {@code java.util.Collections$SingletonList}.
     */
    static class SingletonListSerializer extends Serializer<List<?>> {

        static final Class<?> SINGLETON_LIST_TYPE = ReflectUtil.loadClass("java.util.Collections$SingletonList");
        static final Field SINGLETON_LIST_ELEMENT_FIELD = ReflectUtil.getAccessibleField(SINGLETON_LIST_TYPE, "element");

        private final ObjectInstantiator<List<?>> instantiator = new UnSafeInstantiator(type);

        public SingletonListSerializer(Class<?> type) {
            super(type);
        }

        @Override
        public void write(ObjectOutput output, List<?> list) throws IOException {
            output.writeObject(list.get(0));
        }

        @Override
        public List<?> read(ObjectInput input) throws IOException {
            try {
                List<?> list = instantiator.newInstance();
                ReadContext.get().putReferenceObject(list);
                SINGLETON_LIST_ELEMENT_FIELD.set(list, input.readObject());
                return list;
            } catch (IllegalAccessException e) {
                throw new IOException(e);
            }
        }
    }


    /**
     * The serializer for {@code java.util.Collections$SingletonSet}.
     */
    static class SingletonSetSerializer extends Serializer<Set<?>> {

        static final Class<?> SINGLETON_SET_TYPE = ReflectUtil.loadClass("java.util.Collections$SingletonSet");
        static final Field SINGLETON_SET_ELEMENT_FIELD = ReflectUtil.getAccessibleField(SINGLETON_SET_TYPE, "element");

        private final ObjectInstantiator<Set<?>> instantiator = new UnSafeInstantiator(type);

        public SingletonSetSerializer(Class<?> type) {
            super(type);
        }

        @Override
        public void write(ObjectOutput output, Set<?> set) throws IOException {
            output.writeObject(set.iterator().next());
        }

        @Override
        public Set<?> read(ObjectInput input) throws IOException {
            try {
                Set<?> set = instantiator.newInstance();
                ReadContext.get().putReferenceObject(set);
                SINGLETON_SET_ELEMENT_FIELD.set(set, input.readObject());
                return set;
            } catch (IllegalAccessException e) {
                throw new IOException(e);
            }
        }
    }


    /**
     * The serializer for {@code java.util.Collections$UnmodifiableCollection}.
     */
    public static class UnmodifiableCollectionSerializer<T extends Collection> extends Serializer<T> {

        static final Class<?> UNMODIFIABLE_COLLECTION_TYPE = ReflectUtil.loadClass("java.util.Collections$UnmodifiableCollection");
        static final Field UNMODIFIABLE_COLLECTION_C_FIELD = ReflectUtil.getAccessibleField(UNMODIFIABLE_COLLECTION_TYPE, "c");

        private final ObjectInstantiator<T> instantiator = new UnSafeInstantiator(type);

        public UnmodifiableCollectionSerializer(Class<?> type) {
            super(type);
        }

        @Override
        public void write(ObjectOutput output, T value) throws IOException {
            try {
                Object object = UNMODIFIABLE_COLLECTION_C_FIELD.get(value);
                output.writeObject(object);
            } catch (IllegalAccessException e) {
                throw new IOException(e);
            }
        }

        @Override
        public T read(ObjectInput input) throws IOException {
            try {
                T collection = instantiator.newInstance();
                ReadContext.get().putReferenceObject(collection);
                Object c = input.readObject();

                UNMODIFIABLE_COLLECTION_C_FIELD.set(collection, c);
                return collection;
            } catch (IllegalAccessException e) {
                throw new IOException(e);
            }
        }

    }


    /**
     * The serializer for {@code java.util.Collections$UnmodifiableList}.
     */
    public static class UnmodifiableListSerializer<T extends List> extends Serializer<T> {

        static final Class<?> UNMODIFIABLE_LIST_TYPE = ReflectUtil.loadClass("java.util.Collections$UnmodifiableList");
        static final Field UNMODIFIABLE_LIST_C_FIELD = ReflectUtil.getAccessibleField(UNMODIFIABLE_LIST_TYPE, "c");
        static final Field UNMODIFIABLE_LIST_FIELD = ReflectUtil.getAccessibleField(UNMODIFIABLE_LIST_TYPE, "list");

        private final ObjectInstantiator<T> instantiator = new UnSafeInstantiator(type);

        public UnmodifiableListSerializer(Class<?> type) {
            super(type);
        }

        @Override
        public void write(ObjectOutput output, T value) throws IOException {
            try {
                Object object = UNMODIFIABLE_LIST_C_FIELD.get(value);
                output.writeObject(object);
            } catch (IllegalAccessException e) {
                throw new IOException(e);
            }
        }

        @Override
        public T read(ObjectInput input) throws IOException {
            try {
                T list = instantiator.newInstance();
                ReadContext.get().putReferenceObject(list);
                Object c = input.readObject();

                UNMODIFIABLE_LIST_C_FIELD.set(list, c);
                UNMODIFIABLE_LIST_FIELD.set(list, c);
                return list;
            } catch (IllegalAccessException e) {
                throw new IOException(e);
            }
        }

    }


    /**
     * The serializer for {@code java.util.Collections$UnmodifiableSortedSet}.
     */
    public static class UnmodifiableSortedSetSerializer<T extends Set> extends Serializer<T> {

        static final Class<?> UNMODIFIABLE_SORTED_SET_TYPE = ReflectUtil.loadClass("java.util.Collections$UnmodifiableSortedSet");
        static final Field UNMODIFIABLE_SORTED_SET_C_FIELD = ReflectUtil.getAccessibleField(UNMODIFIABLE_SORTED_SET_TYPE, "c");
        static final Field UNMODIFIABLE_SORTED_SET_SS_FIELD = ReflectUtil.getAccessibleField(UNMODIFIABLE_SORTED_SET_TYPE, "ss");

        private final ObjectInstantiator<T> instantiator = new UnSafeInstantiator(type);

        public UnmodifiableSortedSetSerializer(Class<?> type) {
            super(type);
        }

        @Override
        public void write(ObjectOutput output, T value) throws IOException {
            try {
                output.writeObject(UNMODIFIABLE_SORTED_SET_C_FIELD.get(value));
            } catch (IllegalAccessException e) {
                throw new IOException(e);
            }
        }

        @Override
        public T read(ObjectInput input) throws IOException {
            try {
                T set = instantiator.newInstance();
                ReadContext.get().putReferenceObject(set);
                Object c = input.readObject();

                UNMODIFIABLE_SORTED_SET_C_FIELD.set(set, c);
                UNMODIFIABLE_SORTED_SET_SS_FIELD.set(set, c);
                return set;
            } catch (IllegalAccessException e) {
                throw new IOException(e);
            }
        }

    }


    /**
     * The serializer for {@code java.util.Collections$SynchronizedCollection}.
     */
    public static class SynchronizedCollectionSerializer<T extends Collection> extends Serializer<T> {

        static final Class<?> SYNCHRONIZED_COLLECTION_TYPE = ReflectUtil.loadClass("java.util.Collections$SynchronizedCollection");
        static final Field SYNCHRONIZED_COLLECTION_C_FIELD = ReflectUtil.getAccessibleField(SYNCHRONIZED_COLLECTION_TYPE, "c");
        static final Field SYNCHRONIZED_COLLECTION_MUTEX_FIELD = ReflectUtil.getAccessibleField(SYNCHRONIZED_COLLECTION_TYPE, "mutex");

        private final ObjectInstantiator<T> instantiator = new UnSafeInstantiator(type);

        public SynchronizedCollectionSerializer(Class<? extends T> type) {
            super(type);
        }

        @Override
        public void write(ObjectOutput output, T value) throws IOException {
            try {
                output.writeObject(SYNCHRONIZED_COLLECTION_C_FIELD.get(value));
            } catch (IllegalAccessException e) {
                throw new IOException(e);
            }
        }

        @Override
        public T read(ObjectInput input) throws IOException {
            try {
                T collection = instantiator.newInstance();
                ReadContext.get().putReferenceObject(collection);
                SYNCHRONIZED_COLLECTION_C_FIELD.set(collection, input.readObject());
                SYNCHRONIZED_COLLECTION_MUTEX_FIELD.set(collection, collection);
                return collection;
            } catch (IllegalAccessException e) {
                throw new IOException(e);
            }
        }

    }


    /**
     * The serializer for {@code java.util.Collections$SynchronizedList}.
     */
    public static class SynchronizedListSerializer<T extends List> extends Serializer<T> {

        static final Class<?> SYNCHRONIZED_LIST_TYPE = ReflectUtil.loadClass("java.util.Collections$SynchronizedList");
        static final Field SYNCHRONIZED_LIST_C_FIELD = ReflectUtil.getAccessibleField(SYNCHRONIZED_LIST_TYPE, "c");
        static final Field SYNCHRONIZED_LIST_LIST_FIELD = ReflectUtil.getAccessibleField(SYNCHRONIZED_LIST_TYPE, "list");
        static final Field SYNCHRONIZED_LIST_MUTEX_FIELD = ReflectUtil.getAccessibleField(SYNCHRONIZED_LIST_TYPE, "mutex");

        private final ObjectInstantiator<T> instantiator = new UnSafeInstantiator(type);

        public SynchronizedListSerializer(Class<? extends T> type) {
            super(type);
        }

        @Override
        public void write(ObjectOutput output, T value) throws IOException {
            try {
                output.writeObject(SYNCHRONIZED_LIST_C_FIELD.get(value));
            } catch (IllegalAccessException e) {
                throw new IOException(e);
            }
        }

        @Override
        public T read(ObjectInput input) throws IOException {
            try {
                T list = instantiator.newInstance();
                ReadContext.get().putReferenceObject(list);
                Object c = input.readObject();
                SYNCHRONIZED_LIST_C_FIELD.set(list, c);
                SYNCHRONIZED_LIST_LIST_FIELD.set(list, c);
                SYNCHRONIZED_LIST_MUTEX_FIELD.set(list, list);
                return list;
            } catch (IllegalAccessException e) {
                throw new IOException(e);
            }
        }
    }


    /**
     * The serializer for {@code java.util.Collections$SynchronizedSortedSet}.
     */
    public static class SynchronizedSortedSetSerializer<T extends Set> extends Serializer<T> {

        static final Class<?> SYNCHRONIZED_SORTED_SET_TYPE = ReflectUtil.loadClass("java.util.Collections$SynchronizedSortedSet");
        static final Field SYNCHRONIZED_SORTED_SET_C_FIELD = ReflectUtil.getAccessibleField(SYNCHRONIZED_SORTED_SET_TYPE, "c");
        static final Field SYNCHRONIZED_SORTED_SET_SS_FIELD = ReflectUtil.getAccessibleField(SYNCHRONIZED_SORTED_SET_TYPE, "ss");
        static final Field SYNCHRONIZED_SORTED_SET_MUTEX_FIELD = ReflectUtil.getAccessibleField(SYNCHRONIZED_SORTED_SET_TYPE, "mutex");

        private final ObjectInstantiator<T> instantiator = new UnSafeInstantiator(type);

        public SynchronizedSortedSetSerializer(Class<? extends T> type) {
            super(type);
        }

        @Override
        public void write(ObjectOutput output, T value) throws IOException {
            try {
                output.writeObject(SYNCHRONIZED_SORTED_SET_C_FIELD.get(value));
            } catch (IllegalAccessException e) {
                throw new IOException(e);
            }
        }

        @Override
        public T read(ObjectInput input) throws IOException {
            try {
                T set = instantiator.newInstance();
                ReadContext.get().putReferenceObject(set);
                Object c = input.readObject();
                SYNCHRONIZED_SORTED_SET_C_FIELD.set(set, c);
                SYNCHRONIZED_SORTED_SET_SS_FIELD.set(set, c);
                SYNCHRONIZED_SORTED_SET_MUTEX_FIELD.set(set, set);
                return set;
            } catch (IllegalAccessException e) {
                throw new IOException(e);
            }
        }
    }


    /**
     * The serializer for {@code java.util.Collections$SynchronizedNavigableSet}.
     */
    public static class SynchronizedNavigableSetSerializer<T extends Set> extends Serializer<T> {

        static final Class<?> SYNCHRONIZED_NAVIGABLE_SET_TYPE = ReflectUtil.loadClass("java.util.Collections$SynchronizedNavigableSet");
        static final Field SYNCHRONIZED_NAVIGABLE_SET_C_FIELD = ReflectUtil.getAccessibleField(SYNCHRONIZED_NAVIGABLE_SET_TYPE, "c");
        static final Field SYNCHRONIZED_NAVIGABLE_SET_SS_FIELD = ReflectUtil.getAccessibleField(SYNCHRONIZED_NAVIGABLE_SET_TYPE, "ss");
        static final Field SYNCHRONIZED_NAVIGABLE_SET_NS_FIELD = ReflectUtil.getAccessibleField(SYNCHRONIZED_NAVIGABLE_SET_TYPE, "ns");
        static final Field SYNCHRONIZED_NAVIGABLE_SET_MUTEX_FIELD = ReflectUtil.getAccessibleField(SYNCHRONIZED_NAVIGABLE_SET_TYPE, "mutex");

        private final ObjectInstantiator<T> instantiator = new UnSafeInstantiator(type);

        public SynchronizedNavigableSetSerializer(Class<? extends T> type) {
            super(type);
        }

        @Override
        public void write(ObjectOutput output, T value) throws IOException {
            try {
                output.writeObject(SYNCHRONIZED_NAVIGABLE_SET_C_FIELD.get(value));
            } catch (IllegalAccessException e) {
                throw new IOException(e);
            }
        }

        @Override
        public T read(ObjectInput input) throws IOException {
            try {
                T set = instantiator.newInstance();
                ReadContext.get().putReferenceObject(set);
                Object c = input.readObject();
                SYNCHRONIZED_NAVIGABLE_SET_C_FIELD.set(set, c);
                SYNCHRONIZED_NAVIGABLE_SET_SS_FIELD.set(set, c);
                SYNCHRONIZED_NAVIGABLE_SET_NS_FIELD.set(set, c);
                SYNCHRONIZED_NAVIGABLE_SET_MUTEX_FIELD.set(set, set);
                return set;
            } catch (IllegalAccessException e) {
                throw new IOException(e);
            }
        }
    }


    /**
     * The serializer for {@code java.util.Collections$CheckedCollection}.
     */
    public static class CheckedCollectionSerializer<T extends Collection> extends Serializer<T> {

        static final Class<?> CHECKED_COLLECTION_TYPE = ReflectUtil.loadClass("java.util.Collections$CheckedCollection");
        static final Field CHECKED_COLLECTION_C_FIELD = ReflectUtil.getAccessibleField(CHECKED_COLLECTION_TYPE, "c");
        static final Field CHECKED_COLLECTION_TYPE_FIELD = ReflectUtil.getAccessibleField(CHECKED_COLLECTION_TYPE, "type");

        private final ObjectInstantiator<T> instantiator = new UnSafeInstantiator(type);

        public CheckedCollectionSerializer(Class<? extends T> type) {
            super(type);
        }

        @Override
        public void write(ObjectOutput output, T value) throws IOException {
            try {
                output.writeObject(CHECKED_COLLECTION_C_FIELD.get(value));
                output.writeObject(CHECKED_COLLECTION_TYPE_FIELD.get(value));
            } catch (IllegalAccessException e) {
                throw new IOException(e);
            }
        }

        @Override
        public T read(ObjectInput input) throws IOException {
            try {
                T collection = instantiator.newInstance();
                ReadContext.get().putReferenceObject(collection);
                CHECKED_COLLECTION_C_FIELD.set(collection, input.readObject());
                CHECKED_COLLECTION_TYPE_FIELD.set(collection, input.readObject());
                return collection;
            } catch (IllegalAccessException e) {
                throw new IOException(e);
            }
        }

    }


    /**
     * The serializer for {@code java.util.Collections$CheckedCollection}.
     */
    public static class CheckedListSerializer<T extends List> extends Serializer<T> {

        static final Class<?> CHECKED_LIST_TYPE = ReflectUtil.loadClass("java.util.Collections$CheckedList");
        static final Field CHECKED_LIST_C_FIELD = ReflectUtil.getAccessibleField(CHECKED_LIST_TYPE, "c");
        static final Field CHECKED_LIST_LIST_FIELD = ReflectUtil.getAccessibleField(CHECKED_LIST_TYPE, "list");
        static final Field CHECKED_LIST_TYPE_FIELD = ReflectUtil.getAccessibleField(CHECKED_LIST_TYPE, "type");

        private final ObjectInstantiator<T> instantiator = new UnSafeInstantiator(type);

        public CheckedListSerializer(Class<? extends T> type) {
            super(type);
        }

        @Override
        public void write(ObjectOutput output, T value) throws IOException {
            try {
                output.writeObject(CHECKED_LIST_C_FIELD.get(value));
                output.writeObject(CHECKED_LIST_TYPE_FIELD.get(value));
            } catch (IllegalAccessException e) {
                throw new IOException(e);
            }
        }

        @Override
        public T read(ObjectInput input) throws IOException {
            try {
                T list = instantiator.newInstance();
                ReadContext.get().putReferenceObject(list);

                Object c = input.readObject();
                Object type = input.readObject();
                CHECKED_LIST_C_FIELD.set(list, c);
                CHECKED_LIST_LIST_FIELD.set(list, c);
                CHECKED_LIST_TYPE_FIELD.set(list, type);
                return list;
            } catch (IllegalAccessException e) {
                throw new IOException(e);
            }
        }

    }


    /**
     * The serializer for {@code java.util.Collections$CheckedSortedSet}.
     */
    public static class CheckedSortedSetSerializer<T extends Set> extends Serializer<T> {

        static final Class<?> CHECKED_SORTED_SET_TYPE = ReflectUtil.loadClass("java.util.Collections$CheckedSortedSet");
        static final Field CHECKED_SORTED_SET_C_FIELD = ReflectUtil.getAccessibleField(CHECKED_SORTED_SET_TYPE, "c");
        static final Field CHECKED_SORTED_SET_SS_FIELD = ReflectUtil.getAccessibleField(CHECKED_SORTED_SET_TYPE, "ss");
        static final Field CHECKED_SORTED_SET_TYPE_FIELD = ReflectUtil.getAccessibleField(CHECKED_SORTED_SET_TYPE, "type");

        private final ObjectInstantiator<T> instantiator = new UnSafeInstantiator(type);

        public CheckedSortedSetSerializer(Class<? extends T> type) {
            super(type);
        }

        @Override
        public void write(ObjectOutput output, T value) throws IOException {
            try {
                output.writeObject(CHECKED_SORTED_SET_C_FIELD.get(value));
                output.writeObject(CHECKED_SORTED_SET_TYPE_FIELD.get(value));
            } catch (IllegalAccessException e) {
                throw new IOException(e);
            }
        }

        @Override
        public T read(ObjectInput input) throws IOException {
            try {
                T set = instantiator.newInstance();
                ReadContext.get().putReferenceObject(set);

                Object c = input.readObject();
                Object type = input.readObject();
                CHECKED_SORTED_SET_C_FIELD.set(set, c);
                CHECKED_SORTED_SET_SS_FIELD.set(set, c);
                CHECKED_SORTED_SET_TYPE_FIELD.set(set, type);
                return set;
            } catch (IllegalAccessException e) {
                throw new IOException(e);
            }
        }

    }


    /**
     * The serializer for {@code java.util.Collections$CheckedNavigableSet}.
     */
    public static class CheckedNavigableSetSerializer<T extends Set> extends Serializer<T> {

        static final Class<?> CHECKED_NAVIGABLE_SET_TYPE = ReflectUtil.loadClass("java.util.Collections$CheckedNavigableSet");
        static final Field CHECKED_NAVIGABLE_SET_C_FIELD = ReflectUtil.getAccessibleField(CHECKED_NAVIGABLE_SET_TYPE, "c");
        static final Field CHECKED_NAVIGABLE_SET_SS_FIELD = ReflectUtil.getAccessibleField(CHECKED_NAVIGABLE_SET_TYPE, "ss");
        static final Field CHECKED_NAVIGABLE_SET_NS_FIELD = ReflectUtil.getAccessibleField(CHECKED_NAVIGABLE_SET_TYPE, "ns");
        static final Field CHECKED_NAVIGABLE_SET_TYPE_FIELD = ReflectUtil.getAccessibleField(CHECKED_NAVIGABLE_SET_TYPE, "type");

        private final ObjectInstantiator<T> instantiator = new UnSafeInstantiator(type);

        public CheckedNavigableSetSerializer(Class<? extends T> type) {
            super(type);
        }

        @Override
        public void write(ObjectOutput output, T value) throws IOException {
            try {
                output.writeObject(CHECKED_NAVIGABLE_SET_C_FIELD.get(value));
                output.writeObject(CHECKED_NAVIGABLE_SET_TYPE_FIELD.get(value));
            } catch (IllegalAccessException e) {
                throw new IOException(e);
            }
        }

        @Override
        public T read(ObjectInput input) throws IOException {
            try {
                T set = instantiator.newInstance();
                ReadContext.get().putReferenceObject(set);

                Object c = input.readObject();
                Object type = input.readObject();
                CHECKED_NAVIGABLE_SET_C_FIELD.set(set, c);
                CHECKED_NAVIGABLE_SET_SS_FIELD.set(set, c);
                CHECKED_NAVIGABLE_SET_NS_FIELD.set(set, c);
                CHECKED_NAVIGABLE_SET_TYPE_FIELD.set(set, type);
                return set;
            } catch (IllegalAccessException e) {
                throw new IOException(e);
            }
        }

    }


    /**
     * The serializer for {@code java.util.Collections$CheckedQueue}.
     */
    public static class CheckedQueueSerializer<T extends Queue> extends Serializer<T> {

        static final Class<?> CHECKED_QUEUE_TYPE = ReflectUtil.loadClass("java.util.Collections$CheckedQueue");
        static final Field CHECKED_QUEUE_C_FIELD = ReflectUtil.getAccessibleField(CHECKED_QUEUE_TYPE, "c");
        static final Field CHECKED_QUEUE_QUEUE_FIELD = ReflectUtil.getAccessibleField(CHECKED_QUEUE_TYPE, "queue");
        static final Field CHECKED_LIST_TYPE_FIELD = ReflectUtil.getAccessibleField(CHECKED_QUEUE_TYPE, "type");

        private final ObjectInstantiator<T> instantiator = new UnSafeInstantiator(type);

        public CheckedQueueSerializer(Class<? extends T> type) {
            super(type);
        }

        @Override
        public void write(ObjectOutput output, T value) throws IOException {
            try {
                output.writeObject(CHECKED_QUEUE_C_FIELD.get(value));
                output.writeObject(CHECKED_LIST_TYPE_FIELD.get(value));
            } catch (IllegalAccessException e) {
                throw new IOException(e);
            }
        }

        @Override
        public T read(ObjectInput input) throws IOException {
            try {
                T queue = instantiator.newInstance();
                ReadContext.get().putReferenceObject(queue);

                Object c = input.readObject();
                Object type = input.readObject();
                CHECKED_QUEUE_C_FIELD.set(queue, c);
                CHECKED_QUEUE_QUEUE_FIELD.set(queue, c);
                CHECKED_LIST_TYPE_FIELD.set(queue, type);
                return queue;
            } catch (IllegalAccessException e) {
                throw new IOException(e);
            }
        }

    }


    /**
     * The serializer for {@code java.util.JumboEnumSet}, {@code java.util.RegularEnumSet}.
     */
    public static class EnumSetSerializer extends Serializer<EnumSet<? extends Enum<?>>> {
        static final Class<EnumSet> ENUM_SET_TYPE = EnumSet.class;
        static final Field ENUM_SET_TYPE_FIELD = ReflectUtil.getAccessibleField(ENUM_SET_TYPE, "elementType");

        public EnumSetSerializer(Class<?> type) {
            super(type);
        }

        @Override
        public void write(ObjectOutput output, EnumSet<? extends Enum<?>> value) throws IOException {
            try {
                output.writeObject(ENUM_SET_TYPE_FIELD.get(value));
                output.writeVarInt(value.size());
                for (Enum<? extends Enum<?>> item : value) {
                    output.writeObject(item);
                }
            } catch (IllegalAccessException e) {
                throw new IOException(e);
            }
        }

        @Override
        public EnumSet<? extends Enum<?>> read(ObjectInput input) throws IOException {
            Class<? extends Enum> elementType = (Class<? extends Enum>) input.readObject();
            int size = input.readVarInt();
            EnumSet set = EnumSet.noneOf(elementType);
            ReadContext.get().putReferenceObject(set);

            for (int i = 0; i < size; i++) {
                set.add(input.readObject());
            }

            return set;
        }

    }

    /**
     * The serializer for {@code java.util.PriorityQueue}.
     */
    public static class PriorityQueueSerializer extends CollectionSerializer<PriorityQueue> {

        static final Field PRIORITY_QUEUE_COMPARATOR_FIELD = ReflectUtil.getAccessibleField(PriorityQueue.class, "comparator");

        public PriorityQueueSerializer(Class<?> type) {
            super(type);
        }

        @Override
        protected void writeHeader(ObjectOutput output, PriorityQueue queue) throws IOException {
            super.writeHeader(output, queue);
            output.writeObject(queue.comparator());
        }

        @Override
        protected PriorityQueue create(ObjectInput input, int size) throws IOException {
            try {
                PriorityQueue queue = size == 0 ? new PriorityQueue() : new PriorityQueue(size);
                ReadContext.get().putReferenceObject(queue);
                PRIORITY_QUEUE_COMPARATOR_FIELD.set(queue, input.readObject());
                return queue;
            } catch (IllegalAccessException e) {
                throw new IOException(e);
            }
        }
    }


    /**
     * The serializer for {@code java.util.BitSet}.
     */
    public static class BitSetSerializer extends Serializer<BitSet> {

        public BitSetSerializer(Class<?> type) {
            super(type);
        }

        @Override
        public void write(ObjectOutput output, BitSet value) throws IOException {
            long[] items = value.toLongArray();
            int length = items.length;

            output.writeVarInt(length);
            for (int i = 0; i < length; i++) {
                output.writeLong(items[i]);
            }
        }

        @Override
        public BitSet read(ObjectInput input) throws IOException {
            int length = input.readVarInt();
            long[] items = new long[length];

            for (int i = 0; i < length; i++) {
                items[i] = input.readLong();
            }
            return BitSet.valueOf(items);
        }

    }


    /**
     * The serializer for {@code java.util.TreeSet}.
     */
    public static class TreeSetSerializer extends CollectionSerializer<TreeSet> {

        static final Field TREE_SET_M_FIELD = ReflectUtil.getAccessibleField(TreeSet.class, "m");
        static final Field TREE_MAP_COMPARATOR_FIELD = ReflectUtil.getAccessibleField(TreeMap.class, "comparator");

        public TreeSetSerializer(Class<?> type) {
            super(type);
        }

        @Override
        protected void writeHeader(ObjectOutput output, TreeSet value) throws IOException {
            output.writeObject(value.comparator());
        }

        @Override
        protected TreeSet create(ObjectInput input, int size) throws IOException {
            try {
                TreeSet treeSet = new TreeSet();
                ReadContext.get().putReferenceObject(treeSet);
                Object treeMap = TREE_SET_M_FIELD.get(treeSet);
                Object comparator = input.readObject();

                TREE_MAP_COMPARATOR_FIELD.set(treeMap, comparator);
                return treeSet;
            } catch (IllegalAccessException e) {
                throw new IOException(e);
            }
        }

    }


    public static void register(SerializerFactory factory) {
        factory.register(new CollectionSerializer<>(Collection.class));
        factory.register(new CollectionSerializer<>(ConcurrentSkipListSet.class));
        factory.register(HASH_SET_SERIALIZER);
        factory.register(LINKED_HASH_SET_SERIALIZER);
        factory.register(new SetFromMapSerializer(SET_FROM_MAP_TYPE));
        factory.register(new ListSerializer(List.class));
        factory.register(ARRAY_LIST_SERIALIZER);
        factory.register(LINKED_LIST_SERIALIZER);
        factory.register(VECTOR_SERIALIZER);
        factory.register(new ArraysAsListSerializer(ARRAY_AS_LIST_TYPE));
        factory.register(new CopiesListSerializer(COPIES_LIST_TYPE));
        // Supports java8.
        if (!RuntimeEnv.isJava9Plus()) {
            factory.register(new ArraySubListSerializer(ARRAY_LIST_SUB_LIST_TYPE));
            factory.register(new UtilSubListSerializer(UTIL_SUB_LIST_TYPE));
            factory.register(new UtilSubListSerializer(RANDOM_ACCESS_SUB_LIST_TYPE));
        }

        factory.register(new EmptyCollectionSerializer(Collections.emptySet().getClass(),
                Collections::emptySet));
        // Collections#emptySortedSet and Collections#emptyNavigableSet returns the same instance.
        factory.register(new EmptyCollectionSerializer(Collections.emptyNavigableSet().getClass(),
                Collections::emptyNavigableSet));
        factory.register(new EmptyCollectionSerializer(Collections.emptyList().getClass(),
                Collections::emptyList));

        factory.register(new SingletonListSerializer(SINGLETON_LIST_TYPE));
        factory.register(new SingletonSetSerializer(SINGLETON_SET_TYPE));

        factory.register(new UnmodifiableCollectionSerializer(UNMODIFIABLE_COLLECTION_TYPE));
        factory.register(new UnmodifiableListSerializer(UNMODIFIABLE_LIST_TYPE));
        factory.register(new UnmodifiableListSerializer(ReflectUtil.loadClass("java.util.Collections$UnmodifiableRandomAccessList")));
        factory.register(new UnmodifiableCollectionSerializer(ReflectUtil.loadClass("java.util.Collections$UnmodifiableSet")));
        factory.register(new UnmodifiableSortedSetSerializer(UNMODIFIABLE_SORTED_SET_TYPE));
        factory.register(new UnmodifiableSortedSetSerializer(ReflectUtil.loadClass("java.util.Collections$UnmodifiableNavigableSet")));

        factory.register(new SynchronizedCollectionSerializer(SYNCHRONIZED_COLLECTION_TYPE));
        factory.register(new SynchronizedListSerializer(SYNCHRONIZED_LIST_TYPE));
        factory.register(new SynchronizedListSerializer(ReflectUtil.loadClass("java.util.Collections$SynchronizedRandomAccessList")));
        factory.register(new SynchronizedCollectionSerializer(ReflectUtil.loadClass("java.util.Collections$SynchronizedSet")));
        factory.register(new SynchronizedSortedSetSerializer(SYNCHRONIZED_SORTED_SET_TYPE));
        factory.register(new SynchronizedNavigableSetSerializer(SYNCHRONIZED_NAVIGABLE_SET_TYPE));

        factory.register(new CheckedCollectionSerializer(CHECKED_COLLECTION_TYPE));
        factory.register(new CheckedListSerializer(CHECKED_LIST_TYPE));
        factory.register(new CheckedCollectionSerializer(ReflectUtil.loadClass("java.util.Collections$CheckedRandomAccessList")));
        factory.register(new CheckedCollectionSerializer(ReflectUtil.loadClass("java.util.Collections$CheckedSet")));
        factory.register(new CheckedSortedSetSerializer(CHECKED_SORTED_SET_TYPE));
        factory.register(new CheckedNavigableSetSerializer(CHECKED_NAVIGABLE_SET_TYPE));
        factory.register(new CheckedQueueSerializer(CHECKED_QUEUE_TYPE));

        factory.register(new EnumSetSerializer(ENUM_SET_TYPE));
        factory.register(new EnumSetSerializer(ReflectUtil.loadClass("java.util.RegularEnumSet")));
        factory.register(new EnumSetSerializer(ReflectUtil.loadClass("java.util.JumboEnumSet")));

        factory.register(new PriorityQueueSerializer(PriorityQueue.class));
        factory.register(new BitSetSerializer(BitSet.class));
        factory.register(TREE_SET_SERIALIZER);
    }
}
