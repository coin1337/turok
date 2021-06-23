package com.oldturok.turok.util;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;

public class ColourUtils {
   private ArrayList<ColourUtils.ColorName> initColorList() {
      ArrayList<ColourUtils.ColorName> colorList = new ArrayList();
      colorList.add(new ColourUtils.ColorName("AliceBlue", 240, 248, 255));
      colorList.add(new ColourUtils.ColorName("AntiqueWhite", 250, 235, 215));
      colorList.add(new ColourUtils.ColorName("Aqua", 0, 255, 255));
      colorList.add(new ColourUtils.ColorName("Aquamarine", 127, 255, 212));
      colorList.add(new ColourUtils.ColorName("Azure", 240, 255, 255));
      colorList.add(new ColourUtils.ColorName("Beige", 245, 245, 220));
      colorList.add(new ColourUtils.ColorName("Bisque", 255, 228, 196));
      colorList.add(new ColourUtils.ColorName("Black", 0, 0, 0));
      colorList.add(new ColourUtils.ColorName("BlanchedAlmond", 255, 235, 205));
      colorList.add(new ColourUtils.ColorName("Blue", 0, 0, 255));
      colorList.add(new ColourUtils.ColorName("BlueViolet", 138, 43, 226));
      colorList.add(new ColourUtils.ColorName("Brown", 165, 42, 42));
      colorList.add(new ColourUtils.ColorName("BurlyWood", 222, 184, 135));
      colorList.add(new ColourUtils.ColorName("CadetBlue", 95, 158, 160));
      colorList.add(new ColourUtils.ColorName("Chartreuse", 127, 255, 0));
      colorList.add(new ColourUtils.ColorName("Chocolate", 210, 105, 30));
      colorList.add(new ColourUtils.ColorName("Coral", 255, 127, 80));
      colorList.add(new ColourUtils.ColorName("CornflowerBlue", 100, 149, 237));
      colorList.add(new ColourUtils.ColorName("Cornsilk", 255, 248, 220));
      colorList.add(new ColourUtils.ColorName("Crimson", 220, 20, 60));
      colorList.add(new ColourUtils.ColorName("Cyan", 0, 255, 255));
      colorList.add(new ColourUtils.ColorName("DarkBlue", 0, 0, 139));
      colorList.add(new ColourUtils.ColorName("DarkCyan", 0, 139, 139));
      colorList.add(new ColourUtils.ColorName("DarkGoldenRod", 184, 134, 11));
      colorList.add(new ColourUtils.ColorName("DarkGray", 169, 169, 169));
      colorList.add(new ColourUtils.ColorName("DarkGreen", 0, 100, 0));
      colorList.add(new ColourUtils.ColorName("DarkKhaki", 189, 183, 107));
      colorList.add(new ColourUtils.ColorName("DarkMagenta", 139, 0, 139));
      colorList.add(new ColourUtils.ColorName("DarkOliveGreen", 85, 107, 47));
      colorList.add(new ColourUtils.ColorName("DarkOrange", 255, 140, 0));
      colorList.add(new ColourUtils.ColorName("DarkOrchid", 153, 50, 204));
      colorList.add(new ColourUtils.ColorName("DarkRed", 139, 0, 0));
      colorList.add(new ColourUtils.ColorName("DarkSalmon", 233, 150, 122));
      colorList.add(new ColourUtils.ColorName("DarkSeaGreen", 143, 188, 143));
      colorList.add(new ColourUtils.ColorName("DarkSlateBlue", 72, 61, 139));
      colorList.add(new ColourUtils.ColorName("DarkSlateGray", 47, 79, 79));
      colorList.add(new ColourUtils.ColorName("DarkTurquoise", 0, 206, 209));
      colorList.add(new ColourUtils.ColorName("DarkViolet", 148, 0, 211));
      colorList.add(new ColourUtils.ColorName("DeepPink", 255, 20, 147));
      colorList.add(new ColourUtils.ColorName("DeepSkyBlue", 0, 191, 255));
      colorList.add(new ColourUtils.ColorName("DimGray", 105, 105, 105));
      colorList.add(new ColourUtils.ColorName("DodgerBlue", 30, 144, 255));
      colorList.add(new ColourUtils.ColorName("FireBrick", 178, 34, 34));
      colorList.add(new ColourUtils.ColorName("FloralWhite", 255, 250, 240));
      colorList.add(new ColourUtils.ColorName("ForestGreen", 34, 139, 34));
      colorList.add(new ColourUtils.ColorName("Fuchsia", 255, 0, 255));
      colorList.add(new ColourUtils.ColorName("Gainsboro", 220, 220, 220));
      colorList.add(new ColourUtils.ColorName("GhostWhite", 248, 248, 255));
      colorList.add(new ColourUtils.ColorName("Gold", 255, 215, 0));
      colorList.add(new ColourUtils.ColorName("GoldenRod", 218, 165, 32));
      colorList.add(new ColourUtils.ColorName("Gray", 128, 128, 128));
      colorList.add(new ColourUtils.ColorName("Green", 0, 128, 0));
      colorList.add(new ColourUtils.ColorName("GreenYellow", 173, 255, 47));
      colorList.add(new ColourUtils.ColorName("HoneyDew", 240, 255, 240));
      colorList.add(new ColourUtils.ColorName("HotPink", 255, 105, 180));
      colorList.add(new ColourUtils.ColorName("IndianRed", 205, 92, 92));
      colorList.add(new ColourUtils.ColorName("Indigo", 75, 0, 130));
      colorList.add(new ColourUtils.ColorName("Ivory", 255, 255, 240));
      colorList.add(new ColourUtils.ColorName("Khaki", 240, 230, 140));
      colorList.add(new ColourUtils.ColorName("Lavender", 230, 230, 250));
      colorList.add(new ColourUtils.ColorName("LavenderBlush", 255, 240, 245));
      colorList.add(new ColourUtils.ColorName("LawnGreen", 124, 252, 0));
      colorList.add(new ColourUtils.ColorName("LemonChiffon", 255, 250, 205));
      colorList.add(new ColourUtils.ColorName("LightBlue", 173, 216, 230));
      colorList.add(new ColourUtils.ColorName("LightCoral", 240, 128, 128));
      colorList.add(new ColourUtils.ColorName("LightCyan", 224, 255, 255));
      colorList.add(new ColourUtils.ColorName("LightGoldenRodYellow", 250, 250, 210));
      colorList.add(new ColourUtils.ColorName("LightGray", 211, 211, 211));
      colorList.add(new ColourUtils.ColorName("LightGreen", 144, 238, 144));
      colorList.add(new ColourUtils.ColorName("LightPink", 255, 182, 193));
      colorList.add(new ColourUtils.ColorName("LightSalmon", 255, 160, 122));
      colorList.add(new ColourUtils.ColorName("LightSeaGreen", 32, 178, 170));
      colorList.add(new ColourUtils.ColorName("LightSkyBlue", 135, 206, 250));
      colorList.add(new ColourUtils.ColorName("LightSlateGray", 119, 136, 153));
      colorList.add(new ColourUtils.ColorName("LightSteelBlue", 176, 196, 222));
      colorList.add(new ColourUtils.ColorName("LightYellow", 255, 255, 224));
      colorList.add(new ColourUtils.ColorName("Lime", 0, 255, 0));
      colorList.add(new ColourUtils.ColorName("LimeGreen", 50, 205, 50));
      colorList.add(new ColourUtils.ColorName("Linen", 250, 240, 230));
      colorList.add(new ColourUtils.ColorName("Magenta", 255, 0, 255));
      colorList.add(new ColourUtils.ColorName("Maroon", 128, 0, 0));
      colorList.add(new ColourUtils.ColorName("MediumAquaMarine", 102, 205, 170));
      colorList.add(new ColourUtils.ColorName("MediumBlue", 0, 0, 205));
      colorList.add(new ColourUtils.ColorName("MediumOrchid", 186, 85, 211));
      colorList.add(new ColourUtils.ColorName("MediumPurple", 147, 112, 219));
      colorList.add(new ColourUtils.ColorName("MediumSeaGreen", 60, 179, 113));
      colorList.add(new ColourUtils.ColorName("MediumSlateBlue", 123, 104, 238));
      colorList.add(new ColourUtils.ColorName("MediumSpringGreen", 0, 250, 154));
      colorList.add(new ColourUtils.ColorName("MediumTurquoise", 72, 209, 204));
      colorList.add(new ColourUtils.ColorName("MediumVioletRed", 199, 21, 133));
      colorList.add(new ColourUtils.ColorName("MidnightBlue", 25, 25, 112));
      colorList.add(new ColourUtils.ColorName("MintCream", 245, 255, 250));
      colorList.add(new ColourUtils.ColorName("MistyRose", 255, 228, 225));
      colorList.add(new ColourUtils.ColorName("Moccasin", 255, 228, 181));
      colorList.add(new ColourUtils.ColorName("NavajoWhite", 255, 222, 173));
      colorList.add(new ColourUtils.ColorName("Navy", 0, 0, 128));
      colorList.add(new ColourUtils.ColorName("OldLace", 253, 245, 230));
      colorList.add(new ColourUtils.ColorName("Olive", 128, 128, 0));
      colorList.add(new ColourUtils.ColorName("OliveDrab", 107, 142, 35));
      colorList.add(new ColourUtils.ColorName("Orange", 255, 165, 0));
      colorList.add(new ColourUtils.ColorName("OrangeRed", 255, 69, 0));
      colorList.add(new ColourUtils.ColorName("Orchid", 218, 112, 214));
      colorList.add(new ColourUtils.ColorName("PaleGoldenRod", 238, 232, 170));
      colorList.add(new ColourUtils.ColorName("PaleGreen", 152, 251, 152));
      colorList.add(new ColourUtils.ColorName("PaleTurquoise", 175, 238, 238));
      colorList.add(new ColourUtils.ColorName("PaleVioletRed", 219, 112, 147));
      colorList.add(new ColourUtils.ColorName("PapayaWhip", 255, 239, 213));
      colorList.add(new ColourUtils.ColorName("PeachPuff", 255, 218, 185));
      colorList.add(new ColourUtils.ColorName("Peru", 205, 133, 63));
      colorList.add(new ColourUtils.ColorName("Pink", 255, 192, 203));
      colorList.add(new ColourUtils.ColorName("Plum", 221, 160, 221));
      colorList.add(new ColourUtils.ColorName("PowderBlue", 176, 224, 230));
      colorList.add(new ColourUtils.ColorName("Purple", 128, 0, 128));
      colorList.add(new ColourUtils.ColorName("Red", 255, 0, 0));
      colorList.add(new ColourUtils.ColorName("RosyBrown", 188, 143, 143));
      colorList.add(new ColourUtils.ColorName("RoyalBlue", 65, 105, 225));
      colorList.add(new ColourUtils.ColorName("SaddleBrown", 139, 69, 19));
      colorList.add(new ColourUtils.ColorName("Salmon", 250, 128, 114));
      colorList.add(new ColourUtils.ColorName("SandyBrown", 244, 164, 96));
      colorList.add(new ColourUtils.ColorName("SeaGreen", 46, 139, 87));
      colorList.add(new ColourUtils.ColorName("SeaShell", 255, 245, 238));
      colorList.add(new ColourUtils.ColorName("Sienna", 160, 82, 45));
      colorList.add(new ColourUtils.ColorName("Silver", 192, 192, 192));
      colorList.add(new ColourUtils.ColorName("SkyBlue", 135, 206, 235));
      colorList.add(new ColourUtils.ColorName("SlateBlue", 106, 90, 205));
      colorList.add(new ColourUtils.ColorName("SlateGray", 112, 128, 144));
      colorList.add(new ColourUtils.ColorName("Snow", 255, 250, 250));
      colorList.add(new ColourUtils.ColorName("SpringGreen", 0, 255, 127));
      colorList.add(new ColourUtils.ColorName("SteelBlue", 70, 130, 180));
      colorList.add(new ColourUtils.ColorName("Tan", 210, 180, 140));
      colorList.add(new ColourUtils.ColorName("Teal", 0, 128, 128));
      colorList.add(new ColourUtils.ColorName("Thistle", 216, 191, 216));
      colorList.add(new ColourUtils.ColorName("Tomato", 255, 99, 71));
      colorList.add(new ColourUtils.ColorName("Turquoise", 64, 224, 208));
      colorList.add(new ColourUtils.ColorName("Violet", 238, 130, 238));
      colorList.add(new ColourUtils.ColorName("Wheat", 245, 222, 179));
      colorList.add(new ColourUtils.ColorName("White", 255, 255, 255));
      colorList.add(new ColourUtils.ColorName("WhiteSmoke", 245, 245, 245));
      colorList.add(new ColourUtils.ColorName("Yellow", 255, 255, 0));
      colorList.add(new ColourUtils.ColorName("YellowGreen", 154, 205, 50));
      return colorList;
   }

