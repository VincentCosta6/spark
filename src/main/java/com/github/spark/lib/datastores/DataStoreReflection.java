package com.github.spark.lib.datastores;

import com.github.spark.lib.SparkContext;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Set;

public class DataStoreReflection {
    public static ArrayList<DataStore<? extends DataStoreItem>> findDataStores() {
        ArrayList<DataStore<? extends DataStoreItem>> dataStoresToRegister = new ArrayList<>();
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

                    dataStoresToRegister.add(dataStoreInstance);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return dataStoresToRegister;
    }

    public static Class<? extends DataStoreItem> getDataStoreItemClass(Class<? extends DataStore<? extends DataStoreItem>> clazz) {
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

    public static Field getPrimaryKeyField(Class<? extends DataStoreItem> clazz) {
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
}
