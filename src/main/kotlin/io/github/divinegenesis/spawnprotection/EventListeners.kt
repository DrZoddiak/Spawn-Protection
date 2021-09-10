package io.github.divinegenesis.spawnprotection

import net.kyori.adventure.text.format.NamedTextColor
import org.spongepowered.api.Sponge
import org.spongepowered.api.data.Keys
import org.spongepowered.api.entity.living.player.server.ServerPlayer
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.entity.living.player.RespawnPlayerEvent
import org.spongepowered.api.event.network.ServerSideConnectionEvent
import org.spongepowered.api.scheduler.Task
import org.spongepowered.api.service.permission.SubjectData
import java.util.concurrent.TimeUnit

class EventListener {
    private val optionKey = "protect-time"

    @Listener
    fun onRespawn(event: RespawnPlayerEvent.Post) {
        spawnProtection(event.entity())
    }

    @Listener
    fun onSpawn(event: ServerSideConnectionEvent.Join) {
        spawnProtection(event.player())
    }

    private fun spawnProtection(player: ServerPlayer) {
        val subjectData = player.subjectData()
        val globalData = SubjectData.GLOBAL_CONTEXT

        //For debugging purposes
        subjectData.setOption(globalData, optionKey, "10")

        val time = subjectData.options(globalData)[optionKey]
        if (time.isNullOrEmpty() || time == "0") {
            return
        }
        player.offer(Keys.INVULNERABLE, true)

        player.sendMessage((!"You have $time seconds of invulnerability").color(NamedTextColor.GREEN))

        //schedule the task
        Sponge.asyncScheduler().submit(countDownTimer(player, time.toLong()))
        Sponge.asyncScheduler().submit(protectionTimer(player, time.toLong()))
    }

    private fun protectionTimer(player: ServerPlayer, time: Long): Task {

        return Task.builder()
            .delay(time, TimeUnit.SECONDS)
            .name("${player.name()}-spawn-protection")
            .execute(Runnable {
                player.offer(Keys.INVULNERABLE, false)
                Sponge.asyncScheduler().tasks(SpawnProtection.plugin).forEach { task ->
                    if (task.name().startsWith(player.name())) {
                        task.cancel()
                    }
                }
            })
            .plugin(SpawnProtection.plugin)
            .build()
    }

    private fun countDownTimer(player: ServerPlayer, timer: Long): Task {
        var timer = timer

        return Task.builder()
            .interval(1, TimeUnit.SECONDS)
            .name("${player.name()}-countdown")
            .execute(Runnable {
                if (timer <= 5) {
                    if (timer != 0L) { //Occasionally 0 would be sent to the player
                        player.sendMessage((!"$timer").color(NamedTextColor.RED))
                    }
                }
                timer--
            })
            .plugin(SpawnProtection.plugin)
            .build()
    }
}