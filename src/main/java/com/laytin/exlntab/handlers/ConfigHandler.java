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
    public static String skinUrl = "https://tlauncher.org/upload/all/nickname/%player%.png";
    public static String tabHeader = "Project name | Project server";
    public static boolean useGroupName;
    public static boolean useColouredBg;
    public static boolean useColorCodes;
    public static boolean drawTPS;
    public static boolean drawRoles;
    public static boolean useVanillaTeams;
    public static Map<String, String> roleColors = new HashMap<>(); // role ->colour
    static{
        roleColors.put("default", "#525252");
    }
    public static String getColourByRole(String role) {
        if(roleColors.containsKey(role))
            return roleColors.get(role);
        return "#525252";
    }

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
            avatarUrl= getString("tab", "Avatar URL", avatarUrl);
            skinUrl = getString("tab", "Skins URL", skinUrl);
            tabHeader = getString("tab", "Tab Header", tabHeader);
            ConfigCategory cc = config.getCategory("tabGroupColor");
            cc.getOrderedValues().stream().forEach(f-> roleColors.put(f.getName(), f.getString()));
            useGroupName = config.get("tabSpecific", "Use group name", useGroupName).getBoolean();
            useColorCodes = config.get("tabSpecific", "Use colour codes", useColorCodes).getBoolean();
            useColouredBg = config.get("tabSpecific", "Use colored bg", true).getBoolean();
            drawTPS = config.get("tabSpecific", "Draw TPS", drawTPS).getBoolean();
            drawRoles = config.get("tabSpecific", "Draw role", true).getBoolean();
            useVanillaTeams = config.get("tabSpecific", "Use Vanilla teams", true).getBoolean();
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