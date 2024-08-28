package com.laytin.exlntab.bukkit;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;

@SideOnly(Side.SERVER)
public class BukkitConverter {
    private static final Method getBukkitEntity;
    public static final Entity toBukkitEntity(Entity entity) throws Exception {
        return (Entity)getBukkitEntity.invoke(entity, new Object[0]);
    }

    public static final Player toBukkitEntity(EntityPlayer player) throws Exception {
        return (Player)getBukkitEntity.invoke(player, new Object[0]);
    }
    public static final World toBukkitWorld(World world) {
        return (World) Bukkit.getWorld(world.getWorldInfo().getWorldName());
    }
    static {
        try {
            getBukkitEntity = Entity.class.getDeclaredMethod("getBukkitEntity", new Class[0]);
            getBukkitEntity.setAccessible(true);
        } catch (Throwable throwable) {
            throw new RuntimeException("Failed hooking CraftBukkit methods!", throwable);
        }
    }
}
