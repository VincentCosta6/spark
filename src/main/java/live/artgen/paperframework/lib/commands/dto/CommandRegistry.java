package live.artgen.paperframework.lib.commands.dto;

import live.artgen.paperframework.lib.commands.CommandHandler;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class CommandRegistry {
    private Map<String, CommandNode> commands = new HashMap<>();

    public void registerCommandHandler(Object handler) {
        Class<?> clazz = handler.getClass();
        CommandHandler commandHandlerAnnotation = clazz.getAnnotation(CommandHandler.class);
        if (commandHandlerAnnotation == null) return;

        CommandNode command = new CommandNode(commandHandlerAnnotation.name(), commandHandlerAnnotation.description());
        commands.put(command.getName(), command);

        for (Field field : clazz.getDeclaredFields()) {
            CommandHandler subCommandAnnotation = field.getAnnotation(CommandHandler.class);
            if (subCommandAnnotation != null) {
                try {
                    Object subCommandHandler = field.get(handler);
                    CommandNode subCommand = new CommandNode(subCommandAnnotation.name(), subCommandAnnotation.description());
                    command.addSubCommand(subCommand);
                    registerCommandHandler(subCommandHandler);  // Recursive call for further nesting
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public CommandNode getCommand(String name) {
        return commands.get(name);
    }
}
