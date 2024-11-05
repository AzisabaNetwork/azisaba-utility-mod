package net.azisaba.azisabautilitymod.fabric.commands;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIntArray;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.packet.c2s.play.CreativeInventoryActionC2SPacket;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GenerateKotlinLoveHeadCommand implements Command {
    @SuppressWarnings("SpellCheckingInspection")
    @Override
    public void execute(@NotNull ClientPlayerEntity player, @NotNull String[] args) throws CommandSyntaxException {
        if (args.length == 0) {
            return;
        }
        String name = args[0];
        List<Text> lore = new ArrayList<>();
        lore.add(Text.literal("リアルで会った人に贈られる§0§m(旧)§dKotlinLoveのあたま").formatted(Formatting.LIGHT_PURPLE));
        lore.add(Text.empty());
        lore.add(Text.empty());
        lore.add(Text.literal("質問来てた！取引・共有した人はどうなりますか？").formatted(Formatting.WHITE));
        lore.add(Text.literal("結論：§c死刑§0(" + name + "ならそんなことしないよね！)").formatted(Formatting.WHITE));
        lore.add(Text.empty());
        lore.add(Text.literal("質問来てた！金床で名前を変えるのはありですか？").formatted(Formatting.WHITE));
        lore.add(Text.literal("結論：§aセーフ§fだけど§e自己責任").formatted(Formatting.WHITE));
        lore.add(Text.empty());
        lore.add(Text.literal("質問来てた！スキルはありますか？？").formatted(Formatting.WHITE));
        lore.add(Text.literal("結論：§0§mただのバニラアイテムなのでなにもない").formatted(Formatting.WHITE));
        lore.add(Text.empty());
        lore.add(Text.literal("質問来てた！頭は複数個もらえますか？").formatted(Formatting.WHITE));
        lore.add(Text.literal("結論：§e回数分§fもらえる§7(ただし1日1回まで)").formatted(Formatting.WHITE));
        lore.add(Text.empty());
        lore.add(Text.literal("質問来てた！前に無印の頭をもらったものはどうなりますか？").formatted(Formatting.WHITE));
        lore.add(Text.literal("結論：回収も没収もされないし、").formatted(Formatting.WHITE));
        lore.add(Text.literal("MCIDが書いてないものに限り§e取引もOK").formatted(Formatting.WHITE));
        lore.add(Text.empty());
        lore.add(Text.literal("質問来てた！LINE交換しませんか？").formatted(Formatting.WHITE));
        lore.add(Text.literal("結論：§aLINE§fやってません").formatted(Formatting.WHITE));
        lore.add(Text.empty());
        lore.add(Text.literal("質問来てた！このアイテムを紛失したときはどうなりますか？").formatted(Formatting.WHITE));
        lore.add(Text.literal("結論：§e補填§fも§eサポート§fもされない").formatted(Formatting.WHITE));
        lore.add(Text.empty());
        lore.add(Text.literal("ちなみに§4§l取引禁止§fですよ？").formatted(Formatting.WHITE));
        lore.add(Text.empty());
        lore.add(Text.literal(args[1].replaceAll("&([a-flmno0-9])", "§$1").replace("　", " ")).formatted(Formatting.WHITE));
        lore.add(Text.literal(args[2].replaceAll("&([a-flmno0-9])", "§$1").replace("　", " ")).formatted(Formatting.WHITE));

        NbtCompound skullOwnerTag = new NbtCompound();
        skullOwnerTag.put("Id", new NbtIntArray(new int[] {-1139716447,1816670647,-1829724271,1008052645}));
        skullOwnerTag.putString("Name", "KotlinLove");
        NbtCompound propertiesTag = getPropertiesTag();
        skullOwnerTag.put("Properties", propertiesTag);

        ItemStack item = new ItemStack(Items.PLAYER_HEAD);
        //item.setDamage(3);
        item.set(DataComponentTypes.LORE, new LoreComponent(lore));
        item.set(DataComponentTypes.ITEM_NAME, Text.literal("KotlinLoveの頭❤").formatted(Formatting.LIGHT_PURPLE));
        //item.set(DataComponents)
        NbtCompound itemTag = new NbtCompound();
        itemTag.put("SkullOwner", skullOwnerTag);
        itemTag.putString("SkullOwnerOrig", "KotlinLove");
        item.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(itemTag));
        player.networkHandler.sendPacket(new CreativeInventoryActionC2SPacket(player.getInventory().selectedSlot + 36, item));
    }

    @NotNull
    private static NbtCompound getPropertiesTag() {
        NbtCompound propertiesTag = new NbtCompound();
        NbtList texturesTag = new NbtList();
        NbtCompound textureTag = new NbtCompound();
        textureTag.putString("Signature", "aLsYfRztYRa05ZLAVCZQH1LrVoKU2kCkmKMUdbHR02W3kTrltI+0cmBZMXBG4EaK5At906BUeyI9qfkm5onFKKXF3KgiMBSIizuuyj4jiiCXxJmTyXQ5lAMHao1/5qlG5m8dZCKf5Tp11cveSxn8cRp3+fEpC0n02uY8nQu7pS46AhcdP5MilM8rpN27bndlMlnHxe5T8df6Xab5D3sBk/bXrKG7V3iXWsZw810vPwMxBkWn+AaoS/pUKzvGfRFxjY68HyDpNxj3vD96ouCVFxVtiWAwTODTnAAylStHRtqMlrLJzgS3ml9lXn+zJhwgyz/CbFXwmlS6fxAEcINOFJrjcplV6ld1CrYEzIjSW3YumOSBc9xoSVYebWAuY7tpK1WPxmA3G2eybEf6S5DFCrA8GjPl7/ju3CU2veAUrQoL2W0aECHWkA7dYUw8WrUgOcUwCZJKIK4gb1RuFEAhICBURoybS9t5utrwuDPmzaETDijn4Q9j/MLKuE0t7r8rORa/asUFN8/XTcH8TPXJ2cQvCO14R3SjloaPzRW9bllPwPct1i2GJSaZ3O8VDxZxO/lD2lneN21Jyc+dbSGwP2ioiEK/R95p6allVMAz4CGHI7IgbPiD5HraDWZpULspGrRCss4qvtrEB7nmA2zEeTwt6vVgNzkECcLe3ByAjcw=");
        textureTag.putString("Value", "ewogICJ0aW1lc3RhbXAiIDogMTY0NDQ0OTU3MDUwMCwKICAicHJvZmlsZUlkIiA6ICIxODY1YWI4YzcwMGI0NzhiOWI1MmE4YzU4NzM5ZGYxYSIsCiAgInByb2ZpbGVOYW1lIiA6ICJLb3RsaW5Mb3ZlIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzYxMTMxMzgwYWU5NmJhZjg0MTYyM2RhNjg5ZTljZGVlMjZiMzQwNGMzMzBlYzMwNzU4Y2JkZmI5MTQwOTZiODYiCiAgICB9LAogICAgIkNBUEUiIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzIzNDBjMGUwM2RkMjRhMTFiMTVhOGIzM2MyYTdlOWUzMmFiYjIwNTFiMjQ4MWQwYmE3ZGVmZDYzNWNhN2E5MzMiCiAgICB9CiAgfQp9");
        texturesTag.add(textureTag);
        propertiesTag.put("textures", texturesTag);
        return propertiesTag;
    }

    @Override
    public @NotNull String getName() {
        return "generateKotlinLoveHead";
    }

    @Override
    public @NotNull String getDescription() {
        return "";
    }

    @Override
    public @NotNull List<String> getUsage() {
        return Collections.singletonList("<name> [amount]");
    }
}
