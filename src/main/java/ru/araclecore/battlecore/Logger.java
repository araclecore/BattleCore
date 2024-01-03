package ru.araclecore.battlecore;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class Logger {

    public static void info(JavaPlugin instance, String message) {
        instance.getLogger().log(Level.INFO, message);
    }

    public static void warn(JavaPlugin instance, String message) {
        instance.getLogger().log(Level.WARNING, message);
    }

    public static void error(JavaPlugin instance, String message) {
        instance.getLogger().log(Level.SEVERE, message);
    }

}
