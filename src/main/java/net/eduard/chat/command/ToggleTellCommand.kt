package net.eduard.chat.command

import net.eduard.api.lib.modules.FakePlayer
import net.eduard.api.lib.manager.CommandManager
import net.eduard.chat.EduChatPlugin
import net.eduard.chat.core.ChatMessages
import org.bukkit.entity.Player

class ToggleTellCommand : CommandManager("toggletell") {
    init {
        description = "Ativar ou deativar mensagens privadas"
    }

    override fun playerCommand(player: Player, args: Array<String>) {
        val fake = FakePlayer(player)
        if (!EduChatPlugin.instance.manager.tellDisabled.contains(fake)) {
            EduChatPlugin.instance.manager.tellDisabled.add(fake)
            player.sendMessage(ChatMessages.tellOff)
        } else {
            EduChatPlugin.instance.manager.tellDisabled.remove(fake)
            player.sendMessage(ChatMessages.tellOn)
        }
    }

}