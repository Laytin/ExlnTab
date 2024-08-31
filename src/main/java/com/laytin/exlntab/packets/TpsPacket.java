package com.laytin.exlntab.packets;

import com.laytin.exlntab.handlers.ListenerClient;
import hohserg.elegant.networking.api.ElegantPacket;
import hohserg.elegant.networking.api.ServerToClientPacket;
import net.minecraft.client.Minecraft;

import java.text.DecimalFormat;

@ElegantPacket
public class TpsPacket implements ServerToClientPacket {
    final double tps;

    public TpsPacket(double tps) {
        this.tps = tps;
    }

    @Override
    public void onReceive(Minecraft mc) {
        DecimalFormat df = new DecimalFormat("##,00");
        ListenerClient.tps = df.format(tps);
    }
}
