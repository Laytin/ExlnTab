package com.laytin.exlntab.packets;

import com.laytin.exlntab.handlers.ListenerClient;
import com.laytin.exlntab.utils.PlayerInfoObj;
import hohserg.elegant.networking.api.ElegantPacket;
import hohserg.elegant.networking.api.ServerToClientPacket;
import net.minecraft.client.Minecraft;
import ru.justagod.cutter.GradleSide;
import ru.justagod.cutter.GradleSideOnly;

import java.util.*;
import java.util.stream.Collectors;

@ElegantPacket
public class AddPlayerListPacket implements ServerToClientPacket {
    final PlayerInfoObj ib;

    public AddPlayerListPacket(PlayerInfoObj ib) {
        this.ib = ib;
    }
    @Override
    public void onReceive(Minecraft mc) {
        new Thread(()->{
            if (ib.getUsername().equals(mc.getSession().getUsername()))
                return;
            Map<String, PlayerInfoObj> temp1 = new HashMap<>(ListenerClient.getInstance().playerList);
            temp1.put(ib.getUsername(),ib);
            List<PlayerInfoObj> temp2 = new ArrayList<>(temp1.values()).stream().sorted(Comparator.comparing(PlayerInfoObj::getWeight).reversed()).collect(Collectors.toList());
            temp1.clear();
            temp1 = temp2.stream().collect(Collectors.toMap(PlayerInfoObj::getUsername, playerInfoObj -> playerInfoObj));
            ListenerClient.getInstance().playerList = temp1;
        }).start();
    }
}
