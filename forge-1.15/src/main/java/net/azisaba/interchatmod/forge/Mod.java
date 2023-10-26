package net.azisaba.interchatmod.forge;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.azisaba.interchatmod.common.util.ByteStreams;
import net.azisaba.interchatmod.forge.model.Guild;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.NotNull;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

@net.minecraftforge.fml.common.Mod("interchatmod")
public class Mod {
    public static final Timer TIMER = new Timer(true);
    public static final Set<Guild> GUILDS = Collections.synchronizedSet(new HashSet<>());
    public static WebSocketChatClient client;

    public Mod() {
        ModConfig.load();

        MinecraftForge.EVENT_BUS.register(this);

        TIMER.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    String url = "https://api-ktor.azisaba.net/interchat/guilds/list";
                    HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                    connection.addRequestProperty("Authorization", "Bearer " + ModConfig.apiKey);
                    String response = ByteStreams.readString(connection.getInputStream(), StandardCharsets.UTF_8);
                    JsonArray arr = new Gson().fromJson(response, JsonArray.class);
                    Set<Guild> localGuilds = getGuildsFromArray(arr);
                    GUILDS.clear();
                    GUILDS.addAll(localGuilds);
                } catch (Exception e) {
                    System.err.println("Failed to fetch guild list");
                    e.printStackTrace();
                }
            }
        }, 1000 * 30, 1000 * 30);

        reconnect();
    }

    public static void reconnect() {
        try {
            if (client != null) {
                client.close();
            }
            System.out.println("Attempting to connect to the server");
            // it has to be insecure url, java 8 does not have required ssl certificate,
            // and we had to disable Automatic HTTPS Rewrites on Cloudflare settings :<
            URI uri = new URI("ws://api-ktor.azisaba.net/interchat/stream?server=dummy");
            client = new WebSocketChatClient(uri);
            if (uri.getScheme().startsWith("wss")) {
                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, null, null);
                SSLSocketFactory factory = sslContext.getSocketFactory();
                client.setSocketFactory(factory);
            }
            client.connectBlocking();
        } catch (Exception e) {
            System.err.println("Failed to establish WebSocket session");
            e.printStackTrace();
        }
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

    public static boolean isInAzisaba() {
        ServerData serverData = Minecraft.getInstance().getCurrentServerData();
        if (serverData == null) return false;
        return serverData.serverIP.endsWith(".azisaba.net") || serverData.serverIP.equals("azisaba.net");
    }

    public static void trySwitch() {
        if (client == null) return;
        ServerData serverData = Minecraft.getInstance().getCurrentServerData();
        if (serverData != null) {
            client.switchServer(serverData.serverIP);
        }
        IntegratedServer singleServer = Minecraft.getInstance().getIntegratedServer();
        if (singleServer != null) {
            client.switchServer(singleServer.getActiveAnvilConverter().getName());
        }
    }

    @SubscribeEvent
    public void handleClientCommand(ClientChatEvent e) {
        if (!e.getMessage().startsWith("/")) return;
        String[] split = e.getMessage().split(" ");
        String command = split[0].substring(1);
        ClientPlayerEntity player = Minecraft.getInstance().player;
        assert player != null;
        if (command.equals("cgs") || command.equals("cg") || command.equals("cguild") || command.equals("reconnectinterchat") || command.equals("interchatconfig")) {
            try {
                player.connection.getCommandDispatcher().execute(e.getMessage().substring(1), player.getCommandSource());
            } catch (CommandSyntaxException ex) {
                player.sendMessage(new StringTextComponent(ex.getMessage()).applyTextStyles(TextFormatting.RED));
            }
            e.setMessage("");
            e.setCanceled(true);
        }
    }
}
