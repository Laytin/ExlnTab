package com.laytin.exlntab;

import com.laytin.exlntab.proxy.CommonProxy;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.SidedProxy;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

@Mod(modid = ExlnTab.MODID, version = ExlnTab.VERSION, name = ExlnTab.NAME)
public class ExlnTab {
    public static final String MODID = "exlntab";
    public static final String NAME = "ExlnTab";
    public static final String VERSION = "0.1.6";
    @SidedProxy(clientSide = "com.laytin.exlntab.proxy.ClientProxy", serverSide = "com.laytin.exlntab.proxy.CommonProxy")
    public static CommonProxy proxy;
    public static final Logger logger = LogManager.getLogger("ExlnTab");
    @Mod.EventHandler
    public void load(FMLInitializationEvent event) {
    }

    @Mod.EventHandler
    public void serverLoad(FMLServerStartingEvent event) {
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }
    public static void registerListener(Object listenerHandler) {
        FMLCommonHandler.instance().bus().register(listenerHandler);
        MinecraftForge.EVENT_BUS.register(listenerHandler);
    }
}
