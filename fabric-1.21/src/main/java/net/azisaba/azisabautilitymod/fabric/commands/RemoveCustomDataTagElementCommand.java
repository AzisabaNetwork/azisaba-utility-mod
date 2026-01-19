package net.azisaba.azisabautilitymod.fabric.commands;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.c2s.play.CreativeInventoryActionC2SPacket;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class RemoveCustomDataTagElementCommand implements Command {
    @Override
    public void execute(@NotNull ClientPlayerEntity player, @NotNull String[] args) throws CommandSyntaxException {
        ItemStack item = player.getInventory().getSelectedStack().copy();
        NbtCompound tag = Objects.requireNonNull(item.get(DataComponentTypes.CUSTOM_DATA)).copyNbt();
        for (String key : args) {
            NbtCompound current = tag;
            String[] arr = key.split("/");
            for (int i = 0; i < arr.length; i++) {
                String s = arr[i];
                if (i == arr.length - 1) {
                    current.remove(s);
                } else {
                    if (!(current.get(s) instanceof NbtCompound)) {
                        break;
                    }
                    current = current.getCompound(s).orElse(null);
                    if (current == null) break;
                }
            }
        }
        if (tag.isEmpty()) {
            item.remove(DataComponentTypes.CUSTOM_DATA);
        } else {
            item.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(tag));
        }
        player.networkHandler.sendPacket(new CreativeInventoryActionC2SPacket(player.getInventory().getSelectedSlot() + 36, item));
    }

    @Override
    public @NotNull String getName() {
        return "removeCustomDataTagElement";
    }

    @Override
    public @NotNull String getDescription() {
        return "Remove the property from the item tag";
    }

    @Override
    public @NotNull List<String> getUsage() {
        return Collections.singletonList("<element name, possibly separated by slash>");
    }
}
