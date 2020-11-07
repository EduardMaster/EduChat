package net.eduard.chat.core

import lib.modules.FakePlayer
import net.eduard.api.lib.manager.EventsManager
import lib.modules.Extra
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import java.util.*

class ChatManager  {
    var format = "(channel)§r(player)§8:§r(color)(message)"


    val chatDefault: ChatChannel
        get() = channels[0]

    var chatType = ChatType.BUKKIT
    var isChatEnabled = false
    var channels = mutableListOf<ChatChannel>()
    var tellDisabled = ArrayList<lib.modules.FakePlayer>()
    var colors: MutableMap<lib.modules.FakePlayer, String> = HashMap()
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


}