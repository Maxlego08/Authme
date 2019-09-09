package fr.maxlego08.auth.packet;

import java.io.IOException;

import fr.maxlego08.auth.auth.AuthAction;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketDataSerializer;
import net.minecraft.server.v1_7_R4.PacketListener;

public class PacketAuthClient extends Packet {

	private final AuthAction action;
	private String message;

	/**
	 * @param action
	 * @param message
	 */
	public PacketAuthClient(AuthAction action, String message) {
		super();
		this.action = action;
		this.message = message;
	}

	/**
	 * @param auth
	 */
	public PacketAuthClient(AuthAction auth) {
		super();
		this.action = auth;
	}

	@Override
	public void a(PacketDataSerializer packet) throws IOException {

	}

	@Override
	public void b(PacketDataSerializer write) throws IOException {
		write.writeInt(action.getId());
		if (action.equals(AuthAction.LOGIN_ERROR))
			write.a(message);
	}

	@Override
	public void handle(PacketListener packet) {
		// TODO Auto-generated method stub

	}

}
