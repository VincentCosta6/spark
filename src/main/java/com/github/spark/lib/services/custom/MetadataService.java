package com.github.spark.lib.services.custom;

import com.github.spark.lib.framework.Framework;
import com.github.spark.lib.services.annotations.ModifyMetaCallback;
import com.github.spark.lib.services.annotations.Service;
import com.google.inject.Inject;
import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

@Service
public class MetadataService {
    @Inject Framework framework;

    private NamespacedKey getNameSpaceKey(String key) {
        return new NamespacedKey(framework.plugin, key);
    }

    public void setMeta(ItemStack stack, String key, String value) {
        setMetaInternal(stack, key, PersistentDataType.STRING, value);
    }

    public void setMeta(ItemStack stack, String key, Boolean value) {
        setMetaInternal(stack, key, PersistentDataType.BOOLEAN, value);
    }

    public void setMeta(ItemStack stack, String key, Integer value) {
        setMetaInternal(stack, key, PersistentDataType.INTEGER, value);
    }

    public <T, Z> void setMeta(ItemStack stack, String key, PersistentDataType<T, Z> container, Z value) {
        setMetaInternal(stack, key, container, value);
    }

    public void setMeta(ItemStack stack, String key, Float value) {
        setMetaInternal(stack, key, PersistentDataType.FLOAT, value);
    }

    private <T, Z> void setMetaInternal(ItemStack stack, String key, PersistentDataType<T, Z> type, Z value) {
        NamespacedKey newKey = getNameSpaceKey(key);
        ItemMeta meta = stack.getItemMeta();

        meta.getPersistentDataContainer().set(newKey, type, value);
        stack.setItemMeta(meta);
    }

    public boolean has(ItemStack stack, String key) {
        NamespacedKey newKey = getNameSpaceKey(key);
        ItemMeta meta = stack.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();

        return container.has(newKey);
    }

    public String getMetaString(ItemStack stack, String key) {
        return getMetaInternal(stack, key, PersistentDataType.STRING);
    }

    public Boolean getMetaBoolean(ItemStack stack, String key) {
        return getMetaInternal(stack, key, PersistentDataType.BOOLEAN);
    }

    public Integer getMetaInteger(ItemStack stack, String key) {
        return getMetaInternal(stack, key, PersistentDataType.INTEGER);
    }

    private <T, Z> Z getMetaInternal(ItemStack stack, String key, PersistentDataType<T, Z> type) {
        NamespacedKey newKey = getNameSpaceKey(key);
        ItemMeta meta = stack.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();

        return container.get(newKey, type);
    }

    public void displayName(ItemStack stack, Component displayName) {
        modifyItemMeta(stack, meta -> {
            meta.displayName(displayName);
        });
    }

    public void setCustomModelData(ItemStack stack, Integer modelData) {
        modifyItemMeta(stack, meta -> {
            meta.setCustomModelData(modelData);
        });
    }

    public void modifyItemMeta(ItemStack stack, ModifyMetaCallback callback) {
        ItemMeta meta = stack.getItemMeta();
        callback.call(meta);
        stack.setItemMeta(meta);
    }
}
