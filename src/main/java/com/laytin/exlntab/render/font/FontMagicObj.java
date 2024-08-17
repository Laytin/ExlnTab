package com.laytin.exlntab.render.font;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class FontMagicObj {
    private StringCache textFont = null;

    public boolean useCustomFont = true;

    private FontMagicObj() {}

    public FontMagicObj(String fontType, int fontSize) {
        this(fontType, fontSize, null);
    }

    public FontMagicObj(String fontType, int fontSize, ResourceLocation resLoc) {
        this.textFont = new StringCache();
        this.textFont.setDefaultFont("Arial", fontSize, true);
        this.useCustomFont = !fontType.equalsIgnoreCase("minecraft");
        try {
            if (!this.useCustomFont || fontType.isEmpty() || fontType.equalsIgnoreCase("default") || resLoc == null) {
                this.textFont.setDefaultFont(fontType, fontSize, true);
            } else {
                this.textFont.setCustomFont(resLoc, fontSize, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public float height() {
        if (this.useCustomFont)
            return this.textFont.fontHeight;
        return (Minecraft.getMinecraft()).fontRenderer.FONT_HEIGHT;
    }

    public float width(String text) {
        if (this.useCustomFont)
            return this.textFont.getStringWidth(text);
        return (Minecraft.getMinecraft()).fontRenderer.getStringWidth(text);
    }
    public FontMagicObj copy() {
        FontMagicObj font = new FontMagicObj();
        font.textFont = this.textFont;
        font.useCustomFont = this.useCustomFont;
        return font;
    }

    public float drawStringWithShadow(String text, float x, float y, int color) {
        float l;
        if (this.useCustomFont) {
            l = this.textFont.renderString(text, x + 1.0F, y + 1.0F, color, true);
            l = Math.max(l, this.textFont.renderString(text, x, y, color, false));
        } else {
            l = (Minecraft.getMinecraft()).fontRenderer.drawStringWithShadow(text, (int)x, (int)y, color);
        }
        return l;
    }

    public float drawString(String text, float x, float y, int color) {
        if (this.useCustomFont)
            return this.textFont.renderString(text, x, y, color, false);
        return (Minecraft.getMinecraft()).fontRenderer.drawStringWithShadow(text, (int)x, (int)y, color);
    }
    public float drawString(String text, float x, float y, int color,FontAlign f) {
        if (this.useCustomFont)
            return this.textFont.renderString(text, x, y, color, false);
        return (Minecraft.getMinecraft()).fontRenderer.drawStringWithShadow(text, (int)x, (int)y, color);
    }

    public String getName() {
        if (!this.useCustomFont)
            return "Minecraft";
        return this.textFont.usedFont().getFontName();
    }

    public StringCache getTextFont() {
        return this.textFont;
    }
}
