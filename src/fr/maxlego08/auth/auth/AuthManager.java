package fr.maxlego08.auth.auth;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

import fr.maxlego08.auth.mail.MailManager;
import fr.maxlego08.auth.packet.PacketAuthClient;
import fr.maxlego08.auth.save.Config;
import fr.maxlego08.auth.zcore.ZPlugin;
import fr.maxlego08.auth.zcore.logger.Logger;
import fr.maxlego08.auth.zcore.logger.Logger.LogType;
import fr.maxlego08.auth.zcore.utils.ZUtils;
import fr.maxlego08.auth.zcore.utils.storage.Persist;
import fr.maxlego08.auth.zcore.utils.storage.Saver;
import net.minecraft.server.v1_7_R4.PlayerConnection;

public class AuthManager extends ZUtils implements Saver {

	private static Map<String, Auth> users = new HashMap<String, Auth>();

	/**
	 * Get user from name
	 * 
	 * @param name
	 * @return Auth
	 */
	public Auth getUser(String name) {
		if (users.containsKey(name))
			return users.get(name);
		return createUser(name);
	}

	/**
	 * Create new user
	 * 
	 * @param name
	 */
	public Auth createUser(String name) {
		Auth auth = new Auth(name);
		users.put(name, auth);
		Logger.info("Create new player -> " + name, LogType.INFO);
		return auth;
	}

	/**
	 * Check if player exist
	 * 
	 * @param name
	 * @return
	 */
	public boolean exist(String name) {
		return users.containsKey(name) && users.get(name).getPassword() != null;
	}

	/**
	 * Check if player has mail
	 * 
	 * @param name
	 * @return
	 */
	public boolean isMail(String name) {
		return users.containsKey(name) && users.get(name).getMail() != null;
	}

	/**
	 * login player
	 * 
	 * @param player
	 * @param password
	 */
	public void login(Player player, String password) {

		/*
		 * If the user does not exist, he will register!
		 */
		if (!exist(player.getName())) {
			send(player, AuthAction.SEND_REGISTER);
			return;
		}

		String hash = hash(password);
		Auth auth = getUser(player.getName());

		/**
		 * if the password is good, the player can log in, otherwise an error
		 * message is sent
		 */
		if (auth.same(hash))
			login(player, auth);
		else
			send(player, AuthAction.LOGIN_ERROR, "§cMot de passe incorrect !");
	}

	/**
	 * register player
	 * 
	 * @param player
	 * @param password
	 */
	public void register(Player player, String password) {

		/**
		 * If the user does not exist, he will login
		 */
		if (exist(player.getName())) {
			send(player, AuthAction.SEND_LOGIN);
			return;
		}

		String hash = hash(password);
		Auth auth = getUser(player.getName());
		auth.setPassword(hash);
		auth.add(new AuthHistorical(new Date().toString(), player.getAddress().getHostName()));
		auth.setLogin(true);
		player.teleport(auth.getLocation());
		send(player, AuthAction.REGISTER_SUCCESS);
		Logger.info(player.getName() + " just signed up!", LogType.INFO);
		player.sendMessage(ZPlugin.z().getPrefix() + " §aConnexion effectué avec succès !");
		sendMailInformation(player);
	}

	/**
	 * Confirm player login
	 * 
	 * @param player
	 * @param code
	 */
	public void confirmLogin(Player player, String code) {
		Auth auth = getUser(player.getName());
		if (MailManager.i.verifCodeConfirm(player.getName(), code)) {

			auth.setMailLogin(false);
			Logger.info(player.getName() + " just confirm !", LogType.INFO);
			player.sendMessage(ZPlugin.z().getPrefix() + " §aConfirmation effectué avec succès !");
			player.teleport(auth.getLocation());
			send(player, AuthAction.CONFIRM_SUCCESS);

		} else {
			send(player, AuthAction.CONFIRM_ERROR, "Code incorrect !");
		}
	}

