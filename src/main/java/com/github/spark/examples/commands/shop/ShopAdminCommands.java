package com.github.spark.examples.commands.shop;

import com.github.spark.lib.commands.Command;
import com.github.spark.lib.commands.CommandHandler;
import com.github.spark.lib.commands.CommandEventContext;

@CommandHandler(name = "admin")
public class ShopAdminCommands implements Command {
    public boolean onCommand(CommandEventContext context) {
        return false;
    }


}
