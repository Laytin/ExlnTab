package com.laytin.exlntab.bukkit;

import com.google.common.io.ByteStreams;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.io.InputStream;
import java.lang.reflect.Method;

@SideOnly(Side.SERVER)
public class PluginInjector {
    private static final Method defineClass;

    public static Class<?> injectClass(String pluginName, Class<?> clazz) {
        Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);
        if (plugin == null) {
            return null;
        } else {
            try {
                InputStream in = clazz.getClassLoader().getResourceAsStream(clazz.getName().replace('.', '/') + "$Inj.class");
                Throwable var4 = null;

                Class var6;
                try {
                    byte[] bytes = ByteStreams.toByteArray(in);
                    var6 = (Class)defineClass.invoke(plugin.getClass().getClassLoader(), null, bytes, 0, bytes.length);
                } catch (Throwable var16) {
                    var4 = var16;
                    throw var16;
                } finally {
                    if (in != null) {
                        if (var4 != null) {
                            try {
                                in.close();
                            } catch (Throwable var15) {
                                var4.addSuppressed(var15);
                            }
                        } else {
                            in.close();
                        }
                    }

                }

                return var6;
            } catch (Throwable var18) {
                var18.printStackTrace();
                return null;
            }
        }
    }

    static {
        try {
            defineClass = ClassLoader.class.getDeclaredMethod("defineClass", String.class, byte[].class, Integer.TYPE, Integer.TYPE);
            defineClass.setAccessible(true);
        } catch (Throwable var1) {
            throw new RuntimeException("Failed hooking ClassLoader.defineClass(String, byte[], int, int) method!", var1);
        }
    }
}
