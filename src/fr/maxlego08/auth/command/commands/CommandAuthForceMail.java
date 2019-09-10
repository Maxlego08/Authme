package fr.maxlego08.auth.command.commands;

import org.bukkit.command.CommandSender;

import fr.maxlego08.auth.Authme;
import fr.maxlego08.auth.auth.AuthManager;
import fr.maxlego08.auth.command.VCommand;

public class CommandAuthForceMail extends VCommand {

	@Override
	protected CommandType perform(Authme main, CommandSender sender, String... args) {

		if (args.length != 3)
			return CommandType.SYNTAX_ERROR;

		String user = args[1];
		String mail = args[2];

		AuthManager.i.forceMail(sender, user, mail);
		
		return CommandType.SUCCESS;
	}

	@Override
	public String getPermission() {
		return "admin.auth.mail";
	}

	@Override
	public String getSyntax() {
		return "/authme forcemail <player> <mail>";
	}

	@Override
	public String getDescription() {
		return "Mettre le mail d'un utilisateur";
	}

}
