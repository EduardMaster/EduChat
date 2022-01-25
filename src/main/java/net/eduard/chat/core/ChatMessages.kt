package net.eduard.chat.core

import net.eduard.chat.EduChatPlugin
import java.lang.reflect.Modifier

object ChatMessages {
    var noTell="§cVocê não possui nenhuma conversa recente."
    var tellTo = "§aPara: §f%target$> §7%message"
    var tellFrom = "§aDe: §f%player$> §7%message"
    var tellDisabled = "§cO jogador §f%player desativou as mensagens privadas"
    var tellOff = "§cVocê desativou mensagens privadas"
    var tellOn = "§aVocê ativou mensagens privadas"
    var chatDisabled = "§cChat desativado!"
    var chatDisabledBypassPermission = "chat.disabled.bypass"
    var chatDisabledTemporarily = "&cChat desabilitado tempariamente!"
    var chatPermission = "§cVoce nao tem permissao para falar neste Chat!"


    fun reloadMessage(plugin : EduChatPlugin){
        for (field in ChatMessages::class.java.declaredFields){
            if (field.name.equals("instance",true))continue
            if (Modifier.isFinal(field.modifiers))continue
            plugin.messages.add(field.name, field.get(0))
            field.set(0, plugin.message(field.name))
        }
        plugin.messages.saveConfig()
    }
}