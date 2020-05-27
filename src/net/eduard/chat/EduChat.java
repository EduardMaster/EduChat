package net.eduard.chat;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import net.eduard.api.lib.modules.Mine;
import net.eduard.api.lib.manager.CommandManager;
import net.eduard.api.lib.storage.StorageAPI;
import net.eduard.api.server.EduardPlugin;
import net.eduard.chat.core.ChatChannel;
import net.eduard.chat.core.ChatManager;

public class EduChat extends EduardPlugin {

	private static EduChat plugin;
	private ChatManager chat;
	private Map<Player, Player> lastPrivateMessage = new HashMap<>();
	public static EduChat getInstance() {
		return plugin;
	}
	public void reload() {
		if (chat !=null) {
			chat.unregisterListener();
		}
		
		if (getConfigs().contains("Chat")) {
			chat = (ChatManager) getConfigs().get("Chat");
			StorageAPI.updateReferences();
		}else {
			chat = new ChatManager();
			chat.register(new ChatChannel("staff", "", "(&2STAFF)", "", "sc"));
			chat.register(this);
			save();
		}
		
	}
	public void save() {
		getConfigs().set("Chat", chat);
		getConfigs().saveConfig();
	}
	
	@Override
	public void onEnable() {
		plugin = this;
		
		reload();
		for (Class<?> claz : getClasses("net.eduard.chat.command")) {
			if (CommandManager.class.isAssignableFrom(claz)) {
				try {
					Mine.createCommand(plugin, (Command) claz.newInstance());
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		
	}
	public ChatManager getChat() {
		return chat;
	}
	public void setChat(ChatManager chat) {
		this.chat = chat;
	}
	public Map<Player, Player> getLastPrivateMessage() {
		return lastPrivateMessage;
	}
	public void setLastPrivateMessage(Map<Player, Player> lastPrivateMessage) {
		this.lastPrivateMessage = lastPrivateMessage;
	}

}