package com.github.spark.lib.modules;

import com.github.spark.lib.datastores.DataStore;
import com.github.spark.lib.datastores.DataStoreItem;
import com.github.spark.lib.datastores.DataStoreRegistry;
import com.github.spark.lib.services.ServiceRegistry;
import com.google.inject.AbstractModule;
import com.github.spark.lib.framework.Framework;

public class FrameworkModule extends AbstractModule  {
    private final Framework framework;
    private final DataStoreRegistry dataStoreRegistry;
    private final ServiceRegistry serviceRegistry;

    public FrameworkModule(
            Framework framework,
            DataStoreRegistry dataStoreRegistry,
            ServiceRegistry serviceRegistry) {
        this.framework = framework;
        this.dataStoreRegistry = dataStoreRegistry;
        this.serviceRegistry = serviceRegistry;
    }

    @Override
    protected void configure() {
        bind(Framework.class).toInstance(framework);
        for (var it = dataStoreRegistry.getEntries(); it.hasNext(); ) {
            DataStore<? extends DataStoreItem> store = it.next();

            bind((Class<DataStore<? extends DataStoreItem>>) store.getClass()).toInstance(store);
        }
        for (var it = serviceRegistry.getEntries(); it.hasNext(); ) {
            Object service = it.next();

            bind((Class<Object>) service.getClass()).toInstance(service);
        }
    }


}
