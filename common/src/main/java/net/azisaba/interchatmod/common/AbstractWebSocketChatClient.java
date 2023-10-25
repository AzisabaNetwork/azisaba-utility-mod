package net.azisaba.interchatmod.common;

import com.google.gson.Gson;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.jetbrains.annotations.Nullable;

import java.net.URI;

public abstract class AbstractWebSocketChatClient extends WebSocketClient {
    protected final LegacyComponentSerializer legacyComponentSerializer =
            LegacyComponentSerializer.builder().extractUrls().character('§').build();
    protected final Gson gson = new Gson();
    private long openAt;

    public AbstractWebSocketChatClient(URI uri) {
        super(uri);
    }

    protected final void sendMessage(String message) {
        sendMessage(legacyComponentSerializer.deserialize(message));
    }

    protected void sendMessage(Component component) {
        sendJsonMessage(GsonComponentSerializer.gson().serialize(component));
    }

    protected void sendJsonMessage(String json) {
        throw new RuntimeException("Implementation must override sendJsonMessage or sendMessage(Component) method");
    }

    protected abstract String getApiKey();

    protected abstract boolean isInAzisaba();

    protected abstract void trySwitch();

    protected abstract void scheduleReconnect();

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        openAt = System.currentTimeMillis();
        auth(getApiKey());
        trySwitch();
        sendMessage("ギルドチャットに接続しました。");
    }

    @Override
    public void onMessage(String message) {
        JsonObject obj = gson.fromJson(message, JsonObject.class);
        if (!obj.has("type")) return;
        String type = obj.get("type").getAsString();
        if (type.equals("message") && !isInAzisaba()) {
            sendMessage(obj.get("message").getAsString());
        }
        if (type.equals("component") && !isInAzisaba()) {
            sendMessage(GsonComponentSerializer.gson().deserialize(obj.get("message").getAsString()));
        }
        if (type.equals("error_message")) {
            sendMessage(Component.text("サーバーからエラーが返されました: " + obj.get("message").getAsString(), NamedTextColor.RED));
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        if (System.currentTimeMillis() - openAt < 1000) {
            sendMessage("ギルドチャットから切断されました。APIキーを確認して、/reconnectinterchat [apikey]を実行してください。");
            return;
        }
        if (remote) {
            sendMessage("ギルドチャットから切断されました。15秒後に再接続を試みます。");
            scheduleReconnect();
        }
    }

    @Override
    public void onError(Exception ex) {
        sendMessage("WebSocket接続でエラーが発生しました: " + ex.getMessage());
        ex.printStackTrace();
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

    public void invite(String player) {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", "invite");
        obj.addProperty("player", player);
        send(gson.toJson(obj));
    }

    public void respondInvite(String guildName, boolean accept) {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", "respond_invite");
        obj.addProperty("guildName", guildName);
        obj.addProperty("accept", accept);
        send(gson.toJson(obj));
    }

    public void nick(@Nullable String nickname) {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", "nick");
        obj.addProperty("nickname", nickname);
        send(gson.toJson(obj));
    }
}
