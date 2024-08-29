package com.laytin.exlntab.bukkit.permission;

import com.laytin.exlntab.bukkit.PluginInjector;
import com.laytin.exlntab.utils.PlayerInfoObj;
import net.luckperms.api.LuckPermsProvider;

public class PluginAdapterLP {
    private static IPermPluginAdapter injection;
    private static boolean loading = false;

    private static boolean inject = false;
    public static void init() throws IllegalAccessException, InstantiationException {
        Class<?> clazz = PluginInjector.injectClass("LuckPerms", PluginAdapterLP.class);
        injection = (IPermPluginAdapter)clazz.newInstance();
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

    public static final class Inj implements IPermPluginAdapter {
        @Override
        public PlayerInfoObj getPlayerGroupWithWeight(String username) {
            PlayerInfoObj ob = new PlayerInfoObj();
            ob.setRole(LuckPermsProvider.get().getUserManager().getUser(username).getPrimaryGroup());
            ob.setRoleDisplayName(LuckPermsProvider.get().getUserManager().getUser(username).getCachedData().getMetaData().getPrefix());
            ob.setUsername(username);
            ob.setWeight(0);
            System.out.println(ob.toString());
            return ob;
        }
    }
}