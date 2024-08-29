package com.laytin.exlntab.bukkit.spark;

import com.laytin.exlntab.bukkit.PluginInjector;
import com.laytin.exlntab.bukkit.permission.IPermPluginAdapter;
import com.laytin.exlntab.bukkit.permission.PluginAdapterLP;
import com.laytin.exlntab.bukkit.permission.PluginAdapterPEX;
import com.laytin.exlntab.utils.PlayerInfoObj;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import me.lucko.spark.api.SparkProvider;
import me.lucko.spark.api.statistic.StatisticWindow;
import net.luckperms.api.LuckPermsProvider;
@SideOnly(Side.SERVER)
public class SparkAdapter {
    private static ISparkAdapter injection;
    private static boolean loading = false;

    private static boolean inject = false;
    public static void init() throws IllegalAccessException, InstantiationException {
        Class<?> clazz = PluginInjector.injectClass("spark", SparkAdapter.class);
        injection = (ISparkAdapter)clazz.newInstance();
    }
    public static boolean autoInject() throws InstantiationException, IllegalAccessException {
        if(inject)
            return true;
        if(injection == null && !loading) {
            loading = true;
            PluginAdapterPEX.init();
            inject = true;
            return true;
        } else if (injection == null) {
            return true;
        }
        loading = false;
        return false;
    }
    private static boolean checkInject() throws InstantiationException, IllegalAccessException {
        autoInject();
        return inject;
    }
    public static double getTps(){
        try {
            checkInject();
            return injection.getTps();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static final class Inj implements ISparkAdapter {
        @Override
        public double getTps() {
            return SparkProvider.get().tps().poll(StatisticWindow.TicksPerSecond.MINUTES_5);
        }
    }
}
