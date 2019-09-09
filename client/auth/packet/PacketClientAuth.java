package fr.oldfight.auth.packet;

import java.io.IOException;

import fr.oldfight.auth.AuthAction;
import fr.oldfight.auth.gui.GuiAuth;
import net.minecraft.client.Minecraft;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;

public class PacketClientAuth extends Packet{

	private AuthAction action;
	private String message;
	
	@Override
	public void readPacketData(PacketBuffer reader) throws IOException {
		action = AuthAction.get(reader.readInt());
		if (action.equals(AuthAction.LOGIN_ERROR))
			message = reader.readStringFromBuffer(32767);
	}

	@Override
	public void writePacketData(PacketBuffer reader) throws IOException {
		
	}

	@Override
	public void processPacket(INetHandler reader) {
		switch (action) {
		case LOGIN_ERROR:
			GuiAuth.sendMessage(message);
			break;
		case RECEIVE_LOGIN_PASSWORD:
			break;
		case RECEIVE_REGISTER_PASSWORD:
			break;
		case REGISTER_ERROR:
			break;
		case LOGIN_SUCCESS:
		case REGISTER_SUCCESS:
			Minecraft.getMinecraft().displayGuiScreen(null);
			break;
		case SEND_LOGIN:
			Minecraft.getMinecraft().displayGuiScreen(new GuiAuth(false));
			break;
		case SEND_REGISTER:
			Minecraft.getMinecraft().displayGuiScreen(new GuiAuth(true));
			break;
		default:
			break;

		}
	}

}
