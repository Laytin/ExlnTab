package com.laytin.exlntab.bukkit.spark;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ru.justagod.cutter.GradleSide;
import ru.justagod.cutter.GradleSideOnly;

@SideOnly(Side.SERVER)
@GradleSideOnly(GradleSide.SERVER)
public interface ISparkAdapter {
    double getTps();
}
