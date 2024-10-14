package net.azisaba.azisabautilitymod.fabric;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.azisaba.azisabautilitymod.fabric.commands.Command;
import net.azisaba.azisabautilitymod.fabric.commands.HelpCommand;
import net.azisaba.azisabautilitymod.fabric.util.AUtil;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.command.CommandSource;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Objects;
import java.util.stream.Stream;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;

public class Commands {
    public static LiteralArgumentBuilder<FabricClientCommandSource> builder() {
        return literal("aziutil")
                .executes(context -> {
                    new HelpCommand().execute(Objects.requireNonNull(MinecraftClient.getInstance().player), new String[0]);
                    return 0;
                })
                .then(argument("command", StringArgumentType.word())
                        .suggests((context, builder) -> CommandSource.suggestMatching(CommandManager.COMMANDS.stream().map(Command::getName), builder))
                        .executes(context -> execute(StringArgumentType.getString(context, "command"), ""))
                        .then(argument("args", StringArgumentType.greedyString())
                                .suggests((context, builder) -> CommandSource.suggestMatching(suggest(StringArgumentType.getString(context, "command"), builder.getRemaining()), builder))
                                .executes(context -> execute(StringArgumentType.getString(context, "command"), StringArgumentType.getString(context, "args")))
                        )
                );
    }

    @SuppressWarnings("SameReturnValue")
    public static int execute(String commandName, String input) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return 0;
        try {
            String[] args = input.isEmpty() ? new String[0] : input.split(" ");
            Command command = CommandManager.getCommand(commandName);
            if (command == null) {
                player.sendMessage(Text.literal("Unknown command: " + commandName));
                return 0;
            }
            command.execute(player, args);
        } catch (Exception e) {
            MutableText text = Text.literal("An internal error occurred while executing command: " + e.getMessage()).formatted(Formatting.RED);
            Style style = text.getStyle();
            style = style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, AUtil.stacktraceComponent(e)));
            text.setStyle(style);
            player.sendMessage(text);
            e.printStackTrace();
        }
        return 0;
    }

    public static Stream<String> suggest(String commandName, String input) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return Stream.empty();
        try {
            String[] args = input.isEmpty() ? new String[0] : input.split(" ");
            Command command = CommandManager.getCommand(commandName);
            if (command != null) {
                return command.getSuggestions(player, args);
            }
        } catch (Exception e) {
            MutableText text = Text.literal("An internal error occurred while suggesting command arguments: " + e.getMessage()).formatted(Formatting.RED);
            Style style = text.getStyle();
            style = style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, AUtil.stacktraceComponent(e)));
            text.setStyle(style);
            player.sendMessage(text);
            e.printStackTrace();
        }
        return Stream.empty();
    }
}
