package net.eduard.chat.listener

import net.eduard.api.lib.manager.EventsManager
import net.eduard.api.lib.modules.Extra
import net.eduard.chat.EduChatPlugin
import net.eduard.chat.core.ChatMessages
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.PlayerCommandPreprocessEvent

class ChatListener : EventsManager() {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onCommand(event: PlayerCommandPreprocessEvent) {
        val message = event.message
        val cmd = Extra.getCommandName(message)
        val player = event.player
        for (channel in EduChatPlugin.instance.manager.channels) {
            if (Extra.startWith("/" + channel.name, cmd) ||
                Extra.startWith("/" + channel.command, cmd)
            ) {

                if (!EduChatPlugin.instance.manager.isChatEnabled &&
                    !player.hasPermission(ChatMessages.chatDisabledBypassPermission)) {
                    event.isCancelled = true
                    player.sendMessage(ChatMessages.chatDisabled)
                    return
                }
                channel.chat(event.player, message.replaceFirst(cmd.toRegex(), ""), EduChatPlugin.instance.manager.chatType)
                event.isCancelled = true
                break
            }

        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onChat(event: AsyncPlayerChatEvent) {
        val player = event.player
        if (!EduChatPlugin.instance.manager.isChatEnabled &&
            !player.hasPermission(ChatMessages.chatDisabledBypassPermission)) {
            event.isCancelled = true
            player.sendMessage(ChatMessages.chatDisabled)
            return
        }
        event.isCancelled = true
        EduChatPlugin.instance.manager.chatDefault.chat(event.player, event.message, EduChatPlugin.instance.manager.chatType)
    }
}