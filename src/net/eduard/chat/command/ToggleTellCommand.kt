package net.eduard.chat.command

import net.eduard.api.lib.game.FakePlayer
import net.eduard.api.lib.manager.CommandManager
import net.eduard.chat.EduChat
import net.eduard.chat.core.ChatMessages
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ToggleTellCommand : CommandManager("toggletell") {
    init{
        description= "Ativar ou deativar mensagens privadas"
    }
    override fun onCommand(sender: CommandSender, command: Command,
                           label: String, args: Array<String>): Boolean {
        if (sender is Player) {
            val p = sender
            val fake= FakePlayer(p)
            if (!EduChat.instance.chat.tellDisabled.contains(fake)) {
                EduChat.instance.chat.tellDisabled.add(fake)
                p.sendMessage(ChatMessages.tellOff)
            } else {
                EduChat.instance.chat.tellDisabled.remove(fake)
                p.sendMessage(ChatMessages.tellOn)
            }
        }
        return true
    }
}