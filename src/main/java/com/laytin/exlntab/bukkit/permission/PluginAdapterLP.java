package com.laytin.exlntab.bukkit.permission;

import com.laytin.exlntab.bukkit.PluginInjector;
import com.laytin.exlntab.utils.PlayerInfoObj;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.NodeType;
import ru.justagod.cutter.GradleSide;
import ru.justagod.cutter.GradleSideOnly;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@GradleSideOnly(GradleSide.SERVER)
@SideOnly(Side.SERVER)
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
            String roleDisplay  =LuckPermsProvider.get().getUserManager().getUser(username).getCachedData().getMetaData().getPrefix();
            ob.setRoleDisplayName(roleDisplay==null? "Player" : roleDisplay);
            ob.setUsername(username);
            try{
                List<Node> nodeList = new ArrayList<>(LuckPermsProvider.get().getUserManager().getUser(username).getNodes());
                Optional<Node> nm = nodeList.stream().filter(f-> f.getKey().contains("prefix")).findFirst();
                if(nm.isPresent()){
                    String temp = nm.get().getKey().substring(nm.get().getKey().indexOf(".")+1);
                    ob.setWeight(Integer.parseInt(temp.substring(0,temp.indexOf("."))));
                }else
                    ob.setWeight(0);
            }catch (Exception e ){
                ob.setWeight(0);
            }
            return ob;
        }
    }
}
