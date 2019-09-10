package fr.maxlego08.auth.command.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.maxlego08.auth.Authme;
import fr.maxlego08.auth.auth.AuthManager;
import fr.maxlego08.auth.command.VCommand;

public class CommandAuthForceLogin extends VCommand {

	@Override
	protected CommandType perform(Authme main, CommandSender sender, String... args) {

		if (args.length != 2)
			return CommandType.SYNTAX_ERROR;

		Player player = getPlayer(1);
		if (player == null)
			return CommandType.SYNTAX_ERROR;

		AuthManager.i.forceLogin(sender, player);

		return CommandType.SUCCESS;
	}

	@Override
	public String getPermission() {
		return "admin.auth.login";
	}

	@Override
	public String getSyntax() {
		return "/authme forcelogin <player>";
	}

	public String getDescription() {
		return "Forcer la connection d'un joueur";
	}

}
