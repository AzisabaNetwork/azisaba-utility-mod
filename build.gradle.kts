plugins {
    java
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

allprojects {
    apply {
        plugin("java")
        plugin("com.github.johnrengelman.shadow")
    }

    group = "net.azisaba.interchatmod"
    version = "0.0.1"

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
