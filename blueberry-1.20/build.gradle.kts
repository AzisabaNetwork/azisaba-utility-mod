import net.blueberrymc.blueberryfarm.blueberry

plugins {
    id("net.blueberrymc.blueberryfarm") version("2.3.0") // https://github.com/BlueberryMC/BlueberryFarm
}

tasks.withType<JavaExec>().configureEach {
    javaLauncher.set(javaToolchains.launcherFor(java.toolchain))
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

blueberry {
    minecraftVersion.set("1.20.1")
    apiVersion.set("2.0.0-SNAPSHOT")
}

dependencies {
    blueberry()
    implementation(project(":common"))
}

tasks {
    shadowJar {
        relocate("net.kyori", "net.azisaba.azisabautilitymod.lib.net.kyori")
    }

    getByName("build") {
        dependsOn("shadowJar")
    }
}
