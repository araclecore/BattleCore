package ru.araclecore.battlecore.Configuration;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;
import ru.araclecore.battlecore.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class Configuration {
    private final JavaPlugin instance;
    private final String filename;
    private final String token;
    private final String URL;
    private FileConfiguration configuration;

    public Configuration(JavaPlugin instance, String filename, @Nullable String token, @Nullable String URL) {
        this.instance = instance;
        this.token = token;
        this.URL = URL;
        this.filename = filename;
        if (token == null || URL == null) create(filename);
        else load();
    }

    private void load() {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(this.URL + this.filename)).GET()
                .setHeader("Authorization", "token %s".formatted(token))
                .build();

        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException exception) {
            Logger.error(instance, exception.getMessage());
        }

        if (response == null) return;

        try {
            File file = new File(filename);
            this.configuration = YamlConfiguration.loadConfiguration(file);
            configuration.loadFromString(response.body());
        } catch (InvalidConfigurationException exception) {
            Logger.error(instance, exception.getMessage());
        }
    }

    private void create(String filename) {
        File file = new File(instance.getDataFolder(), filename);
        this.configuration = YamlConfiguration.loadConfiguration(file);
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                instance.saveResource(filename, true);
                configuration.load(file);
            } catch (IOException | InvalidConfigurationException exception) {
                Logger.error(instance, exception.getMessage());
            }
        }
    }

    public FileConfiguration configuration() {
        return configuration;
    }

    public Integer Integer(String path) {
        if (configuration == null) {
            Logger.warn(instance, "No data was found. Path: " + path);
            return 0;
        }
        return configuration.getInt(path);
    }

    public boolean Boolean(String path) {
        if (configuration == null) {
            Logger.warn(instance, "No data was found. Path: " + path);
            return false;
        }
        return configuration.getBoolean(path);
    }

    public String String(String path) {
        if (configuration == null) {
            Logger.warn(instance, "No data was found. Path: " + path);
            return "none";
        }
        return configuration.getString(path);
    }

    public List<String> Strings(String path) {
        if (configuration == null) {
            Logger.warn(instance, "No data was found. Path: " + path);
            return new ArrayList<>();
        }
        return configuration.getStringList(path);
    }


}
