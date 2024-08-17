package com.laytin.exlntab.proxy;

import com.laytin.exlntab.ExlnTab;
import com.laytin.exlntab.handlers.ConfigHandler;
import com.laytin.exlntab.handlers.ListenerClient;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy{
    public void preInit(FMLPreInitializationEvent event) {
        ConfigHandler.init(event.getSuggestedConfigurationFile());
        ExlnTab.registerListener(new ListenerClient());
    }

    public void init(FMLInitializationEvent event) {}

    public void postInit(FMLPostInitializationEvent event) {}
}
