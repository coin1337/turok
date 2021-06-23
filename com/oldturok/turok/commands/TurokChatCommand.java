package com.oldturok.turok.commands;

import com.oldturok.turok.TurokChat;
import com.oldturok.turok.TurokMessage;
import com.oldturok.turok.util.TurokChatManager;
import java.util.Iterator;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TurokChatCommand {
   public static TurokChatManager chat_manager;

   public TurokChatCommand() {
      chat_manager = new TurokChatManager((new Style()).func_150238_a(TextFormatting.GRAY));
   }

   public static void turok_update_commands() {
      TurokChatManager var10000 = chat_manager;
      TurokChatManager.update_chat_to_accept_commands();
   }

   public void set_prefix(String new_prefix) {
      chat_manager.set_prefix(new_prefix);
   }

   public String get_prefix() {
      return chat_manager.get_prefix();
   }

   @SubscribeEvent
   public void onChat(ClientChatEvent event) {
      String msg = event.getMessage();
      String[] msg_args = chat_manager.GetArgs(event.getMessage());
      boolean command_us = false;
      if (msg_args.length > 0) {
         TurokChatManager var10000 = chat_manager;
         Iterator var5 = TurokChatManager.command_list.iterator();

         while(var5.hasNext()) {
            TurokChat chat = (TurokChat)var5.next();

            try {
               if (chat_manager.GetArgs(event.getMessage())[0].equalsIgnoreCase(chat.name)) {
                  command_us = chat.Get_Message(chat_manager.GetArgs(event.getMessage()));
               }
            } catch (Exception var8) {
            }
         }

         if (!command_us && chat_manager.ContainsPrefix(event.getMessage())) {
            TurokMessage.send_client_msg("Use command help or talk with Rina.");
            command_us = false;
         }

         event.setMessage("");
      }

   }
}
