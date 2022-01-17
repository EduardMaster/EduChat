package net.eduard.chat.core

import net.eduard.api.lib.event.ChatMessageEvent
import net.eduard.api.lib.kotlin.chat
import net.eduard.api.lib.kotlin.formatColors
import net.eduard.api.lib.modules.FakePlayer
import net.eduard.api.lib.modules.Mine
import net.eduard.api.lib.modules.VaultAPI
import net.eduard.chat.EduChatPlugin
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.entity.Player
import java.util.*

/**
 * Canal de Chat
 *
 * @author Eduard
 */
class ChatChannel() {

    var name: String = "canaldetexto"
    var format: String = "formato"
    var prefix = ""
    var suffix = ""
    var distance = 100
    var isGlobal = true
    var permission: String = "permissao.falar"
    var command: String = "/canal"
    var onClick = "/tell %player "
    var onHover = mutableListOf("§aVida: {player_health}")
    val distanceSquared get() = distance * distance


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
        val cor = EduChatPlugin.instance.manager.colors[FakePlayer(player)] ?: ""
        val event = ChatMessageEvent(player, name, message)
        if (player.hasPermission("chat.color"))
            event.message = event.message.formatColors()
        event.format = format
        event.playersInChannel.addAll(getPlayers(player))
        event.onClickCommand = onClick
        event.onHoverText.addAll(onHover)
        event.resetTags()
        event.setTagValue("message", event.message)
        event.setTagValue("channel-prefix", prefix)
        event.setTagValue("channel-suffix", suffix)
        event.setTagValue("player", player.name)
        event.setTagValue("channel", name)
        event.setTagValue("player-group-prefix", VaultAPI.getPlayerGroupPrefix(player.name))
        event.setTagValue("player-color", cor)
        event.setTagValue("color", cor)
        Mine.callEvent(event)
        if (event.isCancelled) return
        val players = event.playersInChannel
        var finalMessage = event.format
        finalMessage = Mine.getReplacers(finalMessage, player);
        for ((key, value) in event.tags) {
            finalMessage = finalMessage.replace("{${key.toLowerCase()}}", value , false)
            finalMessage = finalMessage.replace("(${key.toLowerCase()})" , value , false)
        }
        val newList = mutableListOf<String>()
        for (line in event.onHoverText) {
            newList.add(Mine.getReplacers(line, player))
        }
        event.onHoverText = newList

        when (chatType) {
            ChatType.BUKKIT -> {
                for (playerLoop in players) {
                    playerLoop.sendMessage(finalMessage)
                }
            }
            ChatType.SPIGOT -> {
                val text: TextComponent = finalMessage.chat as TextComponent
                val clickEvent = ClickEvent(
                    ClickEvent.Action.SUGGEST_COMMAND,
                    event.onClickCommand
                        .replace("%player", player.name)
                )
                text.clickEvent = clickEvent
                val textBuilder = ComponentBuilder("")
                textBuilder.append(event.onHoverText.joinToString("\n"))
                val hoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT, textBuilder.create())
                text.hoverEvent = hoverEvent
                for (alvo in players) {
                    if (alvo != player) {
                        text.text = finalMessage
                            .replace(alvo.name, "§6@§n" + alvo.name + "§r", true)
                    }
                    alvo.spigot().sendMessage(text)
                }
            }
        }
    }

    fun getPlayers(player: Player): List<Player> {
        val list: MutableList<Player> = ArrayList()
        for (alvo in Mine.getPlayers()) {
            if (!isGlobal) {
                if (alvo.world != player.world) {
                    continue
                }
                if (distance > 0) {
                    val newDistance = alvo.location.distanceSquared(player.location)
                    if (newDistance > distanceSquared) continue
                }
            }
            list.add(alvo)
        }
        return list
    }

}