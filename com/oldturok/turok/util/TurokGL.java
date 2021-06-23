package com.oldturok.turok.util;

import java.awt.Color;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public class TurokGL {
   public static Minecraft mc = Minecraft.func_71410_x();

   public static void refresh_color(float red, float green, float blue, float alpha) {
      GL11.glColor4f(red / 255.0F, green / 255.0F, blue / 255.0F, alpha / 255.0F);
   }

   public static void TurokRGBA(float alpha, float tick) {
      float[] tick_color = new float[]{(float)(System.currentTimeMillis() % 11520L) / 11520.0F};
      int color_rgb = Color.HSBtoRGB(tick_color[0], 1.0F, 1.0F);
      GL11.glColor4f((float)((color_rgb >> 16 & 255) / 255), (float)((color_rgb >> 8 & 255) / 255), (float)((color_rgb & 255) / 255), alpha / 255.0F);
      tick_color[0] += tick;
   }

   public static void DisableGL(int opengl) {
      GL11.glDisable(opengl);
   }

   public static void EnableGL(int opengl) {
      GL11.glEnable(opengl);
   }

   public static void FixRefreshColor() {
      DisableGL(3553);
      DisableGL(3042);
   }

   public static void LineGL(float size) {
      GL11.glLineWidth(size);
   }
}
