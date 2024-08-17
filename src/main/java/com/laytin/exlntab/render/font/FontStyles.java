package com.laytin.exlntab.render.font;

import net.minecraft.util.ResourceLocation;

public enum FontStyles {
    //OpenSans_ExtraBold(new FontMagicObj("extra", 32, new ResourceLocation("exlnmod", "fonts/Cunia.otf"))),
    OpenSans_Italic(new FontMagicObj("italic", 32, new ResourceLocation("exlnmod", "fonts/Roboto-Italic.ttf"))),
    OpenSans_Regular(new FontMagicObj("regular", 32, new ResourceLocation("exlnmod", "fonts/Roboto-Regular.ttf"))),
    //BoldItalic(new FontMagicObj("BoldItalic", 32, new ResourceLocation("exlnmod", "fonts/Cunia.otf"))),
    SemiBold(new FontMagicObj("SemiBold", 32, new ResourceLocation("exlnmod", "fonts/Roboto-Bold.ttf"))),
    //BoldItalicText(new FontMagicObj("BoldItalic", 20, new ResourceLocation("exlnmod", "fonts/Cunia.otf"))),
    DefaultMinecraftFont(new FontMagicObj("minecraft", 20, null));

    private final FontMagicObj fontContainer;

    FontStyles(FontMagicObj fontContainer) {
        this.fontContainer = fontContainer;
    }

    public FontMagicObj getFontContainer() {
        return this.fontContainer;
    }
}
