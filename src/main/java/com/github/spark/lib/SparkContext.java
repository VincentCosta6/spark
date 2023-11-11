package com.github.spark.lib;

public class SparkContext {
    public static String basePackage = "com.github.spark.examples";

    public static void setBasePackage(String newBasePackageName) {
        SparkContext.basePackage = newBasePackageName;
    }
}
