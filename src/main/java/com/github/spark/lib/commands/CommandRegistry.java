package com.github.spark.lib.commands;

import com.github.spark.lib.Framework;
import com.github.spark.lib.SparkContext;
import com.github.spark.lib.commands.dto.CommandContext;
import com.github.spark.lib.commands.dto.CommandNode;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CommandRegistry {
    private final Map<String, CommandNode> commands = new HashMap<>();
    private Framework framework;

    public CommandRegistry(Framework framework) {
        this.framework = framework;
    }

    public void findAndRegisterCommands() {
        Reflections reflections = new Reflections(SparkContext.basePackage, Scanners.TypesAnnotated);

        Set<Class<?>> commandHandlers = reflections.getTypesAnnotatedWith(CommandHandler.class);
        for (Class<?> handlerClass : commandHandlers) {
            if (handlerClass.getEnclosingClass() == null && Command.class.isAssignableFrom(handlerClass)) {
                try {
                    CommandHandler commandHandler = handlerClass.getAnnotation(CommandHandler.class);
                    if (!commandHandler.root()) continue;

                    Object handlerInstance = handlerClass.getDeclaredConstructor().newInstance();
                    framework.injectMembers(handlerInstance);
                    CommandNode rootNode = new CommandNode(null,
                        commandHandler.name(),
                        commandHandler.description(),
                        (Command) handlerInstance
                    );
                    registerSubCommands(rootNode, handlerClass, (Command) handlerInstance);
                    commands.put(handlerClass.getAnnotation(CommandHandler.class).name(), rootNode);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void registerSubCommands(CommandNode parentNode, Class<?> parentDef, Command parentInstance) throws NoSuchMethodException {
        for (Method method : parentDef.getDeclaredMethods()) {
            method.setAccessible(true);
            CommandHandler commandHandlerAnnotation = method.getAnnotation(CommandHandler.class);
            if (commandHandlerAnnotation != null) {
                Method commandMethod = parentInstance.getClass().getMethod(method.getName(), CommandContext.class);
                commandMethod.setAccessible(true);
                CommandNode commandNode = new CommandNode(parentNode,
                    commandHandlerAnnotation.name(),
                    commandHandlerAnnotation.description(),
                    parentInstance,
                    commandMethod
                );
                parentNode.addSubCommand(commandNode);
            }
        }

        for (Field field : parentDef.getDeclaredFields()) {
            CommandHandler subCommandAnnotation = field.getAnnotation(CommandHandler.class);
            if (subCommandAnnotation != null) {
                try {
                    field.setAccessible(true);
                    Object subCommandHandler = field.get(parentInstance);
                    if (subCommandHandler == null) {
                        subCommandHandler = field.getType().getDeclaredConstructor().newInstance();
                        field.set(parentInstance, subCommandHandler);
                    }
                    CommandNode subCommandRoot = new CommandNode(
                            parentNode,
                            subCommandAnnotation.name(),
                            subCommandAnnotation.description(),
                            (Command) subCommandHandler
                    );
                    parentNode.addSubCommand(subCommandRoot);
                    registerSubCommands(subCommandRoot, field.getType(), (Command) subCommandHandler);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public CommandNode getCommand(String name) {
        return commands.get(name);
    }
}
