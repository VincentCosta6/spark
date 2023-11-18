package com.github.spark.lib.datastores;

import com.github.spark.lib.datastores.events.MutateCallback;

import java.io.Serial;
import java.io.Serializable;

public abstract class DataStoreItem implements Serializable {
    @Serial
    private static final long serialVersionUID = 42L;

    private transient DataStore<? extends DataStoreItem> datastore;

    public static DataStoreItem createDefault() {
        return null;
    }

    public void setDatastore(DataStore<? extends DataStoreItem> newDatastore) {
        this.datastore = newDatastore;
    }

    public DataStoreItem mutate(MutateCallback callback) {
        callback.call();
        datastore.notifyObserversOfMutation(this);
        return this;
    }
}
