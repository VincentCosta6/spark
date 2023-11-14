package com.github.spark.examples.services;

import com.github.spark.examples.datastores.PlayerStateDataStore;
import com.github.spark.lib.framework.Framework;
import com.github.spark.lib.services.Service;
import com.google.inject.Inject;

@Service
public class CustomService {
    @Inject Framework framework;
    @Inject PlayerStateDataStore playerStore;

    int test;

    public CustomService(int test) {
        this.test = test;
    }

    public void printTestAndPlayerStateCount() {
        framework.log("hello! " + this.test);
        framework.log("PlayerStates: " + playerStore.size());
    }
}
