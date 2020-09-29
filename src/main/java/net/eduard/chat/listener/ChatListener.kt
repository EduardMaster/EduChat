package net.eduard.chat.listener

import net.eduard.api.lib.manager.EventsManager
import net.eduard.api.lib.modules.Extra
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
        val cmd = Extra.getCommandName(msg)
        for (channel in EduChat.instance.chat.channels) {
            if (Extra.startWith("/" + channel.name, cmd)) {
                channel.chat(event.player, msg.replaceFirst(cmd.toRegex(), ""), EduChat.instance.chat.chatType)
                event.isCancelled = true
                break
            }
            if (Extra.startWith("/" + channel.command, cmd)) {
                channel.chat(event.player, msg.replaceFirst(cmd.toRegex(), ""), EduChat.instance.chat.chatType)
                event.isCancelled = true
                break
            }
        }
    }
    @EventHandler
    fun chat(e: AsyncPlayerChatEvent) {
        val p = e.player
        if (!EduChat.instance.chat.isChatEnabled && !p.hasPermission(ChatMessages.chatDisabledBypassPermission)) {
            e.isCancelled = true
            p.sendMessage(ChatMessages.chatDisabled)
        }
    }
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onChat(event: AsyncPlayerChatEvent) {
        event.isCancelled = true
        EduChat.instance.chat.chatDefault.chat(event.player, event.message, EduChat.instance.chat.chatType)
    }
}