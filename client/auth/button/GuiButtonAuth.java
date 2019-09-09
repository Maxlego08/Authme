package fr.oldfight.auth.button;

import java.awt.Color;

import fr.oldfight.utils.ColorType;
import fr.oldfight.utils.Module;
import fr.oldfight.utils.TextRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class GuiButtonAuth extends GuiButton {

	private ColorType color = ColorType.DEFAULT;

	public GuiButtonAuth(int p_i46323_1_, int p_i46323_2_, int p_i46323_3_, int p_i46323_4_, int p_i46323_5_,
			String p_i46323_6_) {

		super(p_i46323_1_, p_i46323_2_, p_i46323_3_, p_i46323_4_, p_i46323_5_, p_i46323_6_);
	}

	public GuiButtonAuth(int p_i46323_1_, int p_i46323_2_, int p_i46323_3_, int p_i46323_4_, int p_i46323_5_,
			String p_i46323_6_, ColorType color) {

		super(p_i46323_1_, p_i46323_2_, p_i46323_3_, p_i46323_4_, p_i46323_5_, p_i46323_6_);
		this.color = color;
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY) {

		if (this.enabled) {

			this.field_146123_n = (mouseX >= this.field_146128_h && mouseY >= this.field_146129_i
					&& mouseX < this.field_146128_h + this.field_146120_f
					&& mouseY < this.field_146129_i + this.field_146121_g);

			Module back2 = new Module(this.field_146128_h + 4, this.field_146129_i + 3, this.field_146120_f - 6,
					this.field_146121_g - 5);
			back2.setFillColor(!this.field_146123_n ? this.color.getColor1() : this.color.getColor2(), -2, -2, -1, -1);
			TextRenderer textRenderer = new TextRenderer();
			textRenderer.renderCenteredText(this.displayString, this.field_146128_h + this.field_146120_f / 2,
					this.field_146129_i + (this.field_146121_g - 8) / 2, new Color(255, 255, 255).getRGB());
		}
	}
}
