package net.eduard.chat.command

import net.eduard.api.lib.game.FakePlayer
import net.eduard.api.lib.manager.CommandManager
import net.eduard.chat.EduChat
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ToggleTellCommand : CommandManager("toggletell") {
    override fun onCommand(sender: CommandSender, command: Command,
                           label: String, args: Array<String>): Boolean {
        if (sender is Player) {
            val p = sender
            val fake= FakePlayer(p)
            if (EduChat.instance.chat.tellDisabled.contains(fake)) {
                EduChat.instance.chat.tellDisabled.remove(fake)
                p.sendMessage("§cVocê desativou mensagens privadas")
            } else {
                EduChat.instance.chat.tellDisabled.add(fake)
                p.sendMessage("§aVocê ativou mensagens privadas")
            }
        }
        return true
    }
}