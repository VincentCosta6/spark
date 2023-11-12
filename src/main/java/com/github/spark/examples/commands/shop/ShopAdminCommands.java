package com.github.spark.examples.commands.shop;

import com.github.spark.lib.commands.Command;
import com.github.spark.lib.commands.CommandHandler;
import com.github.spark.lib.commands.dto.CommandContext;

@CommandHandler(name = "admin")
public class ShopAdminCommands implements Command {
    public boolean onCommand(CommandContext context) {
        context.playerCommandEvent().player().sendMessage("admin command!");
        return false;
    }

    @CommandHandler(name = "add-item", description = "usage: add-item [item-id] [amount]")
    public boolean onBuyCommand(CommandContext context) {
        context.playerCommandEvent().player().sendMessage("add item command!");
        return false;
    }
}
