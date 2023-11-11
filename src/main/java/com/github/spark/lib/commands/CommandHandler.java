package com.github.spark.lib.commands;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandHandler {
    /**
     * This is used to find out what commands are root
     * @return
     */
    boolean root() default false;
    String name();
    String description() default "";
}
