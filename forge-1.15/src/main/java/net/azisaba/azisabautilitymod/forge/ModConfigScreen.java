package net.azisaba.azisabautilitymod.forge;

import net.azisaba.azisabautilitymod.forge.components.StringOption;
import net.azisaba.azisabautilitymod.forge.components.TextFieldTooltipWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.screen.OptionsScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.settings.AbstractOption;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class ModConfigScreen extends OptionsScreen {
    private final AbstractOption[] options = new AbstractOption[]{
            new StringOption(
                    "text.config.azisaba-utility-mod-config.option.apiKey",
                    "text.config.azisaba-utility-mod-config.option.apiKey.tooltip",
                    () -> ModConfig.apiKey,
                    s -> ModConfig.apiKey = s
            ),
    };
    private final @NotNull Screen previous;

    public ModConfigScreen(@NotNull Screen previous) {
        super(previous, Minecraft.getInstance().gameSettings);
        this.previous = previous;
    }

    @Override
    protected void init() {
        int i = 0;

        for (AbstractOption abstractoption : options) {
            int j = this.width / 2 - 155 + i % 2 * 160;
            int k = this.height / 6 - 12 + 24 * (i >> 1);
            assert this.minecraft != null;
            this.addButton(abstractoption.createWidget(this.minecraft.gameSettings, j, k, 150));
            ++i;
        }
        this.addButton(new Button(this.width / 2 - 100, this.height - 27, 200, 20, "Done", (button) -> {
            ModConfig.save();
            assert this.minecraft != null;
            this.minecraft.displayGuiScreen(this.previous);
        }));
    }

    public Optional<Widget> getHoveredWidget(double mouseX, double mouseY) {
        for (IGuiEventListener listener : this.children()) {
            if (!(listener instanceof Widget)) continue;
            if (listener.isMouseOver(mouseX, mouseY)) {
                return Optional.of((Widget) listener);
            }
        }

        return Optional.empty();
    }

    @Nullable
    public List<String> getHoveredButtonTooltip(int mouseX, int mouseY) {
        Optional<Widget> optional = getHoveredWidget(mouseX, mouseY);
        if (optional.isPresent()) {
            if (optional.get() instanceof TextFieldTooltipWidget) {
                Optional<List<String>> optionalTooltip = ((TextFieldTooltipWidget) optional.get()).getOrderedTooltip();
                return optionalTooltip.orElse(null);
            }
        }
        return null;
    }

    @Override
    public void render(int mouseX, int mouseY, float delta) {
        super.render(mouseX, mouseY, delta);
        List<String> list = getHoveredButtonTooltip(mouseX, mouseY);
        if (list != null) {
            renderTooltip(list, mouseX, mouseY);
        }
    }

    @Override
    public void removed() {
        ModConfig.save();
    }
}
