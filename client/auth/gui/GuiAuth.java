package fr.oldfight.auth.gui;

import java.awt.Color;

import fr.oldfight.auth.AuthAction;
import fr.oldfight.auth.AuthGui;
import fr.oldfight.auth.button.GuiButtonAuth;
import fr.oldfight.auth.packet.PacketServerAuth;
import fr.oldfight.guis.auction.GuiTextAuction;
import fr.oldfight.guis.modified.GuiMainMenu;
import fr.oldfight.utils.ColorType;
import fr.oldfight.utils.Module;
import fr.oldfight.utils.TextRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.WorldClient;

public class GuiAuth extends GuiScreen {

	private GuiTextAuction textField;
	private String password;

	private static String messageError = "";
	private static long messageErrorTime = 0;

	private final AuthGui gui;

	/**
	 * @param gui
	 */
	public GuiAuth(AuthGui gui) {
		super();
		this.gui = gui;
	}

	@Override
	public void initGui() {

		this.buttonList.clear();

		this.textField = new GuiTextAuction(this.mc.fontRenderer, this.width / 2 - 60, this.height / 2 - 14, 120, 20);
		this.textField.setFocused(true);

		this.buttonList.add(new GuiButtonAuth(0, this.width / 2 - 50, this.height / 2 + 70, 100, 20, "Connection"));
		this.buttonList
				.add(new GuiButtonAuth(50, this.width / 2 + 60, this.height / 2 - 110, 20, 20, "X", ColorType.ERROR));

	}

	@Override
	protected void actionPerformed(GuiButton b) {
		if (b.id == 50) {
			this.mc.theWorld.sendQuittingDisconnectingPacket();
			this.mc.loadWorld((WorldClient) null);
			this.mc.displayGuiScreen(new GuiMainMenu());
		} else if (b.id == 0) {
			if (password == null)
				sendMessage("§cVous devez rentrer un mot de passe !");
			else if (password.length() < gui.getLenght())
				sendMessage("§cVotre mot de passe doit avoir minimum §6" + gui.getLenght() + " §ccaractères");
			else {
				this.mc.thePlayer.sendQueue.addToSendQueue(new PacketServerAuth(password,
						(gui.equals(AuthGui.REGISTER) ? AuthAction.RECEIVE_REGISTER_PASSWORD
								: gui.equals(AuthGui.LOGIN) ? AuthAction.RECEIVE_LOGIN_PASSWORD
										: AuthAction.RECEIVE_LOGIN_CONFIRM)));
			}
		}
	}

	@Override
	public void drawScreen(int x, int y, float p_73863_3_) {

		this.drawDefaultBackground();

		Module module = new Module(this.width / 2 - 80, this.height / 2 - 110, 160, 220);
		module.setFillColor(new Color(40, 40, 40).getRGB(), 10, 10);

		Module down = new Module(this.width / 2 - 70, this.height / 2 + 110, 160, 2);
		down.setFillColor(new Color(255, 180, 0).getRGB());

		TextRenderer textRenderer = new TextRenderer();
		textRenderer.renderCenteredText("§6"+gui.getName(), this.width / 2, this.height / 2 - 100, 1);
		textRenderer.renderCenteredText("§c" + messageError, this.width / 2 + 5, this.height / 2 + 50, 1);

		this.textField.drawTextBox();

		if (messageErrorTime != 0 && Math.abs(System.currentTimeMillis() - messageErrorTime) >= 5000) {
			messageError = "";
			messageErrorTime = 0;
		}

		super.drawScreen(x, y, p_73863_3_);
	}

	public void updateScreen() {

		if (this.textField != null)
			this.textField.updateCursorCounter();
	}

	protected void keyTyped(char chart, int p_73869_2_) {
		if (this.textField.isFocused()) {
			this.textField.textboxKeyTyped(chart, p_73869_2_);
			this.password = textField.getText();
		}
	}

	protected void mouseClicked(int p_73864_1_, int p_73864_2_, int p_73864_3_) {
		super.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
		if (this.textField != null)
			this.textField.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
	}

	public static void sendMessage(String message) {
		messageErrorTime = System.currentTimeMillis();
		messageError = message;
	}

}
