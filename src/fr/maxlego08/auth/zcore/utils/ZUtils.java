package fr.maxlego08.auth.zcore.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("deprecation")
public abstract class ZUtils {

	protected Location changeStringLocationToLocationEye(String s) {
		String[] a = s.split(",");
		World w = Bukkit.getServer().getWorld(a[0]);
		float x = Float.parseFloat(a[1]);
		float y = Float.parseFloat(a[2]);
		float z = Float.parseFloat(a[3]);
		float yaw = Float.parseFloat(a[4]);
		float pitch = Float.parseFloat(a[5]);
		return new Location(w, x, y, z, yaw, pitch);
	}
	
	protected String changeLocationToStringEye(Location location) {
		String ret = location.getWorld().getName() + "," + location.getX() + "," + location.getY() + ","
				+ location.getZ() + "," + location.getYaw() + "," + location.getPitch();
		return ret;
	}
	
	/**
	 * 
	 * */

	protected Location changeStringLocationToLocation(String s) {
		String[] a = s.split(",");
		World w = Bukkit.getServer().getWorld(a[0]);
		float x = Float.parseFloat(a[1]);
		float y = Float.parseFloat(a[2]);
		float z = Float.parseFloat(a[3]);
		return new Location(w, x, y, z);
	}

	/**
	 * 
	 * */

	protected String changeLocationToString(Location location) {
		String ret = location.getWorld().getName() + "," + location.getBlockX() + "," + location.getBlockY() + ","
				+ location.getBlockZ();
		return ret;
	}

	/**
	 * 
	 * */

	protected String encode(ItemStack item) {
		return ItemDecoder.serializeItemStack(item);
	}

	/**
	 * 
	 * */

	protected ItemStack decode(String item) {
		return ItemDecoder.deserializeItemStack(item);
	}

	/**
	 * 
	 * */

	protected String betterMaterial(Material material) {
		return TextUtil.getMaterialLowerAndMajAndSpace(material);
	}

	/*
	 * 
	 * */

	public int getNumberBetween(int a, int b) {
		return ThreadLocalRandom.current().nextInt(a, b);
	}

	/*
	 * 
	 * */

	protected boolean hasInventoryFull(Player p) {
		int slot = 0;
		ItemStack[] arrayOfItemStack;
		int x = (arrayOfItemStack = p.getInventory().getContents()).length;
		for (int i = 0; i < x; i++) {
			ItemStack contents = arrayOfItemStack[i];
			if ((contents == null))
				slot++;
		}
		return slot == 0;
	}

	/*
	 * 
	 * */

	protected void give(Player player, ItemStack item) {
		if (hasInventoryFull(player))
			player.getWorld().dropItem(player.getLocation(), item);
		else
			player.getInventory().addItem(item);
	}

	private static transient Material[] byId;

	static {
		byId = new Material[0];
		for (Material material : Material.values()) {
			if (byId.length > material.getId()) {
				byId[material.getId()] = material;
			} else {
				byId = Arrays.copyOfRange(byId, 0, material.getId() + 2);
				byId[material.getId()] = material;
			}
		}
	}

	public Material getMaterial(int id) {
		return byId.length > id && id >= 0 ? byId[id] : null;
	}

	protected boolean same(ItemStack stack, String name) {
		return stack.hasItemMeta() && stack.getItemMeta().hasDisplayName()
				&& stack.getItemMeta().getDisplayName().equals(name);
	}

	protected boolean contains(ItemStack stack, String name) {
		return stack.hasItemMeta() && stack.getItemMeta().hasDisplayName()
				&& stack.getItemMeta().getDisplayName().contains(name);
	}

	protected void removeItemInItem(Player player, int how) {
		if (player.getItemInHand().getAmount() > how)
			player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
		else
			player.setItemInHand(new ItemStack(Material.AIR));
		player.updateInventory();
	}

	protected boolean same(Location l, Location l2) {
		return (l.getBlockX() == l2.getBlockX()) && (l.getBlockY() == l2.getBlockY())
				&& (l.getBlockZ() == l2.getBlockZ());
	}

	protected void teleport(Player player, int delay, Location location) {
		ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);
		Location playerLocation = player.getLocation();
		AtomicInteger verif = new AtomicInteger(delay);
		if (!location.getChunk().isLoaded())
			location.getChunk().load();
		ses.scheduleWithFixedDelay(() -> {
			if (!same(playerLocation, player.getLocation())) {
				player.sendMessage("&cVous ne devez pas bouger !");
				ses.shutdown();
				return;
			}
			int s = verif.getAndDecrement();
			player.sendMessage(s != 0 ? "&eTÚlÚportatio dans &6" + s + " &esecondes !" : "&eTÚlÚportation !");
			if (s == 0) {
				ses.shutdown();
				player.teleport(location);
			}
		}, 0, 1, TimeUnit.SECONDS);
	}

	protected String hash(String passwordToHash, byte[] salt) {
		String generatedPassword = null;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			md.update(salt);
			byte[] bytes = md.digest(passwordToHash.getBytes());
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < bytes.length; i++) {
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			generatedPassword = sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return generatedPassword;
	}

}
