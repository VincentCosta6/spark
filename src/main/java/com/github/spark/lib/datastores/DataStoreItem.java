package com.github.spark.lib.datastores;

import com.github.spark.lib.datastores.events.MutateCallback;
import com.github.spark.lib.util.JacksonUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;
import java.io.Serializable;

public abstract class DataStoreItem implements Serializable {
    @Serial
    private static final long serialVersionUID = 42L;

    private transient DataStore<? extends DataStoreItem> datastore;

    @Contract(pure=true)
    public static @Nullable DataStoreItem createDefault() {
        return null;
    }

    public void setDatastore(DataStore<? extends DataStoreItem> newDatastore) {
        this.datastore = newDatastore;
    }

    public DataStoreItem mutate(MutateCallback callback) {
        DataStoreItem clonedItem = JacksonUtils.clone(this, DataStoreItem.class);
        callback.call();
        datastore.notifyObserversOfMutation(this, clonedItem);
        return this;
    }
}
