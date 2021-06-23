package com.oldturok.turok.module.modules.chat;

import com.oldturok.turok.event.events.PacketEvent;
import com.oldturok.turok.module.Module;
import com.oldturok.turok.setting.Setting;
import com.oldturok.turok.setting.Settings;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerDigging.Action;

@Module.Info(
   name = "AnnoucSpamDontUse",
   description = "For spam annouc.",
   category = Module.Category.TUROK_CHAT
)
public class AnnoucSpam extends Module {
   private Setting<Integer> tick_ = this.register(Settings.integerBuilder("Tick").withRange(1, 50).withValue((int)10).build());
   private Setting<Integer> delay = this.register(Settings.integerBuilder("Delay").withRange(2000, 10000).withValue((int)2000).build());
   private Setting<Boolean> walk = this.register(Settings.b("Walk", false));
   private Setting<Boolean> break_ = this.register(Settings.b("Break", false));
   public int tick;
   public int no_spam;
   public boolean send_m = false;
   public int old_x;
   public int old_z;
   public static int x;
   public static int z;
   public int moved;
   public boolean block_break = false;
   public String type_block;
   public boolean get_damage = false;
   public float last_health;
   public float stable_health;
   public float health;
   public float damage;
   public int count = 0;
   public static ArrayList<Module> combat_modules = new ArrayList();
   public Boolean combat_actived = false;
   public String type_module;
   @EventHandler
   private Listener<PacketEvent.Send> packetEventSendListener = new Listener((event) -> {
      if (event.getPacket() instanceof CPacketPlayerDigging) {
         CPacketPlayerDigging player = (CPacketPlayerDigging)event.getPacket();
         if (player.func_180762_c().equals(Action.STOP_DESTROY_BLOCK)) {
            this.type_block = mc.field_71441_e.func_180495_p(player.func_179715_a()).func_177230_c().func_149732_F();
            this.block_break = true;
         }
      }

   }, new Predicate[0]);

   public void get_value() {
      if (mc.field_71439_g.field_71158_b.field_192832_b >= 0.1F) {
         x = this.old_x;
         z = this.old_z;
         this.old_x = (int)mc.field_71439_g.field_70165_t;
         this.old_z = (int)mc.field_71439_g.field_70161_v;
         this.moved = x - this.old_x + (z - this.old_z);
         this.send_m = true;
      } else {
         this.send_m = false;
      }

   }

   public void reset_var() {
      x = this.old_x;
      z = this.old_z;
      this.old_x = (int)mc.field_71439_g.field_70165_t;
      this.old_z = (int)mc.field_71439_g.field_70161_v;
      this.moved = 0;
   }

   public void update_tick() {
      Date hora = new Date();
      String data = (new SimpleDateFormat("HH:mm:ss")).format(hora);
      this.tick += (Integer)this.tick_.getValue();
      if (this.tick >= (Integer)this.delay.getValue()) {
         StringBuilder annoyer = new StringBuilder();
         if ((Boolean)this.walk.getValue()) {
            if (this.send_m) {
               annoyer.append("Im walking in " + data + " and ");
               this.count = 1;
            } else {
               annoyer.append("Im just stoped in " + data + " and ");
            }
         }

         if ((Boolean)this.break_.getValue() && this.block_break) {
            if (this.count == 1) {
               annoyer.append("I breaked " + this.type_block + ", ");
               this.block_break = false;
            } else {
               annoyer.append("breaked " + this.type_block + ", ");
               this.block_break = false;
            }
         }

         annoyer.append("thanks Turok.");
         this.send(annoyer.toString());
         annoyer.setLength(0);
         this.tick = 0;
      }

   }

   public void breaked_block(String block) {
      this.send("I breaked " + block + ", thanks Turok.");
   }

   public void onUpdate() {
      this.get_value();
      this.update_tick();
   }

   public void send(String message) {
      mc.field_71439_g.field_71174_a.func_147297_a(new CPacketChatMessage(message));
   }
}