	/**
	 * Send packet to player
	 * 
	 * @param player
	 * @param action
	 */
	public void send(Player player, AuthAction action, String... strings) {
		PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
		if (strings.length == 1)
			connection.sendPacket(new PacketAuthClient(action, strings[0]));
		else
			connection.sendPacket(new PacketAuthClient(action));
	}

	/**
	 * 
	 * @param player
	 */
	public void join(Player player) {
		Executors.newSingleThreadScheduledExecutor().schedule(new Runnable() {
			@Override
			public void run() {
				if (exist(player.getName()))
					send(player, AuthAction.SEND_LOGIN);
				else
					send(player, AuthAction.SEND_REGISTER);

			}
		}, 650, TimeUnit.MILLISECONDS);
	}

	/**
	 * @param player
	 * @return true is player is login
	 */
	private boolean isLogin(Player player) {
		if (!exist(player.getName()))
			return true;
		return getUser(player.getName()).isLogin() || getUser(player.getName()).isMailLogin();
	}

	/**
	 * @param player
	 * @return true if player can interact
	 */
	public boolean canInteract(Player player) {
		if (!isLogin(player) || getUser(player.getName()).isMailLogin()) {
			getUser(player.getName()).setLocation(player.getLocation());
			if (exist(player.getName()))
				send(player, AuthAction.SEND_LOGIN);
			else if (getUser(player.getName()).isMailLogin())
				send(player, AuthAction.SEND_LOGIN_CONFIRM);
			else
				send(player, AuthAction.SEND_REGISTER);
			return false;
		}
		return true;
	}

	/**
	 * @param player
	 */
	public void updateLogMail(Player player) {
		Auth auth = getUser(player.getName());
		if (auth.getMail() == null){
			player.sendMessage(ZPlugin.z().getPrefix() + " §cVous n'avez pas enregistrer votre mail !");
			return;
		}
		auth.setLogMail(!auth.isLogMail());
		player.sendMessage(ZPlugin.z().getPrefix() + " §aVous venez §2"
				+ (auth.isLogMail() ? "d'activer" : "de désactiver") + " §ales notifications de connection par mail");
	}

	/**
	 * @param player
	 */
	public void updateLoginMail(Player player) {
		Auth auth = getUser(player.getName());
		if (auth.getMail() == null){
			player.sendMessage(ZPlugin.z().getPrefix() + " §cVous n'avez pas enregistrer votre mail !");
			return;
		}
		auth.setLoginMail(!auth.isLoginMail());
		player.sendMessage(ZPlugin.z().getPrefix() + " §aVous venez §2"
				+ (auth.isLoginMail() ? "d'activer" : "de désactiver") + " §ala connection par mail");
	}

	/**
	 * @param sender
	 * @param player
	 */
	public void forceLogin(CommandSender sender, Player player) {

		if (!exist(player.getName())) {
			sender.sendMessage(ZPlugin.z().getPrefix() + " §cLe joueur n'ai pas encore inscrit !");
			return;
		}

		Auth auth = getUser(player.getName());

		if (auth.isLogin()) {
			sender.sendMessage(ZPlugin.z().getPrefix() + " §cLe joueur est déjà connecté !");
			return;
		}

		sender.sendMessage(ZPlugin.z().getPrefix() + " §aVous venez de connecter§2" + player.getName() + " §a!");
		login(player, auth);
	}

	/**
	 * @param sender
	 * @param player
	 * @param password
	 */
	public void forceRegister(CommandSender sender, Player player, String password) {

		if (exist(player.getName())) {
			sender.sendMessage(ZPlugin.z().getPrefix() + " §cLe joueur est déjà inscrit !");
			return;
		}

		sender.sendMessage(ZPlugin.z().getPrefix() + " §aVous venez d'inscrire §2" + player.getName()
				+ " §aavec le mot de passe §2" + password + " §a!");
		register(player, password);

	}

