package com.github.spark.examples.commands.shop;

import com.github.spark.examples.services.CustomService;
import com.github.spark.lib.commands.Command;
import com.github.spark.lib.commands.annotations.CommandHandler;
import com.github.spark.lib.commands.dto.CommandContext;
import com.google.inject.Inject;

@CommandHandler(name = "admin")
public class ShopAdminCommands implements Command {
    @Inject
    CustomService customService;

    public boolean onCommand(CommandContext context) {
        context.playerCommandEvent().player().sendMessage("admin command!");
        return false;
    }

    @CommandHandler(name = "add-item", description = "usage: add-item [item-id] [amount]")
    public boolean onBuyCommand(CommandContext context) {
        customService.printTestAndPlayerStateCount();
        context.playerCommandEvent().player().sendMessage("add item command!");
        return false;
    }
}
