package com.github.spark.examples.commands.shop;

import com.github.spark.lib.commands.Command;
import com.github.spark.lib.commands.CommandHandler;
import com.github.spark.lib.commands.CommandEventContext;
import com.github.spark.lib.commands.dto.CommandContext;

@CommandHandler(name = "admin")
public class ShopAdminCommands implements Command {
    public boolean onCommand(CommandContext context) {
        context.playerCommandEvent().player().sendMessage("admin command!");
        return false;
    }


}