	/**
	 * Login a player
	 * 
	 * @param player
	 * @param auth
	 */
	private void login(Player player, Auth auth) {
		send(player, AuthAction.LOGIN_SUCCESS);
		auth.add(new AuthHistorical(new Date().toString(), player.getAddress().getHostName()));
		auth.setLogin(true);
		Logger.info(player.getName() + " just signed in!", LogType.INFO);
		player.sendMessage(ZPlugin.z().getPrefix() + " §aConnexion effectué avec succès !");
		sendMailInformation(player);
		if (auth.isLogMail())
			MailManager.i.sendLogEmail(auth, player);
		if (auth.isLoginMail() && !auth.sameAdress(player.getAddress().getHostName())) {
			MailManager.i.sendLoginEmail(auth, player);
			send(player, AuthAction.SEND_LOGIN_CONFIRM);
		} else
			player.teleport(auth.getLocation());
	}

	/**
	 * @param player
	 * @param password
	 */
	public void unregister(Player player, String password) {
		if (!exist(player.getName())) {
			send(player, AuthAction.SEND_REGISTER);
			return;
		}

		String hash = hash(password);
		Auth auth = getUser(player.getName());

		if (auth.getPassword().equals(hash)) {

			if (auth.getMail() != null) {
				send(player, AuthAction.SEND_UNREGISTER_CONFIRM);
				MailManager.i.sendMailUnregisterConfirm(auth, player);
			} else {
				users.remove(player.getName());
				player.kickPlayer("§cVous venez de vous désinscrire !");
				Logger.info(player.getName() + " just delete account !", LogType.INFO);
			}
		} else
			player.sendMessage(ZPlugin.z().getPrefix() + " §cMot de passe incorrect !");
	}

	public void unregisterConfirm(Player player, String code) {

		if (!exist(player.getName())) {
			send(player, AuthAction.SEND_REGISTER);
			return;
		}

		Auth auth = getUser(player.getName());

		if (MailManager.i.verifCodeUnregister(player.getName(), code)) {
			auth.setMailLogin(false);
			Logger.info(player.getName() + " just delete account !", LogType.INFO);
			users.remove(player.getName());
			player.kickPlayer("§cVous venez de vous désinscrire !");
		} else {
			send(player, AuthAction.CONFIRM_ERROR, "Code incorrect !");
		}
	}

	/**
	 * @return users
	 */
	public static Map<String, Auth> getUsers() {
		return users;
	}

	/**
	 * @param player
	 */
	private void sendMailInformation(Player player) {
		if (Config.useMail && !isMail(player.getName())) {
			player.sendMessage(ZPlugin.z().getPrefix()
					+ " §aVous n'avez pas vérifié votre mail ! Le serveur se dédouane de toutes responsabilités si une autre personne ce connecter sur votre compte ");
			player.sendMessage(ZPlugin.z().getPrefix()
					+ " §aFaite §2/authme setmail <votre mail> §apour faire vérifier votre mail");
		}
	}

	public void showInformation(String name, CommandSender sender){
		
		if (!users.containsKey(name)){
			sender.sendMessage(ZPlugin.z().getPrefix() + " §cLe joueur §6" + name + " §cn'existe pas !");
			return;
		}
		
		Auth auth = getUser(name);
		AuthHistorical historical = auth.getLast();
		sender.sendMessage(ZPlugin.z().getArrow() + " §aPseudo§7: §2" + name);
		sender.sendMessage(ZPlugin.z().getArrow() + " §aDernière connection§7: §2" + historical.getDate());
		sender.sendMessage(ZPlugin.z().getArrow() + " §aDernière adresse§7: §2" + historical.getAdress());
		
	}
	
	public static transient AuthManager i = new AuthManager();

	@Override
	public void save(Persist persist) {
		persist.save(i, "auth");
	}

	@Override
	public void load(Persist persist) {
		persist.loadOrSaveDefault(i, AuthManager.class, "auth");
	}

}
