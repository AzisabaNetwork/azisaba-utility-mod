package net.azisaba.interchatmod.blueberry;

import net.azisaba.interchatmod.common.AbstractWebSocketChatClient;
import net.blueberrymc.common.Blueberry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;

import java.net.URI;

public class WebSocketChatClient extends AbstractWebSocketChatClient {
    private final Mod mod;
    private final Minecraft minecraft;

    public WebSocketChatClient(URI uri, Mod mod, Minecraft minecraft) {
        super(uri);
        this.mod = mod;
        this.minecraft = minecraft;
    }

    @Override
    protected void sendJsonMessage(String json) {
        sendMessage(Component.Serializer.fromJson(json));
    }

    private void sendMessage(Component component) {
        if (component == null) return;
        mod.getLogger().info("[WS] {}", component.getString());
        if (ICConfig.hideEverything) return;
        LocalPlayer player = minecraft.player;
        if (player == null) return;
        player.sendSystemMessage(component);
    }

    @Override
    protected String getApiKey() {
        return ICConfig.apiKey;
    }

    @Override
    protected boolean isInAzisaba() {
        return Mod.isInAzisaba();
    }

    @Override
    protected void trySwitch() {
        mod.trySwitch();
    }

    @Override
    protected void scheduleReconnect() {
        Blueberry.getUtil().getClientScheduler().runTaskLaterAsynchronously(mod, 1000 * 5, mod::reconnect);
        Blueberry.getUtil().getClientScheduler().runTaskLaterAsynchronously(mod, 1000 * 15, () -> {
            if (!mod.client.isOpen()) {
                mod.reconnect();
            }
        });
        Blueberry.getUtil().getClientScheduler().runTaskLaterAsynchronously(mod, 1000 * 30, () -> {
            if (!mod.client.isOpen()) {
                mod.reconnect();
            }
        });
    }

    @Override
    public void onError(Exception ex) {
        mod.getLogger().error("WebSocket error", ex);
    }
}
