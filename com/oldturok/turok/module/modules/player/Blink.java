package com.oldturok.turok.module.modules.player;

import com.oldturok.turok.event.events.PacketEvent;
import com.oldturok.turok.module.Module;
import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;

@Module.Info(
   name = "Blink",
   description = "Clone you and back type a phantasmm! buuu!",
   category = Module.Category.TUROK_PLAYER
)
public class Blink extends Module {
   Queue<CPacketPlayer> packets = new LinkedList();
   @EventHandler
   public Listener<PacketEvent.Send> listener = new Listener((event) -> {
      if (this.isEnabled() && event.getPacket() instanceof CPacketPlayer) {
         event.cancel();
         this.packets.add((CPacketPlayer)event.getPacket());
      }

   }, new Predicate[0]);
   private EntityOtherPlayerMP clonedPlayer;

   protected void onEnable() {
      if (mc.field_71439_g != null) {
         this.clonedPlayer = new EntityOtherPlayerMP(mc.field_71441_e, mc.func_110432_I().func_148256_e());
         this.clonedPlayer.func_82149_j(mc.field_71439_g);
         this.clonedPlayer.field_70759_as = mc.field_71439_g.field_70759_as;
         mc.field_71441_e.func_73027_a(-100, this.clonedPlayer);
      }

   }

   protected void onDisable() {
      while(!this.packets.isEmpty()) {
         mc.field_71439_g.field_71174_a.func_147297_a((Packet)this.packets.poll());
      }

      EntityPlayer localPlayer = mc.field_71439_g;
      if (localPlayer != null) {
         mc.field_71441_e.func_73028_b(-100);
         this.clonedPlayer = null;
      }

   }

   public String getHudInfo() {
      return String.valueOf(this.packets.size());
   }
}
