package net.azisaba.azisabautilitymod.fabric.mixin;

import net.minecraft.client.network.ClientCommonNetworkHandler;
import net.minecraft.network.ClientConnection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ClientCommonNetworkHandler.class)
public interface MixinClientCommonNetworkHandlerAccessor {
    @Accessor
    ClientConnection getConnection();
}
