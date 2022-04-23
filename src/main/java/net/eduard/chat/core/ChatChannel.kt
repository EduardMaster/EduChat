package net.eduard.chat.core

import net.eduard.api.lib.event.ChatMessageEvent
import net.eduard.api.lib.kotlin.chat
import net.eduard.api.lib.kotlin.fixColors
import net.eduard.api.lib.kotlin.formatColors
import net.eduard.api.lib.manager.CooldownManager
import net.eduard.api.lib.modules.FakePlayer
import net.eduard.api.lib.modules.Mine
import net.eduard.api.lib.modules.VaultAPI
import net.eduard.chat.EduChatPlugin
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Sound
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
    var permission: String = "chat.use"
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
            player.sendMessage("§cNão pode enviar mensagem vazia.")
            return
        }
        if (!player.hasPermission(permission)) {
            player.sendMessage(ChatMessages.chatPermission)
            return
        }
        if (!player.hasPermission("chat.delay.bypass"))
            if (!chatLocalCooldown.cooldown(player)) return

        val cor = EduChatPlugin.instance.manager.colors[FakePlayer(player)] ?: ""
        val event = ChatMessageEvent(player, name, message)
        if (player.hasPermission("chat.color"))
            event.message = event.message.formatColors()
        event.format = format
        event.playersInChannel.addAll(getPlayers(player))
        event.onClickCommand = onClick
        event.onHoverText.addAll(onHover)
        event.resetTags()
        //event.setTagValue("message", event.message)
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
        event.tags.remove("message")
        finalMessage = Mine.applyPlaceholders(finalMessage, player);
        for ((tagKey, tagValue) in event.tags) {
            finalMessage = finalMessage.replace("{${tagKey.toLowerCase()}}", tagValue, false)
            finalMessage = finalMessage.replace("(${tagKey.toLowerCase()})", tagValue, false)
        }
        val hoverLines = mutableListOf<String>()
        for (line in event.onHoverText) {
            hoverLines.add(Mine.applyPlaceholders(line, player))
        }
        event.onHoverText = hoverLines

        when (chatType) {
            ChatType.BUKKIT -> {
                for (playerLoop in players) {
                    playerLoop.sendMessage(finalMessage)
                }
            }
            ChatType.SPIGOT -> {
                val originalMessage = event.message
                val textComponent: TextComponent = finalMessage.chat as TextComponent
                val lastColor = textComponent.color
                var clickEvent = ClickEvent(
                    ClickEvent.Action.SUGGEST_COMMAND,
                    event.onClickCommand.replace("%player", player.name)
                )
                if (originalMessage.contains("https://")) {
                    val linkAndRest = originalMessage.substringAfter("https://")
                    val link = if (linkAndRest.contains(" ")) linkAndRest.split(" ")[0] else linkAndRest
                    clickEvent = ClickEvent(
                        ClickEvent.Action.OPEN_URL,
                        "https://$link"
                    )
                } else if (originalMessage.contains("http://")) {
                    val linkAndRest = originalMessage.substringAfter("http://")
                    val link = if (linkAndRest.contains(" ")) linkAndRest.split(" ")[0] else linkAndRest
                    clickEvent = ClickEvent(
                        ClickEvent.Action.OPEN_URL,
                        "https://$link"
                    )
                }
                textComponent.clickEvent = clickEvent
                val textBuilder = ComponentBuilder("")
                textBuilder.append(event.onHoverText.joinToString("\n"))
                val hoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT, textBuilder.create())
                textComponent.hoverEvent = hoverEvent
                var modifiedMessage = originalMessage

                for (target in players) {
                    modifiedMessage = modifiedMessage
                        .replace(target.name, "§6@§n" + target.name + "$lastColor", true)
                }
                textComponent.text = finalMessage.replace("(message)", modifiedMessage)
                textComponent.fixColors()
                for (target in players) {
                    if (modifiedMessage.contains(target.name)) {
                        target.playSound(target.location, Sound.NOTE_PLING, 2f, 1f)
                    }
                    target.spigot().sendMessage(textComponent)
                }

            }
        }
    }

    fun getPlayers(player: Player): List<Player> {
        val list: MutableList<Player> = ArrayList()
        for (playerLoop in Mine.getPlayers()) {
            if (!playerLoop.hasPermission(permission)) continue
            if (!isGlobal) {
                if (playerLoop.world != player.world) {
                    continue
                }
                if (distance > 0) {
                    val newDistance = playerLoop.location.distanceSquared(player.location)
                    if (newDistance > distanceSquared) continue
                }
            }
            list.add(playerLoop)
        }
        return list
    }

    companion object {
        fun cooldownConfiguration() {
            chatLocalCooldown
                .apply {
                    noMessages()
                    duration = EduChatPlugin.instance.manager.charCooldownTicks
                    messageOnCooldown = EduChatPlugin.instance.manager.chatCooldownMessage
                }
        }

        val chatLocalCooldown = CooldownManager()


    }
}