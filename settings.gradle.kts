rootProject.name = "InterChatMod"

pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
        mavenCentral()
        maven { url = uri("https://repo.blueberrymc.net/repository/maven-public/") }
    }
}
include("blueberry-1.20")
