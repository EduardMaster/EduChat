package net.eduard.chat.command

import net.eduard.api.lib.manager.CommandManager
import net.eduard.api.lib.modules.Extra
import net.eduard.api.lib.modules.Mine
import net.eduard.chat.EduChat
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ResponseCommand : CommandManager("response", "responder", "r") {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (Mine.onlyPlayer(sender)) {
            val p = sender as Player
            if (args.isEmpty()) {
                sendUsage(sender)
            } else {
                if (!EduChat.instance.lastPrivateMessage.containsKey(p)) {
                    p.sendMessage("§cVocê não possui nenhuma conversa recente.")
                } else {
                    val alvo = EduChat.instance.lastPrivateMessage[p]
                    alvo!!.chat("/tell " + alvo.name + " " + Extra.getText(0, *args))
                }
            }
        }
        return true
    }

    init {
        usage = "§c/r <mensagen>"
    }
}