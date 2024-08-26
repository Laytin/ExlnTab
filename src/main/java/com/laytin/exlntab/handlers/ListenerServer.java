package com.laytin.exlntab.handlers;

import com.laytin.exlntab.bukkit.PluginAdapterUtil;
import com.laytin.exlntab.utils.PlayerInfoObj;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.Map;

@SideOnly(Side.SERVER)
public class ListenerServer {
    private Map<String, PlayerInfoObj> playerListTemp = new HashMap<>();
    //private static PluginAdapterUtil injector = new PluginAdapterUtil();

    @SubscribeEvent
    public void onJoinEvent(PlayerEvent.PlayerLoggedInEvent event) {
        //injector.getPlayerGroupWithWeight();
        System.out.println("Logged in:"+event.player.getDisplayName());
    }
    @SubscribeEvent
    public void onLeftEvent(PlayerEvent.PlayerLoggedOutEvent event) {
        System.out.println("Logged out:"+event.player.getDisplayName());
    }
}
