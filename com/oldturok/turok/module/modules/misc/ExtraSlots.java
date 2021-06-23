package com.oldturok.turok.module.modules.misc;

import com.oldturok.turok.event.events.PacketEvent;
import com.oldturok.turok.module.Module;
import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.network.play.client.CPacketCloseWindow;

@Module.Info(
   name = "ExtraSlots",
   description = "Extra archive in inventory.",
   category = Module.Category.TUROK_MISC
)
public class ExtraSlots extends Module {
   @EventHandler
   private Listener<PacketEvent.Send> listener = new Listener((event) -> {
      if (event.getPacket() instanceof CPacketCloseWindow) {
         event.cancel();
      }

   }, new Predicate[0]);
}
