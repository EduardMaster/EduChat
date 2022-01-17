package net.eduard.chat.command

import net.eduard.api.lib.manager.CommandManager
import net.eduard.api.lib.modules.Extra
import net.eduard.chat.EduChatPlugin
import net.eduard.chat.core.ChatMessages
import org.bukkit.entity.Player

class ResponseCommand : CommandManager("response", "responder", "r") {
    init {
        usage = "/responder <mensagen>"
        description = "Envia uma mensagem para a ultima pessoa que te mandou mensagem"
    }

    override fun playerCommand(player: Player, args: Array<String>) {
        if (args.isEmpty()) {
            sendUsage(player)
        } else {
            if (!EduChatPlugin.instance.lastPrivateMessage.containsKey(player)) {
                player.sendMessage(ChatMessages.noTell)
            } else {
                val alvo = EduChatPlugin.instance.lastPrivateMessage[player]!!
                player.chat("/tell " + alvo.name + " " + Extra.getText(0, *args))
            }
        }
    }


}