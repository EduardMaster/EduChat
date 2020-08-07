package net.eduard.chat

import net.eduard.api.lib.storage.StorageAPI
import net.eduard.api.server.EduardPlugin
import net.eduard.chat.command.*
import net.eduard.chat.core.ChatChannel
import net.eduard.chat.core.ChatManager
import org.bukkit.entity.Player
import java.util.*

class EduChat : EduardPlugin() {
    lateinit var chat: ChatManager
    var lastPrivateMessage: MutableMap<Player, Player> = HashMap()
    override fun reload() {
        if (configs.contains("chat")) {
            chat = configs["chat", ChatManager::class.java]
            StorageAPI.updateReferences()
        } else {
            chat = ChatManager()
            chat.register(ChatChannel("staff", "", "(&2STAFF)", "", "sc"))
            chat.register(this)
            save()
        }
        for (canal in chat.getChannels()) {
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
        ChatReloadCommand().register()
        ColorCommand().register()
        ResponseCommand().register()
        TellCommand().register()
        ToggleTellCommand().register()
        ToggleChatCommand().register()
    }

    companion object {
        lateinit var instance: EduChat
    }
}