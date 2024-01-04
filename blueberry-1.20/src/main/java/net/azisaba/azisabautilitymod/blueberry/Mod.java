package net.azisaba.azisabautilitymod.blueberry;

import net.azisaba.azisabautilitymod.blueberry.commands.AziUtilCommand;
import net.blueberrymc.client.commands.ClientCommandManager;
import net.blueberrymc.common.Blueberry;
import net.blueberrymc.common.bml.BlueberryMod;
import net.blueberrymc.common.bml.config.VisualConfigManager;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Mod extends BlueberryMod {
    @Override
    public void onLoad() {
        if (!Blueberry.isClient()) {
            throw new RuntimeException("This mod cannot be installed on server");
        }
        ClientCommandManager.register("aziutil", new AziUtilCommand(this));
    }

    @Override
    public void onPreInit() {
        Blueberry.getEventManager().registerEvents(this, this);
        try {
            getConfig().reloadConfig();
        } catch (IOException e) {
            getLogger().warn("Failed to reload config", e);
        }
        VisualConfigManager.load(getConfig(), AUCConfig.class);
        setVisualConfig(VisualConfigManager.createFromClass(AUCConfig.class));
        getVisualConfig().onSave(vc -> {
            VisualConfigManager.save(getConfig(), vc);
            try {
                getConfig().saveConfig();
            } catch (IOException e) {
                getLogger().warn("Failed to save config", e);
            }
            onReload();
        });
    }

    @Override
    public boolean onReload() {
        try {
            getConfig().reloadConfig();
        } catch (IOException e) {
            getLogger().warn("Failed to reload config", e);
        }
        VisualConfigManager.load(getConfig(), AUCConfig.class);
        return false;
    }

    @Contract("_ -> new")
    private static @NotNull String makeRequest(String path) throws IOException {
        String url = "https://api-ktor.azisaba.net/" + path;
        if (AUCConfig.apiHostOverride != null && !AUCConfig.apiHostOverride.isBlank()) {
            url = AUCConfig.apiHostOverride + "/" + path;
        }
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.addRequestProperty("Authorization", "Bearer " + AUCConfig.apiKey);
        return new String(connection.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
    }
}
