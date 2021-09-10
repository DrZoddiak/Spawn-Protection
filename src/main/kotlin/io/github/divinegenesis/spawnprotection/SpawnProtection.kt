package io.github.divinegenesis.spawnprotection

import org.apache.logging.log4j.Logger
import com.google.inject.Inject
import net.kyori.adventure.text.Component
import org.apache.logging.log4j.LogManager
import org.spongepowered.api.Sponge
import org.spongepowered.api.command.Command
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.lifecycle.*
import org.spongepowered.plugin.PluginContainer
import org.spongepowered.plugin.builtin.jvm.Plugin

@Suppress("UNUSED_PARAMETER")
@Plugin("spawnprotection")
class SpawnProtection @Inject internal constructor(
    private val container: PluginContainer
) {

    companion object {
        val logger = logger<SpawnProtection>()
        lateinit var plugin: PluginContainer
    }

    @Listener
    fun onPluginConstruction(event: ConstructPluginEvent) {
        plugin = this.container

        logger.info("Spawn-Protection constructing..")
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
}

operator fun String.not() = Component.text(this)

inline fun <reified T> logger(): Logger = LogManager.getLogger(T::class.java)