package com.laytin.exlntab.handlers;

import com.laytin.exlntab.bukkit.permission.PluginAdapterLP;
import com.laytin.exlntab.bukkit.permission.PluginAdapterPEX;
import com.laytin.exlntab.bukkit.spark.SparkAdapter;
import com.laytin.exlntab.packets.AddPlayerListPacket;
import com.laytin.exlntab.packets.FillPlayerListPacket;
import com.laytin.exlntab.packets.RemovePlayerListPacket;
import com.laytin.exlntab.packets.TpsPacket;
import com.laytin.exlntab.utils.PlayerInfoObj;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayerMP;
import org.bukkit.Bukkit;

import java.util.*;

@SideOnly(Side.SERVER)
public class ListenerServer {
    private int tpsTicks;
    private Map<String, PlayerInfoObj> playerListTemp = new HashMap<>();
    private boolean inited = false;
    private boolean sparkInited = false;
    private static int abc = 0;
    private void init(){
        if(Bukkit.getServer().getPluginManager().isPluginEnabled("PermissionEX")){
            try {
                PluginAdapterPEX.init();
                System.out.println("[EXLNTab] Init PermissionEX plugin");
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            }
            abc = 1;
        }else if(Bukkit.getServer().getPluginManager().isPluginEnabled("LuckPerms")){
            try {
                PluginAdapterLP.init();
                System.out.println("[EXLNTab] Init LuckPerms plugin");
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            }
            abc = 2;
        }else {
            throw new RuntimeException("[EXLNTab] No permission plugin found. Just use PermissionEX or LuckPerms!");
        }
        if(Bukkit.getServer().getPluginManager().isPluginEnabled("spark")){
            try {
                SparkAdapter.init();
                System.out.println("[EXLNTab] Init Spark plugin");
                sparkInited =true;
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            }
        }
        inited=true;
    }

    @SubscribeEvent
    public void onJoinEvent(PlayerEvent.PlayerLoggedInEvent event) {
        if(!inited)
            init();
        PlayerInfoObj ib;
        switch (abc) {
            case 1:
                ib = PluginAdapterPEX.getPlayerGroupWithWeight(event.player.getDisplayName());
                break;
            case 2:
                ib = PluginAdapterLP.getPlayerGroupWithWeight(event.player.getDisplayName());
                break;
            default:
                ib = new PlayerInfoObj("default", "Player", event.player.getDisplayName(), 0);
                break;
        }
        playerListTemp.put(ib.getUsername(),ib);
        new FillPlayerListPacket(new ArrayList<>(playerListTemp.values())).sendToPlayer((EntityPlayerMP) event.player);
        new AddPlayerListPacket(ib).sendToClients();
        if(sparkInited)
            new TpsPacket(SparkAdapter.getTps()).sendToPlayer((EntityPlayerMP) event.player);
    }
    @SubscribeEvent
    public void tpsTickEvent(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            if (this.tpsTicks > 3600) {
                if(!inited)
                    init();
                else if(sparkInited)
                    new TpsPacket(SparkAdapter.getTps()).sendToClients();
                this.tpsTicks = 0;
            }
            this.tpsTicks++;
        }
    }
    @SubscribeEvent
    public void onLeftEvent(PlayerEvent.PlayerLoggedOutEvent event) {
        playerListTemp.remove(event.player.getDisplayName());
        new RemovePlayerListPacket(event.player.getDisplayName()).sendToClients();
    }
}
