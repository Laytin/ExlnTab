package com.laytin.exlntab.bukkit;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.Map;

@SideOnly(Side.SERVER)
public interface IPluginAdapter {
    Map<String,Integer> getPlayerGroupWithWeight();
}
