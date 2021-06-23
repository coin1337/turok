package com.oldturok.turok.util;

import com.oldturok.turok.gui.rgui.render.font.FontRenderer;
import com.oldturok.turok.gui.turok.TurokGUI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

public class Wrapper {
   private static FontRenderer fontRenderer;

   public static void init() {
      fontRenderer = TurokGUI.fontRenderer;
   }

   public static Minecraft getMinecraft() {
      return Minecraft.func_71410_x();
   }

   public static EntityPlayerSP getPlayer() {
      return getMinecraft().field_71439_g;
   }

   public static World getWorld() {
      return getMinecraft().field_71441_e;
   }

   public static int getKey(String keyname) {
      return Keyboard.getKeyIndex(keyname.toUpperCase());
   }

   public static FontRenderer getFontRenderer() {
      return fontRenderer;
   }
}
