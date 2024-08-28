package com.laytin.exlntab.packets;

import com.laytin.exlntab.handlers.ListenerClient;
import com.laytin.exlntab.utils.PlayerInfoObj;
import hohserg.elegant.networking.api.ClientToServerPacket;
import hohserg.elegant.networking.api.ElegantPacket;
import hohserg.elegant.networking.api.ServerToClientPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.*;
import java.util.stream.Collectors;

@ElegantPacket
public class FillPlayerListPacket implements ServerToClientPacket {
    final List<PlayerInfoObj> myList;
    public FillPlayerListPacket(List<PlayerInfoObj> myList) {
        this.myList = myList;
    }
    @Override
    public void onReceive(Minecraft mc) {
        new Thread(()->{
            List<PlayerInfoObj> ll =  myList.stream().sorted(Comparator.comparingInt(PlayerInfoObj::getWeight)).collect(Collectors.toList());
            Collections.reverse(ll);
            ListenerClient.getInstance().playerList = ll.stream().collect(Collectors.toMap(PlayerInfoObj::getUsername, playerInfoObj -> playerInfoObj));
        }).start();
    }
}
