package fr.maxlego08.auth.command.commands;

import org.bukkit.command.CommandSender;

import fr.maxlego08.auth.Authme;
import fr.maxlego08.auth.auth.AuthManager;
import fr.maxlego08.auth.command.VCommand;

public class CommandBlacklistRemove extends VCommand {

	@Override
	protected CommandType perform(Authme main, CommandSender sender, String... args) {

		if (args.length != 2);
		
		String user = args[1];
		
		AuthManager.i.removeBlacklistPlayer(user, sender);
		
		return CommandType.SUCCESS;
	}

	@Override
	public String getPermission() {
		// TODO Auto-generated method stub
		return "admin.auth.blacklist.remove";
	}

	@Override
	public String getSyntax() {
		return "/authme blremove <joueur>";
	}
	
	@Override
	public String getDescription() {
		return "Retirer la blacklist d'un joueur";
	}

}
