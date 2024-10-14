package net.azisaba.azisabautilitymod.fabric.mixin;

import io.netty.channel.ChannelPipeline;
import net.azisaba.azisabautilitymod.fabric.connection.UpdateTimePacketHandler;
import net.minecraft.network.ClientConnection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConnection.class)
public abstract class MixinClientConnection {
    @Inject(at = @At("TAIL"), method = "addFlowControlHandler")
    public void addHandlers(ChannelPipeline pipeline, CallbackInfo ci) {
        pipeline.addBefore("packet_handler", "azisabautilitymod_time_handler", new UpdateTimePacketHandler());
    }
}
