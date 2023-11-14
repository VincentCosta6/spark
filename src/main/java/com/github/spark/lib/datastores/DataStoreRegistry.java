package com.github.spark.lib.datastores;

import com.github.spark.lib.framework.Framework;
import com.github.spark.lib.framework.FrameworkInjectable;

import java.util.*;
import java.util.logging.Level;

public class DataStoreRegistry implements FrameworkInjectable {
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

    public void findAndRegisterDataStores() {
        ArrayList<DataStore<? extends DataStoreItem>> dataStores = DataStoreReflection.findDataStores();
        dataStores.forEach(framework::addDataStore);
    }

    public boolean injectMembers() {
        for(Object service : stores.values()) {
            framework.injectMembers(service);
        }
        return true;
    }
}
