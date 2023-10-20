package net.azisaba.interchatmod.blueberry.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.azisaba.interchatmod.blueberry.Mod;
import net.blueberrymc.client.commands.ClientCommandHandler;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import org.jetbrains.annotations.NotNull;

public class GuildChatCommand implements ClientCommandHandler {
    private final Mod mod;

    public GuildChatCommand(Mod mod) {
        this.mod = mod;
    }

    @Override
    public void register(@NotNull CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register(
                Commands.literal("cg")
                        .then(Commands.argument("message", StringArgumentType.greedyString())
                                .executes(ctx -> executeChat(StringArgumentType.getString(ctx, "message")))
                        )
        );
    }

    private int executeChat(String message) {
        mod.client.sendMessageToGuild(null, message);
        return 1;
    }
}
