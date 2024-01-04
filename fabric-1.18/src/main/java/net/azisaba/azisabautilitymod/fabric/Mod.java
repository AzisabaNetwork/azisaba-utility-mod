package net.azisaba.azisabautilitymod.fabric;

import com.terraformersmc.modmenu.api.ModMenuApi;
import net.azisaba.azisabautilitymod.common.util.ByteStreams;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Mod implements ModInitializer, ModMenuApi {
    public static final Logger LOGGER = LoggerFactory.getLogger("AzisabaUtilityMod");

    @Override
    public void onInitialize() {
        ClientCommandManager.DISPATCHER.register(Commands.builder());

        ModConfig.load();
    }

    private static @NotNull String makeRequest(String path) throws IOException {
        String url = "https://api-ktor.azisaba.net/" + path;
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.addRequestProperty("Authorization", "Bearer " + ModConfig.apiKey);
        return ByteStreams.readString(connection.getInputStream(), StandardCharsets.UTF_8);
    }
}
