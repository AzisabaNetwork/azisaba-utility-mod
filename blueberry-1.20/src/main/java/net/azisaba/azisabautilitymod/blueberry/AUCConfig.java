package net.azisaba.azisabautilitymod.blueberry;

import net.blueberrymc.common.bml.config.VisualConfigManager.*;

@Config
@Name("AzisabaUtilityMod")
public class AUCConfig {
    @Order(1000)
    @Name("API Key")
    @Key("api-key")
    @Description(@Name("/apikeyでとれるやつ"))
    public static String apiKey = "";

    @Order(10000)
    @Name("Endpoint Host override")
    @Key("endpoint-host-override")
    @Description(@Name("Do not touch unless you know what you're doing"))
    public static String endpointHostOverride = "";

    @Order(10001)
    @Name("API Host override")
    @Key("api-host-override")
    @Description(@Name("Do not touch unless you know what you're doing"))
    public static String apiHostOverride = "";
}
