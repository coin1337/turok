package com.oldturok.turok;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentBase;

public class TurokMessage {
   public static Minecraft mc = Minecraft.func_71410_x();

   public static void user_send_msg(String message) {
      if (mc.field_71439_g != null) {
         mc.field_71439_g.field_71174_a.func_147297_a(new CPacketChatMessage(message));
      }

   }

   public static void send_msg(String message) {
      if (mc.field_71439_g != null) {
         mc.field_71439_g.func_145747_a(new TurokMessage.ChatMessage(message));
      }

   }

   public static void send_client_msg(String message) {
      send_msg("ᴛᴜʀѳᴋ -  " + message);
   }

   public static void send_error_msg(String message) {
      send_msg("ᴛᴜʀѳᴋ - " + ChatFormatting.RED + message);
   }

   public static class ChatMessage extends TextComponentBase {
      String text;

      public ChatMessage(String text) {
         Pattern p = Pattern.compile("&[0123456789abcdefrlosmk]");
         Matcher m = p.matcher(text);
         StringBuffer sb = new StringBuffer();

         while(m.find()) {
            String replacement = "§" + m.group().substring(1);
            m.appendReplacement(sb, replacement);
         }

         m.appendTail(sb);
         this.text = sb.toString();
      }

      public String func_150261_e() {
         return this.text;
      }

      public ITextComponent func_150259_f() {
         return new TurokMessage.ChatMessage(this.text);
      }
   }
}
