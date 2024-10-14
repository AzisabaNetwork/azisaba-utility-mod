package net.azisaba.azisabautilitymod.fabric.util;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

public class AUtil {
    public static @NotNull MutableText stacktraceComponent(Throwable t) {
        MutableText component = Text.literal(t.getClass().getTypeName() + ": " + t.getMessage() + "\n");
        for (StackTraceElement element : t.getStackTrace()) {
            component.append(Text.literal("  at " + element.toString()));
            component.append(Text.literal("\n"));
        }
        if (t.getCause() != null) {
            component.append("Caused by: ");
            component.append(stacktraceComponent(t.getCause()));
        }
        return component;
    }
}
