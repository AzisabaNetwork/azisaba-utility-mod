plugins {
    java
    `java-library`
    id("com.gradleup.shadow") version "8.3.3"
}

allprojects {
    apply {
        plugin("java")
        plugin("java-library")
        plugin("com.gradleup.shadow")
    }

    group = "net.azisaba.azisabautilitymod"
    version = "0.4.1"

    repositories {
        // mavenLocal()
        mavenCentral()
        maven { url = uri("https://repo.azisaba.net/repository/maven-public/") }
        maven { url = uri("https://libraries.minecraft.net/") }
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
            archiveBaseName.set("AzisabaUtilityMod-${project.name}")
        }
    }
}
