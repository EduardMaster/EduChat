package net.eduard.chat.core

import net.eduard.api.server.ChatAPI
import net.eduard.chat.EduChatPlugin

class ChatAPIImpl : ChatAPI {
    val manager get() = EduChatPlugin.instance.manager

    override fun getPlayersMuted(): List<String> {
        return manager.playersMuted
    }

    override fun isMuted(playerName: String): Boolean {
        return playerName.toLowerCase() in manager.playersMuted
    }

    override fun mute(playerName: String) {
        if (isMuted(playerName)) return
        manager.playersMuted.add(playerName.toLowerCase())
    }
}