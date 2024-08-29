package com.laytin.exlntab.bukkit.permission;

import com.laytin.exlntab.utils.PlayerInfoObj;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.SERVER)
public interface IPermPluginAdapter {
    PlayerInfoObj getPlayerGroupWithWeight(String username);
}
