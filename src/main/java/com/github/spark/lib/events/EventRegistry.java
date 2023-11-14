package com.github.spark.lib.events;

import com.github.spark.lib.framework.Framework;
import com.github.spark.lib.framework.FrameworkInjectable;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class EventRegistry implements FrameworkInjectable {
    private final Framework framework;
    private final Set<Listener> eventHandlers = new HashSet<>();

    public EventRegistry(Framework framework) {
        this.framework = framework;
    }

    public void findAndRegisterEventHandlers(Framework framework) {
        ArrayList<Listener> foundEventHandlers = EventReflection.findEventHandlers();
        eventHandlers.addAll(foundEventHandlers);
        eventHandlers.forEach(framework::addListener);
    }

    public boolean injectMembers() {
        eventHandlers.forEach(framework::injectMembers);
        return true;
    }
}
