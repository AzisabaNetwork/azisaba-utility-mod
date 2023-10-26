package net.azisaba.interchatmod.forge.components;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.TextFieldWidget;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class TextFieldTooltipWidget extends TextFieldWidget {
    private final Supplier<Optional<List<String>>> tooltipSupplier;

    public TextFieldTooltipWidget(FontRenderer textRenderer, int x, int y, int width, int height, String text, Supplier<Optional<List<String>>> tooltipSupplier) {
        super(textRenderer, x, y, width, height, text);
        this.tooltipSupplier = tooltipSupplier;
    }

    public TextFieldTooltipWidget(FontRenderer textRenderer, int x, int y, int width, int height, @Nullable TextFieldWidget copyFrom, String text, Supplier<Optional<List<String>>> tooltipSupplier) {
        super(textRenderer, x, y, width, height, copyFrom, text);
        this.tooltipSupplier = tooltipSupplier;
    }

    public Optional<List<String>> getOrderedTooltip() {
        return tooltipSupplier.get();
    }
}
