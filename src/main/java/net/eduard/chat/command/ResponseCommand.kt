package net.eduard.chat.command

import net.eduard.api.lib.manager.CommandManager
import net.eduard.api.lib.modules.Extra
import net.eduard.api.lib.modules.Mine
import net.eduard.chat.EduChat
import net.eduard.chat.core.ChatMessages
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ResponseCommand : CommandManager("response", "responder", "r") {
    init {
        usage = "/responder <mensagen>"
        description = "Envia uma mensagem para a ultima pessoa que te mandou mensagem"
    }
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (Mine.onlyPlayer(sender)) {
            val player = sender as Player
            if (args.isEmpty()) {
                sendUsage(sender)
            } else {
                if (!EduChat.instance.lastPrivateMessage.containsKey(player)) {
                    player.sendMessage(ChatMessages.noTell)
                } else {
                    val alvo = EduChat.instance.lastPrivateMessage[player]!!
                    alvo.chat("/tell " + alvo.name + " " +Extra.getText(0, *args))
                }
            }
        }
        return true
    }


}