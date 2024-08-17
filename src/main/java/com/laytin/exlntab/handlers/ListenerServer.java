package com.laytin.exlntab.handlers;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.SERVER)
public class ListenerServer {
    @SubscribeEvent
    public void onJoinEvent(PlayerEvent.PlayerLoggedInEvent event) {
        System.out.println("Joined:"+event.player.getDisplayName());
    }
}
