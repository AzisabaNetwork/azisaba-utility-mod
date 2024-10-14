package net.azisaba.azisabautilitymod.fabric.commands;

import io.netty.channel.Channel;
import net.azisaba.azisabautilitymod.fabric.connection.UpdateTimePacketHandler;
import net.azisaba.azisabautilitymod.fabric.mixin.MixinClientCommonNetworkHandlerAccessor;
import net.azisaba.azisabautilitymod.fabric.mixin.MixinClientConnectionAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.ClientConnection;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ClearTPSCommand implements Command {
    @Override
    public void execute(@NotNull ClientPlayerEntity player, @NotNull String[] args) throws Exception {
        assert MinecraftClient.getInstance().player != null;
        ClientConnection clientConnection = ((MixinClientCommonNetworkHandlerAccessor) MinecraftClient.getInstance().player.networkHandler).getConnection();
        Channel channel = ((MixinClientConnectionAccessor) clientConnection).getChannel();
        UpdateTimePacketHandler updateTimePacketHandler = Objects.requireNonNull(channel, "channel").pipeline().get(UpdateTimePacketHandler.class);
        if (updateTimePacketHandler != null) {
            updateTimePacketHandler.times.clear();
            UpdateTimePacketHandler.admin.clear();
        }
    }

    @Override
    public @NotNull String getName() {
        return "clearTps";
    }

    @Override
    public @NotNull String getDescription() {
        return "Clear \"TPS\" times";
    }
}
