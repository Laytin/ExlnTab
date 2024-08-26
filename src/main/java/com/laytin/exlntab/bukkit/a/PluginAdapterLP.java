package com.laytin.exlntab.bukkit.a;

import com.laytin.exlntab.bukkit.IPluginAdapter;
import com.laytin.exlntab.bukkit.PluginInjector;

import java.util.Map;

public class PluginAdapterLP implements A {
    private static IPluginAdapter injection;
    private static boolean loading = false;

    private static boolean inject = false;
    public static void init() throws IllegalAccessException, InstantiationException {
        Class<?> clazz = PluginInjector.injectClass("LuckPerms", PluginAdapterLP.class);
        injection = (IPluginAdapter)clazz.newInstance();
    }
    public static boolean autoInject() throws InstantiationException, IllegalAccessException {
        if(inject)
            return true;
        if(injection == null && !loading) {
            loading = true;
            PluginAdapterPEX.init();
            inject = true;
            return true;
        } else if (injection == null) {
            return true;
        }
        loading = false;
        return false;
    }
    private static boolean checkInject() throws InstantiationException, IllegalAccessException {
        autoInject();
        return inject;
    }
    public static Map<String, Integer> getPlayerGroupWithWeight() {
        try {
            checkInject();
            return injection.getPlayerGroupWithWeight();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static final class Inj implements IPluginAdapter {
        @Override
        public Map<String, Integer> getPlayerGroupWithWeight() {
            return null;
        }
    }
}
