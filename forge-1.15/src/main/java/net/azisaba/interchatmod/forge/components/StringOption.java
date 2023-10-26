package net.azisaba.interchatmod.forge.components;

import net.minecraft.client.GameSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.settings.AbstractOption;
import net.minecraft.util.text.TranslationTextComponent;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class StringOption extends AbstractOption {
    private final TranslationTextComponent key;
    private final String tooltipKey;
    private final Supplier<String> getter;
    private final Consumer<String> setter;

    public StringOption(String key, String tooltipKey, Supplier<String> getter, Consumer<String> setter) {
        super(key);
        this.key = new TranslationTextComponent(key);
        this.tooltipKey = tooltipKey;
        this.getter = getter;
        this.setter = setter;
    }

    @Override
    public @NotNull Widget createWidget(@NotNull GameSettings options, int x, int y, int width) {
        TextFieldWidget widget = new TextFieldTooltipWidget(Minecraft.getInstance().fontRenderer, x, y, width, 20, key.getString(), this::getOrderedTooltip);
        widget.setMaxStringLength(1024);
        widget.setText(getter.get());
        widget.setResponder(setter);
        return widget;
    }

    public Optional<List<String>> getOrderedTooltip() {
        return Optional.of(Minecraft.getInstance().fontRenderer.listFormattedStringToWidth(new TranslationTextComponent(tooltipKey).getString(), 200));
    }
}
