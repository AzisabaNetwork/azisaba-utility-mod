package net.azisaba.azisabautilitymod.fabric.commands;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class InspectCommand implements Command {
    @Override
    public void execute(@NotNull ClientPlayerEntity player, @NotNull String[] args) {
        ItemStack stack = player.getInventory().getSelectedStack();
        NbtCompound tag = Objects.requireNonNull(stack.get(DataComponentTypes.CUSTOM_DATA)).copyNbt();
        MutableText component = Text.literal("");
        component.append(NbtHelper.toPrettyPrintedText(tag));
        component.getWithStyle(component.getStyle()
                .withHoverEvent(new HoverEvent.ShowText(Text.literal("Click to copy")))
                .withClickEvent(new ClickEvent.CopyToClipboard(tag.toString())));
        player.sendMessage(component, false);
    }

    @Override
    public @NotNull String getName() {
        return "inspect";
    }

    @Override
    public @NotNull String getDescription() {
        return "Displays the NBT tag of the item in hand.";
    }
}
