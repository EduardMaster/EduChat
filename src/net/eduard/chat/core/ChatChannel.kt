package net.eduard.chat.core

import net.eduard.api.lib.game.FakePlayer
import net.eduard.api.lib.modules.Mine
import net.eduard.api.lib.modules.VaultAPI
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
class ChatChannel() {
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
    var onClick = "/tell \$player "
    var onHover = mutableListOf("§aVida: \$player_health")


    constructor(name: String, format: String, prefix: String, suffix: String, command: String) : this() {
        this.format = format
        this.name = name
        this.prefix = prefix
        this.suffix = suffix
        this.command = command
        permission = "chat.$name"
    }

    fun chat(player: Player, message: String, chatType: ChatType) {
        if (message.isEmpty()) {
            player.sendMessage("§cNão pode enviar mensagem vazia")
            return
        }
        if (!player.hasPermission(permission)) {
            player.sendMessage(ChatMessages.chatPermission)
            return
        }
        val cor = manager.colors[FakePlayer(player)] ?: ""
        val event = ChatMessageEvent(player, this, message)
        event.setTagValue("message", event.message)
        event.setTagValue("channel-prefix", prefix)
        event.setTagValue("channel-suffix", suffix)
        event.setTagValue("player", player.name)
        event.setTagValue("channel", prefix)
        event.setTagValue("player-group-prefix", VaultAPI.getPlayerGroupPrefix(player.name))
        event.setTagValue("player-color", cor)
        event.setTagValue("color", cor)
        Mine.callEvent(event)
        if (event.isCancelled) return
        val players = event.playersInChannel
        var formato = event.format
        for ((key, value) in event.tags) {
            formato = formato.replace("{" + key.toLowerCase() + "}", value)
            formato = formato.replace("(" + key.toLowerCase() + ")", value)
        }
        formato = Mine.getReplacers(formato,player);
        val newList = mutableListOf<String>()
        for (line in event.onHoverText) {
            newList.add(Mine.getReplacers(line, player))
        }
        event.onHoverText = newList

//
        when (chatType) {
            ChatType.BUKKIT -> {
                for (p in players) {
                    p.sendMessage(formato)
                }
            }
            ChatType.SPIGOT -> {
                val text = TextComponent(formato)

                val clickEvent = ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,
                        event.onClickCommand.replace("\$player", player.name))
                text.clickEvent = clickEvent


                val textBuilder = ComponentBuilder("")


                textBuilder.append(event.onHoverText.joinToString("\n"))

                val hoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT, textBuilder.create())
                text.hoverEvent = hoverEvent

                for (p in players) {
                    p.spigot().sendMessage(text)
                }
            }
            ChatType.FANCYFUL -> {
                val text = FancyMessage(formato)

                text.suggest(event.onClickCommand.replace("\$player", player.name))


                text.tooltip(event.onHoverText)

                text.send(players)
            }
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