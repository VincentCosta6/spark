package com.github.spark.lib.datastores;

import com.github.spark.lib.Framework;
import com.github.spark.lib.SparkContext;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.logging.Level;

public class DataStoreRegistry {
    private final Framework framework;
    private final HashMap<Class<DataStore<?>>, DataStore<? extends DataStoreItem>> stores = new HashMap<>();

    public DataStoreRegistry(Framework framework) {
        this.framework = framework;
    }

    public <T extends DataStore<?>> void addDataStore(T store) {
        try {
            framework.log(Level.INFO, "Loading datastore: " + store.getName(), true);
            stores.put((Class<DataStore<?>>) store.getClass(), store);
        } catch (Exception e) {
            framework.log(Level.SEVERE, "Issue creating/loading store: " + store.getClass().getName());
        }
    }

    public Iterator<DataStore<? extends DataStoreItem>> getEntries() {
        return stores.values().iterator();
    }
}
