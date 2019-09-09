package fr.maxlego08.auth.auth;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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
		if (auth.same(hash)) {
			send(player, AuthAction.LOGIN_SUCCESS);
			auth.add(new AuthHistorical(new Date().toString(), player.getAddress().getHostName()));
			auth.setLogin(true);
			Logger.info(player.getName() + " just signed in!", LogType.INFO);
			player.sendMessage(ZPlugin.z().getPrefix() + " �aConnexion effectu� avec succ�s !");
			if (Config.useMail && !isMail(player.getName())) {
				player.sendMessage(ZPlugin.z().getPrefix()
						+ " �aVous n'avez pas v�rifi� votre mail ! Le serveur se d�douane de toutes responsabilit�s si une autre personne ce connecter sur votre compte ");
				player.sendMessage(ZPlugin.z().getPrefix()
						+ " �aFaite �2/authme setmail <votre mail> �apour faire v�rifier votre mail");
			}
			if (auth.isLogMail())
				MailManager.i.sendLogEmail(auth, player);
			if (auth.isLoginMail()) {
				MailManager.i.sendLoginEmail(auth, player);
				send(player, AuthAction.SEND_LOGIN_CONFIRM);
			} else
				player.teleport(auth.getLocation());
		} else
			send(player, AuthAction.LOGIN_ERROR, "�cMot de passe incorrect !");
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
		player.sendMessage(ZPlugin.z().getPrefix() + " �aConnexion effectu� avec succ�s !");
		if (Config.useMail && !isMail(player.getName())) {
			player.sendMessage(ZPlugin.z().getPrefix()
					+ " �aVous n'avez pas v�rifi� votre mail ! Le serveur se d�douane de toutes responsabilit�s si une autre personne ce connecter sur votre compte ");
			player.sendMessage(ZPlugin.z().getPrefix()
					+ " �aFaite �2/authme setmail <votre mail> �apour faire v�rifier votre mail");
		}
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
			player.sendMessage(ZPlugin.z().getPrefix() + " �aConfirmation effectu� avec succ�s !");
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
		auth.setLogMail(!auth.isLogMail());
		player.sendMessage(ZPlugin.z().getPrefix() + " �aVous venez �2"
				+ (auth.isLogMail() ? "d'activer" : "de d�sactiver") + " �ales notifications de connection par mail");
	}

	/**
	 * @param player
	 */
	public void updateLoginMail(Player player) {
		Auth auth = getUser(player.getName());
		auth.setLoginMail(!auth.isLoginMail());
		player.sendMessage(ZPlugin.z().getPrefix() + " �aVous venez �2"
				+ (auth.isLoginMail() ? "d'activer" : "de d�sactiver") + " �ala connection par mail");
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
