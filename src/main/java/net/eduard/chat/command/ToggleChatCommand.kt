package net.eduard.chat.command

import net.eduard.api.lib.manager.CommandManager
import lib.modules.Mine
import net.eduard.chat.EduChat
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class ToggleChatCommand : CommandManager("chat", "togglechat") {
    init{
        description ="Ativar ou desativar o chat"
    }
    var messageOn = "§6Chat ativado!"
    var messageOff = "§6Chat foi temporariamente desativado!"

    override fun onCommand(sender: CommandSender, command: Command,
                           label: String, args: Array<String>): Boolean {
        if (EduChat.instance.chat.isChatEnabled) {
            EduChat.instance.chat.isChatEnabled = false
            lib.modules.Mine.broadcast(messageOff)
        } else {
            EduChat.instance.chat.isChatEnabled = true
            lib.modules.Mine.broadcast(messageOn)
        }
        return true
    }


}