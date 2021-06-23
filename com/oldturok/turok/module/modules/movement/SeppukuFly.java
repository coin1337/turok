package com.oldturok.turok.module.modules.movement;

import com.oldturok.turok.event.events.PacketEvent;
import com.oldturok.turok.module.Module;
import com.oldturok.turok.setting.Setting;
import com.oldturok.turok.setting.Settings;
import com.oldturok.turok.util.BlockInteractionHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayer.Position;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.InputUpdateEvent;

@Module.Info(
   name = "TurokSeppukuFly",
   description = "A seppuku fly embed in Turok!",
   category = Module.Category.TUROK_MOVEMENT
)
public class SeppukuFly extends Module {
   public final Setting<Float> fly_speed = this.register(Settings.floatBuilder("Speed").withValue((Number)0.1F).withMaximum(5.0F).withMinimum(0.0F).build());
   public final Setting<Boolean> fly_no_kick = this.register(Settings.b("NoKick", true));
   private int fly_teleport_id;
   private List<CPacketPlayer> fly_packets = new ArrayList();
   @EventHandler
   public Listener<InputUpdateEvent> listener;
   @EventHandler
   public Listener<PacketEvent.Send> sendListener;
   @EventHandler
   public Listener<PacketEvent.Receive> receiveListener;

   public SeppukuFly() {
      CPacketPlayer[] fly_bounds = new CPacketPlayer[1];
      double[] fly_speed_y = new double[1];
      double[] fly_speed_y_2 = new double[1];
      double[] fly_n = new double[1];
      double[][] fly_direcional_speed = new double[1][1];
      int[] fly_i = new int[1];
      int[] fly_j = new int[1];
      int[] fly_k = new int[1];
      this.listener = new Listener((event) -> {
         if (this.fly_teleport_id <= 0) {
            fly_bounds[0] = new Position(Minecraft.func_71410_x().field_71439_g.field_70165_t, 0.0D, Minecraft.func_71410_x().field_71439_g.field_70161_v, Minecraft.func_71410_x().field_71439_g.field_70122_E);
            this.fly_packets.add(fly_bounds[0]);
            Minecraft.func_71410_x().field_71439_g.field_71174_a.func_147297_a(fly_bounds[0]);
         } else {
            mc.field_71439_g.func_70016_h(0.0D, 0.0D, 0.0D);
            if (mc.field_71441_e.func_184144_a(mc.field_71439_g, mc.field_71439_g.func_174813_aQ().func_72321_a(-0.0625D, 0.0D, -0.0625D)).isEmpty()) {
               fly_speed_y[0] = 0.0D;
               if (mc.field_71474_y.field_74314_A.func_151470_d()) {
                  if ((Boolean)this.fly_no_kick.getValue()) {
                     fly_speed_y_2[0] = mc.field_71439_g.field_70173_aa % 20 == 0 ? -0.03999999910593033D : 0.06199999898672104D;
                  } else {
                     fly_speed_y_2[0] = 0.06199999898672104D;
                  }
               } else if (mc.field_71474_y.field_74311_E.func_151470_d()) {
                  fly_speed_y_2[0] = -0.062D;
               } else {
                  if (mc.field_71441_e.func_184144_a(mc.field_71439_g, mc.field_71439_g.func_174813_aQ().func_72321_a(-0.0625D, -0.0625D, -0.0625D)).isEmpty()) {
                     if (mc.field_71439_g.field_70173_aa % 4 == 0) {
                        fly_n[0] = (double)((Boolean)this.fly_no_kick.getValue() ? -0.04F : 0.0F);
                     } else {
                        fly_n[0] = 0.0D;
                     }
                  } else {
                     fly_n[0] = 0.0D;
                  }

                  fly_speed_y_2[0] = fly_n[0];
               }

               fly_direcional_speed[0] = BlockInteractionHelper.directionSpeed((double)(Float)this.fly_speed.getValue());
               if (!mc.field_71474_y.field_74314_A.func_151470_d() && !mc.field_71474_y.field_74311_E.func_151470_d() && !mc.field_71474_y.field_74351_w.func_151470_d() && !mc.field_71474_y.field_74368_y.func_151470_d() && !mc.field_71474_y.field_74366_z.func_151470_d() && !mc.field_71474_y.field_74370_x.func_151470_d()) {
                  if ((Boolean)this.fly_no_kick.getValue() && mc.field_71441_e.func_184144_a(mc.field_71439_g, mc.field_71439_g.func_174813_aQ().func_72321_a(-0.0625D, -0.0625D, -0.0625D)).isEmpty()) {
                     mc.field_71439_g.func_70016_h(0.0D, mc.field_71439_g.field_70173_aa % 2 == 0 ? 0.03999999910593033D : -0.03999999910593033D, 0.0D);
                     this.move(0.0D, mc.field_71439_g.field_70173_aa % 2 == 0 ? 0.03999999910593033D : -0.03999999910593033D, 0.0D);
                  }
               } else if (fly_direcional_speed[0][0] != 0.0D || fly_speed_y_2[0] != 0.0D || fly_direcional_speed[0][1] != 0.0D) {
                  int var10002;
                  if (mc.field_71439_g.field_71158_b.field_78901_c && (mc.field_71439_g.field_70702_br != 0.0F || mc.field_71439_g.field_191988_bg != 0.0F)) {
                     mc.field_71439_g.func_70016_h(0.0D, 0.0D, 0.0D);
                     this.move(0.0D, 0.0D, 0.0D);

                     for(fly_i[0] = 0; fly_i[0] <= 3; var10002 = fly_i[0]++) {
                        mc.field_71439_g.func_70016_h(0.0D, fly_speed_y_2[0] * (double)fly_i[0], 0.0D);
                        this.move(0.0D, fly_speed_y_2[0] * (double)fly_i[0], 0.0D);
                     }
                  } else if (mc.field_71439_g.field_71158_b.field_78901_c) {
                     mc.field_71439_g.func_70016_h(0.0D, 0.0D, 0.0D);
                     this.move(0.0D, 0.0D, 0.0D);

                     for(fly_j[0] = 0; fly_j[0] <= 3; var10002 = fly_j[0]++) {
                        mc.field_71439_g.func_70016_h(0.0D, fly_speed_y_2[0] * (double)fly_j[0], 0.0D);
                        this.move(0.0D, fly_speed_y_2[0] * (double)fly_j[0], 0.0D);
                     }
                  } else {
                     for(fly_k[0] = 0; fly_k[0] <= 2; var10002 = fly_k[0]++) {
                        mc.field_71439_g.func_70016_h(fly_direcional_speed[0][0] * (double)fly_k[0], fly_speed_y_2[0] * (double)fly_k[0], fly_direcional_speed[0][1] * (double)fly_k[0]);
                        this.move(fly_direcional_speed[0][0] * (double)fly_k[0], fly_speed_y_2[0] * (double)fly_k[0], fly_direcional_speed[0][1] * (double)fly_k[0]);
                     }
                  }
               }
            }

         }
      }, (Predicate[])(new Predicate[0]));
      CPacketPlayer[] packet = new CPacketPlayer[1];
      this.sendListener = new Listener((event) -> {
         if (event.getPacket() instanceof CPacketPlayer && !(event.getPacket() instanceof Position)) {
            event.cancel();
         }

         if (event.getPacket() instanceof CPacketPlayer) {
            packet[0] = (CPacketPlayer)event.getPacket();
            if (this.fly_packets.contains(packet[0])) {
               this.fly_packets.remove(packet[0]);
            } else {
               event.cancel();
            }
         }

      }, (Predicate[])(new Predicate[0]));
      SPacketPlayerPosLook[] packet2 = new SPacketPlayerPosLook[1];
      this.receiveListener = new Listener((event) -> {
         if (event.getPacket() instanceof SPacketPlayerPosLook) {
            packet2[0] = (SPacketPlayerPosLook)event.getPacket();
            if (Minecraft.func_71410_x().field_71439_g.func_70089_S() && Minecraft.func_71410_x().field_71441_e.func_175667_e(new BlockPos(Minecraft.func_71410_x().field_71439_g.field_70165_t, Minecraft.func_71410_x().field_71439_g.field_70163_u, Minecraft.func_71410_x().field_71439_g.field_70161_v)) && !(Minecraft.func_71410_x().field_71462_r instanceof GuiDownloadTerrain)) {
               if (this.fly_teleport_id <= 0) {
                  this.fly_teleport_id = packet2[0].func_186965_f();
               } else {
                  event.cancel();
               }
            }
         }

      }, (Predicate[])(new Predicate[0]));
   }

