package com.github.spark.lib.datastores;

import com.github.spark.lib.Framework;
import com.github.spark.lib.SparkContext;
import com.github.spark.lib.commands.Command;
import com.github.spark.lib.commands.CommandHandler;
import com.github.spark.lib.commands.dto.CommandNode;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

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

    public void findAndRegisterDataStores() {
        Reflections reflections = new Reflections(SparkContext.basePackage, Scanners.TypesAnnotated);

        Set<Class<?>> dataStoreClasses = reflections.getTypesAnnotatedWith(RegisterDataStore.class);
        for (Class<?> dataStoreClass : dataStoreClasses) {
            if (dataStoreClass.getEnclosingClass() == null && DataStore.class.isAssignableFrom(dataStoreClass)) {
                try {
                    RegisterDataStore commandHandler = dataStoreClass.getAnnotation(RegisterDataStore.class);

                    DataStore<?> dataStoreInstance = (DataStore<?>) dataStoreClass.getDeclaredConstructor().newInstance();
                    if (!commandHandler.name().isBlank()) {
                        dataStoreInstance.setName(commandHandler.name());
                    }
                    dataStoreInstance.setVersion(commandHandler.version());

                    framework.addDataStore(dataStoreInstance);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Iterator<DataStore<? extends DataStoreItem>> getEntries() {
        return stores.values().iterator();
    }
}
