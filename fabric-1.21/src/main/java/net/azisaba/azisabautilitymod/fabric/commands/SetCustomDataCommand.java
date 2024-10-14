package net.azisaba.azisabautilitymod.fabric.commands;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.network.packet.c2s.play.CreativeInventoryActionC2SPacket;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class SetCustomDataCommand implements Command {
    @Override
    public void execute(@NotNull ClientPlayerEntity player, @NotNull String[] args) throws CommandSyntaxException {
        ItemStack item = player.getInventory().getMainHandStack().copy();
        item.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(StringNbtReader.parse(String.join(" ", args))));
        player.networkHandler.sendPacket(new CreativeInventoryActionC2SPacket(player.getInventory().selectedSlot + 36, item));
    }

    @Override
    public @NotNull String getName() {
        return "setCustomData";
    }

    @Override
    public @NotNull String getDescription() {
        return "Sets the current item's custom data";
    }

    @Override
    public @NotNull List<String> getUsage() {
        return Collections.singletonList("<nbt tag>");
    }
}
