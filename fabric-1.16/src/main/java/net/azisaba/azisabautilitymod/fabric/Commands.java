package net.azisaba.azisabautilitymod.fabric;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;

import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.literal;

public class Commands {
    public static LiteralArgumentBuilder<FabricClientCommandSource> builder() {
        return literal("aziutil");
    }
}
