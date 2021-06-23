package com.oldturok.turok.gui.turok;

import com.oldturok.turok.gui.rgui.render.font.FontRenderer;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public class RootFontRenderer implements FontRenderer {
   private final float fontsize;
   private final net.minecraft.client.gui.FontRenderer fontRenderer;
   public int main_x;
   public int main_y;

   public RootFontRenderer(float fontsize) {
      this.fontRenderer = Minecraft.func_71410_x().field_71466_p;
      this.fontsize = fontsize;
   }

   public int getFontHeight() {
      return (int)((float)Minecraft.func_71410_x().field_71466_p.field_78288_b * this.fontsize);
   }

   public int getStringHeight(String text) {
      return this.getFontHeight();
   }

   public int getX() {
      return this.main_x;
   }

   public int getY() {
      return this.main_y;
   }

   public int getStringWidth(String text) {
      return (int)((float)this.fontRenderer.func_78256_a(text) * this.fontsize);
   }

   public void drawString(int x, int y, String text) {
      this.drawString(x, y, 255, 255, 255, text);
   }

   public void drawString(int x, int y, int r, int g, int b, String text) {
      this.drawString(x, y, -16777216 | (r & 255) << 16 | (g & 255) << 8 | b & 255, text);
   }

   public void drawString(int x, int y, Color color, String text) {
      this.drawString(x, y, color.getRGB(), text);
   }

   public void drawString(int x, int y, int colour, String text) {
      this.drawString(x, y, colour, text, true);
   }

   public void drawString(int x, int y, int colour, String text, boolean shadow) {
      this.main_x = x;
      this.main_y = y;
      this.prepare(x, y);
      Minecraft.func_71410_x().field_71466_p.func_175065_a(text, 0.0F, 0.0F, colour, shadow);
      this.pop(x, y);
   }

   public void drawStringWithShadow(int x, int y, int r, int g, int b, String text) {
      this.drawString(x, y, -16777216 | (r & 255) << 16 | (g & 255) << 8 | b & 255, text, true);
   }

   private void prepare(int x, int y) {
      GL11.glEnable(3553);
      GL11.glEnable(3042);
      GL11.glTranslatef((float)x, (float)y, 0.0F);
      GL11.glScalef(this.fontsize, this.fontsize, 1.0F);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
   }

   private void pop(int x, int y) {
      GL11.glScalef(1.0F / this.fontsize, 1.0F / this.fontsize, 1.0F);
      GL11.glTranslatef((float)(-x), (float)(-y), 0.0F);
      GL11.glDisable(3553);
   }
}
