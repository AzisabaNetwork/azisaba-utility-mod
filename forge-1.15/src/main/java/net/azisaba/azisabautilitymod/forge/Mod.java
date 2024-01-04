package net.azisaba.azisabautilitymod.forge;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.azisaba.azisabautilitymod.common.util.ByteStreams;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@net.minecraftforge.fml.common.Mod("azisabautilitymod")
public class Mod {
    public Mod() {
        ModConfig.load();

        MinecraftForge.EVENT_BUS.register(this);
    }

    private static @NotNull String makeRequest(String path) throws IOException {
        String url = "https://api-ktor.azisaba.net/" + path;
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.addRequestProperty("Authorization", "Bearer " + ModConfig.apiKey);
        return ByteStreams.readString(connection.getInputStream(), StandardCharsets.UTF_8);
    }

    @SubscribeEvent
    public void handleClientCommand(ClientChatEvent e) {
        if (!e.getMessage().startsWith("/")) return;
        String[] split = e.getMessage().split(" ");
        String command = split[0].substring(1);
        ClientPlayerEntity player = Minecraft.getInstance().player;
        assert player != null;
        if (command.equals("aziutil")) {
            try {
                player.connection.getCommandDispatcher().execute(e.getMessage().substring(1), player.getCommandSource());
            } catch (CommandSyntaxException ex) {
                player.sendMessage(new StringTextComponent(ex.getMessage()).applyTextStyles(TextFormatting.RED));
            }
            e.setMessage("");
            e.setCanceled(true);
        }
    }
}
