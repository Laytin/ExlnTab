package com.laytin.exlntab.bukkit.spark;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.SERVER)
public interface ISparkAdapter {
    double getTps();
}
