package com.github.spark.lib.datastores;

import java.io.Serial;
import java.io.Serializable;

public abstract class DataStoreItem implements Serializable {
    @Serial
    private static final long serialVersionUID = 42L;

    public static DataStoreItem createDefault() {
        return null;
    }
}
