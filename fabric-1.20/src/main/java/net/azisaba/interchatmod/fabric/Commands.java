package net.azisaba.interchatmod.fabric;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.azisaba.interchatmod.fabric.model.Guild;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.command.CommandSource;
import net.minecraft.text.Text;

public class Commands {
    public static LiteralArgumentBuilder<FabricClientCommandSource> builderGS() {
        return ClientCommandManager.literal("cgs")
                .then(ClientCommandManager.argument("guild", StringArgumentType.word())
                        .suggests((ctx, builder) -> CommandSource.suggestMatching(Mod.GUILDS.stream().map(Guild::name), builder))
                        .executes(ctx -> executeFocus(ctx.getSource(), StringArgumentType.getString(ctx, "guild")))
                        .then(ClientCommandManager.argument("message", StringArgumentType.greedyString())
                                .executes(ctx -> executeChat(ctx.getSource(), StringArgumentType.getString(ctx, "guild"), StringArgumentType.getString(ctx, "message")))
                        )
                );
    }

    public static LiteralArgumentBuilder<FabricClientCommandSource> builderG() {
        return ClientCommandManager.literal("cg")
                .then(ClientCommandManager.argument("message", StringArgumentType.greedyString())
                        .executes(ctx -> executeChat(ctx.getSource(), null, StringArgumentType.getString(ctx, "message")))
                );
    }

    public static LiteralArgumentBuilder<FabricClientCommandSource> builderReconnectInterChat() {
        return ClientCommandManager.literal("reconnectinterchat")
                .executes(ctx -> {
                    Mod.reconnect();
                    return 0;
                })
                .then(ClientCommandManager.argument("apikey", StringArgumentType.greedyString())
                        .executes(ctx -> {
                            Mod.CONFIG.apiKey(StringArgumentType.getString(ctx, "apikey"));
                            Mod.reconnect();
                            return 0;
                        })
                );
    }

    private static int executeFocus(FabricClientCommandSource source, String guildName) {
        Guild guild = Mod.GUILDS.stream().filter(g -> g.name().equalsIgnoreCase(guildName)).findAny().orElse(null);
        if (guild == null) {
            source.sendError(Text.literal("そんなぎるどないよ " + guildName));
            return 0;
        }
        Mod.client.selectGuild(guild.id());
        source.sendFeedback(Text.literal(guild.name() + " にちゃっとするようにしたよ(/cg <めっせーじ>でできるよ)"));
        return 1;
    }

    private static int executeChat(FabricClientCommandSource source, String guildName, String message) {
        if (guildName != null) {
            Guild guild = Mod.GUILDS.stream().filter(g -> g.name().equalsIgnoreCase(guildName)).findAny().orElse(null);
            if (guild == null) {
                source.sendError(Text.literal("そんなぎるどないよ " + guildName));
                return 0;
            }
            Mod.client.sendMessageToGuild(guild.id(), message);
        } else {
            Mod.client.sendMessageToGuild(null, message);
        }
        return 1;
    }
}
