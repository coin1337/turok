package com.oldturok.turok.gui.turok;

import org.lwjgl.opengl.GL11;

public class RenderHelper {
   public static void drawArc(float cx, float cy, float r, float start_angle, float end_angle, int num_segments) {
      GL11.glBegin(4);

      for(int i = (int)((float)num_segments / (360.0F / start_angle)) + 1; (float)i <= (float)num_segments / (360.0F / end_angle); ++i) {
         double previousangle = 6.283185307179586D * (double)(i - 1) / (double)num_segments;
         double angle = 6.283185307179586D * (double)i / (double)num_segments;
         GL11.glVertex2d((double)cx, (double)cy);
         GL11.glVertex2d((double)cx + Math.cos(angle) * (double)r, (double)cy + Math.sin(angle) * (double)r);
         GL11.glVertex2d((double)cx + Math.cos(previousangle) * (double)r, (double)cy + Math.sin(previousangle) * (double)r);
      }

      GL11.glEnd();
   }

   public static void drawArcOutline(float cx, float cy, float r, float start_angle, float end_angle, int num_segments) {
      GL11.glBegin(2);

      for(int i = (int)((float)num_segments / (360.0F / start_angle)) + 1; (float)i <= (float)num_segments / (360.0F / end_angle); ++i) {
         double angle = 6.283185307179586D * (double)i / (double)num_segments;
         GL11.glVertex2d((double)cx + Math.cos(angle) * (double)r, (double)cy + Math.sin(angle) * (double)r);
      }

      GL11.glEnd();
   }

   public static void drawCircleOutline(float x, float y, float radius) {
      drawCircleOutline(x, y, radius, 0, 360, 40);
   }

   public static void drawCircleOutline(float x, float y, float radius, int start, int end, int segments) {
      drawArcOutline(x, y, radius, (float)start, (float)end, segments);
   }

   public static void drawCircle(float x, float y, float radius) {
      drawCircle(x, y, radius, 0, 360, 64);
   }

   public static void drawCircle(float x, float y, float radius, int start, int end, int segments) {
      drawArc(x, y, radius, (float)start, (float)end, segments);
   }

   public static void drawOutlinedRoundedRectangle(int x, int y, int width, int height, float radius, float dR, float dG, float dB, float dA, float outlineWidth) {
      drawRoundedRectangle((float)x, (float)y, (float)width, (float)height, radius);
      GL11.glColor4f(dR, dG, dB, dA);
      drawRoundedRectangle((float)x + outlineWidth, (float)y + outlineWidth, (float)width - outlineWidth * 2.0F, (float)height - outlineWidth * 2.0F, radius);
   }

   public static void drawRectangle(float x, float y, float width, float height) {
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glBegin(2);
      GL11.glVertex2d((double)width, 0.0D);
      GL11.glVertex2d(0.0D, 0.0D);
      GL11.glVertex2d(0.0D, (double)height);
      GL11.glVertex2d((double)width, (double)height);
      GL11.glEnd();
   }

   public static void drawFilledRectangle(float x, float y, float width, float height) {
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glBegin(7);
      GL11.glVertex2d((double)(x + width), (double)y);
      GL11.glVertex2d((double)x, (double)y);
      GL11.glVertex2d((double)x, (double)(y + height));
      GL11.glVertex2d((double)(x + width), (double)(y + height));
      GL11.glEnd();
   }

   public static void drawRoundedRectangle(float x, float y, float width, float height, float radius) {
      GL11.glEnable(3042);
      drawArc(x + width - radius, y + height - radius, radius, 0.0F, 90.0F, 16);
      drawArc(x + radius, y + height - radius, radius, 90.0F, 180.0F, 16);
      drawArc(x + radius, y + radius, radius, 180.0F, 270.0F, 16);
      drawArc(x + width - radius, y + radius, radius, 270.0F, 360.0F, 16);
      GL11.glBegin(4);
      GL11.glVertex2d((double)(x + width - radius), (double)y);
      GL11.glVertex2d((double)(x + radius), (double)y);
      GL11.glVertex2d((double)(x + width - radius), (double)(y + radius));
      GL11.glVertex2d((double)(x + width - radius), (double)(y + radius));
      GL11.glVertex2d((double)(x + radius), (double)y);
      GL11.glVertex2d((double)(x + radius), (double)(y + radius));
      GL11.glVertex2d((double)(x + width), (double)(y + radius));
      GL11.glVertex2d((double)x, (double)(y + radius));
      GL11.glVertex2d((double)x, (double)(y + height - radius));
      GL11.glVertex2d((double)(x + width), (double)(y + radius));
      GL11.glVertex2d((double)x, (double)(y + height - radius));
      GL11.glVertex2d((double)(x + width), (double)(y + height - radius));
      GL11.glVertex2d((double)(x + width - radius), (double)(y + height - radius));
      GL11.glVertex2d((double)(x + radius), (double)(y + height - radius));
      GL11.glVertex2d((double)(x + width - radius), (double)(y + height));
      GL11.glVertex2d((double)(x + width - radius), (double)(y + height));
      GL11.glVertex2d((double)(x + radius), (double)(y + height - radius));
      GL11.glVertex2d((double)(x + radius), (double)(y + height));
      GL11.glEnd();
   }
}
