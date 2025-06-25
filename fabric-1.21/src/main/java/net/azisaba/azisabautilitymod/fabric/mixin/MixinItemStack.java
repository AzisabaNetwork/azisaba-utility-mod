package net.azisaba.azisabautilitymod.fabric.mixin;

import net.azisaba.azisabautilitymod.fabric.connection.UpdateTimePacketHandler;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Mixin(ItemStack.class)
public abstract class MixinItemStack {
    @Shadow public abstract ComponentMap getComponents();

    @ModifyVariable(method = "getTooltip", at = @At("RETURN"), ordinal = 0)
    public List<Text> addTooltip(List<Text> list) {
        if (!(list instanceof ArrayList<Text>)) {
            list = new ArrayList<>(list);
        }
        NbtCompound tag = getComponents().getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt();
        if (tag.contains("MYTHIC_TYPE", 8)) {
            String mmId = tag.getString("MYTHIC_TYPE");
            list.add(Text.literal("MMID: " + mmId).formatted(Formatting.DARK_GRAY));
        }
        var publicBukkitValues = tag.getCompound("PublicBukkitValues");
        if (publicBukkitValues.contains("mythicmobs:type", 8)) {
            String mmId = publicBukkitValues.getString("mythicmobs:type");
            list.add(Text.literal("MMID: " + mmId).formatted(Formatting.DARK_GRAY));
        }
        if (getComponents().contains(DataComponentTypes.CUSTOM_MODEL_DATA)) {
            int cmd = Objects.requireNonNull(getComponents().get(DataComponentTypes.CUSTOM_MODEL_DATA)).value();
            list.add(Text.literal("CustomModelData: " + cmd).formatted(Formatting.DARK_GRAY));
        }
        if (tag.contains("soulbound", 8)) {
            String uuid = tag.getString("soulbound");
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
        return list;
    }
}
