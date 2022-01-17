package net.eduard.chat.command

import net.eduard.api.lib.manager.CommandManager
import net.eduard.api.lib.modules.FakePlayer
import net.eduard.chat.EduChatPlugin
import org.bukkit.Sound
import org.bukkit.entity.Player

class ColorCommand : CommandManager("color", "cor") {
    init {
        usage = "/cor <cor>"
        description = "Altera a cor da sua mensagem no chat"
    }

    override fun playerCommand(player: Player, args: Array<String>) {
        if (args.isEmpty()) {
            sendUsage(player)
            return
        }
        var cor = args[0]
        if (cor.equals("reset", ignoreCase = true)) {
            EduChatPlugin.instance.manager.colors[FakePlayer(player)] = ""
            player.sendMessage("§aCor removida com sucesso!")
            return
        }
        if (cor.length != 1) {
            player.sendMessage("§cVocê pode escolher apenas 1 cor§f de [A-F] e [1-9]");
            return;
        }

        cor = "§$cor";
        EduChatPlugin.instance.manager.colors[FakePlayer(player)] = cor
        player.sendMessage("§aCor alterada com sucesso.")
        player.playSound(player.location, Sound.LEVEL_UP, 1f, 1f)

    }


}