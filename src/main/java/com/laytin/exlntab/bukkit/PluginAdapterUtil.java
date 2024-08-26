package com.laytin.exlntab.bukkit;

import com.laytin.exlntab.bukkit.a.A;
import com.laytin.exlntab.bukkit.a.PluginAdapterLP;
import com.laytin.exlntab.bukkit.a.PluginAdapterPEX;
import org.bukkit.Bukkit;

import java.util.Map;

public class PluginAdapterUtil implements IPluginAdapter{
    private static A adapter;
    public PluginAdapterUtil() {
        initAdapter();
    }

    private void initAdapter(){
        if(Bukkit.getServer().getPluginManager().isPluginEnabled("PermissionEX")){
            adapter = new PluginAdapterPEX();
            try {
                PluginAdapterPEX.init();
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            }
        }else if(Bukkit.getServer().getPluginManager().isPluginEnabled("LuckPerms")){
            adapter = new PluginAdapterLP();
            try {
                PluginAdapterLP.init();
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            }
        }else {
            throw new RuntimeException("[EXLNTab] No permission plugin found. Just use PermissionEX or LuckPerms!");
        }

    }
    private boolean checkfirst(){
        if(adapter==null)
            throw new RuntimeException("[EXLNTab] No permission plugin found. Just use PermissionEX or LuckPerms!");
        return true;
    }

    @Override
    public Map<String, Integer> getPlayerGroupWithWeight() {
        checkfirst();
        if(adapter instanceof PluginAdapterPEX)
            return PluginAdapterPEX.getPlayerGroupWithWeight();
        return PluginAdapterLP.getPlayerGroupWithWeight();
    }
}
