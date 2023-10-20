package net.azisaba.interchatmod.blueberry;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.azisaba.interchatmod.blueberry.commands.GuildChatCommand;
import net.azisaba.interchatmod.blueberry.commands.GuildFocusCommand;
import net.azisaba.interchatmod.blueberry.model.Guild;
import net.blueberrymc.client.commands.ClientCommandManager;
import net.blueberrymc.client.event.player.ClientLocalPlayerChatEvent;
import net.blueberrymc.client.event.render.gui.ScreenChangedEvent;
import net.blueberrymc.common.Blueberry;
import net.blueberrymc.common.bml.BlueberryMod;
import net.blueberrymc.common.bml.config.VisualConfigManager;
import net.blueberrymc.common.bml.event.EventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.server.IntegratedServer;
import org.jetbrains.annotations.NotNull;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Mod extends BlueberryMod {
    public WebSocketChatClient client;
    public Set<Guild> guilds = Collections.synchronizedSet(new HashSet<>());

    @Override
    public void onLoad() {
        if (!Blueberry.isClient()) {
            throw new RuntimeException("This mod cannot be installed on server");
        }
        ClientCommandManager.register("cgf", new GuildFocusCommand("cgf", this));
        ClientCommandManager.register("cgs", new GuildFocusCommand("cgs", this));
        ClientCommandManager.register("cg", new GuildChatCommand(this));
    }

    @Override
    public void onPreInit() {
        Blueberry.getEventManager().registerEvents(this, this);
        try {
            getConfig().reloadConfig();
        } catch (IOException e) {
            getLogger().warn("Failed to reload config", e);
        }
        VisualConfigManager.load(getConfig(), ICConfig.class);
        setVisualConfig(VisualConfigManager.createFromClass(ICConfig.class));
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
        VisualConfigManager.load(getConfig(), ICConfig.class);
        reconnect();
        return false;
    }

    public void reconnect() {
        try {
            if (client != null) {
                client.close();
            }
            URI uri;
            if (ICConfig.endpointHostOverride == null || ICConfig.endpointHostOverride.isBlank()) {
                uri = new URI("wss://api-ktor.azisaba.net/interchat/stream?server=dummy");
            } else {
                uri = new URI(ICConfig.endpointHostOverride + "/interchat/stream?server=dummy");
            }
            client = new WebSocketChatClient(uri, this, Minecraft.getInstance());
            if (uri.getScheme().startsWith("wss")) {
                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, null, null);
                SSLSocketFactory factory = sslContext.getSocketFactory();
                client.setSocketFactory(factory);
            }
            client.connect();
        } catch (Exception e) {
            getLogger().error("Failed to establish WebSocket session", e);
        }
    }

    @Override
    public void onPostInit() {
        reconnect();

        Blueberry.getUtil().getClientScheduler().runTaskTimerAsynchronously(this, 1000 * 30, 1000 * 30, () -> {
            try {
                String url = "https://api-ktor.azisaba.net/interchat/guilds/list";
                if (ICConfig.apiHostOverride != null && !ICConfig.apiHostOverride.isBlank()) {
                    url = ICConfig.apiHostOverride + "/interchat/guilds/list";
                }
                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                connection.addRequestProperty("Authorization", "Bearer " + ICConfig.apiKey);
                String response = new String(connection.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
                JsonArray arr = new Gson().fromJson(response, JsonArray.class);
                Set<Guild> localGuilds = getGuildsFromArray(arr);
                guilds.clear();
                guilds.addAll(localGuilds);
            } catch (Exception e) {
                getLogger().warn("Failed to fetch guild list", e);
            }
        });
    }

    @NotNull
    private static Set<Guild> getGuildsFromArray(JsonArray arr) {
        Set<Guild> localGuilds = new HashSet<>();
        for (JsonElement element : arr) {
            JsonObject obj = element.getAsJsonObject();
            localGuilds.add(
                    new Guild(
                            obj.get("id").getAsLong(),
                            obj.get("name").getAsString(),
                            obj.get("format").getAsString(),
                            obj.get("capacity").getAsInt(),
                            obj.get("open").getAsBoolean(),
                            obj.get("deleted").getAsBoolean()
                    )
            );
        }
        return localGuilds;
    }

    @EventHandler
    public void onScreenChanged(ScreenChangedEvent e) {
        trySwitch();
    }

    public void trySwitch() {
        if (client == null) return;
        ServerData serverData = Minecraft.getInstance().getCurrentServer();
        if (serverData != null) {
            client.switchServer(serverData.ip);
        }
        IntegratedServer singleServer = Minecraft.getInstance().getSingleplayerServer();
        if (singleServer != null) {
            client.switchServer(singleServer.getWorldData().getLevelName());
        }
    }

    @EventHandler
    public void onChat(ClientLocalPlayerChatEvent e) {
        if (!ICConfig.chatWithoutCommand) return;
        if (e.getMessage().startsWith("!")) {
            e.setMessage(e.getMessage().substring(1));
            return;
        }
        client.sendMessageToGuild(null, e.getMessage());
        e.setCancelled(true);
    }
}
