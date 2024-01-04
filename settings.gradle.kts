rootProject.name = "AzisabaUtilityMod"

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
include("fabric-1.19")
include("fabric-1.18")
include("fabric-1.17")
include("fabric-1.16")
include("forge-1.15")
