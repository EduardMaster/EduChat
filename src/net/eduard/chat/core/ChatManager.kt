package net.eduard.chat.core

import net.eduard.api.lib.game.FakePlayer
import net.eduard.api.lib.manager.EventsManager
import net.eduard.api.lib.modules.Extra
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import java.util.*

class ChatManager : EventsManager() {
    var format = "(channel)§r(player)§8:§r(color)(message)"


    val chatDefault: ChatChannel
        get() = channels[0]

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

    init {
        register(ChatChannel("local", "", "§8(§e§lL§8)", "", "l"))
        register(ChatChannel("global", "", "§8(§6§lG§8)", "", "g"))
        register(ChatChannel("staff", "", "&8(&dSTAFF&8)", "", "sc"))
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onChat(event: AsyncPlayerChatEvent) {
        event.isCancelled = true
        chatDefault.chat(event.player, event.message, chatType)
    }

    @EventHandler
    fun onCommand(event: PlayerCommandPreprocessEvent) {
        val msg = event.message
        val cmd = Extra.getCommandName(msg)
        for (channel in channels) {
            if (Extra.startWith("/" + channel.name, cmd)) {
                channel.chat(event.player, msg.replaceFirst(cmd.toRegex(), ""), chatType)
                event.isCancelled = true
                break
            }
            if (Extra.startWith("/" + channel.command, cmd)) {
                channel.chat(event.player, msg.replaceFirst(cmd.toRegex(), ""), chatType)
                event.isCancelled = true
                break
            }
        }
    }

}