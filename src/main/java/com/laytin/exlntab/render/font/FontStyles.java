package com.laytin.exlntab.render.font;

import net.minecraft.util.ResourceLocation;

public enum FontStyles {
    OpenSans_Regular(new FontMagicObj("regular", 32, new ResourceLocation("exlnmod", "fonts/Roboto-Regular.ttf"))),
    SemiBold(new FontMagicObj("SemiBold", 52, new ResourceLocation("exlnmod", "fonts/Roboto-Bold.ttf"))),
    DefaultMinecraftFont(new FontMagicObj("minecraft", 20, null));

    private final FontMagicObj fontContainer;

    FontStyles(FontMagicObj fontContainer) {
        this.fontContainer = fontContainer;
    }

    public FontMagicObj getFontContainer() {
        return this.fontContainer;
    }
}
