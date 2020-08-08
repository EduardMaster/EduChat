package net.eduard.chat.event

import net.eduard.api.lib.kotlin.formatColors
import net.eduard.chat.core.ChatChannel
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.HandlerList
import org.bukkit.event.player.PlayerEvent
import java.lang.Exception

class ChatMessageEvent(player: Player, var channel: ChatChannel, var message: String) : PlayerEvent(player), Cancellable {



    var format: String
    var onClickCommand: String
    var onHoverText = mutableListOf<String>()
    var playersInChannel = mutableListOf<Player>()
    var tags = mutableMapOf<String, String>()
    private var cancelled = false
    override fun isCancelled(): Boolean {
        return cancelled
    }

    override fun setCancelled(cancel: Boolean) {
        cancelled = cancel
    }


    init {

        if (player.hasPermission("chat.color")) this.message = this.message.formatColors()
        format = channel.format
        playersInChannel.addAll(channel.getPlayers(player))
        onClickCommand = channel.onClick
        onHoverText.addAll(channel.onHover)
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
    override fun getHandlers(): HandlerList {
        return handlerList
    }

}