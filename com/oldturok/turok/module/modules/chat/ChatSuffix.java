package com.oldturok.turok.module.modules.chat;

import com.oldturok.turok.event.events.PacketEvent;
import com.oldturok.turok.module.Module;
import com.oldturok.turok.setting.Setting;
import com.oldturok.turok.setting.Settings;
import com.oldturok.turok.util.TurokFancyChat;
import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.network.play.client.CPacketChatMessage;

@Module.Info(
   name = "ChatSuffix",
   category = Module.Category.TUROK_CHAT,
   description = "A different chat."
)
public class ChatSuffix extends Module {
   private Setting<Boolean> commands = this.register(Settings.b("Commands", true));
   private Setting<Boolean> i_speak_taco = this.register(Settings.b("I Speak Taco Suffix", false));
   private Setting<Boolean> turok = this.register(Settings.b("Turok Suffix", true));
   boolean suffix_accept;
   String suffix;
   @EventHandler
   public Listener<PacketEvent.Send> listener = new Listener((event) -> {
      if (event.getPacket() instanceof CPacketChatMessage) {
         if ((Boolean)this.turok.getValue()) {
            this.suffix = TurokFancyChat.TUROK_SUFFIX;
            this.suffix_accept = true;
            this.i_speak_taco.setValue(false);
         }

         if ((Boolean)this.i_speak_taco.getValue()) {
            this.suffix = TurokFancyChat.I_SPEAK_TACO_SUFFIX;
            this.suffix_accept = true;
            this.turok.setValue(false);
         }

         String msg = ((CPacketChatMessage)event.getPacket()).func_149439_c();
         if (msg.startsWith("/") && (Boolean)this.commands.getValue()) {
            this.suffix_accept = false;
         }

         if (msg.startsWith("&") && (Boolean)this.commands.getValue()) {
            this.suffix_accept = false;
         }

         if (msg.startsWith("?") && (Boolean)this.commands.getValue()) {
            this.suffix_accept = false;
         }

         if (msg.startsWith("!") && (Boolean)this.commands.getValue()) {
            this.suffix_accept = false;
         }

         if (msg.startsWith(":") && (Boolean)this.commands.getValue()) {
            this.suffix_accept = false;
         }

         if (msg.startsWith(";") && (Boolean)this.commands.getValue()) {
            this.suffix_accept = false;
         }

         if (msg.startsWith(".") && (Boolean)this.commands.getValue()) {
            this.suffix_accept = false;
         }

         if (msg.startsWith(",") && (Boolean)this.commands.getValue()) {
            this.suffix_accept = false;
         }

         if (msg.startsWith("-") && (Boolean)this.commands.getValue()) {
            this.suffix_accept = false;
         }

         if (msg.startsWith("_") && (Boolean)this.commands.getValue()) {
            this.suffix_accept = false;
         }

         if (msg.startsWith("#") && (Boolean)this.commands.getValue()) {
            this.suffix_accept = false;
         }

         if (this.suffix_accept) {
            msg = msg + this.suffix;
         } else {
            msg = msg + "";
         }

         if (msg.length() >= 256) {
            msg = msg.substring(0, 256);
         }

         ((CPacketChatMessage)event.getPacket()).field_149440_a = msg;
      }

   }, new Predicate[0]);
}
