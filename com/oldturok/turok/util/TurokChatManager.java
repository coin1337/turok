package com.oldturok.turok.util;

import com.oldturok.turok.TurokChat;
import com.oldturok.turok.commands.TurokFriend;
import com.oldturok.turok.commands.TurokHelp;
import com.oldturok.turok.commands.TurokListFriends;
import com.oldturok.turok.commands.TurokPos;
import com.oldturok.turok.commands.TurokPrefix;
import com.oldturok.turok.commands.TurokToggle;
import com.oldturok.turok.setting.Setting;
import com.oldturok.turok.setting.Settings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import net.minecraft.util.text.Style;

public class TurokChatManager {
   public static ArrayList<TurokChat> command_list = new ArrayList();
   static HashMap<String, TurokChat> lookup = new HashMap();
   public static final Setting<String> prefix = Settings.s("Prefix", "-");
   public final Style format;

   public static void update_chat_to_accept_commands() {
      lookup.clear();
      Iterator var0 = command_list.iterator();

      while(var0.hasNext()) {
         TurokChat commands = (TurokChat)var0.next();
         lookup.put(commands.get_name().toLowerCase(), commands);
      }

   }

   public TurokChatManager(Style format_) {
      this.format = format_;
      command_list.add(new TurokFriend());
      command_list.add(new TurokHelp());
      command_list.add(new TurokListFriends());
      command_list.add(new TurokPos());
      command_list.add(new TurokPrefix());
      command_list.add(new TurokToggle());
   }

   public String[] GetArgs(String message) {
      String[] arguments = new String[0];
      if (message.startsWith((String)prefix.getValue())) {
         arguments = message.replaceFirst((String)prefix.getValue(), "").split(" ");
      }

      return arguments;
   }

   public boolean ContainsPrefix(String message) {
      return message.startsWith((String)prefix.getValue());
   }

   public static ArrayList<TurokChat> get_commands() {
      return command_list;
   }

   public String get_prefix() {
      return (String)prefix.getValue();
   }

   public void set_prefix(String new_prefix) {
      prefix.setValue(new_prefix);
   }

   public static TurokChat get_command(String cmd) {
      return (TurokChat)lookup.get(cmd.toLowerCase());
   }
}
