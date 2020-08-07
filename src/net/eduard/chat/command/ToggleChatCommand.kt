package net.eduard.chat.command

import net.eduard.api.lib.manager.CommandManager
import net.eduard.api.lib.modules.Mine
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.event.EventHandler
import org.bukkit.event.player.AsyncPlayerChatEvent

class ToggleChatCommand : CommandManager("chat", "togglechat") {
    var chatEnabled = true
    var messageOn = "§6Chat ativado!"
    var messageOff = "§6Chat foi temporariamente desativado!"
    var messageDisabled = "§cChat desativado!"
    var chatPerm = "togglechat.bypass"
    override fun onCommand(sender: CommandSender, command: Command,
                           label: String, args: Array<String>): Boolean {
        if (chatEnabled) {
            chatEnabled = false
            Mine.broadcast(messageOff)
        } else {
            chatEnabled = true
            Mine.broadcast(messageOn)
        }
        return true
    }

    @EventHandler
    fun event(e: AsyncPlayerChatEvent) {
        val p = e.player
        if (!chatEnabled && !p.hasPermission(chatPerm)) {
            e.isCancelled = true
            p.sendMessage(messageDisabled)
        }
    }
}