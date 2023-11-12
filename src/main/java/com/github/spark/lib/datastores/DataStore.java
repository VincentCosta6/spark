package com.github.spark.lib.datastores;

import com.github.spark.examples.datastores.PlayerState;
import org.bukkit.entity.Player;

import java.io.*;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

public abstract class DataStore<T extends DataStoreItem> implements DataStoreI, Serializable {
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
            insert(instance);
        }
        return instance;
    }

    public boolean exists(String id) {
        return map.containsKey(id);
    }

    public T insert(T item) {
        return map.put(item.getItemId(), item);
    }

    public T remove(String id) {
        return map.remove(id);
    }

    public int size() {
        return map.size();
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
                    map.put(item.getItemId(), item);
                }
                setName(dataStoreInFile.getName());
                setVersion(dataStoreInFile.getVersion());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
}


