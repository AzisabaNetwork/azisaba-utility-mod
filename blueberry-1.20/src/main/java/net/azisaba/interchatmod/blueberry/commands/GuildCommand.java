package net.azisaba.interchatmod.blueberry.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.azisaba.interchatmod.blueberry.Mod;
import net.blueberrymc.client.commands.ClientCommandHandler;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import org.jetbrains.annotations.NotNull;

public class GuildCommand implements ClientCommandHandler {
    private final Mod mod;

    public GuildCommand(Mod mod) {
        this.mod = mod;
    }

    @Override
    public void register(@NotNull CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register(
                Commands.literal("cguild")
                        .then(Commands.literal("invite")
                                .then(Commands.argument("player", StringArgumentType.word())
                                        .executes(ctx -> executeInvite(StringArgumentType.getString(ctx, "player")))
                                )
                        )
                        .then(Commands.literal("accept")
                                .then(Commands.argument("guild", StringArgumentType.word())
                                        .executes(ctx -> executeRespondInvite(StringArgumentType.getString(ctx, "guild"), true))
                                )
                        )
                        .then(Commands.literal("reject")
                                .then(Commands.argument("guild", StringArgumentType.word())
                                        .executes(ctx -> executeRespondInvite(StringArgumentType.getString(ctx, "guild"), false))
                                )
                        )
                        .then(Commands.literal("nick")
                                .executes(ctx -> executeNick(null))
                                .then(Commands.argument("nickname", StringArgumentType.greedyString())
                                        .executes(ctx -> executeNick(StringArgumentType.getString(ctx, "nickname")))
                                )
                        )
        );
    }

    private int executeInvite(String player) {
        mod.client.invite(player);
        return 1;
    }

    private int executeRespondInvite(String guildName, boolean accept) {
        mod.client.respondInvite(guildName, accept);
        return 1;
    }

    private int executeNick(String nickname) {
        mod.client.nick(nickname);
        return 1;
    }
}
