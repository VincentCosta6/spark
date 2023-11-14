package com.github.spark.lib.events.registrry;

import com.github.spark.lib.common.Registry;
import com.github.spark.lib.framework.Framework;
import org.bukkit.event.Listener;

import java.util.ArrayList;

public class EventRegistry extends Registry<Class<Listener>, Listener> {
    public EventRegistry(Framework framework) {
        super(framework);
    }

    @Override
    public void findAndRegisterItems() {
        ArrayList<Listener> foundEventHandlers = EventReflection.findEventHandlers();
        foundEventHandlers.forEach(eventHandler -> {
            items.put((Class<Listener>) eventHandler.getClass(), eventHandler);
            framework.addListener(eventHandler);
        });
    }
}
