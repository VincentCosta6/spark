package com.github.spark.lib.datastores;

import com.github.spark.lib.services.custom.ObserverService;
import com.google.inject.Inject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

public abstract class DataStore<T extends DataStoreItem> implements DataStoreI, Serializable {
    @Serial
    private static final long serialVersionUID = 42L;

    @Inject private transient ObserverService observerService;

    public transient Field cachedPrimaryKey;
    private transient boolean isDirty = false;

    private HashMap<String, T> map = new HashMap<>();
    private String name = this.getClass().getSimpleName();
    private int version = 0;

    public DataStore() {}

    public T findById(String id) {
        return map.get(id);
    }

    public T getOrDefaultCreate(String id, Supplier<T> defaultCreator) {
        T instance = findById(id);
        if (instance == null) {
            instance = defaultCreator.get();
            instance.setDatastore(this);
            insert(instance);
        }
        return instance;
    }

    public boolean exists(String id) {
        return map.containsKey(id);
    }

    public T insert(T item) {
        onItemCreated(item);
        try {
            return map.put((String) cachedPrimaryKey.get(item), item);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to compute primary key");
        }
    }

    public T remove(String id) {
        T oldItem = map.remove(id);
        onItemRemoved(oldItem);
        return oldItem;
    }

    public int size() {
        return map.size();
    }

    public void clear() {
        onDateStoreCleared();
        map.clear();
    }

    public T changeKey(String oldKey, String newKey) {
        T foundItem = findById(oldKey);
        if (foundItem == null) {
            return null;
        }

        remove(oldKey);
        try {
            cachedPrimaryKey.set(foundItem, newKey);
        } catch (IllegalAccessException e) {

        }
        insert(foundItem);

        return foundItem;
    }

    public void onDataStoreLoaded() {}
    public void onDateStoreCleared() {}
    public void onItemCreated(T newItem) {}
    public void onItemRemoved(T oldItem) {}
    public void onItemMutated(T item) {}

    public Iterator<T> getIterator() {
        return map.values().iterator();
    }

    @Override
    public void onSave(File folder) {
        try {
            String fileName = name + ".store";

            File fileToWrite;
            File[] files = folder.listFiles();
            if (files != null) {
                Optional<File> writeOptional = Arrays.stream(Objects.requireNonNull(folder.listFiles()))
                        .filter(Objects::nonNull)
                        .filter(file -> file.getName().contentEquals(fileName))
                        .findFirst();

                if (writeOptional.isPresent()) {
                    fileToWrite = writeOptional.get();
                } else {
                    fileToWrite = new File(folder, fileName);
                    fileToWrite.createNewFile();
                }
            } else {
                fileToWrite = new File(folder, fileName);
                fileToWrite.createNewFile();
            }

            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileToWrite))) {
                oos.writeObject(this);
                isDirty = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLoad(File folder) {
        try {
            String fileName = name + ".store";

            File fileToRead;
            File[] files = folder.listFiles();
            if (files != null) {
                Optional<File> writeOptional = Arrays.stream(Objects.requireNonNull(folder.listFiles()))
                        .filter(Objects::nonNull)
                        .filter(file -> file.getName().contentEquals(fileName))
                        .findFirst();

                if (writeOptional.isPresent()) {
                    fileToRead = writeOptional.get();
                } else {
                    return;
                }
            } else {
                return;
            }

            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileToRead))) {
                DataStore<T> dataStoreInFile = (DataStore<T>) ois.readObject();

                for(T item : dataStoreInFile.map.values()) {
                    map.put((String) cachedPrimaryKey.get(item), item);
                    item.setDatastore(this);
                }
                setName(dataStoreInFile.getName());
                setVersion(dataStoreInFile.getVersion());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        onDataStoreLoaded();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public void notifyObserversOfMutation(DataStoreItem item) {
        isDirty = true;
        observerService.notifyObserverOfMutation(item.getClass(), item);
        onItemMutated((T) item);
    }

    public boolean isDirty() {
        return isDirty;
    }
}


