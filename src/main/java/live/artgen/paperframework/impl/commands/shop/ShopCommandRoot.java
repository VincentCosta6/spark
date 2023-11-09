package live.artgen.paperframework.impl.commands.shop;

import live.artgen.paperframework.lib.commands.Command;
import live.artgen.paperframework.lib.commands.CommandEventContext;
import live.artgen.paperframework.lib.commands.CommandHandler;

@CommandHandler(name = "shop", description = "buy/sell shop items")
public class ShopCommandRoot implements Command {
    public boolean onCommand(CommandEventContext context) {

    }

    @CommandHandler(name = "admin")
    public ShopAdminCommands adminCommands;

    @CommandHandler(name = "buy", description = "usage: /shop buy [item-id] [amount]")
    public boolean onBuyCommand(CommandEventContext context) {

    }

    @CommandHandler(name = "sell", description = "usage: /shop sell [item-id] [amount] or /shop sell (sells item in your hand)")
    public boolean onSellCommand(CommandEventContext context) {

    }
}
