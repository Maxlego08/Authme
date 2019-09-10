package fr.maxlego08.auth.auth;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

import fr.maxlego08.auth.listener.ListenerAdapter;
import fr.maxlego08.auth.save.Config;
import fr.maxlego08.auth.zcore.ZPlugin;

public class AuthListener extends ListenerAdapter {

	@Override
	protected void onPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
		if (!AuthManager.i.canConnect(event.getName())) {
			event.setKickMessage("�cUne erreur est survenu lors du chargement de votre pseudo :/");
			event.setLoginResult(Result.KICK_OTHER);
			return;
		}
		if (Config.isBlacklist(event.getName())) {
			event.setKickMessage(getRandomColor("Hi hi hi !\nVous �tes blacklist du serveur :3\nEn plus le message il est tout color� c'est zoli"));
			event.setLoginResult(Result.KICK_OTHER);
			return;
		}
	}

	private List<String> colors = Arrays.asList("a", "b", "d", "e", "f", "3", "2", "4", "5", "6", "7", "8", "3", "9", "c");

	private String getRandomColor(String string) {
		Random random = new Random();
		String coloredString = "";
		for (String letter : string.split("")) 
			coloredString += "�" + colors.get(random.nextInt(colors.size() - 1)) + letter;
		return coloredString;
	}

	@Override
	protected void onConnect(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		AuthManager.i.join(player);
		Auth auth = AuthManager.i.getUser(player.getName());
		auth.setLogin(false);
		auth.setLocation(player.getLocation());
		if (Config.spawnLocation != null)
			player.teleport(changeStringLocationToLocationEye(Config.spawnLocation));
		Executors.newSingleThreadScheduledExecutor().schedule(new Runnable() {
			@Override
			public void run() {
				if (!auth.isLogin() && player.isOnline())
					Bukkit.getScheduler().runTask(ZPlugin.z(), () -> {
						player.teleport(auth.getLocation());
						player.kickPlayer("�cTu as mis trop de temps � te connecter !");
					});
			}
		}, 1, TimeUnit.MINUTES);
	}

	@Override
	protected void onCommand(PlayerCommandPreprocessEvent event) {
		if (!AuthManager.i.canInteract(event.getPlayer()))
			event.setCancelled(true);
	}

	@Override
	protected void onBlockBreak(BlockBreakEvent event) {
		if (!AuthManager.i.canInteract(event.getPlayer()))
			event.setCancelled(true);
	}

	@Override
	protected void onBlockPlace(BlockPlaceEvent event) {
		if (!AuthManager.i.canInteract(event.getPlayer()))
			event.setCancelled(true);
	}

	@Override
	protected void onPickUp(PlayerPickupItemEvent event) {
		if (!AuthManager.i.canInteract(event.getPlayer()))
			event.setCancelled(true);
	}

	@Override
	protected void onDrop(PlayerDropItemEvent event) {
		if (!AuthManager.i.canInteract(event.getPlayer()))
			event.setCancelled(true);
	}

	@Override
	protected void onInteract(PlayerInteractEvent event) {
		if (!AuthManager.i.canInteract(event.getPlayer()))
			event.setCancelled(true);
	}

	@Override
	protected void onInventoryClick(InventoryClickEvent event) {
		if (event.getWhoClicked() instanceof Player && !AuthManager.i.canInteract((Player) event.getWhoClicked()))
			event.setCancelled(true);
	}

	@Override
	protected void onPlayerTalk(AsyncPlayerChatEvent event) {
		if (!AuthManager.i.canInteract(event.getPlayer()))
			event.setCancelled(true);
	}
}
