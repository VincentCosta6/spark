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

    public void findAndRegisterDataStores() {
        Reflections reflections = new Reflections(SparkContext.basePackage, Scanners.TypesAnnotated);

        Set<Class<?>> dataStoreClasses = reflections.getTypesAnnotatedWith(RegisterDataStore.class);
        for (Class<?> dataStoreClass : dataStoreClasses) {
            if (dataStoreClass.getEnclosingClass() == null && DataStore.class.isAssignableFrom(dataStoreClass)) {
                try {
                    RegisterDataStore registerDataStoreAnnotation = dataStoreClass.getAnnotation(RegisterDataStore.class);
                    Class<? extends DataStoreItem> dataStoreItem = getDataStoreItemClass((Class<? extends DataStore<? extends DataStoreItem>>) dataStoreClass);

                    DataStore<?> dataStoreInstance = (DataStore<?>) dataStoreClass.getDeclaredConstructor().newInstance();
                    dataStoreInstance.cachedPrimaryKey = getPrimaryKeyField(dataStoreItem);
                    if (!registerDataStoreAnnotation.name().isBlank()) {
                        dataStoreInstance.setName(registerDataStoreAnnotation.name());
                    }
                    dataStoreInstance.setVersion(registerDataStoreAnnotation.version());

                    framework.addDataStore(dataStoreInstance);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Class<? extends DataStoreItem> getDataStoreItemClass(Class<? extends DataStore<? extends DataStoreItem>> clazz) {
        Type genericSuperclass = clazz.getGenericSuperclass();

        if (genericSuperclass instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
            Type[] typeArguments = parameterizedType.getActualTypeArguments();

            if (typeArguments.length > 0) {
                Type datastoreItemType = typeArguments[0];

                if (datastoreItemType instanceof Class<?>) {
                    // Check if datastoreItemType is a subclass of DataStoreItem
                    if (DataStoreItem.class.isAssignableFrom((Class<?>) datastoreItemType)) {
                        return (Class<? extends DataStoreItem>) datastoreItemType;
                    }
                }
            }
        }

        return null;
    }

    public Field getPrimaryKeyField(Class<? extends DataStoreItem> clazz) {
        boolean foundKey = false;
        Field foundField = null;
        for (Field field : clazz.getDeclaredFields()) {
            ItemKey key = field.getAnnotation(ItemKey.class);
            if (key != null) {
                if (foundKey) {
                    throw new RuntimeException("The DataStoreItem: " + clazz.getSimpleName() + " does not have a @ItemKey annotation");
                }

                foundKey = true;
                foundField = field;
            }
        }

        return foundField;
    }

    public Iterator<DataStore<? extends DataStoreItem>> getEntries() {
        return stores.values().iterator();
    }
}
