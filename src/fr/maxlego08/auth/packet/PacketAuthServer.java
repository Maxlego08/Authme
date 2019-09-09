package fr.maxlego08.auth.packet;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.maxlego08.auth.auth.AuthAction;
import fr.maxlego08.auth.auth.AuthManager;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketDataSerializer;
import net.minecraft.server.v1_7_R4.PacketListener;

public class PacketAuthServer extends Packet {

	private Player player;
	private AuthAction action;
	private String password;

	@Override
	public void a(PacketDataSerializer reader) throws IOException {
		this.player = Bukkit.getPlayer(reader.c(32767));
		this.action = AuthAction.get(reader.readInt());
		this.password = reader.c(32767);
	}

	@Override
	public void b(PacketDataSerializer reader) throws IOException {
	}

	@Override
	public void handle(PacketListener reader) {
		switch (action) {
		case RECEIVE_LOGIN_PASSWORD:
			AuthManager.i.login(player, password);
			break;
		case RECEIVE_REGISTER_PASSWORD:
			AuthManager.i.register(player, password);
			break;
		case RECEIVE_LOGIN_CONFIRM:
			AuthManager.i.confirmLogin(player, password);
			break;
		default:
			break;
		}
	}

}
