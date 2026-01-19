package net.azisaba.azisabautilitymod.fabric.mixin;

import io.netty.channel.Channel;
import net.azisaba.azisabautilitymod.fabric.connection.UpdateTimePacketHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.DebugHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.network.ClientConnection;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

@Mixin(DebugHud.class)
public class MixinDebugHud {
    @Unique
    private static final DecimalFormat TPS_FORMAT = new DecimalFormat("00.00");
    @Unique
    private long azisaba$tpsLastUpdated = 0;
    @Unique
    private String azisaba$tps = "";
    @Shadow @Final private MinecraftClient client;

    @ModifyArg(index = 1, at = @At(value = "INVOKE", ordinal = 0, target = "Lnet/minecraft/client/gui/hud/DebugHud;drawText(Lnet/minecraft/client/gui/DrawContext;Ljava/util/List;Z)V"), method = "render")
    protected List<String> drawText(List<String> text) {
        if (client.player == null) return text;
        ClientConnection clientConnection = ((MixinClientCommonNetworkHandlerAccessor) client.player.networkHandler).getConnection();
        Channel channel = ((MixinClientConnectionAccessor) clientConnection).getChannel();
        UpdateTimePacketHandler updateTimePacketHandler = channel.pipeline().get(UpdateTimePacketHandler.class);
        if (updateTimePacketHandler != null && System.currentTimeMillis() - azisaba$tpsLastUpdated > 500) {
            azisaba$tpsLastUpdated = System.currentTimeMillis();
            double tps5s = 20.0 / (updateTimePacketHandler.getAverageMsPerSecond(5000) / 20.0 / 50.0);
            double tps10s = 20.0 / (updateTimePacketHandler.getAverageMsPerSecond(10000) / 20.0 / 50.0);
            double tps15s = 20.0 / (updateTimePacketHandler.getAverageMsPerSecond(15000) / 20.0 / 50.0);
            double tps30s = 20.0 / (updateTimePacketHandler.getAverageMsPerSecond(30000) / 20.0 / 50.0);
            double tps60s = 20.0 / (updateTimePacketHandler.getAverageMsPerSecond(60000) / 20.0 / 50.0);
            double tps180s = 20.0 / (updateTimePacketHandler.getAverageMsPerSecond(180000) / 20.0 / 50.0);
            azisaba$tps = String.format(
                    Locale.ROOT,
                    "§dTPS§f(§a5s§f/§a10s§f/§a15s§f/§a30s§f/§a1m§f/§a3m§f): %s%s§f, %s%s§f, %s%s§f, %s%s§f, %s%s§f, %s%s",
                    azisaba$tpsColor(tps5s),
                    azisaba$formatDouble(tps5s),
                    azisaba$tpsColor(tps10s),
                    azisaba$formatDouble(tps10s),
                    azisaba$tpsColor(tps15s),
                    azisaba$formatDouble(tps15s),
                    azisaba$tpsColor(tps30s),
                    azisaba$formatDouble(tps30s),
                    azisaba$tpsColor(tps60s),
                    azisaba$formatDouble(tps60s),
                    azisaba$tpsColor(tps180s),
                    azisaba$formatDouble(tps180s)
            );
        }
        text.add(azisaba$tps);
        PlayerListEntry playerInfo = client.player.networkHandler.getPlayerListEntry(client.player.getUuid());
        if (playerInfo != null) {
            int latency = playerInfo.getLatency();
            text.add("§dPing§f: " + azisaba$pingColor(latency) + latency + " §fms");
        }
        return text;
    }

    @Unique
    private static String azisaba$formatDouble(double d) {
        return TPS_FORMAT.format(d);
    }

    @Unique
    private static @NotNull String azisaba$tpsColor(double d) {
        if (d > 20.1) {
            return "§b";
        } else if (d >= 19) {
            return "§a";
        } else if (d >= 17) {
            return "§e";
        } else if (d >= 14) {
            return "§c";
        } else {
            return "§4";
        }
    }

    @Unique
    private static @NotNull String azisaba$pingColor(int i) {
        if (i > 150) {
            return "§c";
        } else if (i >= 100) {
            return "§e";
        } else if (i >= 50) {
            return "§d";
        } else {
            return "§a";
        }
    }
}
