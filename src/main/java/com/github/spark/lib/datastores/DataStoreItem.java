package com.github.spark.lib.datastores;

import java.io.Serializable;

public abstract class DataStoreItem implements Serializable {
    public static final long serialVersionUID = 42L;

    public static DataStoreItem createDefault() {
        return null;
    }
}
