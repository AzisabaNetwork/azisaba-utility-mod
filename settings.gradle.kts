rootProject.name = "InterChatMod"

pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
        mavenCentral()
        maven {
            name = "Fabric"
            url = uri("https://maven.fabricmc.net/")
        }
        maven { url = uri("https://repo.azisaba.net/repository/maven-public/") }
    }
}
include("blueberry-1.20")
include("fabric-1.20")
include("common")
