package fr.maxlego08.auth.listener;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import fr.maxlego08.auth.zcore.utils.ZUtils;

public abstract class ListenerAdapter extends ZUtils{

	protected void onConnect(PlayerJoinEvent event) {
	}

	protected void onQuit(PlayerQuitEvent event) {
	}

	protected void onMove(PlayerMoveEvent event) {
	}

	protected void onInventoryClick(InventoryClickEvent event) {
	}

	protected void onInventoryClose(InventoryCloseEvent event) {
	}

	protected void onInventoryDrag(InventoryDragEvent event) {
	}

	protected void onBlockBreak(BlockBreakEvent event) {
	}

	protected void onBlockPlace(BlockPlaceEvent event) {
	}

	protected void onEntityDeath(EntityDeathEvent event) {
	}

	protected void onInteract(PlayerInteractEvent event) {
	}

	protected void onPlayerTalk(AsyncPlayerChatEvent event) {
	}

	protected void onCraftItem(CraftItemEvent event) {
	}

	protected void onCommand(PlayerCommandPreprocessEvent event) {
	}

	protected void onGamemodeChange(PlayerGameModeChangeEvent event) {
	}

	protected void onDrop(PlayerDropItemEvent event) {
	}

	protected void onPickUp(PlayerPickupItemEvent event) {
	}

	protected void onMobSpawn(CreatureSpawnEvent event) {
	}
	
	protected void onPlayerPreLogin(AsyncPlayerPreLoginEvent event){
		
	}
}
