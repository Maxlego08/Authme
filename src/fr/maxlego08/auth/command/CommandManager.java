package fr.maxlego08.auth.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.maxlego08.auth.Authme;
import fr.maxlego08.auth.auth.AuthManager;
import fr.maxlego08.auth.command.VCommand.CommandType;
import fr.maxlego08.auth.command.commands.CommandAuthForceLogin;
import fr.maxlego08.auth.command.commands.CommandAuthForceMail;
import fr.maxlego08.auth.command.commands.CommandAuthForceRegister;
import fr.maxlego08.auth.command.commands.CommandAuthSetLocation;
import fr.maxlego08.auth.command.commands.CommandAuthShow;
import fr.maxlego08.auth.command.commands.CommandAuthUnregister;
import fr.maxlego08.auth.command.commands.CommandBlacklistAdd;
import fr.maxlego08.auth.command.commands.CommandBlacklistRemove;
import fr.maxlego08.auth.command.commands.CommandMailSet;
import fr.maxlego08.auth.command.commands.CommandMailVerif;
import fr.maxlego08.auth.save.Config;
import fr.maxlego08.auth.zcore.ZPlugin;
import fr.maxlego08.auth.zcore.logger.Logger.LogType;
import fr.maxlego08.auth.zcore.utils.TextUtil;

public class CommandManager implements CommandExecutor {

	private final Authme main;
	private List<VCommand> commands = new ArrayList<>();

	public CommandManager(Authme main) {
		this.main = main;

		VCommand command = addCommand("authme", new ZCommand().setCommand(cmd -> sendHelp("authme", cmd.getSender()))
				.setPermission("admin.auth").setSyntaxe("/auth").setDescription("Voir les commandes"));
		addCommand(new ZCommand().setCommand(cmd -> ZPlugin.z().reload(cmd.getSender()))
				.setDescription("Reload la config").setSyntaxe("/authme reload").setPermission("admin.auth.reload")
				.addSubCommand("reload").setParent(command));
		addCommand(new CommandMailSet().addSubCommand("setmail").setParent(command).setNoConsole(true));
		addCommand(new CommandMailVerif().addSubCommand("verifmail").setParent(command).setNoConsole(true));
		addCommand(new CommandAuthSetLocation().addSubCommand("setlocation").setParent(command).setNoConsole(true));
		addCommand(new ZCommand().setCommand(cmd -> AuthManager.i.updateLogMail(cmd.getPlayer()))
				.setDescription("Activer ou désactiver les notifications par mail").setSyntaxe("/authme notif")
				.addSubCommand("notif").setParent(command).setNoConsole(true));
		addCommand(new ZCommand().setCommand(cmd -> AuthManager.i.updateLoginMail(cmd.getPlayer()))
				.setDescription("Activer ou désactiver la connection par mail").setSyntaxe("/authme login")
				.addSubCommand("login").setParent(command).setNoConsole(true));
		addCommand(new CommandAuthForceLogin().addSubCommand("forcelogin").setParent(command));
		addCommand(new CommandAuthForceRegister().addSubCommand("register").setParent(command));
		addCommand(new CommandAuthForceMail().addSubCommand("forcemail").setParent(command));
		addCommand(new CommandAuthUnregister().addSubCommand("unregister").setParent(command).setNoConsole(true));
		addCommand(new ZCommand().setCommand(cmd -> {
			cmd.sendMessage(main.getPrefix() + " §aUtilisateur§7: §2" + AuthManager.getUsers().size());
			cmd.sendMessage(main.getPrefix() + " §aUtilisateur avec le mail confirmé§7: §2"
					+ AuthManager.getUsers().values().stream().filter(auth -> auth.getMail() != null).count());
		}).setDescription("Voir le nombre d'utilisateur").setSyntaxe("/authme stats").addSubCommand("stats")
				.setParent(command));
		addCommand(new ZCommand().setCommand(cmd -> {
			cmd.sendMessage(main.getPrefix() + " §a" + main.getDescription().getFullName());
			cmd.sendMessage(main.getPrefix() + " §aPlugin développé par §lMaxlego08 §a!");
			cmd.sendMessage(main.getPrefix() + " §aGitHub§7: §2https://github.com/Maxlego08/Authme");
		}).setDescription("Voir la version du plugin").setSyntaxe("/authme version").addSubCommand("version", "v", "ver")
				.setParent(command));
		addCommand(new CommandAuthShow().addSubCommand("show").setParent(command));
		addCommand(new ZCommand().setCommand(cmd -> {
			cmd.sendMessage(main.getPrefix() + " §aJoueur blacklist§7: §a" + Config.getBlacklist());
		}).setDescription("Voir les joueurs blacklist").setSyntaxe("/authme blacklistshow").addSubCommand("blacklistshow", "bls")
				.setParent(command));
		addCommand(new CommandBlacklistAdd().addSubCommand("bladd", "blacklistadd").setParent(command));
		addCommand(new CommandBlacklistRemove().addSubCommand("blremove", "blacklistremove").setParent(command));

		main.getLog().log("Loading " + getUniqueCommand() + " commands", LogType.SUCCESS);

	}

