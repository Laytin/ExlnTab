package com.laytin.exlntab.handlers;

import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigHandler {
    public static Configuration config;
    public static String avatarUrl = "";
    public static String skinUrl = "";
    public static String tabHeader = "";
    public static boolean useGroupName;
    public static boolean useGroupPrefix;
    public static Map<String, String> roleColors = new HashMap<>(); // role ->
    public ConfigHandler() {
    }
    public static void init(File confFile) {
        if (config == null) {
            config = new Configuration(confFile);
            config.load();
            syncConfig();
        }

    }

    public static void syncConfig() {
        try {
            avatarUrl= getString("tab", "Avatar URL", skinUrl);
            skinUrl = getString("tab", "Skins URL", skinUrl);
            tabHeader = getString("tab", "Tab Header", tabHeader);
            ConfigCategory cc = config.getCategory("tabGroupColor");
            cc.getOrderedValues().stream().forEach(System.out::println);
        } catch (Exception var4) {
            System.out.println("Unable to load Config");
            var4.printStackTrace();
        } finally {
            if (config.hasChanged()) {
                config.save();
            }

        }

    }

    private static String getString(String category, String propertyName, String defaultValue) {
        return config.get(category, propertyName, defaultValue).getString();
    }

    private static int getInt(String category, String propertyName, int defaultValue) {
        return config.get(category, propertyName, defaultValue).getInt(defaultValue);
    }
}