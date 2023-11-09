package com.github.spark.lib.datastores;

import java.io.Serializable;

public abstract class DataStoreItem implements Serializable {
    public abstract String getItemId();

    public static DataStoreItem createDefault() {
        return null;
    }
}
