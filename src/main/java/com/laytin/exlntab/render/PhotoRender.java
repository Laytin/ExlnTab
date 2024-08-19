package com.laytin.exlntab.render;

import com.laytin.exlntab.render.elem.GifAnim;
import com.laytin.exlntab.utils.PhotoLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import org.lwjgl.opengl.GL11;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class PhotoRender {
    protected final Minecraft mc = Minecraft.getMinecraft();
    private ConcurrentHashMap<String, BufferedImage> skinImages = new ConcurrentHashMap();
    private ConcurrentHashMap<String, ResourceLocation> skinImagesStream = new ConcurrentHashMap();
    private ConcurrentHashMap<String, DynamicTexture> finalImages = new ConcurrentHashMap();
    private ConcurrentHashMap<String, GifAnim> finalImagesGif = new ConcurrentHashMap();
    private List<String> startUpload = Collections.synchronizedList(new ArrayList());
    private List<String> endUpload = Collections.synchronizedList(new ArrayList());
    private static PhotoRender instance;
    public static PhotoRender getInstance() {
        if(instance==null)
            instance=new PhotoRender();
        return instance;
    }
    private void drawImage(float xPos, float yPos, float width, float height) {
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV((double)xPos, (double)(yPos + height), 0.0D, 0.0D, 1.0D);
        tessellator.addVertexWithUV((double)(xPos + width), (double)(yPos + height), 0.0D, 1.0D, 1.0D);
        tessellator.addVertexWithUV((double)(xPos + width), (double)yPos, 0.0D, 1.0D, 0.0D);
        tessellator.addVertexWithUV((double)xPos, (double)yPos, 0.0D, 0.0D, 0.0D);
        tessellator.draw();
    }
    public void drawAvatar(String username, float xPos, float yPos, float width, float height,float alpha){
        username = StringUtils.stripControlCodes(username);
        if(finalImages.containsKey(username)) {
            GL11.glPushMatrix();
            GL11.glDisable(2929);
            GL11.glBindTexture(3553, (this.finalImages.get(username)).getGlTextureId());
            GL11.glColor4f(1.0F, 1.0F, 1.0F, alpha);
            drawImage(xPos, yPos, width, height);
            GL11.glPopMatrix();
            GL11.glEnable(2929);
        }else if (finalImagesGif.containsKey(username)){
            finalImagesGif.get(username).gifPlay(xPos,yPos,width,height,1);
            System.out.println("Try paint gif");
        } else if(endUpload.contains(username)){
            if(skinImages.containsKey(username))
                finalImages.put(username,new DynamicTexture(skinImages.get(username)));
            if(skinImagesStream.containsKey(username))
                finalImagesGif.put(username,new GifAnim(skinImagesStream.get(username)));
        } else if (!startUpload.contains(username)) {
            startUpload.add(username);
            (new Thread(new PhotoLoader(username, this.skinImages, this.endUpload))).start();
        }
    }
}
