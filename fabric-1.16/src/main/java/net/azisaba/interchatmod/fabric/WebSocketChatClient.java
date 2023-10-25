package net.azisaba.interchatmod.fabric;

import net.azisaba.interchatmod.common.AbstractWebSocketChatClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;

import java.net.URI;
import java.util.TimerTask;
import java.util.logging.Level;

public class WebSocketChatClient extends AbstractWebSocketChatClient {
    public WebSocketChatClient(URI uri) {
        super(uri);
    }

    @Override
    protected String getApiKey() {
        return ModConfig.apiKey;
    }

    @Override
    protected boolean isInAzisaba() {
        return Mod.isInAzisaba();
    }

    @Override
    protected void trySwitch() {
        Mod.trySwitch();
    }

    @Override
    protected void scheduleReconnect() {
        Mod.TIMER.schedule(new TimerTask() {
            @Override
            public void run() {
                Mod.reconnect();
            }
        }, 1000 * 15);
    }

    @Override
    protected void sendJsonMessage(String json) {
        sendMessage(Text.Serializer.fromJson(json));
    }

    private void sendMessage(Text text) {
        if (text == null) return;
        System.out.println("[WS] " + text.getString());
        if (ModConfig.hideEverything) return;
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return;
        player.sendMessage(text, false);
    }

    @Override
    public void onError(Exception ex) {
        System.err.println("WebSocket error");
        ex.printStackTrace();
    }
}
