import org.spongepowered.gradle.plugin.config.PluginLoaders
import org.spongepowered.plugin.metadata.model.PluginDependency

plugins {
    kotlin("jvm") version "1.4.21"
    id("org.spongepowered.gradle.plugin") version "2.0.0"
}

project.group = "dev.divinegenesis"

repositories {
    mavenCentral()
}

sponge {
    apiVersion("8.0.0-SNAPSHOT")
    license("CHANGEME")
    loader {
        name(PluginLoaders.JAVA_PLAIN)
        version("1.0")
    }
    plugin("spawnprotection") {
        displayName("SpawnProtection")
        entrypoint("io.github.divinegenesis.spawnprotection.SpawnProtection")
        description("Grants users spawn protection when they respawn!")
        links {
            homepage("https://github.com/DivineGenesis/SpawnProtection")
            source("https://github.com/DivineGenesis/SpawnProtection")
            issues("https://github.com/DivineGenesis/SpawnProtection/issues")
        }
        contributor("DrZodd") {
            description("Dev")
        }
        dependency("spongeapi") {
            loadOrder(PluginDependency.LoadOrder.AFTER)
            optional(false)
        }
    }
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.spongepowered:configurate-extra-kotlin:4.1.1")
}

tasks {
    val java = "11"
    compileKotlin {
        kotlinOptions { jvmTarget = java }
        sourceCompatibility = java
    }
}