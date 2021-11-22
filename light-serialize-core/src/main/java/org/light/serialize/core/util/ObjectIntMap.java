package org.light.serialize.core.util;

import java.util.Arrays;

/**
 * An unordered map where identity comparison is used for the objects keys and the values are unboxed ints.
 * Null keys are not allowed. No allocation is done except when growing the table size.
 * <p>
 * This class performs fast contains and remove (typically O(1), worst case O(n) but that is rare in practice). Add may be
 * slightly slower, depending on hash collisions. Hashcode are rehashed to reduce collisions and the need to resize. Load factors
 * greater than 0.91 greatly increase the chances to resize to the next higher POT size.
 * <p>
 * <p>
 * Unordered sets and maps are not designed to provide especially fast iteration.
 * <p>
 * This implementation uses linear probing with the backward shift algorithm for removal. Hashcode are rehashed using Fibonacci
 * hashing, instead of the more common power-of-two mask, to better distribute poor hashCodes
 *
 * @see {@code com.esotericsoftware.kryo.util.IdentityObjectIntMap}
 * TODO: clear内存泄露检查，对比ArrayList or HashMap
 */
public class ObjectIntMap<K> {

    private static final int MAX_CAPACITY = 1 << 30;

    /**
     * The number of key-value mappings contained in this identity hash map.
     */
    private int  size;

    /**
     * The key table.
     */
    K[] keyTable;

    /**
     * The value table.
     */
    int[] valueTable;

    /**
     * The load factor for the hash table.
     */
    float loadFactor;

    /**
     * The next size value at which to resize.
     * threshold = (capacity * load factor).
     */
    int threshold;

    /**
     * A bitmask used to confine hashcode to the size of the table. Must be all 1 bits in its low positions,
     * ie a power of two minus 1.
     * mask = tableSize - 1.
     */
    protected int mask;

    /**
     * Used by {@link #place(Object)} to bit shift the upper bits of a {@code int} into a usable range (&gt;= 0 and &lt;=
     * {@link #mask}).
     * shift = Integer.numberOfLeadingZeros(mask);
     */
    protected int shift;

    public ObjectIntMap() {
        this(51, 0.8f);
    }

    public ObjectIntMap(int initCapacity) {
        this(initCapacity, 0.8f);
    }

    public ObjectIntMap(int initCapacity, float loadFactor) {
        if (initCapacity < 0) {
            throw new IllegalArgumentException("capacity must be >= 0: " + initCapacity);
        }

        if (loadFactor <= 0 || loadFactor >= 1f || Float.isNaN(loadFactor)) {
            throw new IllegalArgumentException("loadFactor must be > 0 and < 1: " + loadFactor);
        }

        this.loadFactor = loadFactor;
        int tableSize = tableSize(initCapacity, loadFactor);
        threshold = (int) (tableSize * loadFactor);
        mask = tableSize - 1;
        shift = Long.numberOfLeadingZeros(mask);

        keyTable = (K[]) new Object[tableSize];
        valueTable = new int[tableSize];
    }

    /**
     * Returns the value for the specified key, or the default value if the key is not in the map.
     */
    public int get(K key, int defaultValue) {
        int index = locateKey(key);
        return index < 0 ? defaultValue : valueTable[index];
    }

    /**
     * Returns the key's current value and increments the stored value. If the key is not in the map,
     * defaultValue + increment is put into the map and defaultValue is returned.
     */
    public int getAndIncrement(K key, int defaultValue, int increment) {
        int i = locateKey(key);
        if (i >= 0) { // Existing key was found.
            int oldValue = valueTable[i];
            valueTable[i] += increment;
            return oldValue;
        }
        i = -(i + 1); // Empty space was found.
        keyTable[i] = key;
        valueTable[i] = defaultValue + increment;
        if (++size >= threshold) resize(keyTable.length << 1);
        return defaultValue;
    }

    /**
     * Removes the mapping for the specified key from this map if present.
     */
    public int remove(K key, int defaultValue) {
        int index = locateKey(key);
        if (index < 0) {
            return defaultValue;
        }

        K[] keyTable = this.keyTable;
        int[] valueTable = this.valueTable;
        int oldValue = valueTable[index];
        int mask = this.mask, next = index + 1 & mask;

        while ((key = keyTable[next]) != null) {
            int placement = place(key);
            if ((next - placement & mask) > (index - placement & mask)) {
                keyTable[index] = key;
                valueTable[index] = valueTable[next];
                index = next;
            }
            next = next + 1 & mask;
        }
        keyTable[index] = null;
        size--;
        return oldValue;
    }

    public boolean containsKey(K key) {
        return locateKey(key) >= 0;
    }

