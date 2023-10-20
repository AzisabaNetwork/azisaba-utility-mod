package net.azisaba.interchatmod.blueberry;

import net.blueberrymc.common.bml.config.VisualConfigManager.*;

@Config
@Name("InterChatMod")
public class ICConfig {
    @Order(1000)
    @Name("API Key")
    @Key("api-key")
    @Description(@Name("/apikeyでとれるやつ"))
    public static String apiKey = "";

    @Order(1001)
    @Name("Hide everything")
    @Key("hide-everything")
    @Description(@Name("ゲーム画面にメッセージを出さないようにする"))
    public static boolean hideEverything = false;

    @Order(1002)
    @Name("Chat without command")
    @Key("chat-without-command")
    @Description(@Name("コマンド(/cg, /cgf, /cgs)なしでチャット可能になります。先頭に「!」を付けると一時的に無効化できます。"))
    public static boolean chatWithoutCommand = false;

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
