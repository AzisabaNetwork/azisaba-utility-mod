package net.azisaba.azisabautilitymod.fabric.commands;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.network.packet.c2s.play.CreativeInventoryActionC2SPacket;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MergeCustomDataCommand implements Command {
    @Override
    public void execute(@NotNull ClientPlayerEntity player, @NotNull String[] args) throws CommandSyntaxException {
        ItemStack item = player.getInventory().getSelectedStack().copy();
        NbtCompound tag = Objects.requireNonNull(item.get(DataComponentTypes.CUSTOM_DATA)).copyNbt();
        tag.copyFrom(StringNbtReader.readCompound(String.join(" ", args)));
        item.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(tag));
        player.networkHandler.sendPacket(new CreativeInventoryActionC2SPacket(player.getInventory().getSelectedSlot() + 36, item));
    }

    @Override
    public @NotNull String getName() {
        return "mergeCustomData";
    }

    @Override
    public @NotNull String getDescription() {
        return "Merge the item tag";
    }

    @Override
    public @NotNull List<String> getUsage() {
        return Collections.singletonList("<tag>");
    }
}