    /**
     * Doesn't return a value, unlike other maps.
     */
    public void put(K key, int value) {
        int index = locateKey(key);
        if (index >= 0) { // Existing key was found.
            valueTable[index] = value;
            return;
        }
        index = -(index + 1); // Empty space was found.
        keyTable[index] = key;
        valueTable[index] = value;

        if (++size >= threshold) {
            resize(keyTable.length << 1);
        }
    }

    /**
     * Returns the index of the key if already present, else {@code -(index + 1)} for the next empty index.
     */
    private int locateKey(K key) {
        if (key == null) {
            throw new IllegalArgumentException("key cannot be null.");
        }
        K[] keyTable = this.keyTable;
        for (int i = place(key); ; i = i + 1 & mask) {
            K other = keyTable[i];
            if (other == null) return -(i + 1); // Empty space is available.
            if (other == key) return i; // Same key was found.
        }
    }

    /**
     * Returns an index >= 0 and <= {@link #mask} for the specified {@code item}.
     * The default implementation uses Fibonacci hashing on the item's {@link System#identityHashCode}: the hashcode is multiplied by a
     * long constant (2 to the 64th, divided by the golden ratio) then the uppermost bits are shifted into the lowest positions to
     * obtain an index in the desired range. Multiplication by a long may be slower than int (eg on GWT) but greatly improves
     * rehashing, allowing even very poor hashcode, such as those that only differ in their upper bits, to be used without high
     * collision rates. Fibonacci hashing has increased collision rates when all or most hashcode are multiples of larger
     * Fibonacci numbers.
     */
    protected int place(K item) {
        return (int) (item.hashCode() * 0x9E3779B97F4A7C15L >>> shift);
    }

    /**
     * @param newSize
     */
    final void resize(int newSize) {
        int oldCapacity = keyTable.length;
        threshold = (int) (newSize * loadFactor);
        mask = newSize - 1;
        shift = Long.numberOfLeadingZeros(mask);

        K[] oldKeyTable = keyTable;
        int[] oldValueTable = valueTable;

        keyTable = (K[]) new Object[newSize];
        valueTable = new int[newSize];

        if (size > 0) {
            for (int i = 0; i < oldCapacity; i++) {
                K key = oldKeyTable[i];
                if (key != null) putResize(key, oldValueTable[i]);
            }
        }
    }

    /**
     * Skips checks for existing keys, doesn't increment size.
     */
    private void putResize(K key, int value) {
        K[] keyTable = this.keyTable;
        for (int i = place(key); ; i = (i + 1) & mask) {
            if (keyTable[i] == null) {
                keyTable[i] = key;
                valueTable[i] = value;
                return;
            }
        }
    }

    public int size() {
        return size;
    }

    public void clear () {
        if (size == 0) {
            return;
        }
        size = 0;
        Arrays.fill(keyTable, null);
    }

    @Override
    public int hashCode() {
        int h = size;
        K[] keyTable = this.keyTable;
        int[] valueTable = this.valueTable;
        for (int i = 0, n = keyTable.length; i < n; i++) {
            K key = keyTable[i];
            if (key != null) h += System.identityHashCode(key) + valueTable[i];
        }

        return h;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof ObjectIntMap)) {
            return false;
        }

        ObjectIntMap other = (ObjectIntMap) obj;
        if (other.size != size) {
            return false;
        }

        K[] keyTable = this.keyTable;
        int[] valueTable = this.valueTable;
        for (int i = 0, n = keyTable.length; i < n; i++) {
            K key = keyTable[i];
            if (key != null) {
                int otherValue = other.get(key, 0);
                if (otherValue == 0 && !other.containsKey(key)) return false;
                if (otherValue != valueTable[i]) return false;
            }
        }
        return true;
    }

    @Override
    public String toString () {
        return toString(", ", true);
    }

    private String toString (String separator, boolean braces) {
        if (size == 0) return braces ? "{}" : "";
        StringBuilder buffer = new StringBuilder(32);
        if (braces) buffer.append('{');
        K[] keyTable = this.keyTable;
        int[] valueTable = this.valueTable;
        int i = keyTable.length;
        while (i-- > 0) {
            K key = keyTable[i];
            if (key == null) continue;
            buffer.append(key);
            buffer.append('=');
            buffer.append(valueTable[i]);
            break;
        }
        while (i-- > 0) {
            K key = keyTable[i];
            if (key == null) continue;
            buffer.append(separator);
            buffer.append(key);
            buffer.append('=');
            buffer.append(valueTable[i]);
        }
        if (braces) buffer.append('}');
        return buffer.toString();
    }

    /**
     * Calculate table size.
     */
    protected static int tableSize(int initCapacity, float loadFactor) {
        return nextPowerOfTwo(Math.max(2, (int) Math.ceil(initCapacity / loadFactor)));
    }

    /**
     * Returns a power of two size for the given target capacity.
     */
    protected static final int nextPowerOfTwo(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= MAX_CAPACITY) ? MAX_CAPACITY : n + 1;
    }
}
