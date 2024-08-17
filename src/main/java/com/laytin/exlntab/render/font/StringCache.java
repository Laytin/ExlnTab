package com.laytin.exlntab.render.font;

import java.awt.Font;
import java.awt.Point;
import java.awt.font.GlyphVector;
import java.lang.ref.WeakReference;
import java.text.Bidi;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.WeakHashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class StringCache {
    private static final int BASELINE_OFFSET = 7;

    private static final int UNDERLINE_OFFSET = 1;

    private static final int UNDERLINE_THICKNESS = 2;

    private static final int STRIKETHROUGH_OFFSET = -6;

    private static final int STRIKETHROUGH_THICKNESS = 2;

    private GlyphCache glyphCache;

    private int[] colorTable;

    private WeakHashMap<Key, Entry> stringCache = new WeakHashMap<>();

    private WeakHashMap<String, Key> weakRefCache = new WeakHashMap<>();

    private Key lookupKey = new Key();

    private Glyph[][] digitGlyphs = new Glyph[4][];

    private boolean digitGlyphsReady = false;

    private boolean antiAliasEnabled = false;

    private Thread mainThread;

    public float fontHeight;

    private static class Key {
        public String str;

        private Key() {
        }

        public int hashCode() {
            int code = 0, length = this.str.length();
            boolean colorCode = false;
            for (int index = 0; index < length; index++) {
                char c = this.str.charAt(index);
                if (c >= '0' && c <= '9' && !colorCode)
                    c = '0';
                code = code * 31 + c;
                colorCode = (c == '§');
            }
            return code;
        }

        public boolean equals(Object o) {
            if (o == null)
                return false;
            String other = o.toString();
            int length = this.str.length();
            if (length != other.length())
                return false;
            boolean colorCode = false;
            for (int index = 0; index < length; index++) {
                char c1 = this.str.charAt(index);
                char c2 = other.charAt(index);
                if (c1 != c2 && (c1 < '0' || c1 > '9' || c2 < '0' || c2 > '9' || colorCode))
                    return false;
                colorCode = (c1 == '§');
            }
            return true;
        }

        public String toString() {
            return this.str;
        }
    }

    private static class Entry {
        public WeakReference<Key> keyRef;

        public int advance;

        public Glyph[] glyphs;

        public ColorCode[] colors;

        public boolean specialRender;

        private Entry() {
        }
    }

    private static class ColorCode implements Comparable<Integer> {
        public static final byte UNDERLINE = 1;

        public static final byte STRIKETHROUGH = 2;

        public int stringIndex;

        public int stripIndex;

        public byte colorCode;

        public byte fontStyle;

        public byte renderStyle;

        private ColorCode() {
        }

        public int compareTo(Integer i) {
            return (this.stringIndex == i.intValue()) ? 0 : ((this.stringIndex < i.intValue()) ? -1 : 1);
        }
    }

    private static class Glyph implements Comparable<Glyph> {
        public int stringIndex;

        public GlyphCache.Entry texture;

        public float x;

        public float y;

        public float advance;

        private Glyph() {
        }

        public int compareTo(Glyph o) {
            return (this.stringIndex == o.stringIndex) ? 0 : ((this.stringIndex < o.stringIndex) ? -1 : 1);
        }
    }

    public StringCache() {
        this.mainThread = Thread.currentThread();
        this.glyphCache = new GlyphCache();
        this.colorTable = new int[32];
        for (int i = 0; i < 32; i++) {
            int j = (i >> 3 & 0x1) * 85;
            int k = (i >> 2 & 0x1) * 170 + j;
            int l = (i >> 1 & 0x1) * 170 + j;
            int i1 = (i >> 0 & 0x1) * 170 + j;
            if (i == 6)
                k += 85;
            if ((Minecraft.getMinecraft()).gameSettings.anaglyph) {
                int j1 = (k * 30 + l * 59 + i1 * 11) / 100;
                int k1 = (k * 30 + l * 70) / 100;
                int l1 = (k * 30 + i1 * 70) / 100;
                k = j1;
                l = k1;
                i1 = l1;
            }
            if (i >= 16) {
                k /= 4;
                l /= 4;
                i1 /= 4;
            }
            this.colorTable[i] = (k & 0xFF) << 16 | (l & 0xFF) << 8 | i1 & 0xFF;
        }
        cacheDightGlyphs();
    }

    public void setDefaultFont(String fontName, int fontSize, boolean antiAlias) {
        this.glyphCache.setDefaultFont(fontName, fontSize, antiAlias);
        this.antiAliasEnabled = antiAlias;
        this.weakRefCache.clear();
        this.stringCache.clear();
        cacheDightGlyphs();
        updateHeight();
    }

    public Font usedFont() {
        return this.glyphCache.getUsedFonts().get(0);
    }

    public void setCustomFont(ResourceLocation resource, int fontSize, boolean antiAlias) throws Exception {
        this.glyphCache.setCustomFont(resource, fontSize, antiAlias);
        this.antiAliasEnabled = antiAlias;
        this.weakRefCache.clear();
        this.stringCache.clear();
        cacheDightGlyphs();
        updateHeight();
    }

    public void updateHeight() {
        float height = 0.0F;
        float minY = 2.14748365E9F;
        float maxY = -2.14748365E9F;
        for (Glyph g : (cacheString("AaBbCcDdEeHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZzАаБбВвГгДдЕеЁЕЖжЗзИийЙЬы1234567890")).glyphs) {
            if (g.texture.height > height)
                height = g.texture.height;
            if (g.y < minY)
                minY = g.y;
            if (g.y > maxY)
                maxY = g.y;
        }
        this.fontHeight = (maxY + height) / 2.0F - minY / 2.0F;
    }

    private void cacheDightGlyphs() {
        this.digitGlyphsReady = false;
        this.digitGlyphs[0] = (cacheString("0123456789")).glyphs;
        this.digitGlyphs[1] = (cacheString("§l0123456789")).glyphs;
        this.digitGlyphs[2] = (cacheString("§o0123456789")).glyphs;
        this.digitGlyphs[3] = (cacheString("§l§o0123456789")).glyphs;
        this.digitGlyphsReady = true;
    }

    public float renderString(String str, float startX, float startY, int initialColor, boolean shadowFlag) {
        if (str == null)
            return 0.0F;
        if (str.isEmpty())
            return startX;
        Entry entry = cacheString(str);
        startY += 7.0F;
        if ((initialColor >> 24 & 0xFF) == 0)
            initialColor -= 16777216;
        int color = initialColor;
        int boundTextureName = 0;
        if (this.antiAliasEnabled) {
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
        }
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        if (shadowFlag) {
            tessellator.setColorRGBA(0, 0, 0, color >> 24 & 0xFF);
        } else {
            tessellator.setColorRGBA(color >> 16 & 0xFF, color >> 8 & 0xFF, color & 0xFF, color >> 24 & 0xFF);
        }
        int fontStyle = 0;
        for (int glyphIndex = 0, colorIndex = 0; glyphIndex < entry.glyphs.length; glyphIndex++) {
            while (colorIndex < entry.colors.length && (entry.glyphs[glyphIndex]).stringIndex >= (entry.colors[colorIndex]).stringIndex) {
                if (!shadowFlag)
                    color = applyColorCode((entry.colors[colorIndex]).colorCode, initialColor, shadowFlag);
                fontStyle = (entry.colors[colorIndex]).fontStyle;
                colorIndex++;
            }
            Glyph glyph = entry.glyphs[glyphIndex];
            GlyphCache.Entry texture = glyph.texture;
            float glyphX = glyph.x;
            char c = str.charAt(glyph.stringIndex);
            if (c >= '0' && c <= '9') {
                int oldWidth = texture.width;
                texture = (this.digitGlyphs[fontStyle][c - 48]).texture;
                int newWidth = texture.width;
                glyphX += (oldWidth - newWidth >> 1);
            }
            if (boundTextureName != texture.textureName) {
                tessellator.draw();
                tessellator.startDrawingQuads();
                if (shadowFlag) {
                    tessellator.setColorRGBA(0, 0, 0, color >> 24 & 0xFF);
                } else {
                    tessellator.setColorRGBA(color >> 16 & 0xFF, color >> 8 & 0xFF, color & 0xFF, color >> 24 & 0xFF);
                }
                GL11.glBindTexture(3553, texture.textureName);
                boundTextureName = texture.textureName;
            }
            float x1 = startX + glyphX / 2.0F;
            float x2 = startX + (glyphX + texture.width) / 2.0F;
            float y1 = startY + glyph.y / 2.0F;
            float y2 = startY + (glyph.y + texture.height) / 2.0F;
            tessellator.addVertexWithUV(x1, y1, 0.0D, texture.u1, texture.v1);
            tessellator.addVertexWithUV(x1, y2, 0.0D, texture.u1, texture.v2);
            tessellator.addVertexWithUV(x2, y2, 0.0D, texture.u2, texture.v2);
            tessellator.addVertexWithUV(x2, y1, 0.0D, texture.u2, texture.v1);
        }
        tessellator.draw();
        if (entry.specialRender) {
            int renderStyle = 0;
            color = initialColor;
            GL11.glDisable(3553);
            tessellator.startDrawingQuads();
            tessellator.setColorRGBA(color >> 16 & 0xFF, color >> 8 & 0xFF, color & 0xFF, color >> 24 & 0xFF);
            for (int i = 0, j = 0; i < entry.glyphs.length; i++) {
                while (j < entry.colors.length && (entry.glyphs[i]).stringIndex >= (entry.colors[j]).stringIndex) {
                    color = applyColorCode((entry.colors[j]).colorCode, initialColor, shadowFlag);
                    renderStyle = (entry.colors[j]).renderStyle;
                    j++;
                }
                Glyph glyph = entry.glyphs[i];
                float glyphSpace = glyph.advance - glyph.texture.width;
                if ((renderStyle & 0x1) != 0) {
                    float x1 = startX + (glyph.x - glyphSpace) / 2.0F;
                    float x2 = startX + (glyph.x + glyph.advance) / 2.0F;
                    float y1 = startY + 0.5F;
                    float y2 = startY + 1.5F;
                    tessellator.addVertex(x1, y1, 0.0D);
                    tessellator.addVertex(x1, y2, 0.0D);
                    tessellator.addVertex(x2, y2, 0.0D);
                    tessellator.addVertex(x2, y1, 0.0D);
                }
                if ((renderStyle & 0x2) != 0) {
                    float x1 = startX + (glyph.x - glyphSpace) / 2.0F;
                    float x2 = startX + (glyph.x + glyph.advance) / 2.0F;
                    float y1 = startY + -3.0F;
                    float y2 = startY + -2.0F;
                    tessellator.addVertex(x1, y1, 0.0D);
                    tessellator.addVertex(x1, y2, 0.0D);
                    tessellator.addVertex(x2, y2, 0.0D);
                    tessellator.addVertex(x2, y1, 0.0D);
                }
            }
            tessellator.draw();
            GL11.glEnable(3553);
        }
        return entry.advance / 2.0F + startX;
    }

    public float getStringWidth(String str) {
        if (str == null || str.isEmpty())
            return 0.0F;
        Entry entry = cacheString(str);
        return entry.advance / 2.0F;
    }

    private int sizeString(String str, float width, boolean breakAtSpaces) {
        if (str == null || str.isEmpty())
            return 0;
        width += width;
        Glyph[] glyphs = (cacheString(str)).glyphs;
        int wsIndex = -1;
        int advance = 0, index = 0;
        while (index < glyphs.length && advance <= width) {
            if (breakAtSpaces) {
                char c = str.charAt((glyphs[index]).stringIndex);
                if (c == ' ') {
                    wsIndex = index;
                } else if (c == '\n') {
                    wsIndex = index;
                    break;
                }
            }
            advance = (int) (advance + (glyphs[index]).advance);
            index++;
        }
        if (index < glyphs.length && wsIndex != -1 && wsIndex < index)
            index = wsIndex + 1;
        return (index < glyphs.length) ? (glyphs[index]).stringIndex : str.length();
    }

    public float sizeStringToWidth(String str, float width) {
        return sizeString(str, width, true);
    }

    public String trimStringToWidth(String str, float width, boolean reverse) {
        int length = sizeString(str, width, false);
        str = str.substring(0, length);
        if (reverse)
            str = (new StringBuilder(str)).reverse().toString();
        return str;
    }

    public String trimStringToWidthSaveWords(String str, float width, boolean reverse) {
        int length = sizeString(str, width, true);
        str = str.substring(0, length);
        if (reverse)
            str = (new StringBuilder(str)).reverse().toString();
        return str;
    }

    private int applyColorCode(int colorCode, int color, boolean shadowFlag) {
        if (colorCode != -1) {
            colorCode = shadowFlag ? (colorCode + 16) : colorCode;
            color = this.colorTable[colorCode] & 0xFFFFFF | color & 0xFF000000;
        }
        Tessellator.instance.setColorRGBA(color >> 16 & 0xFF, color >> 8 & 0xFF, color & 0xFF, color >> 24 & 0xFF);
        return color;
    }

    private Entry cacheString(String str) {
        Entry entry = null;
        if (this.mainThread == Thread.currentThread()) {
            this.lookupKey.str = str;
            entry = this.stringCache.get(this.lookupKey);
        }
        if (entry == null) {
            char[] text = str.toCharArray();
            entry = new Entry();
            int length = stripColorCodes(entry, str, text);
            List<Glyph> glyphList = new ArrayList<>();
            entry.advance = layoutBidiString(glyphList, text, 0, length, entry.colors);
            entry.glyphs = new Glyph[glyphList.size()];
            entry.glyphs = glyphList.<Glyph>toArray(entry.glyphs);
            Arrays.sort((Object[]) entry.glyphs);
            int colorIndex = 0, shift = 0;
            for (int glyphIndex = 0; glyphIndex < entry.glyphs.length; glyphIndex++) {
                Glyph glyph = entry.glyphs[glyphIndex];
                while (colorIndex < entry.colors.length && glyph.stringIndex + shift >= (entry.colors[colorIndex]).stringIndex) {
                    shift += 2;
                    colorIndex++;
                }
                glyph.stringIndex += shift;
            }
            if (this.mainThread == Thread.currentThread()) {
                Key key = new Key();
                key.str = new String(str);
                entry.keyRef = new WeakReference<>(key);
                this.stringCache.put(key, entry);
            }
        }
        if (this.mainThread == Thread.currentThread()) {
            Key oldKey = entry.keyRef.get();
            if (oldKey != null)
                this.weakRefCache.put(str, oldKey);
            this.lookupKey.str = null;
        }
        return entry;
    }

    private int stripColorCodes(Entry cacheEntry, String str, char[] text) {
        List<ColorCode> colorList = new ArrayList<>();
        int start = 0, shift = 0;
        byte fontStyle = 0;
        byte renderStyle = 0;
        byte colorCode = -1;
        int next;
        while ((next = str.indexOf('§', start)) != -1 && next + 1 < str.length()) {
            System.arraycopy(text, next - shift + 2, text, next - shift, text.length - next - 2);
            int code = "0123456789abcdefklmnor".indexOf(Character.toLowerCase(str.charAt(next + 1)));
            switch (code) {
                case 16:
                    break;
                case 17:
                    fontStyle = (byte) (fontStyle | 0x1);
                    break;
                case 18:
                    renderStyle = (byte) (renderStyle | 0x2);
                    cacheEntry.specialRender = true;
                    break;
                case 19:
                    renderStyle = (byte) (renderStyle | 0x1);
                    cacheEntry.specialRender = true;
                    break;
                case 20:
                    fontStyle = (byte) (fontStyle | 0x2);
                    break;
                case 21:
                    fontStyle = 0;
                    renderStyle = 0;
                    colorCode = -1;
                    break;
                default:
                    if (code >= 0 && code <= 15) {
                        colorCode = (byte) code;
                        fontStyle = 0;
                        renderStyle = 0;
                    }
                    break;
            }
            ColorCode entry = new ColorCode();
            entry.stringIndex = next;
            entry.stripIndex = next - shift;
            entry.colorCode = colorCode;
            entry.fontStyle = fontStyle;
            entry.renderStyle = renderStyle;
            colorList.add(entry);
            start = next + 2;
            shift += 2;
        }
        cacheEntry.colors = new ColorCode[colorList.size()];
        cacheEntry.colors = colorList.<ColorCode>toArray(cacheEntry.colors);
        return text.length - shift;
    }

    private int layoutBidiString(List<Glyph> glyphList, char[] text, int start, int limit, ColorCode[] colors) {
        int advance = 0;
        if (Bidi.requiresBidi(text, start, limit)) {
            Bidi bidi = new Bidi(text, start, null, 0, limit - start, -2);
            if (bidi.isRightToLeft())
                return layoutStyle(glyphList, text, start, limit, 1, advance, colors);
            int runCount = bidi.getRunCount();
            byte[] levels = new byte[runCount];
            Integer[] ranges = new Integer[runCount];
            for (int index = 0; index < runCount; index++) {
                levels[index] = (byte) bidi.getRunLevel(index);
                ranges[index] = new Integer(index);
            }
            Bidi.reorderVisually(levels, 0, (Object[]) ranges, 0, runCount);
            for (int visualIndex = 0; visualIndex < runCount; visualIndex++) {
                int logicalIndex = ranges[visualIndex].intValue();
                int layoutFlag = ((bidi.getRunLevel(logicalIndex) & 0x1) == 1) ? 1 : 0;
                advance = layoutStyle(glyphList, text, start + bidi.getRunStart(logicalIndex), start + bidi.getRunLimit(logicalIndex), layoutFlag, advance, colors);
            }
            return advance;
        }
        return layoutStyle(glyphList, text, start, limit, 0, advance, colors);
    }

    private int layoutStyle(List<Glyph> glyphList, char[] text, int start, int limit, int layoutFlags, int advance, ColorCode[] colors) {
        int currentFontStyle = 0;
        int colorIndex = Arrays.binarySearch((Object[]) colors, Integer.valueOf(start));
        if (colorIndex < 0)
            colorIndex = -colorIndex - 2;
        while (start < limit) {
            int next = limit;
            while (colorIndex >= 0 && colorIndex < colors.length - 1 && (colors[colorIndex]).stripIndex == (colors[colorIndex + 1]).stripIndex)
                colorIndex++;
            if (colorIndex >= 0 && colorIndex < colors.length)
                currentFontStyle = (colors[colorIndex]).fontStyle;
            while (++colorIndex < colors.length) {
                if ((colors[colorIndex]).fontStyle != currentFontStyle) {
                    next = (colors[colorIndex]).stripIndex;
                    break;
                }
            }
            advance = layoutString(glyphList, text, start, next, layoutFlags, advance, currentFontStyle);
            start = next;
        }
        return advance;
    }

    private int layoutString(List<Glyph> glyphList, char[] text, int start, int limit, int layoutFlags, int advance, int style) {
        if (this.digitGlyphsReady)
            for (int index = start; index < limit; index++) {
                if (text[index] >= '0' && text[index] <= '9')
                    text[index] = '0';
            }
        while (start < limit) {
            Font font = this.glyphCache.lookupFont(text, start, limit, style);
            int next = font.canDisplayUpTo(text, start, limit);
            if (next == -1)
                next = limit;
            if (next == start)
                next++;
            advance = layoutFont(glyphList, text, start, next, layoutFlags, advance, font);
            start = next;
        }
        return advance;
    }

    private int layoutFont(List<Glyph> glyphList, char[] text, int start, int limit, int layoutFlags, int advance, Font font) {
        if (this.mainThread == Thread.currentThread())
            this.glyphCache.cacheGlyphs(font, text, start, limit, layoutFlags);
        GlyphVector vector = this.glyphCache.layoutGlyphVector(font, text, start, limit, layoutFlags);
        Glyph glyph = null;
        int numGlyphs = vector.getNumGlyphs();
        for (int index = 0; index < numGlyphs; index++) {
            Point position = vector.getGlyphPixelBounds(index, null, advance, 0.0F).getLocation();
            if (glyph != null)
                glyph.advance = position.x - glyph.x;
            glyph = new Glyph();
            glyph.stringIndex = start + vector.getGlyphCharIndex(index);
            glyph.texture = this.glyphCache.lookupGlyph(font, vector.getGlyphCode(index));
            glyph.x = position.x;
            glyph.y = position.y;
            glyphList.add(glyph);
        }
        advance = (int) (advance + vector.getGlyphPosition(numGlyphs).getX());
        if (glyph != null)
            glyph.advance = advance - glyph.x;
        return advance;
    }
}
