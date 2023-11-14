package com.github.spark.lib.common;

import com.github.spark.lib.framework.Framework;

import java.util.HashMap;
import java.util.Iterator;

public abstract class Registry<K, V> {
    protected final Framework framework;
    protected final HashMap<K, V> items = new HashMap<>();

    public Registry(Framework framework) {
        this.framework = framework;
    }

    public abstract void findAndRegisterItems();

    public Iterator<V> getEntries() {
        return this.items.values().iterator();
    }

    public V addItem(K key, V item) {
        return items.put(key, item);
    }

    public V getItem(K key) {
        return items.get(key);
    }

    public boolean injectMembers() {
        for(Object service : items.values()) {
            framework.injectMembers(service);
        }
        return true;
    }
}