   public static int toRGBA(double r, double g, double b, double a) {
      return toRGBA((float)r, (float)g, (float)b, (float)a);
   }

   public String getColorNameFromRgb(int r, int g, int b) {
      ArrayList<ColourUtils.ColorName> colorList = this.initColorList();
      ColourUtils.ColorName closestMatch = null;
      int minMSE = Integer.MAX_VALUE;
      Iterator var8 = colorList.iterator();

      while(var8.hasNext()) {
         ColourUtils.ColorName c = (ColourUtils.ColorName)var8.next();
         int mse = c.computeMSE(r, g, b);
         if (mse < minMSE) {
            minMSE = mse;
            closestMatch = c;
         }
      }

      if (closestMatch != null) {
         return closestMatch.getName();
      } else {
         return "No matched color name.";
      }
   }

   public String getColorNameFromHex(int hexColor) {
      int r = (hexColor & 16711680) >> 16;
      int g = (hexColor & '\uff00') >> 8;
      int b = hexColor & 255;
      return this.getColorNameFromRgb(r, g, b);
   }

   public int colorToHex(Color c) {
      return Integer.decode("0x" + Integer.toHexString(c.getRGB()).substring(2));
   }

   public String getColorNameFromColor(Color color) {
      return this.getColorNameFromRgb(color.getRed(), color.getGreen(), color.getBlue());
   }

