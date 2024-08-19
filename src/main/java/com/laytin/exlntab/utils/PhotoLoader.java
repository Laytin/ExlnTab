package com.laytin.exlntab.utils;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import com.laytin.exlntab.handlers.ConfigHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

public class PhotoLoader implements Runnable {
    private final String username;
    private ConcurrentHashMap<String, BufferedImage> skinImages;
    private List<String> endUpload;

    public PhotoLoader(String username, ConcurrentHashMap<String, BufferedImage> skinImages, List<String> endUpload) {
        this.username = username;
        this.skinImages = skinImages;
        this.endUpload = endUpload;
    }
    public void run() {
        BufferedImage skinImage = null;
        try {
            skinImage = ImageIO.read(new URL(ConfigHandler.avatarUrl.replace("%player%", this.username)));
            skinImages.put(username, skinImage);
            endUpload.add(username);
        } catch (IOException e) {
        }
        //if avatar ruins
        if(!endUpload.contains(username)){
            try {
                skinImage = ImageIO.read(new URL(ConfigHandler.skinUrl.replace("%player%", this.username)));
            } catch (IOException var4) {
                try {
                    skinImage = ImageIO.read(Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("textures/entity/steve.png")).getInputStream());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            skinImages.put(username,this.faceFromSkin(skinImage));
            endUpload.add(username);
        }
    }
    public ResourceLocation getExternalTexture(String id, String fileUrl) {
        ResourceLocation keyToTexture = new ResourceLocation(id);
        TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
        texturemanager.loadTexture(keyToTexture, (ITextureObject)new ThreadDownloadImageData(null, fileUrl, null, null));
        ITextureObject object = texturemanager.getTexture(keyToTexture);
        return keyToTexture;
    }
    private BufferedImage faceFromSkin(BufferedImage skinImage) {
        int faceSize = 8;
        int overlaySize = 40;
        if (skinImage.getHeight() == 512) {
            faceSize = 128;
            overlaySize = 640;
        }

        BufferedImage merged = new BufferedImage(faceSize, faceSize, 2);
        BufferedImage face = skinImage.getSubimage(faceSize, faceSize, faceSize, faceSize);
        BufferedImage overlay = skinImage.getSubimage(overlaySize, faceSize, faceSize, faceSize);
        Graphics g = merged.getGraphics();
        g.drawImage(face, 0, 0, (ImageObserver)null);
        g.drawImage(overlay, 0, 0, (ImageObserver)null);
        return merged;
    }
    public static String getImageFormat(ImageInputStream iis) throws IOException {
        Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);
        if (!readers.hasNext()) {
            return null;
        }
        ImageReader reader = readers.next();
        return reader.getFormatName();
    }
}

