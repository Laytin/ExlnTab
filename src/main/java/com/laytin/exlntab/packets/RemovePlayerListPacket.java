package com.laytin.exlntab.packets;

import com.laytin.exlntab.handlers.ListenerClient;
import com.laytin.exlntab.utils.PlayerInfoObj;
import hohserg.elegant.networking.api.ElegantPacket;
import hohserg.elegant.networking.api.ServerToClientPacket;
import net.minecraft.client.Minecraft;

import java.util.Optional;

@ElegantPacket
public class RemovePlayerListPacket implements ServerToClientPacket {
    final String username;

    public RemovePlayerListPacket(String username) {
        this.username = username;
    }

    @Override
    public void onReceive(Minecraft mc) {
        ListenerClient.getInstance().playerList.remove(username);
    }
}
