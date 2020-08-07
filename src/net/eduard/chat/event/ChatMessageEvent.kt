package net.eduard.chat.event

import net.eduard.chat.core.ChatChannel
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.HandlerList
import org.bukkit.event.player.PlayerEvent
import java.lang.Exception

class ChatMessageEvent(player: Player, channel: ChatChannel, message: String) : PlayerEvent(player), Cancellable {
    override fun getHandlers(): HandlerList {
        return handlerList
    }

    var tags = mutableMapOf<String, String>()
    var message = ""
    var format: String = "Formato"
    var onClickCommand: String? = null
    var playersInChannel = mutableListOf<Player>()
    var onHoverText = mutableListOf<String>()
    private var cancelled = false
    override fun isCancelled(): Boolean {
        return cancelled
    }

    override fun setCancelled(cancel: Boolean) {
        cancelled = cancel
    }

    var channel: ChatChannel? = null

    init {
        this.message = message
        if (player.hasPermission("chat.color")) this.message = ChatColor.translateAlternateColorCodes('&', message)
        format = channel.format
        playersInChannel.addAll(channel.getPlayers(player))
        resetTags()
    }

    fun setTagValue(tag: String, value: String) {
        tags[tag] = value
    }

    fun getTagValue(tag: String): String? {
        return tags[tag]
    }

    fun resetTags() {
        for (i in format.indices) {
            if (format[i] == '{') {
                try {
                    val tag = format.substring(i + 1).split('}').toTypedArray()[0].toLowerCase()
                    if (!tags.containsKey(tag)) tags[tag] = ""
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
        }
        for (i in format.indices) {
            if (format[i] == '(') {
                try {
                    val tag = format.substring(i + 1).split(')').toTypedArray()[0].toLowerCase()
                    if (!tags.containsKey(tag)) tags[tag] = ""
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
        }
    }


    companion object {

        @JvmStatic
        val handlerList = HandlerList()
    }


}