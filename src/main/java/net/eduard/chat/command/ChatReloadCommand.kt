package net.eduard.chat.command

import net.eduard.api.lib.manager.CommandManager
import net.eduard.chat.EduChatPlugin
import org.bukkit.command.CommandSender

class ChatReloadCommand : CommandManager("chatreload", "educhatreload") {
    init {
        description = "Executa o reload do plugin"
    }

    override fun command(sender: CommandSender, args: Array<String>) {
        EduChatPlugin.instance.reload()
        sender.sendMessage("§aSistema de chat recarregado")
    }

}