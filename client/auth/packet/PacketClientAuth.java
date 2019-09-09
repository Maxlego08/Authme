package fr.oldfight.auth.packet;

import java.io.IOException;

import fr.oldfight.auth.AuthAction;
import fr.oldfight.auth.AuthGui;
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
		if (action.equals(AuthAction.LOGIN_ERROR) || action.equals(AuthAction.CONFIRM_ERROR))
			message = reader.readStringFromBuffer(32767);
	}

	@Override
	public void writePacketData(PacketBuffer reader) throws IOException {
		
	}

	@Override
	public void processPacket(INetHandler reader) {
		switch (action) {
		case LOGIN_ERROR:
		case CONFIRM_ERROR:
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
		case CONFIRM_SUCCESS:
			Minecraft.getMinecraft().displayGuiScreen(null);
			break;
		case SEND_LOGIN:
			Minecraft.getMinecraft().displayGuiScreen(new GuiAuth(AuthGui.LOGIN));
			break;
		case SEND_REGISTER:
			Minecraft.getMinecraft().displayGuiScreen(new GuiAuth(AuthGui.REGISTER));
			break;
		case SEND_LOGIN_CONFIRM:
			Minecraft.getMinecraft().displayGuiScreen(new GuiAuth(AuthGui.CONFIRM));
			break;
		default:
			break;

		}
	}

}
