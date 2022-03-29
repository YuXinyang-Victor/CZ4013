package utils;

import java.security.Key;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * One generic LRU Cache class
 * @param <K>
 * @param <V>
 */
public class LRUCache<K, V> {
    private final LinkedHashMap<K, V> map;
    private final int CAPACITY;

    public LRUCache(int capacity) {
        CAPACITY = capacity;
        map = new LinkedHashMap<>(capacity, 0.75f, true) {
            //access order for building up a LRUCache
            protected boolean removeEldestEntry(Map.Entry eldest) {
                return size() > CAPACITY;
            }
        };
    }
    public V get(K key) {
        return map.getOrDefault(key, null);
    }

    public void set(K key, V value) {
        map.put(key, value);
    }
}
