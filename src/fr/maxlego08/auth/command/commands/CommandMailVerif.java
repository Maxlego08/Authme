package fr.maxlego08.auth.command.commands;

import org.bukkit.command.CommandSender;

import fr.maxlego08.auth.Authme;
import fr.maxlego08.auth.auth.AuthManager;
import fr.maxlego08.auth.command.VCommand;
import fr.maxlego08.auth.mail.MailManager;
import fr.maxlego08.auth.save.Config;

public class CommandMailVerif extends VCommand {

	@Override
	protected CommandType perform(Authme main, CommandSender sender, String... args) {

		if (args.length != 2)
			return CommandType.SYNTAX_ERROR;

		if (!Config.useMail){
			sendMessage(main.getPrefix() + " §cLe système de mail n'est pas actif !");
			return CommandType.DEFAULT;
		}
		
		if (AuthManager.i.isMail(getPlayer().getName())){
			sendMessage(main.getPrefix() + " §cVous avez déjà vérifié votre mail !");
			return CommandType.DEFAULT;
		}
		
		String code = args[1];

		if (!MailManager.i.mailAlreadySend(getPlayer())) {
			sendMessage(main.getPrefix() + " §cVous devez faire §6/authme setmail <mail> §cavant de faire cette commande !");
			return CommandType.DEFAULT;
		}

		MailManager.i.verifCode(getPlayer(), code);
		
		return CommandType.SUCCESS;
	}

	@Override
	public String getPermission() {
		return null;
	}

	@Override
	public String getSyntax() {
		return "/authme verifmail <code>";
	}

	@Override
	public String getDescription() {
		return "Verifier son mail";
	}

}
