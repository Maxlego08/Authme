package fr.maxlego08.auth.command.commands;

import org.bukkit.command.CommandSender;

import fr.maxlego08.auth.Authme;
import fr.maxlego08.auth.command.VCommand;
import fr.maxlego08.auth.mail.MailManager;
import fr.maxlego08.auth.save.Config;

public class CommandMailSet extends VCommand {

	@Override
	protected CommandType perform(Authme main, CommandSender sender, String... args) {
		if (args.length != 2)
			return CommandType.SYNTAX_ERROR;

		if (!Config.useMail){
			sendMessage(main.getPrefix() + " §cLe système de mail n'est pas actif !");
			return CommandType.DEFAULT;
		}
		
		String mail = args[1];
		if (!allowMail(mail)) {
			sendMessage(main.getPrefix() + " §cVous ne pouvez pas utiliser ce domaine !");
			return CommandType.DEFAULT;
		}

		if (MailManager.i.mailAlreadySend(getPlayer())) {
			sendMessage(main.getPrefix() + " §cVous avez déjà reçu le mail !");
			return CommandType.DEFAULT;
		}

		sendMessage(main.getPrefix() + " §aEnvoie du mail...");
		MailManager.i.sendVerificationEmail(getPlayer(), mail);

		return CommandType.SUCCESS;
	}

	@Override
	public String getPermission() {
		return null;
	}

	@Override
	public String getSyntax() {
		return "/authme setmail <mail>";
	}

	@Override
	public String getDescription() {
		return "Verifier votre mail";
	}

	private boolean allowMail(String mail) {
		return Config.allowDomaine.stream().filter(domaine -> mail.endsWith(domaine)).findAny().isPresent();
	}

}
