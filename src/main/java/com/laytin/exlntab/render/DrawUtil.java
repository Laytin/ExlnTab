package com.laytin.exlntab.render;

import com.laytin.exlntab.render.font.FontMagicObj;
import com.laytin.exlntab.render.font.FontStyles;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.awt.*;
import java.util.HashMap;

import static net.minecraftforge.client.IItemRenderer.ItemRenderType.INVENTORY;
import static net.minecraftforge.client.IItemRenderer.ItemRendererHelper.INVENTORY_BLOCK;

public class DrawUtil {
    public static HashMap<String, ResourceLocation> externalTextures = new HashMap<>();
    private static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");
    private static RenderBlocks renderBlocksRi = new RenderBlocks();

    public static void drawString(FontMagicObj fontContainer, String string, float x, float y, float scale, int color) {
        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, 0.0F);
        GL11.glScalef(scale, scale, 1.0F);
        fontContainer.drawString(string, 0.0F, 0.0F, color);
        GL11.glPopMatrix();
    }
    public static void drawStringWithDepth(FontMagicObj fontContainer, String string, float x, float y, float scale, int color) {
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, 0.0F);
        GL11.glScalef(scale, scale, 1.0F);
        fontContainer.drawStringWithShadow(string, 0.0F, 0.0F, color);
        GL11.glPopMatrix();
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        RenderHelper.enableStandardItemLighting();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
    }
    public static void drawCenterStringWithDepth(FontMagicObj fontContainer, String string, float x, float y, float scale, int color) {
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glPushMatrix();
        GL11.glTranslatef(x - fontContainer.width(string) * scale / 2.0F, y, 0.0F);
        GL11.glScalef(scale, scale, 1.0F);
        fontContainer.drawStringWithShadow(string,0,0 , color);
        GL11.glPopMatrix();
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        RenderHelper.enableStandardItemLighting();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
    }
    public static void drawCenteredString(FontStyles fontType, String string, float x, float y, float scale, int color) {
        FontMagicObj fontContainer = fontType.getFontContainer();
        drawString(fontContainer, string, x - fontContainer.width(string) * scale / 2.0F, y, scale, color);
    }
    public static void drawString(FontStyles fontType, String string, float x, float y, float scale, int color) {
        drawString(fontType.getFontContainer(), string, x, y, scale, color);
    }
    public static void drawStringWithAplha(FontStyles fontType, String string, float x, float y, float scale, int color,float alpha) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, alpha);
        drawString(fontType.getFontContainer(), string, x, y, scale, color);
    }

    public static Sprite2D drawImage(float x, float y, float width, float height, ResourceLocation resourceLocation) {
        return drawImage(x, y, width, height, resourceLocation, 1.0F);
    }

    public static Sprite2D drawImage(float x, float y, float width, float height, ResourceLocation resourceLocation, float alpha) {
        Tessellator tessellator = Tessellator.instance;
        Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation);
        RenderHelper.disableStandardItemLighting();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, alpha);
        GL11.glDisable(3008);
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x, (y + height), 0.0D, 0.0D, 1.0D);
        tessellator.addVertexWithUV((x + width), (y + height), 0.0D, 1.0D, 1.0D);
        tessellator.addVertexWithUV((x + width), y, 0.0D, 1.0D, 0.0D);
        tessellator.addVertexWithUV(x, y, 0.0D, 0.0D, 0.0D);
        tessellator.draw();
        GL11.glEnable(3008);
        GL11.glDisable(3042);
        return new Sprite2D(x, y, width, height);
    }

    public static ResourceLocation getExternalTexture(String id, String fileUrl) {
        if (externalTextures.containsKey(id))
            return externalTextures.get(id);
        ResourceLocation keyToTexture = new ResourceLocation("id" + id);
        TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
        texturemanager.loadTexture(keyToTexture, (ITextureObject)new ThreadDownloadImageData(null, fileUrl, null, null));
        ITextureObject object = texturemanager.getTexture(keyToTexture);
        externalTextures.put(id, keyToTexture);
        return keyToTexture;
    }
    static double zLevel = 50.0;
    static RenderBlocks field_147909_c = new RenderBlocks();

    public static void renderItemIntoGUI(TextureManager textureManager, ItemStack itemStack, float posX, float posY, boolean renderEffect, float width, float height, boolean renderStackSize) {
        try {
            if (!forgeHookRender(field_147909_c, textureManager, itemStack, true, (float) zLevel,posX, posY,width,height))
                renderItemIntoGUI1(textureManager,itemStack,posX,posY,renderEffect,width,height,renderStackSize);
        }catch (NullPointerException e){
            System.out.println("null while render");
        }
        if (false && itemStack.hasEffect()) {
            GL11.glDepthFunc(GL11.GL_EQUAL);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDepthMask(false);
            textureManager.bindTexture(RES_ITEM_GLINT);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glColor4f(0.5F, 0.25F, 0.8F, 1.0F);
            renderGlint(posX * 431278612 + posY * 32178161, posX - 2, posY - 2, 20, 20);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            GL11.glDepthMask(true);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glDepthFunc(GL11.GL_LEQUAL);
        }
    }
    public static void renderItemIntoGUI1(TextureManager textureManager, ItemStack itemStack, float posX, float posY, boolean renderEffect, float width, float height, boolean renderStackSize) {
        if(itemStack==null){

        }
        int k = itemStack.getItemDamage();
        Object object = itemStack.getIconIndex();
        int l;
        float f;
        float f3;
        float f4;
        boolean renderWithColor = true;
        if (itemStack.getItemSpriteNumber() == 0 && RenderBlocks.renderItemIn3d(Block.getBlockFromItem(itemStack.getItem()).getRenderType())) {
            textureManager.bindTexture(TextureMap.locationBlocksTexture);
            Block block = Block.getBlockFromItem(itemStack.getItem());
            GL11.glEnable(GL11.GL_ALPHA_TEST);

            if (block.getRenderBlockPass() != 0) {
                GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
                GL11.glEnable(GL11.GL_BLEND);
                OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            } else {
                GL11.glAlphaFunc(GL11.GL_GREATER, 0.5F);
                GL11.glDisable(GL11.GL_BLEND);
            }

            GL11.glPushMatrix();
            GL11.glTranslatef((float)(posX - 2), (float)(posY + 3), (float) (-3.0F + zLevel));
            GL11.glScalef(10.0F*width/16, 10.0F*height/16, 10.0F);
            GL11.glTranslatef(1.0F, 0.5F, 1.0F);
            GL11.glScalef(1.0F, 1.0F, -1.0F);
            GL11.glRotatef(210.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
            l = itemStack.getItem().getColorFromItemStack(itemStack, 0);
            f3 = (float)(l >> 16 & 255) / 255.0F;
            f4 = (float)(l >> 8 & 255) / 255.0F;
            f = (float)(l & 255) / 255.0F;

            if (renderWithColor) {
                GL11.glColor4f(f3, f4, f, 1.0F);
            }

            GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
            renderBlocksRi.useInventoryTint = renderWithColor;
            renderBlocksRi.renderBlockAsItem(block, k, 1.0F);
            renderBlocksRi.useInventoryTint = true;

            if (block.getRenderBlockPass() == 0) {
                GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
            }

            GL11.glPopMatrix();
        } else if (itemStack.getItem().requiresMultipleRenderPasses()) {
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            textureManager.bindTexture(TextureMap.locationItemsTexture);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_BLEND);
            OpenGlHelper.glBlendFunc(0, 0, 0, 0);
            GL11.glColorMask(false, false, false, true);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            Tessellator tessellator = Tessellator.instance;
            tessellator.startDrawingQuads();
            tessellator.setColorOpaque_I(-1);
            tessellator.addVertex((double)(posX - 2), (double)(posY + 18), (double)zLevel);
            tessellator.addVertex((double)(posX + 18), (double)(posY + 18), (double)zLevel);
            tessellator.addVertex((double)(posX + 18), (double)(posY - 2), (double)zLevel);
            tessellator.addVertex((double)(posX - 2), (double)(posY - 2), (double)zLevel);
            tessellator.draw();
            GL11.glColorMask(true, true, true, true);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_ALPHA_TEST);

            Item item = itemStack.getItem();
            for (l = 0; l < item.getRenderPasses(k); ++l) {
                OpenGlHelper.glBlendFunc(770, 771, 1, 0);
                textureManager.bindTexture(item.getSpriteNumber() == 0 ? TextureMap.locationBlocksTexture : TextureMap.locationItemsTexture);
                IIcon iicon = item.getIcon(itemStack, l);
                int i1 = itemStack.getItem().getColorFromItemStack(itemStack, l);
                f = (float)(i1 >> 16 & 255) / 255.0F;
                float f1 = (float)(i1 >> 8 & 255) / 255.0F;
                float f2 = (float)(i1 & 255) / 255.0F;

                if (renderWithColor) {
                    GL11.glColor4f(f, f1, f2, 1.0F);
                }

                GL11.glDisable(GL11.GL_LIGHTING); //Forge: Make sure that render states are reset, ad renderEffect can derp them up.
                GL11.glEnable(GL11.GL_ALPHA_TEST);

                renderIcon(posX, posY, iicon, width, height);

                GL11.glDisable(GL11.GL_ALPHA_TEST);
                GL11.glEnable(GL11.GL_LIGHTING);

                if (renderEffect && itemStack.hasEffect(l)) {
                    renderEffect(textureManager, posX, posY,width,height);
                }
            }

            GL11.glEnable(GL11.GL_LIGHTING);
        } else {
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_BLEND);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            ResourceLocation resourcelocation = textureManager.getResourceLocation(itemStack.getItemSpriteNumber());
            textureManager.bindTexture(resourcelocation);

            if (object == null) {
                object = ((TextureMap)Minecraft.getMinecraft().getTextureManager().getTexture(resourcelocation)).getAtlasSprite("missingno");
            }

            l = itemStack.getItem().getColorFromItemStack(itemStack, 0);
            f3 = (float)(l >> 16 & 255) / 255.0F;
            f4 = (float)(l >> 8 & 255) / 255.0F;
            f = (float)(l & 255) / 255.0F;

            if (renderWithColor) {
                GL11.glColor4f(f3, f4, f, 1.0F);
            }

            GL11.glDisable(GL11.GL_LIGHTING); //Forge: Make sure that render states are reset, a renderEffect can derp them up.
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GL11.glEnable(GL11.GL_BLEND);

            renderIcon(posX, posY, (IIcon)object, width, height);

            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glDisable(GL11.GL_BLEND);

            if (renderEffect && itemStack.hasEffect(0)) {
                renderEffect(textureManager, posX, posY,width,height);
            }
            GL11.glEnable(GL11.GL_LIGHTING);
        }

        GL11.glEnable(GL11.GL_CULL_FACE);

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        if(renderStackSize && itemStack.stackSize>1){
            GL11.glPushMatrix();
            GL11.glTranslatef((posX+12*width/16),(posY+14*width/16), 0.0F);
            GL11.glScalef(0.35F*width/16, 0.35F*width/16, 1.0F);
            FontStyles.SemiBold.getFontContainer().drawStringWithShadow(String.valueOf(itemStack.stackSize), 0.0F, 0.0F, Color.WHITE.getRGB());
            GL11.glPopMatrix();
        }
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
    }
    public static boolean forgeHookRender(RenderBlocks renderBlocks, TextureManager engine, ItemStack item, boolean inColor, float zLevel, float x, float y,float width,float height) {
        if (MinecraftForgeClient.getItemRenderer(item, INVENTORY) == null)
            return false;

        IItemRenderer customRenderer = MinecraftForgeClient.getItemRenderer(item, INVENTORY);

        engine.bindTexture(item.getItemSpriteNumber() == 0 ? TextureMap.locationBlocksTexture : TextureMap.locationItemsTexture);
        if (customRenderer.shouldUseRenderHelper(INVENTORY, item, INVENTORY_BLOCK)) {
            GL11.glPushMatrix();
            GL11.glTranslatef(x - 2, y + 3, -3.0F + zLevel);
            GL11.glScalef(10.0F*width/16, 10.0F*height/16, 10.0F);
            GL11.glTranslatef(1.0F, 0.5F, 1.0F);
            GL11.glScalef(1.0F, 1.0F, -1F);
            GL11.glRotatef(210F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(45F, 0.0F, 1.0F, 0.0F);

            if (inColor) {
                int color = item.getItem().getColorFromItemStack(item, 0);
                float r = (float)(color >> 16 & 0xff) / 255F;
                float g = (float)(color >> 8 & 0xff) / 255F;
                float b = (float)(color & 0xff) / 255F;
                GL11.glColor4f(r, g, b, 1.0F);
            }

            GL11.glRotatef(-90F, 0.0F, 1.0F, 0.0F);
            renderBlocks.useInventoryTint = inColor;
            customRenderer.renderItem(INVENTORY, item, renderBlocks);
            renderBlocks.useInventoryTint = true;
            GL11.glPopMatrix();
        } else {
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glPushMatrix();
            GL11.glTranslatef(x, y, -3.0F + zLevel);

            if (inColor) {
                int color = item.getItem().getColorFromItemStack(item, 0);
                float r = (float)(color >> 16 & 255) / 255.0F;
                float g = (float)(color >> 8 & 255) / 255.0F;
                float b = (float)(color & 255) / 255.0F;
                GL11.glColor4f(r, g, b, 1.0F);
            }

            customRenderer.renderItem(INVENTORY, item, renderBlocks);
            GL11.glPopMatrix();
            GL11.glEnable(GL11.GL_LIGHTING);
        }

        return true;
    }

    public static void renderIcon(float p_94149_1_, float p_94149_2_, IIcon p_94149_3_, float p_94149_4_, float p_94149_5_) {
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV((double)(p_94149_1_ + 0), (double)(p_94149_2_ + p_94149_5_), (double)zLevel, (double)p_94149_3_.getMinU(), (double)p_94149_3_.getMaxV());
        tessellator.addVertexWithUV((double)(p_94149_1_ + p_94149_4_), (double)(p_94149_2_ + p_94149_5_), (double)zLevel, (double)p_94149_3_.getMaxU(), (double)p_94149_3_.getMaxV());
        tessellator.addVertexWithUV((double)(p_94149_1_ + p_94149_4_), (double)(p_94149_2_ + 0), (double)zLevel, (double)p_94149_3_.getMaxU(), (double)p_94149_3_.getMinV());
        tessellator.addVertexWithUV((double)(p_94149_1_ + 0), (double)(p_94149_2_ + 0), (double)zLevel, (double)p_94149_3_.getMinU(), (double)p_94149_3_.getMinV());
        tessellator.draw();
    }
    public static void renderEffect(TextureManager manager, float x, float y,float width,float height) {
        GL11.glDepthFunc(GL11.GL_EQUAL);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDepthMask(false);
        manager.bindTexture(RES_ITEM_GLINT);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glColor4f(0.5F, 0.25F, 0.8F, 1.0F);
        renderGlint(x * 431278612 + y * 32178161, x - 2, y - 2, width, height);
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glDepthFunc(GL11.GL_LEQUAL);
    }

    private static void renderGlint(float p_77018_1_, float p_77018_2_, float p_77018_3_, float p_77018_4_, float p_77018_5_) {
        for (int j1 = 0; j1 < 2; ++j1) {
            OpenGlHelper.glBlendFunc(772, 1, 0, 0);
            float f = 0.00390625F;
            float f1 = 0.00390625F;
            float f2 = (float)(Minecraft.getSystemTime() % (long)(3000 + j1 * 1873)) / (3000.0F + (float)(j1 * 1873)) * 256.0F;
            float f3 = 0.0F;
            Tessellator tessellator = Tessellator.instance;
            float f4 = 4.0F;

            if (j1 == 1) {
                f4 = -1.0F;
            }

            tessellator.startDrawingQuads();
            tessellator.addVertexWithUV((double)(p_77018_2_ + 0), (double)(p_77018_3_ + p_77018_5_), (double)zLevel, (double)((f2 + (float)p_77018_5_ * f4) * f), (double)((f3 + (float)p_77018_5_) * f1));
            tessellator.addVertexWithUV((double)(p_77018_2_ + p_77018_4_), (double)(p_77018_3_ + p_77018_5_), (double)zLevel, (double)((f2 + (float)p_77018_4_ + (float)p_77018_5_ * f4) * f), (double)((f3 + (float)p_77018_5_) * f1));
            tessellator.addVertexWithUV((double)(p_77018_2_ + p_77018_4_), (double)(p_77018_3_ + 0), (double)zLevel, (double)((f2 + (float)p_77018_4_) * f), (double)((f3 + 0.0F) * f1));
            tessellator.addVertexWithUV((double)(p_77018_2_ + 0), (double)(p_77018_3_ + 0), (double)zLevel, (double)((f2 + 0.0F) * f), (double)((f3 + 0.0F) * f1));
            tessellator.draw();
        }
    }
    public static void drawRectFloat(float x, float y, float x2, float y2, int color, float alpha) {
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        float j1;

        if (x < x2) {
            j1 = x;
            x = x2;
            x2 = j1;
        }

        if (y < y2) {
            j1 = y;
            y = y2;
            y2 = j1;
        }

        float f = (float)(color >> 16 & 255) / 255.0F;
        float f1 = (float)(color >> 8 & 255) / 255.0F;
        float f2 = (float)(color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.instance;
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColor4f(f, f1, f2, alpha);
        tessellator.startDrawingQuads();
        tessellator.addVertex((double)x, (double)y2, 0.0D);
        tessellator.addVertex((double)x2, (double)y2, 0.0D);
        tessellator.addVertex((double)x2, (double)y, 0.0D);
        tessellator.addVertex((double)x, (double)y, 0.0D);
        tessellator.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);

        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        RenderHelper.enableStandardItemLighting();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
    }
}
