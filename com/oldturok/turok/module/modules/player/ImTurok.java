package com.oldturok.turok.module.modules.player;

import com.oldturok.turok.module.Module;
import com.oldturok.turok.setting.Setting;
import com.oldturok.turok.setting.Settings;
import net.minecraft.network.play.client.CPacketChatMessage;

@Module.Info(
   name = "Turok",
   description = "Just a simple module for say I am Turok!",
   category = Module.Category.TUROK_CHAT
)
public class ImTurok extends Module {
   private Setting<Boolean> toxic = this.register(Settings.b("Racist Mode : (", false));

   public void onEnable() {
      if ((Boolean)this.toxic.getValue()) {
         this.send("I AM TUROK, I AM FUCKING TUROK, NIGGA, FUCK YOU NIGGA!, sorry : (");
      } else {
         this.send("I AM TUROK!");
      }

      this.disable();
   }

   public void send(String message) {
      mc.field_71439_g.field_71174_a.func_147297_a(new CPacketChatMessage(message));
   }
}
