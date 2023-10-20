package net.azisaba.interchatmod.fabric;

import com.google.gson.Gson;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.jetbrains.annotations.Nullable;

import java.net.URI;
import java.util.TimerTask;

public class WebSocketChatClient extends WebSocketClient {
    private final Gson gson = new Gson();
    private long openAt;

    public WebSocketChatClient(URI uri) {
        super(uri);
    }

    private void sendMessage(Text text) {
        Mod.LOGGER.info("[WS] {}", text.getString());
        if (Mod.CONFIG.hideEverything()) return;
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return;
        player.sendMessage(text);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        openAt = System.currentTimeMillis();
        auth(Mod.CONFIG.apiKey());
        Mod.trySwitch();
        sendMessage(Text.literal("Connected to guild chat."));
    }

    @Override
    public void onMessage(String message) {
        JsonObject obj = gson.fromJson(message, JsonObject.class);
        if (obj.has("message") && !Mod.isInAzisaba()) {
            sendMessage(Text.literal(obj.get("message").getAsString()));
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        Mod.LOGGER.info("Disconnected from server");
        if (System.currentTimeMillis() - openAt < 1000) {
            sendMessage(Text.literal("ギルドチャットから切断されました。APIキーを確認して、/reconnectinterchat [apikey]を実行してください。"));
            return;
        }
        if (remote) {
            sendMessage(Text.literal("ギルドチャットから切断されました。15秒後に再接続を試みます。"));
            Mod.TIMER.schedule(new TimerTask() {
                @Override
                public void run() {
                    Mod.reconnect();
                }
            }, 1000 * 15);
        }
    }

    @Override
    public void onError(Exception ex) {
        Mod.LOGGER.error("WebSocket error", ex);
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

    public void selectGuild(long id) {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", "select");
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
