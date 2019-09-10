package fr.oldfight.auth.text;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import fr.oldfight.utils.Module;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ChatAllowedCharacters;

public class GuiTextAuth extends Gui {

	private final FontRenderer fontrenderer;
	private final int x;
	private final int y;
	private final int w;
	private final int h;
	private String content = "";
	private int field_146217_k = 32;
	private int field_146214_l;

	private boolean drawCustom = true;

	private boolean drawRectangle = false;

	private boolean field_146212_n = true;
	private boolean field_146213_o;
	private boolean field_146226_p = true;
	private int field_146225_q;
	private int field_146224_r;
	private int field_146223_s;
	private int field_146222_t = 14737632;
	private int field_146221_u = 7368816;
	private boolean field_146220_v = true;

	public GuiTextAuth(FontRenderer f, int x, int y, int w, int h) {
		this.fontrenderer = f;
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}

	/**
	 * Increments the cursor counter
	 */
	public void updateCursorCounter() {
		++this.field_146214_l;
	}

	/**
	 * Sets the text of the textbox
	 */
	public void setText(String p_146180_1_) {
		if (p_146180_1_.length() > this.field_146217_k) {
			this.content = p_146180_1_.substring(0, this.field_146217_k);
		} else {
			this.content = p_146180_1_;
		}

		this.func_146202_e();
	}

	/**
	 * Returns the contents of the textbox
	 */
	public String getText() {
		return this.content;
	}

	public String func_146207_c() {
		int var1 = this.field_146224_r < this.field_146223_s ? this.field_146224_r : this.field_146223_s;
		int var2 = this.field_146224_r < this.field_146223_s ? this.field_146223_s : this.field_146224_r;
		return this.content.substring(var1, var2);
	}

	public void updateContent(String string) {
		String var2 = "";
		String var3 = ChatAllowedCharacters.filerAllowedCharacters(string);
		int var4 = this.field_146224_r < this.field_146223_s ? this.field_146224_r : this.field_146223_s;
		int var5 = this.field_146224_r < this.field_146223_s ? this.field_146223_s : this.field_146224_r;
		int var6 = this.field_146217_k - this.content.length() - (var4 - this.field_146223_s);

		if (this.content.length() > 0) {
			var2 = var2 + this.content.substring(0, var4);
		}

		int var8;

		if (var6 < var3.length()) {
			var2 = var2 + var3.substring(0, var6);
			var8 = var6;
		} else {
			var2 = var2 + var3;
			var8 = var3.length();
		}

		if (this.content.length() > 0 && var5 < this.content.length()) {
			var2 = var2 + this.content.substring(var5);
		}

		this.content = var2;
		this.func_146182_d(var4 - this.field_146223_s + var8);
	}

	public void func_146177_a(int p_146177_1_) {
		if (this.content.length() != 0) {
			if (this.field_146223_s != this.field_146224_r) {
				this.updateContent("");
			} else {
				this.func_146175_b(this.func_146187_c(p_146177_1_) - this.field_146224_r);
			}
		}
	}

	public void func_146175_b(int p_146175_1_) {
		if (this.content.length() != 0) {
			if (this.field_146223_s != this.field_146224_r) {
				this.updateContent("");
			} else {
				boolean var2 = p_146175_1_ < 0;
				int var3 = var2 ? this.field_146224_r + p_146175_1_ : this.field_146224_r;
				int var4 = var2 ? this.field_146224_r : this.field_146224_r + p_146175_1_;
				String var5 = "";

				if (var3 >= 0) {
					var5 = this.content.substring(0, var3);
				}

				if (var4 < this.content.length()) {
					var5 = var5 + this.content.substring(var4);
				}

				this.content = var5;

				if (var2) {
					this.func_146182_d(p_146175_1_);
				}
			}
		}
	}

	public int func_146187_c(int p_146187_1_) {
		return this.func_146183_a(p_146187_1_, this.func_146198_h());
	}

	public int func_146183_a(int p_146183_1_, int p_146183_2_) {
		return this.func_146197_a(p_146183_1_, this.func_146198_h(), true);
	}

