package net.azisaba.azisabautilitymod.fabric.mixin;

import net.azisaba.azisabautilitymod.fabric.connection.UpdateTimePacketHandler;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerListHud.class)
public class MixinPlayerListHud {
    @Inject(at = @At("RETURN"), method = "getPlayerName")
    public void rewriteComponent(PlayerListEntry entry, CallbackInfoReturnable<Text> cir) {
        String s = UpdateTimePacketHandler.admin.get(entry.getProfile().getId());
        if (s == null || s.isBlank()) return;
        var component = cir.getReturnValue();
        if (!(component instanceof MutableText text)) return;
        text.append(" §8<" + s + "§8>");
    }
}
