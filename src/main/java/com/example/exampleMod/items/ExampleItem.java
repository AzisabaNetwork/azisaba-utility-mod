package com.example.exampleMod.items;

import net.blueberrymc.common.bml.BlueberryMod;
import net.blueberrymc.world.item.BlueberryItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ExampleItem extends BlueberryItem {
    public ExampleItem(@NotNull BlueberryMod mod) {
        super(
                mod,
                new Properties().stacksTo(64).food(
                        new FoodProperties.Builder()
                                // Normally, the food level restores because it is positive value, but in this case,
                                // the value is negative so eating the item would reduce the food level.
                                .nutrition(-1) // (= food level)
                                .alwaysEat() // you will be able to eat the item even if the player has full food level
                                .effect(new MobEffectInstance(MobEffects.BAD_OMEN, 20 * 120, 5), 1) // f = chances to give effect (0 - 1)
                                .build()
                )
        );
    }

    @NotNull
    @Override
    public InteractionResult useOn(@NotNull UseOnContext context) {
        // To reduce the item count when used, use:
        // context.getItemInHand().shrink(1);
        if (context.getLevel().isClientSide) {
            return InteractionResult.SUCCESS;
        }
        Player player = context.getPlayer();
        if (player == null) return InteractionResult.FAIL;
        float power = getPower(context.getItemInHand());
        // player is passed for first parameter so user will not take damage from explosion
        context.getLevel().explode(player, context.getClickLocation().x, context.getClickLocation().y, context.getClickLocation().z, power, Level.ExplosionInteraction.TNT);
        return InteractionResult.CONSUME;
    }

    /**
     * Get the tag from item stack and returns the effective explosion power.
     * @param stack the item stack
     * @return explosion power, defaults to 5.0f. Can never be infinity or NaN.
     */
    public static float getPower(@NotNull ItemStack stack) {
        CompoundTag tag = stack.getTag(); // get item tag (nullable)
        float power = 5.0f;
        if (tag != null && tag.contains("Power", 99)) { // numeric type
            power = tag.getFloat("Power");
        }
        // set to 5.0f if power is infinity or NaN
        if (Float.isInfinite(power) || Float.isNaN(power)) power = 5.0f;
        return power;
    }

    @Override
    public boolean isFireResistant() {
        // the item will not be destroyed in lava or fire
        return true;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag tooltipFlag) {
        // tooltip
        list.add(Component.literal("Texture is '5F' because default explosion power is 5.0f"));
        super.appendHoverText(itemStack, level, list, tooltipFlag);
    }

    @NotNull
    @Override
    public Component getName(@NotNull ItemStack itemStack) {
        // item name
        float power = getPower(itemStack);
        return Component.literal("Example item (Power: " + power + ")");
    }
}
