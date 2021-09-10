package io.github.divinegenesis.spawnprotection

import org.spongepowered.api.Sponge
import org.spongepowered.api.data.Keys
import org.spongepowered.api.entity.living.player.server.ServerPlayer
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.entity.DamageEntityEvent
import org.spongepowered.api.event.entity.living.player.RespawnPlayerEvent
import org.spongepowered.api.event.filter.cause.First
import org.spongepowered.api.scheduler.Task
import org.spongepowered.api.service.permission.SubjectData
import java.util.concurrent.TimeUnit


class EventListener {
    @Listener
    fun onRespawn(event: RespawnPlayerEvent.Post) {
        //If recreation wasn't because of death return
        val player = event.entity()

        player.offer(Keys.INVULNERABLE, true)

        val globalData = SubjectData.GLOBAL_CONTEXT
        val subjectData = player.subjectData()
        val optionKey = "protect-time"

        /*
        lp user asexualdinosaur meta set protect-time 10
         */


        //Temporary, luckperms would set this
        subjectData.setOption(globalData, optionKey, "10")

        //We know it's there for now
        val time = subjectData.options(globalData)[optionKey]!!.toLong()
        var timer = time

        player.sendMessage(!"You have $time seconds of invulnerability")

        //Create the task


        val countDownTask =

            Task.builder()
            .interval(1, TimeUnit.SECONDS)
            .name("${player.name()}-countdown")
            .execute(Runnable {
                if (timer <= 5) {
                    player.sendMessage(!"$timer")
                }
                timer--
            })
            .plugin(SpawnProtection.plugin)
            .build()



        val spawnProtectionTask = Task.builder()
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
        //schedule the task
        Sponge.asyncScheduler().submit(countDownTask)
        Sponge.asyncScheduler().submit(spawnProtectionTask)
    }
}