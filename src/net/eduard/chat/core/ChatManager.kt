package net.eduard.chat.core

import net.eduard.api.lib.game.FakePlayer
import net.eduard.api.lib.manager.EventsManager
import net.eduard.api.lib.modules.Extra
import net.eduard.api.lib.storage.Storable.StorageAttributes
import org.bukkit.OfflinePlayer
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import java.util.*

class ChatManager : EventsManager() {
    var format = "(channel) (player): (color) (message)"
    var messageChatDisabled = "&cChat desabilitado tempariamente!"
    var messageChatPermission = "§cVoc§ n§o tem permiss§o para falar neste Chat!"

    @StorageAttributes(reference = true)
    var chatDefault: ChatChannel? = null
    var chatType = ChatType.BUKKIT
    var isChatEnabled = false
    var channels = mutableListOf<ChatChannel>()
    var tellDisabled = ArrayList<FakePlayer>()
    var colors: MutableMap<FakePlayer, String> = HashMap()
    fun register(channel: ChatChannel) {
        if (channel.format.isEmpty()) {
            channel.format = format
        }
        channels.add(channel)
    }

    fun unregister(channel: ChatChannel) {
        channels.remove(channel)
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onChat(event: AsyncPlayerChatEvent) {
        event.isCancelled = true
        chatDefault!!.chat(event.player, event.message, chatType)
    }

    @EventHandler
    fun onCommand(event: PlayerCommandPreprocessEvent) {
        val msg = event.message
        val cmd = Extra.getCommandName(msg)
        for (channel in channels) {
            if (Extra.startWith(cmd, "/" + channel.name)) {
                channel.chat(event.player, msg.replaceFirst(cmd.toRegex(), ""), chatType)
                event.isCancelled = true
                break
            }
            if (Extra.startWith(cmd, "/" + channel.command)) {
                channel.chat(event.player, msg.replaceFirst(cmd.toRegex(), ""), chatType)
                event.isCancelled = true
                break
            }
        }
    }

    init {
        val canal = ChatChannel("local", "", "§e§l(L) ", "", "l")
        chatDefault = canal
        register(canal)
        register(ChatChannel("global", "", "§6§l(G)", "", "g"))
    }
}