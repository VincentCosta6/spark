package com.github.spark.lib.common;

public class SparkContext {
    public static String basePackage = "com.github.spark.examples";
    public static Integer saveIntervalSeconds = null;
    public static DatabaseProperties databaseProperties;

    public static void setBasePackage(String newBasePackageName) {
        SparkContext.basePackage = newBasePackageName;
    }
    public static void setSaveInterval(Integer newSaveInterval) {
        SparkContext.saveIntervalSeconds = newSaveInterval;
    }
}
