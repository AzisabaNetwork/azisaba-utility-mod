package net.azisaba.interchatmod.blueberry;

import com.google.gson.Gson;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.blueberrymc.common.Blueberry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.jetbrains.annotations.Nullable;

import java.net.URI;

public class WebSocketChatClient extends WebSocketClient {
    private final Gson gson = new Gson();
    private final Mod mod;
    private final Minecraft minecraft;
    private long openAt;

    public WebSocketChatClient(URI uri, Mod mod, Minecraft minecraft) {
        super(uri);
        this.mod = mod;
        this.minecraft = minecraft;
    }

    private void sendMessage(Component component) {
        mod.getLogger().info("[WS] {}", component.getString());
        if (ICConfig.hideEverything) return;
        LocalPlayer player = minecraft.player;
        if (player == null) return;
        player.sendSystemMessage(component);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        openAt = System.currentTimeMillis();
        auth(ICConfig.apiKey);
        mod.trySwitch();
        sendMessage(Component.literal("Connected to guild chat."));
    }

    @Override
    public void onMessage(String message) {
        JsonObject obj = gson.fromJson(message, JsonObject.class);
        if (obj.has("message")) {
            sendMessage(Component.literal(obj.get("message").getAsString()));
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        mod.getLogger().info("Disconnected from server");
        if (System.currentTimeMillis() - openAt < 1000) {
            sendMessage(Component.literal("Disconnected from guild chat. Please check credentials and try reloading."));
            return;
        }
        if (remote) {
            sendMessage(Component.literal("Disconnected from guild chat, reconnecting after 15 seconds"));
            Blueberry.getUtil().getClientScheduler().runTaskLaterAsynchronously(mod, 1000 * 15, mod::reconnect);
        }
    }

    @Override
    public void onError(Exception ex) {
        mod.getLogger().error("WebSocket error", ex);
    }

    public void auth(String key) {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", "auth");
        obj.addProperty("key", key);
        send(gson.toJson(obj));
    }

    public void switchServer(String server) {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", "switch_server");
        obj.addProperty("server", server);
        send(gson.toJson(obj));
    }

    public void focusGuild(long id) {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", "focus");
        obj.addProperty("guildId", id);
        send(gson.toJson(obj));
    }

    public void sendMessageToGuild(@Nullable Long guildId, String message) {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", "message");
        obj.add("guildId", guildId == null ? JsonNull.INSTANCE : new JsonPrimitive(guildId));
        obj.addProperty("message", message);
        send(gson.toJson(obj));
    }
}
