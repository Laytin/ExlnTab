package com.laytin.exlntab.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class ResourcesProxy {
    public static final ResourceLocation bigWindow = new ResourceLocation("exlnmod", "textures/gui/exlnmenu/bg.png");
    public static final ResourceLocation buttonHover = new ResourceLocation("exlnmod", "textures/gui/exlnmenu/button_hover.png");
    static {
        Minecraft.getMinecraft().getTextureManager().bindTexture(bigWindow);
        Minecraft.getMinecraft().getTextureManager().bindTexture(buttonHover);
    }
}
