package net.eduard.chat.listener

import net.eduard.api.lib.manager.EventsManager
import net.eduard.chat.EduChat
import net.eduard.chat.core.ChatMessages
import org.bukkit.event.EventHandler
import org.bukkit.event.player.AsyncPlayerChatEvent

class ChatListener : EventsManager(){

    @EventHandler
    fun event(e: AsyncPlayerChatEvent) {
        val p = e.player
        if (!EduChat.instance.chat.isChatEnabled && !p.hasPermission(ChatMessages.chatDisabledBypassPermission)) {
            e.isCancelled = true
            p.sendMessage(ChatMessages.chatDisabled)
        }
    }
}