	public int func_146197_a(int p_146197_1_, int p_146197_2_, boolean p_146197_3_) {
		int var4 = p_146197_2_;
		boolean var5 = p_146197_1_ < 0;
		int var6 = Math.abs(p_146197_1_);

		for (int var7 = 0; var7 < var6; ++var7) {
			if (var5) {
				while (p_146197_3_ && var4 > 0 && this.content.charAt(var4 - 1) == 32) {
					--var4;
				}

				while (var4 > 0 && this.content.charAt(var4 - 1) != 32) {
					--var4;
				}
			} else {
				int var8 = this.content.length();
				var4 = this.content.indexOf(32, var4);

				if (var4 == -1) {
					var4 = var8;
				} else {
					while (p_146197_3_ && var4 < var8 && this.content.charAt(var4) == 32) {
						++var4;
					}
				}
			}
		}

		return var4;
	}

	public void func_146182_d(int p_146182_1_) {
		this.func_146190_e(this.field_146223_s + p_146182_1_);
	}

	public void func_146190_e(int p_146190_1_) {
		this.field_146224_r = p_146190_1_;
		int var2 = this.content.length();

		if (this.field_146224_r < 0) {
			this.field_146224_r = 0;
		}

		if (this.field_146224_r > var2) {
			this.field_146224_r = var2;
		}

		this.func_146199_i(this.field_146224_r);
	}

	public void func_146196_d() {
		this.func_146190_e(0);
	}

	public void func_146202_e() {
		this.func_146190_e(this.content.length());
	}

	/**
	 * Call this method from your GuiScreen to process the keys into the textbox
	 */
	public boolean textboxKeyTyped(char chart, int p_146201_2_) {
		if (!this.field_146213_o) {
			return false;
		} else {
			switch (chart) {
			case 1:
				this.func_146202_e();
				this.func_146199_i(0);
				return true;

			case 3:
				GuiScreen.setClipboardString(this.func_146207_c());
				return true;

			case 22:
				if (this.field_146226_p) {
					this.updateContent(GuiScreen.getClipboardString());
				}

				return true;

			case 24:
				GuiScreen.setClipboardString(this.func_146207_c());

				if (this.field_146226_p) {
					this.updateContent("");
				}

				return true;

			default:
				switch (p_146201_2_) {
				case 14:
					if (GuiScreen.isCtrlKeyDown()) {
						if (this.field_146226_p) {
							this.func_146177_a(-1);
						}
					} else if (this.field_146226_p) {
						this.func_146175_b(-1);
					}

					return true;

				case 199:
					if (GuiScreen.isShiftKeyDown()) {
						this.func_146199_i(0);
					} else {
						this.func_146196_d();
					}

					return true;

				case 203:
					if (GuiScreen.isShiftKeyDown()) {
						if (GuiScreen.isCtrlKeyDown()) {
							this.func_146199_i(this.func_146183_a(-1, this.func_146186_n()));
						} else {
							this.func_146199_i(this.func_146186_n() - 1);
						}
					} else if (GuiScreen.isCtrlKeyDown()) {
						this.func_146190_e(this.func_146187_c(-1));
					} else {
						this.func_146182_d(-1);
					}

					return true;

				case 205:
					if (GuiScreen.isShiftKeyDown()) {
						if (GuiScreen.isCtrlKeyDown()) {
							this.func_146199_i(this.func_146183_a(1, this.func_146186_n()));
						} else {
							this.func_146199_i(this.func_146186_n() + 1);
						}
					} else if (GuiScreen.isCtrlKeyDown()) {
						this.func_146190_e(this.func_146187_c(1));
					} else {
						this.func_146182_d(1);
					}

					return true;

				case 207:
					if (GuiScreen.isShiftKeyDown()) {
						this.func_146199_i(this.content.length());
					} else {
						this.func_146202_e();
					}

					return true;

				case 211:
					if (GuiScreen.isCtrlKeyDown()) {
						if (this.field_146226_p) {
							this.func_146177_a(1);
						}
					} else if (this.field_146226_p) {
						this.func_146175_b(1);
					}

					return true;

				default:
					if (ChatAllowedCharacters.isAllowedCharacter(chart)) {
						if (this.field_146226_p) {
							this.updateContent(Character.toString(chart));
						}

						return true;
					} else {
						return false;
					}
				}
			}
		}
	}

