package com.laytin.exlntab.render;

import com.laytin.exlntab.utils.PhotoLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.StringUtils;
import org.lwjgl.opengl.GL11;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class PhotoRender {
    protected final Minecraft mc = Minecraft.getMinecraft();
    private ConcurrentHashMap<String, BufferedImage> cachedImages = new ConcurrentHashMap();
    private ConcurrentHashMap<String, DynamicTexture> cachedTextures = new ConcurrentHashMap();
    private List<String> fetchingSkins = Collections.synchronizedList(new ArrayList());
    private static PhotoRender instance;
    public static PhotoRender getInstance() {
        if(instance==null)
            instance=new PhotoRender();
        return instance;
    }

    private boolean bindFace(String username) {
        if (this.fetchingSkins.contains(username)) {
            if (this.cachedImages.containsKey(username)) {
                DynamicTexture faceTex = new DynamicTexture((BufferedImage)this.cachedImages.get(username));
                this.cachedTextures.put(username, faceTex);
                this.cachedImages.remove(username);
            }

            return false;
        } else if (!this.cachedTextures.containsKey(username)) {
            this.fetchingSkins.add(username);
            (new Thread(new PhotoLoader(username, this.cachedImages, this.fetchingSkins))).start();
            return false;
        } else {
            GL11.glBindTexture(3553, ((DynamicTexture)this.cachedTextures.get(username)).getGlTextureId());
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            return true;
        }
    }
    private void drawImage(int xPos, int yPos, int width, int height) {
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV((double)xPos, (double)(yPos + height), 0.0D, 0.0D, 1.0D);
        tessellator.addVertexWithUV((double)(xPos + width), (double)(yPos + height), 0.0D, 1.0D, 1.0D);
        tessellator.addVertexWithUV((double)(xPos + width), (double)yPos, 0.0D, 1.0D, 0.0D);
        tessellator.addVertexWithUV((double)xPos, (double)yPos, 0.0D, 0.0D, 0.0D);
        tessellator.draw();
    }
    public void drawAvatar(String username, int xPos, int yPos, int width, int height){
        if (bindFace(StringUtils.stripControlCodes(username))) {
/*            GL11.glPushMatrix();
            GL11.glDisable(2929);*/
            drawImage(xPos, yPos, 17, 17);
/*            GL11.glPopMatrix();
            GL11.glEnable(2929);*/
        }
    }
}