	private void addCommand(VCommand command) {
		commands.add(command);
	}

	private VCommand addCommand(String string, VCommand command) {
		commands.add(command);
		command.addSubCommand(string);
		main.getCommand(string).setExecutor(this);
		return command;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {

		for (VCommand vcommand : commands) {
			if (vcommand.getSubCommands().contains(command.getName().toLowerCase())) {
				if (strings.length == 0 && vcommand.getParent() == null) {
					processRequirements(vcommand, sender, strings);
					return true;
				}
			} else if (strings.length != 0 && vcommand.getParent() != null
					&& vcommand.getParent().getSubCommands().contains(command.getName().toLowerCase())) {
				String cmd = strings[0].toLowerCase();
				if (vcommand.getSubCommands().contains(cmd)) {
					processRequirements(vcommand, sender, strings);
					return true;
				}
			}
		}
		sender.sendMessage(TextUtil
				.color(main.getPrefix() + " &cCette commande n'existe pas ou votre syntaxe contient une erreur"));
		return true;
	}

	private void processRequirements(VCommand command, CommandSender sender, String[] strings) {
		if (!(sender instanceof Player) && command.isNoConsole()) {
			sender.sendMessage(
					TextUtil.color(main.getPrefix() + " &eVous devez être un joueur pour faire cette commande"));
			return;
		}
		if (command.getPermission() == null || sender.hasPermission(command.getPermission())) {
			command.setSender(sender);
			command.setArgs(strings);
			CommandType returnType = command.perform(main, sender, strings);
			if (returnType == CommandType.SYNTAX_ERROR) {
				sender.sendMessage(
						TextUtil.color(main.getPrefix() + " &eVous devez faire la commande comme ceci &6%command%"
								.replace("%command%", command.getSyntax())));
			}
			return;
		}
		sender.sendMessage(TextUtil.color(main.getPrefix()
				+ " &cVous n'avez pas la permission de faire cette commande ! Vous pouvez en obtenir avec le /boutique, sur le site ou avec l'Hôtel des ventes !"));

	}

	public List<VCommand> getCommands() {
		return commands;
	}

	private int getUniqueCommand() {
		return (int) commands.stream().filter(command -> command.getParent() == null).count();
	}

	public void sendHelp(String commandString, CommandSender sender) {
		commands.forEach(command -> {
			if (command.getParent() != null && command.getParent().getSubCommands().contains(commandString)
					&& command.getDescription() != null) {
				if ((!(sender instanceof Player) && !command.isNoConsole()) || sender instanceof Player)
					sender.sendMessage("§6» §2" + command.getSyntax() + " §7- §a" + command.getDescription());
			}
		});
	}

}
