package io.github.divinegenesis.spawnprotection

import not
import org.spongepowered.api.entity.living.player.server.ServerPlayer
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.entity.SpawnEntityEvent
import org.spongepowered.api.event.filter.cause.Root


class EventListener {
    @Listener
    fun onRespawn(event: SpawnEntityEvent, @Root player: ServerPlayer) {
        player.sendMessage(!"Hello!")
    }
}