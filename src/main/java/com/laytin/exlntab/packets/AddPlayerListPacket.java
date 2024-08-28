package com.laytin.exlntab.packets;

import com.laytin.exlntab.handlers.ListenerClient;
import com.laytin.exlntab.utils.PlayerInfoObj;
import hohserg.elegant.networking.api.ElegantPacket;
import hohserg.elegant.networking.api.ServerToClientPacket;
import net.minecraft.client.Minecraft;

import java.util.*;

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
            List<PlayerInfoObj> temp2 = new ArrayList<>(temp1.values());
            boolean key = false;
            for (int i = 0; i < temp2.size() + 1; i++) {
                if (!key) {
                    if (temp2.size() == i) //out of range
                        temp1.put(ib.getUsername(), ib);

                    if (temp2.get(i).getWeight() <= ib.getWeight()) {
                        key = true;
                        temp1.put(ib.getUsername(), ib);
                    }else{
                        PlayerInfoObj bb = temp2.get(i);
                        temp1.put(bb.getUsername(), bb);
                    }
                } else {
                    PlayerInfoObj bb = temp2.get(i - 1);
                    temp1.put(bb.getUsername(), bb);
                }
            }
            ListenerClient.getInstance().playerList = temp1;
        }).start();
    }
}
