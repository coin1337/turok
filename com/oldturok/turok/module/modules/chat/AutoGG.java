package com.oldturok.turok.module.modules.chat;

import com.oldturok.turok.event.events.PacketEvent;
import com.oldturok.turok.module.Module;
import com.oldturok.turok.module.ModuleManager;
import com.oldturok.turok.util.EntityUtil;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.client.CPacketUseEntity.Action;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

@Module.Info(
   name = "AutoGG",
   description = "When you kill auto say.",
   category = Module.Category.TUROK_CHAT
)
public class AutoGG extends Module {
   private ConcurrentHashMap<String, Integer> target_players = null;
   @EventHandler
   public Listener<PacketEvent.Send> sendListener = new Listener((event) -> {
      if (mc.field_71439_g != null) {
         if (this.target_players == null) {
            this.target_players = new ConcurrentHashMap();
         }

         if (event.getPacket() instanceof CPacketUseEntity) {
            CPacketUseEntity cpacketUseEntity = (CPacketUseEntity)event.getPacket();
            if (cpacketUseEntity.func_149565_c().equals(Action.ATTACK)) {
               if (ModuleManager.getModuleByName("TurokCrystalAura").isDisabled()) {
                  Entity target_entity = cpacketUseEntity.func_149564_a(mc.field_71441_e);
                  if (EntityUtil.isPlayer(target_entity)) {
                     this.add_target_player(target_entity.func_70005_c_());
                  }
               }
            }
         }
      }
   }, new Predicate[0]);
   @EventHandler
   public Listener<LivingDeathEvent> LivingDeathEvent = new Listener((event) -> {
      if (mc.field_71439_g != null) {
         if (this.target_players == null) {
            this.target_players = new ConcurrentHashMap();
         }

         EntityLivingBase entity = event.getEntityLiving();
         if (entity != null) {
            EntityPlayer player = (EntityPlayer)entity;
            if (!(player.func_110143_aJ() > 0.0F)) {
               String name = player.func_70005_c_();
               if (this.should_announce(name)) {
                  this.send_announce(name);
               }

            }
         }
      }
   }, new Predicate[0]);

   public void onEnable() {
      this.target_players = new ConcurrentHashMap();
   }

   public void onDisable() {
      this.target_players = null;
   }

   public void onUpdate() {
      if (!this.isDisabled() && mc.field_71439_g != null) {
         if (this.target_players == null) {
            this.target_players = new ConcurrentHashMap();
         }

         Iterator var1 = mc.field_71441_e.func_72910_y().iterator();

         while(var1.hasNext()) {
            Entity entity = (Entity)var1.next();
            if (EntityUtil.isPlayer(entity)) {
               EntityPlayer player = (EntityPlayer)entity;
               if (!(player.func_110143_aJ() > 0.0F)) {
                  String name = player.func_70005_c_();
                  if (this.should_announce(name)) {
                     this.send_announce(name);
                     break;
                  }
               }
            }
         }

         this.target_players.forEach((namex, timeout) -> {
            if (timeout <= 0) {
               this.target_players.remove(namex);
            } else {
               this.target_players.put(namex, timeout - 1);
            }

         });
      }
   }

   private boolean should_announce(String name) {
      return this.target_players.containsKey(name);
   }

   private void send_announce(String name) {
      this.target_players.remove(name);
      StringBuilder msg = new StringBuilder();
      msg.append("GG " + name + ", good fight well played, thanks Turok.");
      String msg_ = msg.toString().replaceAll("§", "");
      if (msg_.length() > 255) {
         msg_ = msg_.substring(0, 255);
      }

      mc.field_71439_g.field_71174_a.func_147297_a(new CPacketChatMessage(msg_));
   }

   public void add_target_player(String name) {
      if (!Objects.equals(name, mc.field_71439_g.func_70005_c_())) {
         if (this.target_players == null) {
            this.target_players = new ConcurrentHashMap();
         }

         this.target_players.put(name, 20);
      }
   }
}
