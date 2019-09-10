package fr.maxlego08.auth.command.commands;

import org.bukkit.command.CommandSender;

import fr.maxlego08.auth.Authme;
import fr.maxlego08.auth.auth.AuthManager;
import fr.maxlego08.auth.command.VCommand;

public class CommandAuthShow extends VCommand {

	@Override
	protected CommandType perform(Authme main, CommandSender sender, String... args) {

		if (args.length != 2)
			return CommandType.SYNTAX_ERROR;

		String player = args[1];

		AuthManager.i.showInformation(player, sender);

		return CommandType.SUCCESS;
	}

	@Override
	public String getPermission() {
		return "admin.auth.show";
	}

	@Override
	public String getSyntax() {
		return "/authme show <player>";
	}

	@Override
	public String getDescription() {
		return "Voir les informations d'un joueur";
	}
}
