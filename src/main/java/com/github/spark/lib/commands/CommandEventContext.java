package com.github.spark.lib.commands;

import javax.annotation.Nullable;

public record CommandEventContext(@Nullable CommandEventContext parent, String currentPath, String[] restArgs) {
}
