package net.azisaba.azisabautilitymod.fabric.mixin;

import net.azisaba.azisabautilitymod.fabric.connection.UpdateTimePacketHandler;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtString;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Mixin(ItemStack.class)
public abstract class MixinItemStack {
    @Shadow public abstract ComponentMap getComponents();

    @Inject(method = "getTooltip", at = @At("RETURN"), cancellable = true)
    public void addTooltip(Item.TooltipContext context, @Nullable PlayerEntity player, TooltipType type, CallbackInfoReturnable<List<Text>> cir) {
        List<Text> list = cir.getReturnValue();
        if (!(list instanceof ArrayList<Text>)) {
            list = new ArrayList<>(list);
        }
        NbtCompound tag = getComponents().getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt();
        if (tag.get("MYTHIC_TYPE") instanceof NbtString(String value)) {
            list.add(Text.literal("MMID: " + value).formatted(Formatting.DARK_GRAY));
        }
        var publicBukkitValues = tag.getCompound("PublicBukkitValues").orElseGet(NbtCompound::new);
        if (publicBukkitValues.get("mythicmobs:type") instanceof NbtString(String value)) {
            list.add(Text.literal("MMID: " + value).formatted(Formatting.DARK_GRAY));
        }
        if (getComponents().contains(DataComponentTypes.CUSTOM_MODEL_DATA)) {
            var cmd = Objects.requireNonNull(getComponents().get(DataComponentTypes.CUSTOM_MODEL_DATA));
            if (!cmd.floats().isEmpty()) {
                list.add(Text.literal("CustomModelData (floats): " + cmd.floats()).formatted(Formatting.DARK_GRAY));
            }
            if (!cmd.flags().isEmpty()) {
                list.add(Text.literal("CustomModelData (flags): " + cmd.flags()).formatted(Formatting.DARK_GRAY));
            }
            if (!cmd.colors().isEmpty()) {
                list.add(Text.literal("CustomModelData (colors): " + cmd.colors()).formatted(Formatting.DARK_GRAY));
            }
            if (!cmd.strings().isEmpty()) {
                list.add(Text.literal("CustomModelData (strings): " + cmd.strings()).formatted(Formatting.DARK_GRAY));
            }
        }
        if (tag.get("soulbound") instanceof NbtString(String uuid)) {
            try {
                String name = UpdateTimePacketHandler.uuidToNameMap.get(UUID.fromString(uuid));
                list.add(Text.literal("Soulbound: " + uuid + " (" + name + ")").formatted(Formatting.DARK_GRAY));
            } catch (Exception e) {
                list.add(Text.literal("Soulbound: " + uuid).formatted(Formatting.DARK_GRAY));
            }
        }
        if (getComponents().contains(DataComponentTypes.REPAIR_COST)) {
            int repairCost = Objects.requireNonNull(getComponents().get(DataComponentTypes.REPAIR_COST));
            if (repairCost > 0) {
                list.add(Text.literal("RepairCost: " + repairCost).formatted(Formatting.DARK_GRAY));
            }
        }
        cir.setReturnValue(list);
    }
}