	/**
	 * Args: x, y, buttonClicked
	 */
	public void mouseClicked(int p_146192_1_, int p_146192_2_, int p_146192_3_) {
		boolean var4 = p_146192_1_ >= this.x && p_146192_1_ < this.x + this.w && p_146192_2_ >= this.y
				&& p_146192_2_ < this.y + this.h;

		if (this.field_146212_n) {
			this.setFocused(var4);
		}

		if (this.field_146213_o && p_146192_3_ == 0) {
			int var5 = p_146192_1_ - this.x;

			if (this.drawCustom) {
				var5 -= 4;
			}

			String var6 = this.fontrenderer.trimStringToWidth(this.content.substring(this.field_146225_q),
					this.func_146200_o());
			this.func_146190_e(this.fontrenderer.trimStringToWidth(var6, var5).length() + this.field_146225_q);
		}
	}

	/**
	 * Draws the textbox
	 */
	public void drawTextBox() {
		if (this.func_146176_q()) {

			/*
			 * On draw le rectangle
			 */

			Module module = new Module(this.x - 3, this.y - 3, w + 6, h + 6);
			module.setFillColor(new Color(255, 180, 0).getRGB(), 5, 5);

			Module down = new Module(this.x, this.y, w, h);
			down.setFillColor(new Color(45, 45, 45).getRGB(), 5, 5);

			int var1 = this.field_146226_p ? this.field_146222_t : this.field_146221_u;
			int var2 = this.field_146224_r - this.field_146225_q;
			int var3 = this.field_146223_s - this.field_146225_q;
			String content = this.fontrenderer.trimStringToWidth(this.content.substring(this.field_146225_q),
					this.func_146200_o());
			boolean var5 = var2 >= 0 && var2 <= content.length();
			boolean var6 = this.field_146213_o && this.field_146214_l / 6 % 2 == 0 && var5;
			int var7 = this.drawCustom ? this.x + 4 : this.x;
			int var8 = this.drawCustom ? this.y + (this.h - 8) / 2 : this.y;
			int var9 = var7;

			if (var3 > content.length()) {
				var3 = content.length();
			}

			if (content.length() > 0) {
				String var10 = var5 ? content.substring(0, var2) : content;
				var9 = this.fontrenderer.drawStringWithShadow(getBlurString(var10), var7, var8, var1);
			}

			boolean var13 = this.field_146224_r < this.content.length()
					|| this.content.length() >= this.func_146208_g();
			int var11 = var9;

			if (!var5) {
				var11 = var2 > 0 ? var7 + this.w : var7;
			} else if (var13) {
				var11 = var9 - 1;
				--var9;
			}

			if (content.length() > 0 && var5 && var2 < content.length()) {
				this.fontrenderer.drawStringWithShadow(getBlurString(content.substring(var2)), var9, var8, var1);
			}

			if (var6) {
				if (var13) {
					Gui.drawRect(var11, var8 - 1, var11 + 1, var8 + 1 + this.fontrenderer.FONT_HEIGHT, -3092272);
				} else {
					this.fontrenderer.drawStringWithShadow("_", var11, var8, var1);
				}
			}

			if (var3 != var2) {
				int var12 = var7 + this.fontrenderer.getStringWidth(content.substring(0, var3));
				this.func_146188_c(var11, var8 - 1, var12 - 1, var8 + 1 + this.fontrenderer.FONT_HEIGHT);
			}
		}
	}

	private String getBlurString(String string) {
		String newString = "";
		for (int a = 0; a < string.length(); a++)
			newString += "*";
		return newString;
	}

