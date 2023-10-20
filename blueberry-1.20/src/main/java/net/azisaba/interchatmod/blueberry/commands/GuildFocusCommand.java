package net.azisaba.interchatmod.blueberry.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.azisaba.interchatmod.blueberry.Mod;
import net.azisaba.interchatmod.blueberry.model.Guild;
import net.blueberrymc.client.commands.ClientCommandHandler;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class GuildFocusCommand implements ClientCommandHandler {
    private final String name;
    private final Mod mod;

    public GuildFocusCommand(String name, Mod mod) {
        this.name = name;
        this.mod = mod;
    }

    @Override
    public void register(@NotNull CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register(
                Commands.literal(name)
                        .then(Commands.argument("guild", StringArgumentType.word())
                                .suggests((ctx, builder) -> SharedSuggestionProvider.suggest(mod.guilds.stream().map(Guild::name), builder))
                                .executes(ctx -> executeFocus(ctx.getSource(), StringArgumentType.getString(ctx, "guild")))
                                .then(Commands.argument("message", StringArgumentType.greedyString())
                                        .executes(ctx -> executeChat(ctx.getSource(), StringArgumentType.getString(ctx, "guild"), StringArgumentType.getString(ctx, "message")))
                                )
                        )
        );
    }

    private int executeFocus(CommandSourceStack source, String guildName) {
        Guild guild = mod.guilds.stream().filter(g -> g.name().equalsIgnoreCase(guildName)).findAny().orElse(null);
        if (guild == null) {
            source.sendFailure(Component.literal("そんなぎるどないよ " + guildName));
            return 0;
        }
        mod.client.selectGuild(guild.id());
        source.sendSystemMessage(Component.literal(guild.name() + " にちゃっとするようにしたよ(/cg <めっせーじ>でできるよ)"));
        return 1;
    }

    private int executeChat(CommandSourceStack source, String guildName, String message) {
        Guild guild = mod.guilds.stream().filter(g -> g.name().equalsIgnoreCase(guildName)).findAny().orElse(null);
        if (guild == null) {
            source.sendFailure(Component.literal("そんなぎるどないよ " + guildName));
            return 0;
        }
        mod.client.sendMessageToGuild(guild.id(), message);
        return 1;
    }
}
