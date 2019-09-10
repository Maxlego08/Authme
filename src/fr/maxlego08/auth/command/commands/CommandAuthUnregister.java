package fr.maxlego08.auth.command.commands;

import org.bukkit.command.CommandSender;

import fr.maxlego08.auth.Authme;
import fr.maxlego08.auth.auth.AuthAction;
import fr.maxlego08.auth.auth.AuthManager;
import fr.maxlego08.auth.command.VCommand;

public class CommandAuthUnregister extends VCommand {

	@Override
	protected CommandType perform(Authme main, CommandSender sender, String... args) {
		AuthManager.i.send(getPlayer(), AuthAction.SEND_UNREGISTER);
		return CommandType.SUCCESS;
	}

	@Override
	public String getPermission() {
		return "";
	}

	@Override
	public String getSyntax() {
		return "/authme unregister";
	}

	@Override
	public String getDescription() {
		return "Supprimer son compte";
	}

}
