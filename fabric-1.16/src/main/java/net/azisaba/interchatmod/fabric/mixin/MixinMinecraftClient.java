package net.azisaba.interchatmod.fabric.mixin;

import net.azisaba.interchatmod.fabric.Mod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.TimerTask;
import java.util.logging.Level;

@Mixin(MinecraftClient.class)
public class MixinMinecraftClient {
    @Inject(method = "openScreen", at = @At("HEAD"))
    public void onSetScreen(Screen screen, CallbackInfo ci) {
        Mod.TIMER.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    Mod.trySwitch();
                } catch (Exception e) {
                    System.err.println("Failed to switch");
                    e.printStackTrace();
                }
            }
        }, 200);
    }
}
