package net.eduard.chat.command

import net.eduard.api.lib.manager.CommandManager
import net.eduard.api.lib.modules.Extra
import net.eduard.api.lib.modules.Mine
import net.eduard.chat.EduChat
import net.eduard.chat.core.ChatMessages
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class TellCommand : CommandManager("tell", "privado", "pm", "pv", "t") {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (Mine.onlyPlayer(sender)) {
            val p = sender as Player
            if (args.size <= 1) {
                sendUsage(sender)
            } else {
                if (Mine.existsPlayer(sender, args[0])) {
                    val target = Mine.getPlayer(args[0])
                    val message = Extra.getText(1, *args)
                    if (!EduChat.instance.chat.tellDisabled.contains(target)) {
                        sender.sendMessage(ChatMessages.tellDisabled.replace("\$player", target.name))
                    } else {
                        EduChat.instance.lastPrivateMessage[target] = p
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