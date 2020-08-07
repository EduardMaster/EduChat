package net.eduard.chat.command

import net.eduard.api.lib.game.FakePlayer
import net.eduard.api.lib.manager.CommandManager
import net.eduard.chat.EduChat
import org.bukkit.Sound
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ColorCommand : CommandManager("color", "cor") {
    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<String>): Boolean {
        if (sender is Player) {
            val p = sender
            if (args.isEmpty()) {
                sendUsage(sender)
            } else {
                val cor = args[0]
                if (cor.equals("reset", ignoreCase = true)) {
                    EduChat.instance.chat.colors[FakePlayer(p)] = ""
                    p.sendMessage("§aCor removida com sucesso!")
                    return true
                }
                EduChat.instance.chat.colors[FakePlayer(p)] = cor
                p.sendMessage("§aCor alterada com sucesso.")
                p.playSound(p.location, Sound.LEVEL_UP, 1f, 1f)
            }
        }
        return true
    }

    init {
        usage = "§c/cor <cor>"
    }
}