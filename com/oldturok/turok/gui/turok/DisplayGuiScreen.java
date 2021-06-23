package com.oldturok.turok.gui.turok;

import com.oldturok.turok.TurokMod;
import com.oldturok.turok.gui.rgui.component.Component;
import com.oldturok.turok.gui.rgui.component.container.use.Frame;
import com.oldturok.turok.util.Wrapper;
import java.io.IOException;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class DisplayGuiScreen extends GuiScreen {
   TurokGUI gui;
   public final GuiScreen lastScreen;
   public static int mouseX;
   public static int mouseY;
   Framebuffer framebuffer;

   public DisplayGuiScreen(GuiScreen lastScreen) {
      this.lastScreen = lastScreen;
      TurokGUI gui = TurokMod.get_instance().get_gui_manager();
      Iterator var3 = gui.getChildren().iterator();

      while(var3.hasNext()) {
         Component c = (Component)var3.next();
         if (c instanceof Frame) {
            Frame child = (Frame)c;
            if (child.isPinneable() && child.isVisible()) {
               child.setOpacity(0.5F);
            }
         }
      }

      this.framebuffer = new Framebuffer(Wrapper.getMinecraft().field_71443_c, Wrapper.getMinecraft().field_71440_d, false);
   }

   public void func_146281_b() {
      TurokGUI gui = TurokMod.get_instance().get_gui_manager();
      gui.getChildren().stream().filter((component) -> {
         return component instanceof Frame && ((Frame)component).isPinneable() && component.isVisible();
      }).forEach((component) -> {
         component.setOpacity(0.0F);
      });
   }

   public void func_73866_w_() {
      this.gui = TurokMod.get_instance().get_gui_manager();
   }

   public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
      this.calculateMouse();
      this.gui.drawGUI();
      GL11.glEnable(3553);
      GlStateManager.func_179124_c(1.0F, 1.0F, 1.0F);
   }

   protected void func_73864_a(int mouseX, int mouseY, int mouseButton) throws IOException {
      this.gui.handleMouseDown(DisplayGuiScreen.mouseX, DisplayGuiScreen.mouseY);
   }

   protected void func_146286_b(int mouseX, int mouseY, int state) {
      this.gui.handleMouseRelease(DisplayGuiScreen.mouseX, DisplayGuiScreen.mouseY);
   }

   protected void func_146273_a(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
      this.gui.handleMouseDrag(DisplayGuiScreen.mouseX, DisplayGuiScreen.mouseY);
   }

   public void func_73876_c() {
      if (Mouse.hasWheel()) {
         int a = Mouse.getDWheel();
         if (a != 0) {
            this.gui.handleWheel(mouseX, mouseY, a);
         }
      }

   }

   protected void func_73869_a(char typedChar, int keyCode) throws IOException {
      if (keyCode == 1) {
         this.field_146297_k.func_147108_a(this.lastScreen);
      } else {
         this.gui.handleKeyDown(keyCode);
         this.gui.handleKeyUp(keyCode);
      }

   }

   public static int getScale() {
      int scale = Wrapper.getMinecraft().field_71474_y.field_74335_Z;
      if (scale == 0) {
         scale = 1000;
      }

      int scaleFactor;
      for(scaleFactor = 0; scaleFactor < scale && Wrapper.getMinecraft().field_71443_c / (scaleFactor + 1) >= 320 && Wrapper.getMinecraft().field_71440_d / (scaleFactor + 1) >= 240; ++scaleFactor) {
      }

      if (scaleFactor == 0) {
         scaleFactor = 1;
      }

      return scaleFactor;
   }

   private void calculateMouse() {
      Minecraft minecraft = Minecraft.func_71410_x();
      int scaleFactor = getScale();
      mouseX = Mouse.getX() / scaleFactor;
      mouseY = minecraft.field_71440_d / scaleFactor - Mouse.getY() / scaleFactor - 1;
   }
}
