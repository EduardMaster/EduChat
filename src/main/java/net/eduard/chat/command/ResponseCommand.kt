package net.eduard.chat.command

import net.eduard.api.lib.manager.CommandManager
import lib.modules.Extra
import lib.modules.Mine
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
        if (lib.modules.Mine.onlyPlayer(sender)) {
            val p = sender as Player
            if (args.isEmpty()) {
                sendUsage(sender)
            } else {
                if (!EduChat.instance.lastPrivateMessage.containsKey(p)) {
                    p.sendMessage(ChatMessages.noTell)
                } else {
                    val alvo = EduChat.instance.lastPrivateMessage[p]!!
                    alvo.chat("/tell " + alvo.name + " " + lib.modules.Extra.getText(0, *args))
                }
            }
        }
        return true
    }


}