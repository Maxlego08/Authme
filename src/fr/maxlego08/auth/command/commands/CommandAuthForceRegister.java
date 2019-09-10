package fr.maxlego08.auth.command.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.maxlego08.auth.Authme;
import fr.maxlego08.auth.auth.AuthManager;
import fr.maxlego08.auth.command.VCommand;

public class CommandAuthForceRegister extends VCommand {

	@Override
	protected CommandType perform(Authme main, CommandSender sender, String... args) {

		if (args.length != 3)
			return CommandType.SYNTAX_ERROR;

		Player player = getPlayer(1);
		String password = args[2];

		if (player == null)
			return CommandType.SYNTAX_ERROR;

		if (password.length() < 8) {
			sendMessage(main.getPrefix() + " §cLe mot de passe doit avoir minimum §68 §ccharacères !");
			return CommandType.DEFAULT;
		}

		AuthManager.i.forceRegister(sender, player, password);

		return CommandType.SUCCESS;
	}

	@Override
	public String getPermission() {
		return "admin.auth.register";
	}

	@Override
	public String getSyntax() {
		return "/authme register <player> <password>";
	}

	@Override
	public String getDescription() {
		return "Forcer l'inscription d'un joueur";
	}

}
