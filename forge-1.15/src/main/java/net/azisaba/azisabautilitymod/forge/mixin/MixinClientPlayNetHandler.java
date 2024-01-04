package net.azisaba.azisabautilitymod.forge.mixin;

import com.mojang.brigadier.CommandDispatcher;
import net.azisaba.azisabautilitymod.forge.Commands;
import net.minecraft.client.network.play.ClientPlayNetHandler;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.network.play.server.SCommandListPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetHandler.class)
public class MixinClientPlayNetHandler {
    @Shadow private CommandDispatcher<ISuggestionProvider> commandDispatcher;

    @Inject(method = "handleCommandList", at = @At("TAIL"))
    public void handleCommandList(SCommandListPacket packetIn, CallbackInfo ci) {
        this.commandDispatcher.getRoot().addChild(Commands.builder().build());
    }
}
