package live.artgen.paperframework.lib.datastores;

import java.io.File;

public interface DataStoreI {
    void onSave(File folder);
    void onLoad(File folder);
}
