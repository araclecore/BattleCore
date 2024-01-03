package ru.araclecore.battlecore;

import org.bukkit.plugin.java.JavaPlugin;
import ru.araclecore.battlecore.Configuration.Configuration;

public final class BattleCore extends JavaPlugin {
    public static Configuration settings;
    public static BattleCore instance;
    public static String token;
    public static String URL;
    public static String server;

    @Override
    public void onEnable() {
        instance = this;
        settings = new Configuration(instance, "settings.yml", null, null);
        token = settings.String("Token");
        URL = settings.String("URL");
        server = settings.String("Server");
        getServer().getMessenger().registerOutgoingPluginChannel(this, "battlecore:main");

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
