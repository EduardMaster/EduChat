package net.eduard.chat.command;

import net.eduard.api.lib.modules.Extra;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.eduard.api.lib.Mine;
import net.eduard.api.lib.manager.CommandManager;
import net.eduard.chat.Main;

public class ResponseCommand extends CommandManager {

	public ResponseCommand() {
		super("response", "responder", "r");
		setUsage("§c/r <mensagen>");
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (Mine.onlyPlayer(sender)) {

			Player p = (Player) sender;

			if (args.length == 0) {
				sendUsage(sender);
			} else {

				if (!Main.getInstance().getLastPrivateMessage().containsKey(p)) {

					p.sendMessage("§cVocê não possui nenhuma conversa recente.");

				} else {
					Player alvo = Main.getInstance().getLastPrivateMessage().get(p);
					alvo.chat("/tell " + alvo.getName() + " " + Extra.getText(0, args));
				}

				

			}
		}

		return true;
	}
}
