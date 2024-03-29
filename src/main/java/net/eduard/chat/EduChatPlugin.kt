package net.eduard.chat

import net.eduard.api.server.EduardPlugin
import net.eduard.chat.command.*
import net.eduard.chat.core.ChatChannel
import net.eduard.chat.core.ChatManager
import net.eduard.chat.core.ChatMessages
import net.eduard.chat.listener.ChatListener
import org.bukkit.entity.Player
import java.util.*

class EduChatPlugin : EduardPlugin() {

    companion object {
        lateinit var instance: EduChatPlugin
    }

    lateinit var manager: ChatManager
    var lastPrivateMessage: MutableMap<Player, Player> = HashMap()
    override fun reload() {
        configs.reloadConfig()
        storage.reloadConfig()
        messages.reloadConfig()
        if (configs.contains("chat")) {
            manager = configs["chat", ChatManager::class.java]
        } else {
            save()
        }
        ChatChannel.cooldownConfiguration()
        ChatMessages.reloadMessage(this)
    }

    override fun onDisable() {
        save()
        super.onDisable()
    }

    override fun save() {
        configs["chat"] = manager
        configs.saveConfig()
    }

    override fun onEnable() {
        instance = this
        isFree = true
        super.onEnable()
        reload()
        onActivation()
    }

    override fun onActivation() {

        ChatReloadCommand().registerCommand(this)
        ColorCommand().registerCommand(this)
        ResponseCommand().registerCommand(this)
        ToggleTellCommand().registerCommand(this)
        ToggleChatCommand().registerCommand(this)
        ChatListener().register(this)
        TellCommand().register()

    }

}