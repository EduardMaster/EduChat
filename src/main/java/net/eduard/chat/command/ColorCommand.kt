package net.eduard.chat.command

import net.eduard.api.lib.manager.CommandManager
import net.eduard.api.lib.modules.FakePlayer
import net.eduard.chat.EduChat
import org.bukkit.Sound
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ColorCommand : CommandManager("color", "cor") {
    init {
        usage = "§c/cor <cor>"
        description = "Altera a cor da sua mensagem no chat"
    }
    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<String>): Boolean {
        if (sender is Player) {
            val player = sender
            if (args.isEmpty()) {
                sendUsage(sender)
            } else {
                var cor = args[0]
                if (cor.equals("reset", ignoreCase = true)) {
                    EduChat.instance.chat.colors[FakePlayer(player)] = ""
                    player.sendMessage("§aCor removida com sucesso!")
                    return true
                }
                if (cor.length != 1){
                    player.sendMessage("§cVocê pode escolher apenas 1 cor§f de [A-F] e [1-9]");
                    return true;
                }

                cor = "§$cor";
                EduChat.instance.chat.colors[FakePlayer(player)] = cor
                player.sendMessage("§aCor alterada com sucesso.")
                player.playSound(player.location, Sound.LEVEL_UP, 1f, 1f)
            }
        }
        return true
    }

}