package com.laytin.exlntab.handlers;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class ConfigHandler {
    public static Configuration config;
    public static int playersInARow = 5;
    public static String skinUrl = "https://tlauncher.org/upload/all/nickname/%player%.png";
    public static String tabHeader = "&6&lTab Header";
    public static String tabFooter = "&6&lPlayers online: %online%&7&l/&6&l%maxplayers%";

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
            playersInARow = getInt("tab", "Players in a row", playersInARow);
            skinUrl = getString("tab", "Skins URL, %player% will be replace with player's name", skinUrl);
            tabHeader = getString("tab", "Tab Header, you can use such variables as %online%, %maxplayers%", tabHeader);
            tabFooter = getString("tab", "Tab Footer, you can use such variables as %online%, %maxplayers%", tabFooter);
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