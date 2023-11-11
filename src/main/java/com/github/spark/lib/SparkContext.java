package com.github.spark.lib;

public abstract class SparkContext {
    public static String basePackage = "";

    public static void setBasePackage(String newBasePackageName) {
        SparkContext.basePackage = newBasePackageName;
    }
}
