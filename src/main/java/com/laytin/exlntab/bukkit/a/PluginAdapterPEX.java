package com.laytin.exlntab.bukkit.a;

import com.laytin.exlntab.bukkit.IPluginAdapter;
import com.laytin.exlntab.bukkit.PluginInjector;
import com.laytin.exlntab.utils.PlayerInfoObj;

public class PluginAdapterPEX {
    private static IPluginAdapter injection;
    private static boolean loading = false;

    private static boolean inject = false;
    public static void init() throws IllegalAccessException, InstantiationException {
        Class<?> clazz = PluginInjector.injectClass("PermissionEX", PluginAdapterPEX.class);
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
    public static PlayerInfoObj getPlayerGroupWithWeight(String username) {
        try {
            checkInject();
            return injection.getPlayerGroupWithWeight(username);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static final class Inj implements IPluginAdapter {
        @Override
        public PlayerInfoObj getPlayerGroupWithWeight(String username) {
            return null;
        }
    }
}
