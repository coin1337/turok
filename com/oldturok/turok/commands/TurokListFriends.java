package com.oldturok.turok.commands;

import com.oldturok.turok.TurokChat;
import com.oldturok.turok.TurokFriends;
import com.oldturok.turok.TurokMessage;
import java.util.ArrayList;
import java.util.Iterator;

public class TurokListFriends extends TurokChat {
   String friend_string = "";

   public TurokListFriends() {
      super("friends", "-friends | For get list friends.");
   }

   public boolean Get_Message(String[] message) {
      if (message.length > 0) {
         TurokFriends var10000 = TurokFriends.INSTANCE;
         if (((ArrayList)TurokFriends.list_friends.getValue()).isEmpty()) {
            TurokMessage.send_client_msg("You have 0 friends. lol");
         } else {
            var10000 = TurokFriends.INSTANCE;
            Iterator var2 = ((ArrayList)TurokFriends.list_friends.getValue()).iterator();

            while(var2.hasNext()) {
               TurokFriends.Friend friend = (TurokFriends.Friend)var2.next();
               TurokMessage.send_client_msg("Friend: " + friend.get_user_name());
            }
         }
      } else {
         TurokMessage.send_error_msg("Try use only -friends for get a list, or use -help command friends/friend.");
      }

      return true;
   }
}
