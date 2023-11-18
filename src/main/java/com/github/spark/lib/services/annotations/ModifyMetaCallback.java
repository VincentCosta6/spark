package com.github.spark.lib.services.annotations;

import org.bukkit.inventory.meta.ItemMeta;

@FunctionalInterface
public interface ModifyMetaCallback {
    void call(ItemMeta meta);
}
