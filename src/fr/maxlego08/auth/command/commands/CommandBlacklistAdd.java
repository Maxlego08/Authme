package fr.maxlego08.auth.command.commands;

import org.bukkit.command.CommandSender;

import fr.maxlego08.auth.Authme;
import fr.maxlego08.auth.auth.AuthManager;
import fr.maxlego08.auth.command.VCommand;

public class CommandBlacklistAdd extends VCommand {

	@Override
	protected CommandType perform(Authme main, CommandSender sender, String... args) {

		if (args.length != 2);
		
		String user = args[1];
		
		AuthManager.i.blacklistPlayer(user, sender);
		
		return CommandType.SUCCESS;
	}

	@Override
	public String getPermission() {
		// TODO Auto-generated method stub
		return "admin.auth.blacklist.add";
	}

	@Override
	public String getSyntax() {
		return "/authme bladd <joueur>";
	}
	
	@Override
	public String getDescription() {
		return "Blacklist un joueur";
	}

}
