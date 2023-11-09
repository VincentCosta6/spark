package com.github.spark.lib.command_trees;

import java.util.ArrayList;

public record CommandTreeExecutionContext(String currentPath, String[] restPaths) {
}
