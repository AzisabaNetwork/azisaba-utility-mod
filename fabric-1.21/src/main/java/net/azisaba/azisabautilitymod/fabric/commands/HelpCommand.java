package net.azisaba.azisabautilitymod.fabric.commands;

import net.azisaba.azisabautilitymod.fabric.CommandManager;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.NotNull;

public class HelpCommand implements Command {
    @Override
    public void execute(@NotNull ClientPlayerEntity player, @NotNull String[] args) {
        for (Command command : CommandManager.COMMANDS) {
            player.sendMessage(
                    Text.literal("")
                            .append(Text.literal(("/cgrenade " + command.getName() + " " + command.getUsage()).trim()).formatted(Formatting.AQUA))
                            .append(Text.literal(" - ").formatted(Formatting.GRAY))
                            .append(Text.literal(command.getDescription()).formatted(Formatting.LIGHT_PURPLE)),
                    false
            );
        }
    }

    @Override
    public @NotNull String getName() {
        return "help";
    }

    @Override
    public @NotNull String getDescription() {
        return "Displays the help message.";
    }
}
