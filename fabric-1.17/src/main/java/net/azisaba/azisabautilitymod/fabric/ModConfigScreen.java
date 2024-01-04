package net.azisaba.azisabautilitymod.fabric;

import net.azisaba.azisabautilitymod.fabric.components.StringOption;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.ButtonListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.CyclingOption;
import net.minecraft.client.option.Option;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ModConfigScreen extends GameOptionsScreen {
    private final @Nullable Screen previous;
    private ButtonListWidget list;

    public ModConfigScreen(@Nullable Screen previous) {
        super(previous, MinecraftClient.getInstance().options, Text.of("AzisabaUtilityMod"));
        this.previous = previous;
    }

    @Override
    protected void init() {
        list = new ButtonListWidget(this.client, this.width, this.height, 32, this.height - 32, 25);
        list.addAll(new Option[]{
                new StringOption(
                        "text.config.azisabautilitymod-config.option.apiKey",
                        "text.config.azisabautilitymod-config.option.apiKey.tooltip",
                        () -> ModConfig.apiKey,
                        s -> ModConfig.apiKey = s
                ),
        });
        addSelectableChild(list);
        this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, this.height - 27, 200, 20, ScreenTexts.DONE, (button) -> {
            ModConfig.save();
            assert this.client != null;
            this.client.setScreen(this.previous);
        }));
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        this.list.render(matrices, mouseX, mouseY, delta);
        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 5, 0xffffff);
        super.render(matrices, mouseX, mouseY, delta);
        List<OrderedText> list = getHoveredButtonTooltip(this.list, mouseX, mouseY);
        if (list != null) {
            this.renderOrderedTooltip(matrices, list, mouseX, mouseY);
        }
    }

    @Override
    public void removed() {
        ModConfig.save();
    }
}
