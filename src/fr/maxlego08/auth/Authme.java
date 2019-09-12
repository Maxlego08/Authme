package fr.maxlego08.auth;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import fr.maxlego08.auth.auth.AuthListener;
import fr.maxlego08.auth.auth.AuthManager;
import fr.maxlego08.auth.command.CommandManager;
import fr.maxlego08.auth.inventory.InventoryManager;
import fr.maxlego08.auth.listener.AdapterListener;
import fr.maxlego08.auth.mail.MailManager;
import fr.maxlego08.auth.packet.PacketAuthClient;
import fr.maxlego08.auth.packet.PacketAuthServer;
import fr.maxlego08.auth.save.Config;
import fr.maxlego08.auth.zcore.ZPlugin;
import fr.maxlego08.auth.zcore.logger.Logger.LogType;
import net.minecraft.server.v1_7_R4.EnumProtocol;
import net.minecraft.util.com.google.common.collect.BiMap;

public class Authme extends ZPlugin {

	private CommandManager commandManager;
	private InventoryManager inventoryManager;
	
	@Override
	public void onEnable() {

		preEnable();

		commandManager = new CommandManager(this);
//		inventoryManager = new InventoryManager(this);

		/* Add Listener */

		addListener(new AdapterListener(this));
//		addListener(inventoryManager);
		addListener(new AuthListener());

		/* Add Saver */

		addSave(new Config());
		addSave(new AuthManager());
		addSave(new MailManager());

		getSavers().forEach(saver -> saver.load(getPersist()));

		try {
			setUp();
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchFieldException e) {
			e.printStackTrace();
		}

		autoSave();
		
		postEnable();

	}

	@Override
	public void onDisable() {

		preDisable();

		getSavers().forEach(saver -> saver.save(getPersist()));

		postDisable();

	}

	public CommandManager getCommandManager() {
		return commandManager;
	}

	public InventoryManager getInventoryManager() {
		return inventoryManager;
	}

	@SuppressWarnings("unchecked")
	private void setUp() throws NoSuchMethodException, SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchFieldException {
		Class<EnumProtocol> clazz = EnumProtocol.class;
		Field field = null;
		BiMap<Integer, Class<?>> packetsMap = null;

		field = clazz.getDeclaredField("i");
		field.setAccessible(true);
		packetsMap = (BiMap<Integer, Class<?>>) field.get(EnumProtocol.PLAY);
		packetsMap.put(Config.packetId, PacketAuthClient.class);
		setAsPlayPacket(PacketAuthClient.class);

		field = clazz.getDeclaredField("h");
		field.setAccessible(true);
		packetsMap = (BiMap<Integer, Class<?>>) field.get(EnumProtocol.PLAY);
		packetsMap.put(Config.packetId, PacketAuthServer.class);
		setAsPlayPacket(PacketAuthServer.class);
	}

	private void setAsPlayPacket(Class<?> clazz)
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Field f = EnumProtocol.class.getDeclaredField("f");
		f.setAccessible(true);
		@SuppressWarnings("unchecked")
		Map<Class<?>, EnumProtocol> map = (Map<Class<?>, EnumProtocol>) f.get(EnumProtocol.PLAY);
		map.put(clazz, EnumProtocol.PLAY);
	}

	private void autoSave() {
		final Timer timer = new Timer(true);
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				if (!isEnabled()) {
					timer.cancel();
					return;
				}
				long ms = System.currentTimeMillis();
				getSavers().forEach(saver -> saver.save(getPersist()));
				getLog().log("Sauvegarde des données effectués en " + Math.abs(ms - System.currentTimeMillis()) + " ms",
						LogType.INFO);
			}
		}, 1000 * 60 * 5, 1000 * 60 * 30);
	}
}
