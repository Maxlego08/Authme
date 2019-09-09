package fr.oldfight.auth.packet;

import java.io.IOException;

import fr.oldfight.auth.AuthAction;
import net.minecraft.client.Minecraft;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;

public class PacketServerAuth extends Packet {

	private final String password;
	private final AuthAction action;

	/**
	 * @param password
	 * @param action
	 */
	public PacketServerAuth(String password, AuthAction action) {
		super();
		this.password = password;
		this.action = action;
	}

	@Override
	public void readPacketData(PacketBuffer reader) throws IOException {
	}

	@Override
	public void writePacketData(PacketBuffer reader) throws IOException {
		reader.writeStringToBuffer(Minecraft.getMinecraft().thePlayer.getCommandSenderName());
		reader.writeInt(this.action.getId());
		reader.writeStringToBuffer(password);
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
	}

}
