plugins {
    java
    `java-library`
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

allprojects {
    apply {
        plugin("java")
        plugin("java-library")
        plugin("com.github.johnrengelman.shadow")
    }

    group = "net.azisaba.interchatmod"
    version = "0.2.0"

    repositories {
        // mavenLocal()
        mavenCentral()
        maven { url = uri("https://repo.azisaba.net/repository/maven-public/") }
        maven { url = uri("https://libraries.minecraft.net/") }
        maven { url = uri("https://repo.blueberrymc.net/repository/maven-public/") }
    }

    tasks {
        compileJava {
            options.encoding = "UTF-8"
        }
    }
}

subprojects {
    tasks {
        shadowJar {
            archiveBaseName.set("InterChatMod-${project.name}")
        }
    }
}
