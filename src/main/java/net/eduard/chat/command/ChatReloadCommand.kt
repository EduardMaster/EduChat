package net.eduard.chat.command

import net.eduard.api.lib.manager.CommandManager
import net.eduard.chat.EduChat
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class ChatReloadCommand : CommandManager("chatreload", "educhatreload") {
    init {
        description = "Executa o reload do plugin"
    }

    override fun onCommand(sender: CommandSender, command: Command,
                           label: String, args: Array<String>): Boolean {
        EduChat.instance.reload()
        sender.sendMessage("§aSistema de chat recarregado")
        return true
    }
}