package net.eduard.chat.command

import net.eduard.api.lib.modules.FakePlayer
import net.eduard.api.lib.manager.CommandManager
import net.eduard.api.lib.modules.Extra
import net.eduard.api.lib.modules.Mine
import net.eduard.chat.EduChatPlugin
import net.eduard.chat.core.ChatMessages
import org.bukkit.entity.Player

class TellCommand : CommandManager("tell", "privado", "pm", "pv", "t") {

    init {
        usage = "/tell <player> <message>"
        description = "Manda mensagem para alguem"
    }

    override fun playerCommand(player: Player, args: Array<String>) {
        if (args.size < 2) {
            sendUsage(player)
            return
        }
        val targetName = args[0]
        val target: Player? = Mine.getPlayer(targetName)
        if (target == null) {
            player.sendMessage("§cO jogador $targetName esta Offline ou não existe.")
            return
        }
        val fakeTarget = FakePlayer(target)
        val message = Extra.getText(1, *args)
        if (EduChatPlugin.instance.manager.tellDisabled.contains(fakeTarget)) {
            player.sendMessage(ChatMessages.tellDisabled
                .replace("%player", target.name))
            return
        }
        EduChatPlugin.instance.lastPrivateMessage[player] = target
        EduChatPlugin.instance.lastPrivateMessage[target] = player
        player.sendMessage(
            ChatMessages.tellTo
                .replace("%target", target.name)
                .replace("%message", message)
        )
        target.sendMessage(ChatMessages.tellFrom
            .replace("%player", player.name)
                .replace("%message", message)
        )
    }

}