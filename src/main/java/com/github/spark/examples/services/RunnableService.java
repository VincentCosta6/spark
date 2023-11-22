package com.github.spark.examples.services;

import com.github.spark.lib.framework.Framework;
import com.github.spark.lib.services.annotations.Service;
import com.google.inject.Inject;
import org.bukkit.scheduler.BukkitRunnable;

@Service
public class RunnableService {
    @Inject Framework framework;

    public void createRunnable() {
        new BukkitRunnable(){
            @Override
            public void run(){
//                framework.log("Runnable log");
            }
        }.runTaskTimer(framework.plugin, 0L, 20L);
    }
}
