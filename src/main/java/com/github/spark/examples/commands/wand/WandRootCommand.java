package com.github.spark.examples.commands.wand;

import com.github.spark.examples.constants.Constants;
import com.github.spark.examples.constants.DisplayConstants;
import com.github.spark.lib.commands.Command;
import com.github.spark.lib.commands.annotations.CommandHandler;
import com.github.spark.lib.commands.dto.CommandContext;
import com.github.spark.lib.framework.Framework;
import com.github.spark.lib.services.custom.MetadataService;
import com.google.inject.Inject;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

@CommandHandler(root = true, name = "wand")
public class WandRootCommand implements Command {
    @Inject
    Framework framework;
    @Inject MetadataService metaService;

    @Override
    public boolean onCommand(CommandContext context) {
        Player player = context.player();
        ItemStack stick = new ItemStack(Material.STICK, 1);

        metaService.displayName(stick,
            Component.text(DisplayConstants.MAGIC_WAND_DISPLAY_NAME)
                .color(TextColor.color(175, 25, 225))
        );
        metaService.setMeta(stick, Constants.MAGIC_WAND_KEY, true);

        player.getInventory().addItem(stick);
        return true;
    }
}
