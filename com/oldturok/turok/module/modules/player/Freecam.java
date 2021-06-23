package com.oldturok.turok.module.modules.player;

import com.oldturok.turok.event.events.PacketEvent;
import com.oldturok.turok.event.events.PlayerMoveEvent;
import com.oldturok.turok.module.Module;
import com.oldturok.turok.setting.Setting;
import com.oldturok.turok.setting.Settings;
import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketInput;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.client.event.PlayerSPPushOutOfBlocksEvent;

@Module.Info(
   name = "FreeCamera",
   category = Module.Category.TUROK_PLAYER,
   description = "Leave your body and trascend into the realm of the gods"
)
public class Freecam extends Module {
   private Setting<Integer> speed = this.register(Settings.i("Speed", 5));
   private double posX;
   private double posY;
   private double posZ;
   private float pitch;
   private float yaw;
   private EntityOtherPlayerMP clonedPlayer;
   private boolean isRidingEntity;
   private Entity ridingEntity;
   @EventHandler
   private Listener<PlayerMoveEvent> moveListener = new Listener((event) -> {
      mc.field_71439_g.field_70145_X = true;
   }, new Predicate[0]);
   @EventHandler
   private Listener<PlayerSPPushOutOfBlocksEvent> pushListener = new Listener((event) -> {
      event.setCanceled(true);
   }, new Predicate[0]);
   @EventHandler
   private Listener<PacketEvent.Send> sendListener = new Listener((event) -> {
      if (event.getPacket() instanceof CPacketPlayer || event.getPacket() instanceof CPacketInput) {
         event.cancel();
      }

   }, new Predicate[0]);

   protected void onEnable() {
      if (mc.field_71439_g != null) {
         this.isRidingEntity = mc.field_71439_g.func_184187_bx() != null;
         if (mc.field_71439_g.func_184187_bx() == null) {
            this.posX = mc.field_71439_g.field_70165_t;
            this.posY = mc.field_71439_g.field_70163_u;
            this.posZ = mc.field_71439_g.field_70161_v;
         } else {
            this.ridingEntity = mc.field_71439_g.func_184187_bx();
            mc.field_71439_g.func_184210_p();
         }

         this.pitch = mc.field_71439_g.field_70125_A;
         this.yaw = mc.field_71439_g.field_70177_z;
         this.clonedPlayer = new EntityOtherPlayerMP(mc.field_71441_e, mc.func_110432_I().func_148256_e());
         this.clonedPlayer.func_82149_j(mc.field_71439_g);
         this.clonedPlayer.field_70759_as = mc.field_71439_g.field_70759_as;
         mc.field_71441_e.func_73027_a(-100, this.clonedPlayer);
         mc.field_71439_g.field_70159_w = 0.0D;
         mc.field_71439_g.field_70181_x = 0.0D;
         mc.field_71439_g.field_70179_y = 0.0D;
         mc.field_71439_g.field_70747_aH = (float)((Integer)this.speed.getValue() / 2);
         mc.field_71439_g.field_70145_X = true;
         EntityPlayerSP var10000;
         if (mc.field_71474_y.field_74314_A.func_151470_d()) {
            var10000 = mc.field_71439_g;
            var10000.field_70181_x += (double)(Integer)this.speed.getValue();
         }

         if (mc.field_71474_y.field_74311_E.func_151470_d()) {
            var10000 = mc.field_71439_g;
            var10000.field_70181_x -= (double)(Integer)this.speed.getValue();
         }
      }

   }

   protected void onDisable() {
      EntityPlayer localPlayer = mc.field_71439_g;
      if (localPlayer != null) {
         mc.field_71439_g.func_70080_a(this.posX, this.posY, this.posZ, this.yaw, this.pitch);
         mc.field_71441_e.func_73028_b(-100);
         this.clonedPlayer = null;
         this.posX = this.posY = this.posZ = 0.0D;
         this.pitch = this.yaw = 0.0F;
         mc.field_71439_g.field_71075_bZ.field_75100_b = false;
         mc.field_71439_g.field_71075_bZ.func_75092_a(0.05F);
         mc.field_71439_g.field_70145_X = false;
         mc.field_71439_g.field_70159_w = mc.field_71439_g.field_70181_x = mc.field_71439_g.field_70179_y = 0.0D;
         if (this.isRidingEntity) {
            mc.field_71439_g.func_184205_a(this.ridingEntity, true);
         }
      }

   }

   public void onUpdate() {
      mc.field_71439_g.field_71075_bZ.field_75100_b = true;
      mc.field_71439_g.field_71075_bZ.func_75092_a((float)(Integer)this.speed.getValue() / 100.0F);
      mc.field_71439_g.field_70145_X = true;
      mc.field_71439_g.field_70122_E = false;
      mc.field_71439_g.field_70143_R = 0.0F;
   }
}
