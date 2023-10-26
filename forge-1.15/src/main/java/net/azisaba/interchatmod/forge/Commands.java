package net.azisaba.interchatmod.forge;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.azisaba.interchatmod.forge.model.Guild;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandSource;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.util.text.StringTextComponent;

public class Commands {
    public static LiteralArgumentBuilder<ISuggestionProvider> literal(String name) {
        return LiteralArgumentBuilder.literal(name);
    }

    public static <T> RequiredArgumentBuilder<ISuggestionProvider, T> argument(String name, ArgumentType<T> type) {
        return RequiredArgumentBuilder.argument(name, type);
    }

    public static LiteralArgumentBuilder<ISuggestionProvider> builderGS() {
        return literal("cgs")
                .then(argument("guild", StringArgumentType.word())
                        .suggests((ctx, builder) -> ISuggestionProvider.suggest(Mod.GUILDS.stream().map(Guild::name), builder))
                        .executes(ctx -> executeFocus(ctx.getSource(), StringArgumentType.getString(ctx, "guild")))
                        .then(argument("message", StringArgumentType.greedyString())
                                .executes(ctx -> executeChat(ctx.getSource(), StringArgumentType.getString(ctx, "guild"), StringArgumentType.getString(ctx, "message")))
                        )
                );
    }

    public static LiteralArgumentBuilder<ISuggestionProvider> builderG() {
        return literal("cg")
                .then(argument("message", StringArgumentType.greedyString())
                        .executes(ctx -> executeChat(ctx.getSource(), null, StringArgumentType.getString(ctx, "message")))
                );
    }

    public static LiteralArgumentBuilder<ISuggestionProvider> builderReconnectInterChat() {
        return literal("reconnectinterchat")
                .executes(ctx -> {
                    Mod.reconnect();
                    return 0;
                })
                .then(argument("apikey", StringArgumentType.greedyString())
                        .executes(ctx -> {
                            ModConfig.apiKey = StringArgumentType.getString(ctx, "apikey");
                            Mod.reconnect();
                            return 0;
                        })
                );
    }

    public static LiteralArgumentBuilder<ISuggestionProvider> builderGuild() {
        return literal("cguild")
                .then(literal("invite")
                        .then(argument("player", StringArgumentType.word())
                                .executes(ctx -> {
                                    Mod.client.invite(StringArgumentType.getString(ctx, "player"));
                                    return 0;
                                })
                        )
                )
                .then(literal("accept")
                        .then(argument("guild", StringArgumentType.word())
                                .executes(ctx -> {
                                    Mod.client.respondInvite(StringArgumentType.getString(ctx, "guild"), true);
                                    return 0;
                                })
                        )
                )
                .then(literal("reject")
                        .then(argument("guild", StringArgumentType.word())
                                .executes(ctx -> {
                                    Mod.client.respondInvite(StringArgumentType.getString(ctx, "guild"), false);
                                    return 0;
                                })
                        )
                )
                .then(literal("nick")
                        .executes(ctx -> {
                            Mod.client.nick(null);
                            return 0;
                        })
                        .then(argument("nickname", StringArgumentType.greedyString())
                                .executes(ctx -> {
                                    Mod.client.nick(StringArgumentType.getString(ctx, "nickname"));
                                    return 0;
                                })
                        )
                );
    }

    public static LiteralArgumentBuilder<ISuggestionProvider> builderInterChatConfig() {
        return literal("interchatconfig").executes(ctx -> {
            Minecraft.getInstance().enqueue(() -> Minecraft.getInstance().displayGuiScreen(new ModConfigScreen(Minecraft.getInstance().currentScreen)));
            return 0;
        });
    }

    private static int executeFocus(ISuggestionProvider source, String guildName) {
        Guild guild = Mod.GUILDS.stream().filter(g -> g.name().equalsIgnoreCase(guildName)).findAny().orElse(null);
        if (guild == null) {
            if (source instanceof CommandSource) {
                ((CommandSource) source).sendErrorMessage(new StringTextComponent("そんなぎるどないよ " + guildName));
            }
            return 0;
        }
        Mod.client.selectGuild(guild.id());
        if (source instanceof CommandSource) {
            ((CommandSource) source).sendFeedback(new StringTextComponent(guild.name() + " にちゃっとするようにしたよ(/cg <めっせーじ>でできるよ)"), false);
        }
        return 1;
    }

    private static int executeChat(ISuggestionProvider source, String guildName, String message) {
        if (guildName != null) {
            Guild guild = Mod.GUILDS.stream().filter(g -> g.name().equalsIgnoreCase(guildName)).findAny().orElse(null);
            if (guild == null) {
                if (source instanceof CommandSource) {
                    ((CommandSource) source).sendErrorMessage(new StringTextComponent("そんなぎるどないよ " + guildName));
                }
                return 0;
            }
            Mod.client.sendMessageToGuild(guild.id(), message);
        } else {
            Mod.client.sendMessageToGuild(null, message);
        }
        return 1;
    }
}
