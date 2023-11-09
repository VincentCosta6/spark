package live.artgen.paperframework.lib.modules;

import com.google.inject.AbstractModule;
import live.artgen.paperframework.lib.Framework;
import live.artgen.paperframework.lib.datastores.DataStore;
import live.artgen.paperframework.lib.datastores.DataStoreItem;
import live.artgen.paperframework.lib.datastores.DataStoreRegistry;

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
