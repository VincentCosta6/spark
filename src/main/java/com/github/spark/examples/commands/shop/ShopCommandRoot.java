package com.github.spark.examples.commands.shop;

import com.github.spark.lib.Framework;
import com.github.spark.lib.commands.Command;
import com.github.spark.lib.commands.CommandHandler;
import com.github.spark.lib.commands.CommandEventContext;
import com.github.spark.lib.commands.dto.CommandContext;
import com.google.inject.Inject;

@CommandHandler(name = "shop", description = "buy/sell shop items", root = true)
public class ShopCommandRoot implements Command {
    @Inject Framework framework;

    public boolean onCommand(CommandContext context) {
        context.playerCommandEvent().player().sendMessage("shop command!");
        framework.log("hello");
        return false;
    }

    @CommandHandler(name = "admin")
    public ShopAdminCommands adminCommands;

    @CommandHandler(name = "buy", description = "usage: /shop buy [item-id] [amount]")
    public boolean onBuyCommand(CommandContext context) {
        context.playerCommandEvent().player().sendMessage("buy command!");
        return false;
    }

    @CommandHandler(name = "sell", description = "usage: /shop sell [item-id] [amount] or /shop sell (sells item in your hand)")
    public boolean onSellCommand(CommandContext context) {
        context.playerCommandEvent().player().sendMessage("sell command!");
        return false;
    }
}
