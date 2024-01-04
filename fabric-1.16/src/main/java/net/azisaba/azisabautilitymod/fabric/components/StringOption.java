package net.azisaba.azisabautilitymod.fabric.components;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.Option;
import net.minecraft.client.util.OrderableTooltip;
import net.minecraft.text.OrderedText;
import net.minecraft.text.TranslatableText;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class StringOption extends Option implements OrderableTooltip {
    private final TranslatableText key;
    private final String tooltipKey;
    private final Supplier<String> getter;
    private final Consumer<String> setter;

    public StringOption(String key, String tooltipKey, Supplier<String> getter, Consumer<String> setter) {
        super(key);
        this.key = new TranslatableText(key);
        this.tooltipKey = tooltipKey;
        this.getter = getter;
        this.setter = setter;
    }

    @Override
    public ClickableWidget createButton(GameOptions options, int x, int y, int width) {
        TextFieldWidget widget = new TextFieldTooltipWidget(MinecraftClient.getInstance().textRenderer, x, y, width, 20, key, this::getOrderedTooltip);
        widget.setMaxLength(1024);
        widget.setText(getter.get());
        widget.setChangedListener(s -> {
            if (s.isEmpty()) {
                widget.setSuggestion(new TranslatableText(tooltipKey).getString());
            } else {
                widget.setSuggestion("");
            }
            setter.accept(s);
        });
        return widget;
    }

    @Override
    public Optional<List<OrderedText>> getOrderedTooltip() {
        return Optional.of(MinecraftClient.getInstance().textRenderer.wrapLines(new TranslatableText(tooltipKey), 200));
    }
}
