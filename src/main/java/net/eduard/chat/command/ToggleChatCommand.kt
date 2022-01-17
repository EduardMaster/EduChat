package net.eduard.chat.command

import net.eduard.api.lib.manager.CommandManager
import net.eduard.api.lib.modules.Mine
import net.eduard.chat.EduChatPlugin
import org.bukkit.entity.Player

class ToggleChatCommand : CommandManager("togglechat","chat") {
    init{
        description ="Ativar ou desativar o chat"
    }
    var messageOn = "§6Chat ativado!"
    var messageOff = "§6Chat foi temporariamente desativado!"

    override fun playerCommand(player: Player, args: Array<String>) {
        if (EduChatPlugin.instance.manager.isChatEnabled) {
            EduChatPlugin.instance.manager.isChatEnabled = false
            Mine.broadcast(messageOff)
        } else {
            EduChatPlugin.instance.manager.isChatEnabled = true
            Mine.broadcast(messageOn)
        }
    }

}