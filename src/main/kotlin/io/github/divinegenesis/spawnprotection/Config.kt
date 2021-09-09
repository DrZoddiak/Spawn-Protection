package io.github.divinegenesis.spawnprotection

import org.spongepowered.configurate.objectmapping.ConfigSerializable
import org.spongepowered.configurate.objectmapping.meta.Setting

@ConfigSerializable
class Config {
    @Setting("io.github.divinegenesis.spawnprotection.Modules")
    val modules: Modules = Modules()
}

@ConfigSerializable
class Modules {
    @Setting("Interact-Item")
    val interactItemModule = true
}