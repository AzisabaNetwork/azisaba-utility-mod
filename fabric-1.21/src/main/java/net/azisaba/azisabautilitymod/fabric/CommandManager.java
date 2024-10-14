package net.azisaba.azisabautilitymod.fabric;

import net.azisaba.azisabautilitymod.fabric.commands.*;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class CommandManager {
    public static final List<Command> COMMANDS = Arrays.asList(
            new HelpCommand(),
            new ClearTPSCommand(),
            new InspectCommand(),
            new MergeCustomDataCommand(),
            new SetCustomDataCommand(),
            new RemoveCustomDataTagElementCommand(),
            new GenerateKotlinLoveHeadCommand()
    );

    public static @Nullable Command getCommand(String name) {
        for (Command command : COMMANDS) {
            if (command.getName().equalsIgnoreCase(name)) return command;
        }
        return null;
    }
}
