package com.oldturok.turok.module.modules.combat;

import com.oldturok.turok.event.TurokEvent;
import com.oldturok.turok.event.events.EntityEvent;
import com.oldturok.turok.event.events.PacketEvent;
import com.oldturok.turok.module.Module;
import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;

@Module.Info(
   name = "AntiKnockback",
   description = "Modify knockback to 0.",
   category = Module.Category.TUROK_MOVEMENT
)
public class AntiKnockback extends Module {
   @EventHandler
   private Listener<PacketEvent.Receive> packetEventListener = new Listener((event) -> {
      if (event.getEra() == TurokEvent.Era.PRE) {
         if (event.getPacket() instanceof SPacketEntityVelocity) {
            SPacketEntityVelocity knockback = (SPacketEntityVelocity)event.getPacket();
            if (knockback.func_149412_c() == mc.field_71439_g.field_145783_c) {
               event.cancel();
               knockback.field_149415_b = (int)((float)knockback.field_149415_b * 0.0F);
               knockback.field_149416_c = (int)((float)knockback.field_149416_c * 0.0F);
               knockback.field_149414_d = (int)((float)knockback.field_149414_d * 0.0F);
            }
         } else if (event.getPacket() instanceof SPacketExplosion) {
            event.cancel();
            SPacketExplosion knockbackx = (SPacketExplosion)event.getPacket();
            knockbackx.field_149152_f *= 0.0F;
            knockbackx.field_149153_g *= 0.0F;
            knockbackx.field_149159_h *= 0.0F;
         }
      }

   }, new Predicate[0]);
   @EventHandler
   private Listener<EntityEvent.EntityCollision> entityCollisionListener = new Listener((event) -> {
      if (event.getEntity() == mc.field_71439_g) {
         event.cancel();
      }
   }, new Predicate[0]);
}