   public static int toRGBA(int r, int g, int b, int a) {
      return (r << 16) + (g << 8) + (b << 0) + (a << 24);
   }

   public static int toRGBA(float r, float g, float b, float a) {
      return toRGBA((int)(r * 255.0F), (int)(g * 255.0F), (int)(b * 255.0F), (int)(a * 255.0F));
   }

   public static int toRGBA(float[] colors) {
      if (colors.length != 4) {
         throw new IllegalArgumentException("colors[] must have a length of 4!");
      } else {
         return toRGBA(colors[0], colors[1], colors[2], colors[3]);
      }
   }

   public static int toRGBA(double[] colors) {
      if (colors.length != 4) {
         throw new IllegalArgumentException("colors[] must have a length of 4!");
      } else {
         return toRGBA((float)colors[0], (float)colors[1], (float)colors[2], (float)colors[3]);
      }
   }

   public static int[] toRGBAArray(int colorBuffer) {
      return new int[]{colorBuffer >> 16 & 255, colorBuffer >> 8 & 255, colorBuffer & 255, colorBuffer >> 24 & 255};
   }

   public static final int changeAlpha(int origColor, int userInputedAlpha) {
      origColor &= 16777215;
      return userInputedAlpha << 24 | origColor;
   }

   public static class Colors {
      public static final int WHITE = ColourUtils.toRGBA(255, 255, 255, 255);
      public static final int BLACK = ColourUtils.toRGBA(0, 0, 0, 255);
      public static final int RED = ColourUtils.toRGBA(255, 0, 0, 255);
      public static final int GREEN = ColourUtils.toRGBA(0, 255, 0, 255);
      public static final int BLUE = ColourUtils.toRGBA(0, 0, 255, 255);
      public static final int ORANGE = ColourUtils.toRGBA(255, 128, 0, 255);
      public static final int PURPLE = ColourUtils.toRGBA(163, 73, 163, 255);
      public static final int GRAY = ColourUtils.toRGBA(127, 127, 127, 255);
      public static final int DARK_RED = ColourUtils.toRGBA(64, 0, 0, 255);
      public static final int YELLOW = ColourUtils.toRGBA(255, 255, 0, 255);
      public static final int RAINBOW = Integer.MIN_VALUE;
   }

   public class ColorName {
      public int r;
      public int g;
      public int b;
      public String name;

      public ColorName(String name, int r, int g, int b) {
         this.r = r;
         this.g = g;
         this.b = b;
         this.name = name;
      }

      public int computeMSE(int pixR, int pixG, int pixB) {
         return ((pixR - this.r) * (pixR - this.r) + (pixG - this.g) * (pixG - this.g) + (pixB - this.b) * (pixB - this.b)) / 3;
      }

      public int getR() {
         return this.r;
      }

      public int getG() {
         return this.g;
      }

      public int getB() {
         return this.b;
      }

      public String getName() {
         return this.name;
      }
   }
}
