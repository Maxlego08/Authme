package fr.maxlego08.auth.inventory;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

import fr.maxlego08.auth.Authme;
import fr.maxlego08.auth.exceptions.InventoryAlreadyExistException;
import fr.maxlego08.auth.listener.ListenerAdapter;
import fr.maxlego08.auth.zcore.logger.Logger;
import fr.maxlego08.auth.zcore.logger.Logger.LogType;

public class InventoryManager extends ListenerAdapter {

	private final Authme plugin;
	private Map<Integer, Class<? extends VInventory>> inventories = new HashMap<>();
	private Map<Player, VInventory> playerInventories = new HashMap<>();

	public InventoryManager(Authme template) {
		this.plugin = template;

		template.getLog().log("Loading " + inventories.size() + " inventories", LogType.SUCCESS);
		
	}

	private void addInventory(int id, VInventory inventory) throws InventoryAlreadyExistException {
		if (!inventories.containsKey(inventory.getMenuId())) {
			inventory.setMenuId(id);
			inventories.put(id, inventory.getClass());
		} else
			throw new InventoryAlreadyExistException("Inventory with id " + inventory.getMenuId() + " already exist !");
	}

	/**
	 * Function to be able to open a menu according to its type
	 * 
	 * @param type
	 *            - The menu type
	 * @param player
	 *            - The auction player
	 * @param page
	 *            - The menu page
	 * @return boolean - Returns true if the menu is open otherwise it returns
	 *         false
	 */
	public boolean createMenu(int id, Player player, int page, Object... objects) {
		if (exist(player))
			return false;
		if (getInventory(id) == null)
			return false;
		try {
			VInventory gui;
			gui = getInventory(id).newInstance();
			gui.setPlayer(player);
			gui.setArgs(objects);
			gui.setPage(page);
			gui.openMenu(plugin, player, page, objects);
			playerInventories.put(player, gui);
			return true;
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | SecurityException e) {
			System.out.println("salut ?");
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean createMenu(int id, Player player, boolean b, int page, Object... objects) {
		remove(player);
		return createMenu(id, player, page, objects);
	}
	
	/**
	 * Function to check the player to already have an open menu
	 * 
	 * @return boolean - return true if the player already has an open menu
	 */
	public boolean exist(Player player) {
		return playerInventories.containsKey(player);
	}

	/**
	 * Return the menu according to the type
	 * 
	 * @return VGUI
	 */
	private Class<? extends VInventory> getInventory(int id) {
		return inventories.get(id);
	}

	public void remove(Player player) {
		if (playerInventories.containsKey(player))
			playerInventories.remove(player);
	}

	@Override
	protected void onInventoryClick(InventoryClickEvent event) {
		if (event.getClickedInventory() == null)
			return;
		if (event.getWhoClicked() instanceof Player) {
			Player player = (Player) event.getWhoClicked();
			if (!exist(player))
				return;
			VInventory gui = playerInventories.get(player);
			if (gui.getGuiName() == null || gui.getGuiName().length() == 0) {
				Logger.info("An error has occurred with the menu ! " + gui.getClass().getName());
				return;
			}
			if (event.getView() != null && gui.getPlayer().equals(player)
					&& event.getView().getTitle().equals(gui.getGuiName())) {
				event.setCancelled(true);
				gui.setItem(event.getCurrentItem());
				gui.setSlot(event.getSlot());
				gui.onClick(event, plugin, player);
				return;
			}
		}
	}

	@Override
	protected void onInventoryClose(InventoryCloseEvent event) {
		Player player = (Player) event.getPlayer();
		if (!exist(player))
			return;
		VInventory inventory = playerInventories.get(player);
		remove(player);
		inventory.onClose(event, plugin, player);
	}

	@Override
	protected void onInventoryDrag(InventoryDragEvent event) {
		if (event.getWhoClicked() instanceof Player) {
			Player player = (Player) event.getWhoClicked();
			if (!exist(player))
				return;
			playerInventories.get(player).onDrag(event, plugin, player);
		}
	}

}
