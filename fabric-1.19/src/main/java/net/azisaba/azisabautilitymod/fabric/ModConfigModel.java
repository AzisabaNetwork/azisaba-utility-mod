package net.azisaba.azisabautilitymod.fabric;

import io.wispforest.owo.config.annotation.Config;
import io.wispforest.owo.config.annotation.Modmenu;

@Modmenu(modId = "azisabautilitymod")
@Config(name = "azisabautilitymod-config", wrapperName = "ModConfig")
public class ModConfigModel {
    public String apiKey = "";
}
