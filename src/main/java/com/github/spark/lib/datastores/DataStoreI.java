package com.github.spark.lib.datastores;

import java.io.File;

public interface DataStoreI {
    void onSave(File folder);
    void onLoad(File folder);
}
