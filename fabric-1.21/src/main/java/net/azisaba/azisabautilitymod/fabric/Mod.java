package net.azisaba.azisabautilitymod.fabric;

import com.google.gson.Gson;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

public class Mod implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("AzisabaUtilityMod");
    public static final net.azisaba.azisabautilitymod.fabric.ModConfig CONFIG = net.azisaba.azisabautilitymod.fabric.ModConfig.createAndLoad();

    @Override
    public void onInitialize() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(Commands.builder());
        });
    }

    public static String makeRequest(String path) throws IOException {
        String url = "https://api-ktor.azisaba.net/" + path;
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.addRequestProperty("Authorization", "Bearer " + CONFIG.apiKey());
        return new String(connection.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
    }

    public static <T> T requestGson(@NotNull String url, @NotNull Class<T> clazz) {
        try {
            URLConnection connection = new URI(url).toURL().openConnection();
            connection.setRequestProperty("Authorization", "Bearer " + CONFIG.apiKey());
            connection.connect();
            String s = new String(connection.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            return new Gson().fromJson(s, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
