package com.laytin.exlntab.bukkit.permission;

import com.laytin.exlntab.bukkit.PluginInjector;
import com.laytin.exlntab.utils.PlayerInfoObj;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ru.justagod.cutter.GradleSide;
import ru.justagod.cutter.GradleSideOnly;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

@SideOnly(Side.SERVER)
@GradleSideOnly(GradleSide.SERVER)
public class PluginAdapterPEX {
    private static IPermPluginAdapter injection;
    private static boolean loading = false;

    private static boolean inject = false;
    public static void init() throws IllegalAccessException, InstantiationException {
        Class<?> clazz = PluginInjector.injectClass("PermissionsEx", PluginAdapterPEX.class);
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
            ob.setUsername(username);
            PermissionUser user = PermissionsEx.getUser(username);
            ob.setRole(user.getParentIdentifiers(null).get(0));
            ob.setRoleDisplayName(user.getPrefix(null));
            ob.setWeight(user.getRank(ob.getRole()));
            return ob;
        }
    }
}
