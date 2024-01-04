package net.azisaba.azisabautilitymod.fabric.components;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.OrderableTooltip;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class TextFieldTooltipWidget extends TextFieldWidget implements OrderableTooltip {
    private final Supplier<Optional<List<OrderedText>>> tooltipSupplier;

    public TextFieldTooltipWidget(TextRenderer textRenderer, int x, int y, int width, int height, Text text, Supplier<Optional<List<OrderedText>>> tooltipSupplier) {
        super(textRenderer, x, y, width, height, text);
        this.tooltipSupplier = tooltipSupplier;
    }

    public TextFieldTooltipWidget(TextRenderer textRenderer, int x, int y, int width, int height, @Nullable TextFieldWidget copyFrom, Text text, Supplier<Optional<List<OrderedText>>> tooltipSupplier) {
        super(textRenderer, x, y, width, height, copyFrom, text);
        this.tooltipSupplier = tooltipSupplier;
    }

    @Override
    public Optional<List<OrderedText>> getOrderedTooltip() {
        return tooltipSupplier.get();
    }
}
