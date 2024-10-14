package net.azisaba.azisabautilitymod.fabric.commands;

import net.minecraft.client.network.ClientPlayerEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public interface Command {
    void execute(@NotNull ClientPlayerEntity player, @NotNull String[] args) throws Exception;

    @NotNull
    String getName();

    @NotNull
    String getDescription();

    @NotNull
    default List<String> getUsage() {
        return Collections.emptyList();
    }

    @NotNull
    default Stream<String> getSuggestions(@NotNull ClientPlayerEntity player, @NotNull String @NotNull [] args) throws Exception {
        return getUsage().stream();
    }

    @NotNull
    default String getFullUsage() {
        return ("/aziutil " + getName() + " " + getUsage()).trim();
    }
}
