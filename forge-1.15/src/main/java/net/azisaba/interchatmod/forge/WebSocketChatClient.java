package net.azisaba.interchatmod.forge;

import net.azisaba.interchatmod.common.AbstractWebSocketChatClient;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.util.text.ITextComponent;

import java.net.URI;
import java.util.TimerTask;

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
        sendMessage(
                ITextComponent.Serializer.fromJson(
                        GsonComponentSerializer.colorDownsamplingGson().serialize(
                                GsonComponentSerializer.gson().deserialize(json)
                        )
                )
        );
    }

    private void sendMessage(ITextComponent text) {
        if (text == null) return;
        System.out.println("[WS] " + text.getString());
        if (ModConfig.hideEverything) return;
        ClientPlayerEntity player = Minecraft.getInstance().player;
        if (player == null) return;
        player.sendMessage(text);
    }

    @Override
    public void onError(Exception ex) {
        System.err.println("WebSocket error");
        ex.printStackTrace();
    }
}
