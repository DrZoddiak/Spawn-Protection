import org.spongepowered.configurate.objectmapping.ConfigSerializable
import org.spongepowered.configurate.objectmapping.meta.Comment
import org.spongepowered.configurate.objectmapping.meta.Setting

@ConfigSerializable
class Config {
    @Setting("Modules")
    val modules: Modules = Modules()
}

@ConfigSerializable
class Modules {
    @Setting("Interact-Item")
    val interactItemModule = true
}