   public void onEnable() {
      if (mc.field_71441_e != null) {
         this.fly_teleport_id = 0;
         this.fly_packets.clear();
         CPacketPlayer fly_bounds = new Position(mc.field_71439_g.field_70165_t, 0.0D, mc.field_71439_g.field_70161_v, mc.field_71439_g.field_70122_E);
         this.fly_packets.add(fly_bounds);
         mc.field_71439_g.field_71174_a.func_147297_a(fly_bounds);
      }

   }

   private void move(double x, double y, double z) {
      Minecraft mc = Minecraft.func_71410_x();
      CPacketPlayer pos = new Position(mc.field_71439_g.field_70165_t + x, mc.field_71439_g.field_70163_u + y, mc.field_71439_g.field_70161_v + z, mc.field_71439_g.field_70122_E);
      this.fly_packets.add(pos);
      mc.field_71439_g.field_71174_a.func_147297_a(pos);
      CPacketPlayer fly_bounds = new Position(mc.field_71439_g.field_70165_t + x, 0.0D, mc.field_71439_g.field_70161_v + z, mc.field_71439_g.field_70122_E);
      this.fly_packets.add(fly_bounds);
      mc.field_71439_g.field_71174_a.func_147297_a(fly_bounds);
      ++this.fly_teleport_id;
      mc.field_71439_g.field_71174_a.func_147297_a(new CPacketConfirmTeleport(this.fly_teleport_id - 1));
      mc.field_71439_g.field_71174_a.func_147297_a(new CPacketConfirmTeleport(this.fly_teleport_id));
      mc.field_71439_g.field_71174_a.func_147297_a(new CPacketConfirmTeleport(this.fly_teleport_id + 1));
   }
}
