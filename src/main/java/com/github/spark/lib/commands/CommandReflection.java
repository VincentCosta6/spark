package com.github.spark.lib.commands;

import com.github.spark.lib.framework.Framework;
import com.github.spark.lib.SparkContext;
import com.github.spark.lib.commands.dto.CommandContext;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;

public class CommandReflection {
    public static ArrayList<CommandNode> findCommandNodes(Framework framework) {
        ArrayList<CommandNode> commands = new ArrayList<>();
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
                            commandHandler.name().toLowerCase(),
                            commandHandler.description(),
                            (Command) handlerInstance
                    );
                    registerSubCommands(framework, rootNode, handlerClass, (Command) handlerInstance);
                    commands.add(rootNode);
//                    commands.put(handlerClass.getAnnotation(CommandHandler.class).name(), rootNode);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return commands;
    }

    public static void registerSubCommands(Framework framework, CommandNode parentNode, Class<?> parentDef, Command parentInstance) throws NoSuchMethodException {
        for (Method method : parentDef.getDeclaredMethods()) {
            method.setAccessible(true);
            CommandHandler commandHandlerAnnotation = method.getAnnotation(CommandHandler.class);
            if (commandHandlerAnnotation != null) {
                Method commandMethod = parentInstance.getClass().getMethod(method.getName(), CommandContext.class);
                commandMethod.setAccessible(true);
                CommandNode commandNode = new CommandNode(parentNode,
                        commandHandlerAnnotation.name().toLowerCase(),
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
                    framework.injectMembers(subCommandHandler);
                    CommandNode subCommandRoot = new CommandNode(
                            parentNode,
                            subCommandAnnotation.name().toLowerCase(),
                            subCommandAnnotation.description(),
                            (Command) subCommandHandler
                    );
                    parentNode.addSubCommand(subCommandRoot);
                    registerSubCommands(framework, subCommandRoot, field.getType(), (Command) subCommandHandler);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
