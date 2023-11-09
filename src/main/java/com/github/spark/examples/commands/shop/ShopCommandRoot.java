package com.github.spark.examples.commands.shop;

import com.github.spark.lib.commands.Command;
import com.github.spark.lib.commands.CommandHandler;
import com.github.spark.lib.commands.CommandEventContext;

@CommandHandler(name = "shop", description = "buy/sell shop items")
public class ShopCommandRoot implements Command {
    public boolean onCommand(CommandEventContext context) {
        return false;
    }

    @CommandHandler(name = "admin")
    public ShopAdminCommands adminCommands;

    @CommandHandler(name = "buy", description = "usage: /shop buy [item-id] [amount]")
    public boolean onBuyCommand(CommandEventContext context) {
        return false;
    }

    @CommandHandler(name = "sell", description = "usage: /shop sell [item-id] [amount] or /shop sell (sells item in your hand)")
    public boolean onSellCommand(CommandEventContext context) {
        return false;
    }
}
