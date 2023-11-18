package com.github.spark.examples.services;

import com.github.spark.examples.datastores.PlayerState;
import com.github.spark.lib.framework.Framework;
import com.github.spark.lib.observables.annotations.ObserveMutation;
import com.github.spark.lib.services.annotations.Service;
import com.google.inject.Inject;

@Service
public class ScoreboardService {
    @Inject Framework framework;

    @ObserveMutation(type=PlayerState.class)
    public void onMutation(PlayerState state) {
        System.out.println(state.toString());
    }
}
