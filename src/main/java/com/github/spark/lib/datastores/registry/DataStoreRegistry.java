package com.github.spark.lib.datastores.registry;

import com.github.spark.lib.common.Registry;
import com.github.spark.lib.datastores.DataStore;
import com.github.spark.lib.datastores.DataStoreItem;
import com.github.spark.lib.framework.Framework;

import java.util.*;

public class DataStoreRegistry extends Registry<Class<DataStore<?>>, DataStore<? extends DataStoreItem>> {
    public DataStoreRegistry(Framework framework) {
        super(framework);
    }

    @Override
    public void findAndRegisterItems() {
        ArrayList<DataStore<? extends DataStoreItem>> dataStores = DataStoreReflection.findDataStores();
        dataStores.forEach(dataStore -> {
            items.put((Class<DataStore<?>>) dataStore.getClass(), dataStore);
            dataStore.onLoad(framework.plugin.getDataFolder());
        });
    }
}
