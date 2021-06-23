package com.oldturok.turok;

import net.minecraft.client.Minecraft;

public class TurokChat {
   public String name;
   public String description;
   public Minecraft mc = Minecraft.func_71410_x();

   public TurokChat(String name, String description) {
      this.name = name;
      this.description = description;
   }

   public boolean Get_Message(String[] message) {
      return false;
   }

   public String get_description() {
      return this.description;
   }

   public String get_name() {
      return this.name;
   }
}
