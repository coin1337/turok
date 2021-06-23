package com.oldturok.turok.util;

import java.awt.Color;
import org.lwjgl.opengl.GL11;

public class HueCycler {
   int index = 0;
   int[] cycles;

   public HueCycler(int cycles) {
      if (cycles <= 0) {
         throw new IllegalArgumentException("cycles <= 0");
      } else {
         this.cycles = new int[cycles];
         double hue = 0.0D;
         double add = 1.0D / (double)cycles;

         for(int i = 0; i < cycles; ++i) {
            this.cycles[i] = Color.HSBtoRGB((float)hue, 1.0F, 1.0F);
            hue += add;
         }

      }
   }

   public void reset() {
      this.index = 0;
   }

   public void reset(int index) {
      this.index = index;
   }

   public int next() {
      int a = this.cycles[this.index];
      ++this.index;
      if (this.index >= this.cycles.length) {
         this.index = 0;
      }

      return a;
   }

   public void setNext() {
      int rgb = this.next();
   }

   public void set() {
      int rgb = this.cycles[this.index];
      float red = (float)(rgb >> 16 & 255) / 255.0F;
      float green = (float)(rgb >> 8 & 255) / 255.0F;
      float blue = (float)(rgb & 255) / 255.0F;
      GL11.glColor3f(red, green, blue);
   }

   public void setNext(float alpha) {
      int rgb = this.next();
      float red = (float)(rgb >> 16 & 255) / 255.0F;
      float green = (float)(rgb >> 8 & 255) / 255.0F;
      float blue = (float)(rgb & 255) / 255.0F;
      GL11.glColor4f(red, green, blue, alpha);
   }

   public int current() {
      return this.cycles[this.index];
   }
}
