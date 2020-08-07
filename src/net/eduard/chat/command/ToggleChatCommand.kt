package net.eduard.chat.command

import net.eduard.api.lib.manager.CommandManager
import net.eduard.api.lib.modules.Mine
import net.eduard.chat.EduChat
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class ToggleChatCommand : CommandManager("chat", "togglechat") {
    var messageOn = "§6Chat ativado!"
    var messageOff = "§6Chat foi temporariamente desativado!"

    override fun onCommand(sender: CommandSender, command: Command,
                           label: String, args: Array<String>): Boolean {
        if (EduChat.chatEnabled) {
            EduChat.chatEnabled = false
            Mine.broadcast(messageOff)
        } else {
            EduChat.chatEnabled = true
            Mine.broadcast(messageOn)
        }
        return true
    }


}