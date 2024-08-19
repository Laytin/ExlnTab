package com.laytin.exlntab.proxy;

import com.laytin.exlntab.ExlnTab;
import com.laytin.exlntab.handlers.ConfigHandler;
import com.laytin.exlntab.handlers.ListenerClient;
import com.laytin.exlntab.render.font.FontMagicObj;
import com.laytin.exlntab.render.font.FontStyles;
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
        //init font containers to prevent 1fps lag
        FontStyles.SemiBold.getFontContainer();
        FontStyles.OpenSans_Italic.getFontContainer();
        FontStyles.OpenSans_Regular.getFontContainer();
    }

    public void init(FMLInitializationEvent event) {}

    public void postInit(FMLPostInitializationEvent event) {}
}
