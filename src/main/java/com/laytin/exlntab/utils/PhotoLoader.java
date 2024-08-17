package com.laytin.exlntab.utils;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import javax.imageio.ImageIO;

import com.laytin.exlntab.handlers.ConfigHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;

public class PhotoLoader implements Runnable {
    private final String username;
    private ConcurrentHashMap<String, BufferedImage> cachedImages;
    private List<String> fetchingSkins;

    public PhotoLoader(String username, ConcurrentHashMap<String, BufferedImage> cachedImages, List<String> fetchingSkins) {
        this.username = username;
        this.cachedImages = cachedImages;
        this.fetchingSkins = fetchingSkins;
    }

    public void run() {
        BufferedImage skinImage = null;
        try {
            skinImage = ImageIO.read(new URL(ConfigHandler.skinUrl.replace("%player%", this.username)));
        } catch (IOException var5) {
            try {
                skinImage = ImageIO.read(Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("textures/entity/steve.png")).getInputStream());
            } catch (IOException var4) {
                var5.printStackTrace();
            }
        }
        this.cachedImages.put(this.username, this.faceFromSkin(skinImage));
        this.fetchingSkins.remove(this.username);
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
}

