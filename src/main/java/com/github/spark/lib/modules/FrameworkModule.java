package com.github.spark.lib.modules;

import com.github.spark.lib.datastores.DataStore;
import com.github.spark.lib.datastores.DataStoreItem;
import com.github.spark.lib.datastores.DataStoreRegistry;
import com.google.inject.AbstractModule;
import com.github.spark.lib.Framework;

public class FrameworkModule extends AbstractModule  {
    private final Framework framework;
    private final DataStoreRegistry dataStoreRegistry;

    public FrameworkModule(Framework framework, DataStoreRegistry dataStoreRegistry) {
        this.framework = framework;
        this.dataStoreRegistry = dataStoreRegistry;
    }

    @Override
    protected void configure() {
        bind(Framework.class).toInstance(framework);
        for (var it = dataStoreRegistry.getEntries(); it.hasNext(); ) {
            DataStore<? extends DataStoreItem> store = it.next();

            bind((Class<DataStore<? extends DataStoreItem>>) store.getClass()).toInstance(store);
        }
    }
}
