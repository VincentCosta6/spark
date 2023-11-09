package live.artgen.paperframework.impl.commands.shop;

import live.artgen.paperframework.lib.commands.Command;
import live.artgen.paperframework.lib.commands.CommandEventContext;
import live.artgen.paperframework.lib.commands.CommandHandler;

@CommandHandler(name = "admin")
public class ShopAdminCommands implements Command {
    public boolean onCommand(CommandEventContext context) {
        return false;
    }


}
