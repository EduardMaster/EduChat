package net.eduard.chat

import net.eduard.api.server.EduardPlugin
import net.eduard.chat.command.*
import net.eduard.chat.core.ChatManager
import net.eduard.chat.listener.ChatListener
import org.bukkit.entity.Player
import java.util.*

class EduChat : EduardPlugin() {

    companion object {
        lateinit var instance: EduChat

    }

    var chat: ChatManager = ChatManager()
    var lastPrivateMessage: MutableMap<Player, Player> = HashMap()
    override fun reload() {
        if (configs.contains("chat")) {
            chat = configs["chat", ChatManager::class.java]

        } else {
            save()
        }

        for (canal in chat.channels) {
            canal.manager = (chat)
        }
    }

    override fun save() {
        configs["chat"] = chat
        configs.saveConfig()
    }

    override fun onEnable() {
        instance = this
        isFree = true
        super.onEnable()
        reload()
        ChatReloadCommand().registerCommand(this)
        ColorCommand().registerCommand(this)
        ResponseCommand().registerCommand(this)
        TellCommand().registerCommand(this)
        ToggleTellCommand().registerCommand(this)
        ToggleChatCommand().registerCommand(this)
        ChatListener().register(this)
    }

}