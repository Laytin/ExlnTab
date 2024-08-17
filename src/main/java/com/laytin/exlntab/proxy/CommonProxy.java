package com.laytin.exlntab.proxy;

import com.laytin.exlntab.ExlnTab;
import com.laytin.exlntab.handlers.ConfigHandler;
import com.laytin.exlntab.handlers.ListenerServer;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {
    public void preInit(FMLPreInitializationEvent event) {
        ConfigHandler.init(event.getSuggestedConfigurationFile());
        ExlnTab.registerListener(new ListenerServer());
    }

    public void init(FMLInitializationEvent event) {}

    public void postInit(FMLPostInitializationEvent event) {}
}
