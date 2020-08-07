package net.eduard.chat.core

import net.eduard.api.lib.game.FakePlayer
import net.eduard.api.lib.modules.Mine
import net.eduard.api.lib.modules.VaultAPI
import net.eduard.api.lib.storage.Storable
import net.eduard.chat.util.FancyMessage
import net.eduard.chat.event.ChatMessageEvent
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.entity.Player
import java.util.*

/**
 * Canal de Chat
 *
 * @author Eduard-PC
 */
class ChatChannel() : Storable<Any?> {
    @Transient
    lateinit var manager: ChatManager
    var name: String = "canaldetexto"
    var format: String = "formato"
    var prefix = ""
    var suffix = ""
    var distance = 100
    var isGlobal = true
    var permission: String = "permissao.falar"
    var command: String = "/canal"
    var disabled = false


    constructor(name: String, format: String, prefix: String, suffix: String, command: String) : this() {
        this.format = format
        this.name = name
        this.prefix = prefix
        this.suffix = suffix
        this.command = command
        permission = "chat.$name"
    }

    fun chat(player: Player, message: String, chatType: ChatType) {
        if (!player.hasPermission(permission)) {
            player.sendMessage(message)
            return
        }
        val cor = manager.colors[FakePlayer(player)]?: ""
        val event = ChatMessageEvent(player, this, message)
        event.setTagValue("message", message)
        event.setTagValue("channel-prefix", prefix)
        event.setTagValue("channel-suffix", suffix)
        event.setTagValue("player", player.name)
        event.setTagValue("channel" , prefix)
        event.setTagValue("player-prefix" , VaultAPI.getPlayerGroupPrefix(player.name))
        event.setTagValue("player_color", cor)
        event.setTagValue("color", cor)
        Mine.callEvent(event)
        if (event.isCancelled) return
        val players = event.playersInChannel
        var formato = event.format
        for ((key, value) in event.tags) {
            formato = formato.replace("{" + key.toLowerCase() + "}", value)
        }

//
        if (chatType == ChatType.BUKKIT) {
            for (p in players) {
                p.sendMessage(formato)
            }
        } else if (chatType == ChatType.SPIGOT) {
            val text = TextComponent(formato)
            if (event.onClickCommand != null) {
                val clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, event.onClickCommand)
                text.clickEvent = clickEvent
            }
            if (event.onHoverText.isNotEmpty()) {
                val textBuilder = ComponentBuilder("")
                for (line in event.onHoverText) {
                    textBuilder.append(line)
                }
                val hoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT, textBuilder.create())
                text.hoverEvent = hoverEvent
            }
            for (p in Mine.getPlayers()) {
                p.spigot().sendMessage(text)
            }
        } else if (chatType == ChatType.FANCYFUL) {
            val text = FancyMessage(formato)
            if (event.onClickCommand != null) {
                text.command(event.onClickCommand)
            }
            if (event.onHoverText.isNotEmpty()) {
                text.tooltip(event.onHoverText)
            }
            text.send(players)
        }
    }

    fun getPlayers(player: Player): List<Player> {
        val list: MutableList<Player> = ArrayList()
        for (p in Mine.getPlayers()) {
            if (!isGlobal) {
                if (p.world != player.world) {
                    continue
                }
                if (distance > 0) {
                    val newDistance = p.location.distance(player.location)
                    if (newDistance > distance) continue
                }
            }
            list.add(p)
        }
        return list
    }

}