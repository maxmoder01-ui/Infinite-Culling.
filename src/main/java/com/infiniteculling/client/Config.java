package com.infiniteculling.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Config {

    private static final Gson GSON =
            new GsonBuilder().setPrettyPrinting().create();

    private static final Path FILE =
            FabricLoader.getInstance()
                    .getConfigDir()
                    .resolve("infinite-culling.json");

    public boolean showHud = true;
    public boolean showFps = true;
    public boolean showMemory = true;

    public static Config load() {
        if (Files.exists(FILE)) {
            try {
                Config config = GSON.fromJson(
                        Files.readString(FILE),
                        Config.class
                );

                if (config != null) {
                    return config;
                }
            } catch (Exception e) {
                System.err.println(
                        "[Infinite Culling.] Config error: "
                                + e.getMessage()
                );
            }
        }

        Config config = new Config();
        config.save();
        return config;
    }

    public void save() {
        try {
            Files.createDirectories(FILE.getParent());
            Files.writeString(FILE, GSON.toJson(this));
        } catch (IOException e) {
            System.err.println(
                    "[Infinite Culling.] Could not save config."
            );
        }
    }
            }
