package net.eduard.chat.command

import lib.modules.FakePlayer
import net.eduard.api.lib.manager.CommandManager
import lib.modules.Extra
import lib.modules.Mine
import net.eduard.chat.EduChat
import net.eduard.chat.core.ChatMessages
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class TellCommand : CommandManager("tell", "privado", "pm", "pv", "t") {

    init{
        usage = "/tell <player> <message>"
        description = "Manda mensagem para alguem"
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (lib.modules.Mine.onlyPlayer(sender)) {
            val p = sender as Player
            if (args.size < 2) {
                sendUsage(sender)
            } else {
                if (lib.modules.Mine.existsPlayer(sender, args[0])) {
                    val target = lib.modules.Mine.getPlayer(args[0])
                    val fakeTarget = lib.modules.FakePlayer(target)
                    val message = lib.modules.Extra.getText(1, *args)
                    if (EduChat.instance.chat.tellDisabled.contains(fakeTarget)) {
                        sender.sendMessage(ChatMessages.tellDisabled.replace("\$player", target.name))
                    } else {
                        EduChat.instance.lastPrivateMessage[p] = target
                        sender.sendMessage(ChatMessages.tellTo
                                .replace("\$target", target.name)
                                .replace("$>", "").replace("\$message", message))
                        target.sendMessage(ChatMessages.tellFrom.replace("\$player", sender.getName())
                                .replace("$>", "").replace("\$message", message))
                    }
                }
            }
        }
        return true
    }
}