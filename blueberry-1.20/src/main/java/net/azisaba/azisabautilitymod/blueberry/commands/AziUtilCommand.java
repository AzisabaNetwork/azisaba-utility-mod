package net.azisaba.azisabautilitymod.blueberry.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.azisaba.azisabautilitymod.blueberry.Mod;
import net.blueberrymc.client.commands.ClientCommandHandler;
import net.minecraft.commands.CommandSourceStack;
import org.jetbrains.annotations.NotNull;

import static net.minecraft.commands.Commands.literal;

public class AziUtilCommand implements ClientCommandHandler {
    private final Mod mod;

    public AziUtilCommand(Mod mod) {
        this.mod = mod;
    }

    @Override
    public void register(@NotNull CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register(
                literal("aziutil")
        );
    }
}
