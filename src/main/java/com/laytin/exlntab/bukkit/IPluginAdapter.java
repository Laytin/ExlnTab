package com.laytin.exlntab.bukkit;

import com.laytin.exlntab.utils.PlayerInfoObj;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.Map;

@SideOnly(Side.SERVER)
public interface IPluginAdapter {
    PlayerInfoObj getPlayerGroupWithWeight(String username);
}
