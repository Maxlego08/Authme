package fr.maxlego08.auth.command.commands;

import org.bukkit.command.CommandSender;

import fr.maxlego08.auth.Authme;
import fr.maxlego08.auth.command.VCommand;
import fr.maxlego08.auth.save.Config;

public class CommandAuthSetLocation extends VCommand {

	@Override
	protected CommandType perform(Authme main, CommandSender sender, String... args) {
		String location = "";
		if (args.length == 2)
			location = args[1];
		if (location.equals("null")) {
			Config.spawnLocation = null;
			sendMessage(main.getPrefix() + " §aVous venez de supprimer la location !");
		} else {
			sendMessage(main.getPrefix() + " §aVous venez de mettre la location !");
			Config.spawnLocation = changeLocationToStringEye(getPlayer().getLocation());
		}

		return CommandType.SUCCESS;
	}

	@Override
	public String getPermission() {
		return "admin.auth.location";
	}

	@Override
	public String getSyntax() {
		return "/autme setlocation";
	}

	@Override
	public String getDescription() {
		return "Mettre la zone d'apparition des joueurs";
	}

}
