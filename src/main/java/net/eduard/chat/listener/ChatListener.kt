package net.eduard.chat.listener

import net.eduard.api.lib.manager.EventsManager
import lib.modules.Extra
import net.eduard.chat.EduChat
import net.eduard.chat.core.ChatMessages
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.PlayerCommandPreprocessEvent

class ChatListener : EventsManager(){



    @EventHandler
    fun onCommand(event: PlayerCommandPreprocessEvent) {
        val msg = event.message
        val cmd = lib.modules.Extra.getCommandName(msg)
        for (channel in EduChat.instance.chat.channels) {
            if (lib.modules.Extra.startWith("/" + channel.name, cmd)) {
                channel.chat(event.player, msg.replaceFirst(cmd.toRegex(), ""), EduChat.instance.chat.chatType)
                event.isCancelled = true
                break
            }
            if (lib.modules.Extra.startWith("/" + channel.command, cmd)) {
                channel.chat(event.player, msg.replaceFirst(cmd.toRegex(), ""), EduChat.instance.chat.chatType)
                event.isCancelled = true
                break
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onChat(event: AsyncPlayerChatEvent) {
        val player = event.player
        if (!EduChat.instance.chat.isChatEnabled &&
            !player.hasPermission(ChatMessages.chatDisabledBypassPermission)) {
            event.isCancelled = true
            player.sendMessage(ChatMessages.chatDisabled)
            return
        }
        event.isCancelled = true
        EduChat.instance.chat.chatDefault.chat(event.player, event.message, EduChat.instance.chat.chatType)
    }
}