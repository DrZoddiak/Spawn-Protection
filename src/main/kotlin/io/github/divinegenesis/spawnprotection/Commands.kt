package io.github.divinegenesis.spawnprotection


import net.kyori.adventure.text.Component
import org.spongepowered.api.Sponge
import org.spongepowered.api.command.Command.*
import org.spongepowered.api.command.CommandResult
import org.spongepowered.api.command.parameter.CommandContext
import org.spongepowered.api.command.exception.CommandException
import org.spongepowered.api.entity.living.player.server.ServerPlayer
import org.spongepowered.api.service.pagination.PaginationList

class Commands {

    var help = builder()
        .shortDescription(!"Help")
        .executor(this::helpResult)
        .permission("")
        .build()

    @Throws(CommandException::class)
    private fun helpResult(context: CommandContext): CommandResult {

        val sender = context.cause().root()

        if (sender is ServerPlayer) {
            paginationBuilder(
                !"""
                
                """.trimIndent()
            ).sendTo(sender)
        } else {
            return CommandResult.error(!"Error!")
        }
        return CommandResult.success()
    }

    private fun paginationBuilder(component: Component): PaginationList {
        val paginationService = Sponge.serviceProvider().paginationService()

        return paginationService.builder()
            .title(!"SpawnProtection")
            .padding((!"="))
            .contents(component)
            .build()
    }

}