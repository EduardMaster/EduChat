package net.eduard.chat.core

import lib.modules.FakePlayer
import lib.modules.Mine
import lib.modules.VaultAPI
import net.eduard.chat.event.ChatMessageEvent
import net.eduard.chat.util.FancyMessage
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.ChatColor
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
        val cor = manager.colors[lib.modules.FakePlayer(player)] ?: ""
        val event = ChatMessageEvent(player, this, message)
        event.setTagValue("message", event.message)
        event.setTagValue("channel-prefix", prefix)
        event.setTagValue("channel-suffix", suffix)
        event.setTagValue("player", player.name)
        event.setTagValue("channel", prefix)
        event.setTagValue("player-group-prefix", lib.modules.VaultAPI.getPlayerGroupPrefix(player.name))
        event.setTagValue("player-color", cor)
        event.setTagValue("color", cor)
        lib.modules.Mine.callEvent(event)
        if (event.isCancelled) return
        val players = event.playersInChannel
        var finalMessage = event.format
        finalMessage = lib.modules.Mine.getReplacers(finalMessage,player);
        for ((key, value) in event.tags) {
            finalMessage = finalMessage.replace("{" + key.toLowerCase() + "}", value)
            finalMessage = finalMessage.replace("(" + key.toLowerCase() + ")", value)
        }



        val newList = mutableListOf<String>()
        for (line in event.onHoverText) {
            newList.add(lib.modules.Mine.getReplacers(line, player))
        }
        event.onHoverText = newList

//
        when (chatType) {
            ChatType.BUKKIT -> {
                for (p in players) {
                    p.sendMessage(finalMessage)
                }
            }
            ChatType.SPIGOT -> {
                val text = TextComponent(finalMessage)
                val bigLimit = 120
                val smallLimit = 70
                 if (finalMessage.length > bigLimit){
                    text.text = finalMessage.substring(0..smallLimit)
                    val secondText = ChatColor.getLastColors(text.text)+finalMessage.substring(smallLimit..bigLimit)
                    text.addExtra(secondText)
                    text.addExtra(ChatColor.getLastColors(secondText)+finalMessage.substring(bigLimit))

                } else if (finalMessage.length > smallLimit){
                    text.text = finalMessage.substring(0..smallLimit)
                    text.addExtra(ChatColor.getLastColors(text.text)+finalMessage.substring(smallLimit))
                }


                val clickEvent = ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,
                        event.onClickCommand.replace("\$player", player.name))
                text.clickEvent = clickEvent


                val textBuilder = ComponentBuilder("")


                textBuilder.append(event.onHoverText.joinToString("\n"))

                val hoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT, textBuilder.create())
                text.hoverEvent = hoverEvent

                for (alvo in players) {
                    alvo.spigot().sendMessage(text)
                }
            }
            ChatType.FANCYFUL -> {
                val text = FancyMessage(finalMessage)

                text.suggest(event.onClickCommand.replace("\$player", player.name))


                text.tooltip(event.onHoverText)

                text.send(players)
            }
        }
    }
    val distanceSquared = distance*distance

    fun getPlayers(player: Player): List<Player> {
        val list: MutableList<Player> = ArrayList()
        for (alvo in lib.modules.Mine.getPlayers()) {
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