	private void func_146188_c(int p_146188_1_, int p_146188_2_, int p_146188_3_, int p_146188_4_) {
		int var5;

		if (p_146188_1_ < p_146188_3_) {
			var5 = p_146188_1_;
			p_146188_1_ = p_146188_3_;
			p_146188_3_ = var5;
		}

		if (p_146188_2_ < p_146188_4_) {
			var5 = p_146188_2_;
			p_146188_2_ = p_146188_4_;
			p_146188_4_ = var5;
		}

		if (p_146188_3_ > this.x + this.w) {
			p_146188_3_ = this.x + this.w;
		}

		if (p_146188_1_ > this.x + this.w) {
			p_146188_1_ = this.x + this.w;
		}

		Tessellator var6 = Tessellator.instance;
		GL11.glColor4f(0.0F, 0.0F, 255.0F, 255.0F);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_COLOR_LOGIC_OP);
		GL11.glLogicOp(GL11.GL_OR_REVERSE);
		var6.startDrawingQuads();
		var6.addVertex((double) p_146188_1_, (double) p_146188_4_, 0.0D);
		var6.addVertex((double) p_146188_3_, (double) p_146188_4_, 0.0D);
		var6.addVertex((double) p_146188_3_, (double) p_146188_2_, 0.0D);
		var6.addVertex((double) p_146188_1_, (double) p_146188_2_, 0.0D);
		var6.draw();
		GL11.glDisable(GL11.GL_COLOR_LOGIC_OP);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	public void func_146203_f(int p_146203_1_) {
		this.field_146217_k = p_146203_1_;

		if (this.content.length() > p_146203_1_) {
			this.content = this.content.substring(0, p_146203_1_);
		}
	}

	public int func_146208_g() {
		return this.field_146217_k;
	}

	public int func_146198_h() {
		return this.field_146224_r;
	}

	public boolean func_146181_i() {
		return this.drawCustom;
	}

	public void func_146185_a(boolean d) {
		this.drawCustom = d;
	}

	public void func_146193_g(int p_146193_1_) {
		this.field_146222_t = p_146193_1_;
	}

	public void func_146204_h(int p_146204_1_) {
		this.field_146221_u = p_146204_1_;
	}

	/**
	 * Sets focus to this gui element
	 */
	public void setFocused(boolean p_146195_1_) {
		if (p_146195_1_ && !this.field_146213_o) {
			this.field_146214_l = 0;
		}

		this.field_146213_o = p_146195_1_;
	}

	/**
	 * Getter for the focused field
	 */
	public boolean isFocused() {
		return this.field_146213_o;
	}

	public void func_146184_c(boolean p_146184_1_) {
		this.field_146226_p = p_146184_1_;
	}

	public int func_146186_n() {
		return this.field_146223_s;
	}

	public int func_146200_o() {
		return this.func_146181_i() ? this.w - 8 : this.w;
	}

	public void func_146199_i(int p_146199_1_) {
		int var2 = this.content.length();

		if (p_146199_1_ > var2) {
			p_146199_1_ = var2;
		}

		if (p_146199_1_ < 0) {
			p_146199_1_ = 0;
		}

		this.field_146223_s = p_146199_1_;

		if (this.fontrenderer != null) {
			if (this.field_146225_q > var2) {
				this.field_146225_q = var2;
			}

			int var3 = this.func_146200_o();
			String var4 = this.fontrenderer.trimStringToWidth(this.content.substring(this.field_146225_q), var3);
			int var5 = var4.length() + this.field_146225_q;

			if (p_146199_1_ == this.field_146225_q) {
				this.field_146225_q -= this.fontrenderer.trimStringToWidth(this.content, var3, true).length();
			}

			if (p_146199_1_ > var5) {
				this.field_146225_q += p_146199_1_ - var5;
			} else if (p_146199_1_ <= this.field_146225_q) {
				this.field_146225_q -= this.field_146225_q - p_146199_1_;
			}

			if (this.field_146225_q < 0) {
				this.field_146225_q = 0;
			}

			if (this.field_146225_q > var2) {
				this.field_146225_q = var2;
			}
		}
	}

	public void func_146205_d(boolean p_146205_1_) {
		this.field_146212_n = p_146205_1_;
	}

	public boolean func_146176_q() {
		return this.field_146220_v;
	}

	public void func_146189_e(boolean p_146189_1_) {
		this.field_146220_v = p_146189_1_;
	}

	/**
	 * @return the drawRectangle
	 */
	public boolean isDrawRectangle() {
		return drawRectangle;
	}

	/**
	 * @param drawRectangle
	 *            the drawRectangle to set
	 */
	public void setDrawRectangle(boolean drawRectangle) {
		this.drawRectangle = drawRectangle;
	}

}
