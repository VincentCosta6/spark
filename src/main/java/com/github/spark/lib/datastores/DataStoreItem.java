package com.github.spark.lib.datastores;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.spark.lib.datastores.events.MutateCallback;
import com.github.spark.lib.util.JacksonUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;
import java.io.Serializable;

public abstract class DataStoreItem<T extends DataStoreItem<T>> implements Serializable {
    @Serial
    private static final long serialVersionUID = 42L;

    @JsonIgnore
    private transient DataStore<? extends DataStoreItem> datastore;

    public DataStoreItem() {}

    @JsonCreator
    @Contract(pure=true)
    public static @Nullable DataStoreItem createDefault() {
        return null;
    }

    public void setDatastore(DataStore<? extends DataStoreItem> newDatastore) {
        this.datastore = newDatastore;
    }

    public final T mutate(MutateCallback callback) {
        T clonedItem = JacksonUtils.clone((T) this, (Class<T>) this.getClass());
        callback.call();
        datastore.notifyObserversOfMutation(this, clonedItem);
        return (T) this;
    }
}
