package com.oldturok.turok.commands;

import com.oldturok.turok.TurokChat;
import com.oldturok.turok.TurokMessage;

public class TurokPos extends TurokChat {
   public TurokPos() {
      super("pos", "-pos [leak leak base!@! We are turok!] | For send into end a pos.");
   }

   public boolean Get_Message(String[] message) {
      String msg = "";

      int posX;
      for(posX = 1; posX < message.length; ++posX) {
         msg = msg + message[posX] + " ";
      }

      posX = (int)this.mc.field_71439_g.field_70165_t;
      int posY = (int)this.mc.field_71439_g.field_70163_u;
      int posZ = (int)this.mc.field_71439_g.field_70161_v;
      if (msg.equals("")) {
         TurokMessage.send_client_msg("For send a custom message with pos in the end. '-sendpos BASEEEEE!! foound!!! !! (pos)'");
      } else {
         TurokMessage.user_send_msg(msg + " " + Integer.toString(posX) + " " + Integer.toString(posY) + " " + Integer.toString(posZ));
      }

      return true;
   }
}
