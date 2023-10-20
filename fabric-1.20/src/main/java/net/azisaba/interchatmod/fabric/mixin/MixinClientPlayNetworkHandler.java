package net.azisaba.interchatmod.fabric.mixin;

import net.azisaba.interchatmod.fabric.Mod;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class MixinClientPlayNetworkHandler {
    @Inject(method = "sendChatMessage", at = @At("HEAD"), cancellable = true)
    public void maybeCancel(String content, CallbackInfo ci) {
        if (!Mod.CONFIG.chatWithoutCommand()) {
            return;
        }
        if (content.startsWith("!")) {
            if (content.length() == 1) ci.cancel();
            return;
        }
        ci.cancel();
        Mod.client.sendMessageToGuild(null, content);
    }

    @ModifyArg(method = "sendChatMessage", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/message/MessageBody;<init>(Ljava/lang/String;Ljava/time/Instant;JLnet/minecraft/network/message/LastSeenMessageList;)V"))
    public String modifyMessageBodyArg(String string) {
        return Mod.CONFIG.chatWithoutCommand() ? string.substring(1) : string;
    }

    @ModifyArg(method = "sendChatMessage", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/packet/c2s/play/ChatMessageC2SPacket;<init>(Ljava/lang/String;Ljava/time/Instant;JLnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/network/message/LastSeenMessageList$Acknowledgment;)V"))
    public String modifyPacketArg(String string) {
        return Mod.CONFIG.chatWithoutCommand() ? string.substring(1) : string;
    }
}
