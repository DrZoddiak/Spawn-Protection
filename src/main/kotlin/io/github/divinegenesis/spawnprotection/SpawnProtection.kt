package io.github.divinegenesis.spawnprotection

import org.apache.logging.log4j.Logger
import com.google.inject.Inject
import net.kyori.adventure.text.Component
import org.apache.logging.log4j.LogManager
import org.spongepowered.api.Engine
import org.spongepowered.api.Sponge
import org.spongepowered.api.command.Command
import org.spongepowered.api.config.ConfigDir
import org.spongepowered.api.config.DefaultConfig
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.lifecycle.*
import org.spongepowered.configurate.CommentedConfigurationNode
import org.spongepowered.configurate.ConfigurateException
import org.spongepowered.configurate.reference.ConfigurationReference
import org.spongepowered.configurate.reference.ValueReference
import org.spongepowered.plugin.PluginContainer
import org.spongepowered.plugin.builtin.jvm.Plugin
import java.nio.file.Path

@Suppress("UNUSED_PARAMETER")
@Plugin("spawnprotection")
class SpawnProtection @Inject internal constructor(
    private val container: PluginContainer,
    @DefaultConfig(sharedRoot = false) val reference: ConfigurationReference<CommentedConfigurationNode>,
    @ConfigDir(sharedRoot = false) val configDir: Path
) {

    companion object {
        val logger = logger<SpawnProtection>()
        lateinit var plugin: PluginContainer
        lateinit var config: ValueReference<Config, CommentedConfigurationNode>
        lateinit var configDir: Path
    }

    @Listener
    fun onPluginConstruction(event: ConstructPluginEvent) {
        plugin = this.container
        config = reference.referenceTo(Config::class.java)
        Companion.configDir = configDir

        logger.info("Spawn-Protection constructing..")
        try {
            this.reference.save()
        } catch (e: ConfigurateException) {
            logger.error("Unable to load configuration", e)
        }
        Sponge.eventManager().registerListeners(this.container, EventListener())
    }

    @Listener
    fun onRegisterCommand(event: RegisterCommandEvent<Command.Parameterized>) {
        logger.info("Registering commands..")

        event.register(
            this.container,
            Commands().help, "spawnprotect"
        )
    }

    @Listener
    fun onServerStart(event: StartedEngineEvent<Engine>) {
    }

    @Listener
    fun onReload(event: RefreshGameEvent) {
        logger.info("Game refreshed...")
    }

    @Listener
    fun onShutdown(event: StoppingEngineEvent<Engine>) {
        logger.info("Server shutting down")
    }
}

operator fun String.not() = Component.text(this)

inline fun <reified T> logger(): Logger = LogManager.getLogger(T::class.java)