package com.oldturok.turok.module.modules.movement;

import com.oldturok.turok.module.Module;
import net.minecraft.client.entity.EntityPlayerSP;

@Module.Info(
   name = "Strafe",
   description = "Strafe: only one speed for jump motion.",
   category = Module.Category.TUROK_HIDDEN
)
public class Strafe extends Module {
   float player_forward;
   float player_strafe;
   float player_pitch;
   float player_yaw;

   public void onUpdate() {
      this.player_forward = mc.field_71439_g.field_71158_b.field_192832_b;
      this.player_strafe = mc.field_71439_g.field_71158_b.field_78902_a;
      this.player_pitch = mc.field_71439_g.field_70125_A;
      this.player_yaw = mc.field_71439_g.field_70177_z;
      if (this.player_forward == 0.0F && this.player_strafe == 0.0F) {
         EntityPlayerSP var10000 = mc.field_71439_g;
         var10000.field_70159_w *= 0.0D;
         var10000 = mc.field_71439_g;
         var10000.field_70179_y *= 0.0D;
      } else if (this.player_forward != 0.0F) {
         if (this.player_strafe > 0.0F) {
            this.player_yaw += this.player_strafe > 0.0F ? -45.0F : 45.0F;
         } else if (this.player_strafe < 0.0F) {
            this.player_yaw += this.player_strafe > 0.0F ? 45.0F : -45.0F;
         }

         this.player_strafe = 0.0F;
         if (this.player_forward > 0.0F) {
            this.player_forward = 1.0F;
         } else if (this.player_forward < 0.0F) {
            this.player_forward = -1.0F;
         }
      }

      mc.field_71439_g.field_70159_w = (double)(this.player_forward * 0.2F) * Math.cos(Math.toRadians((double)(this.player_yaw + 90.0F))) + (double)(this.player_strafe * 0.2F) * Math.sin(Math.toRadians((double)(this.player_yaw + 90.0F)));
      mc.field_71439_g.field_70179_y = (double)(this.player_forward * 0.2F) * Math.sin(Math.toRadians((double)(this.player_yaw + 90.0F))) - (double)(this.player_strafe * 0.2F) * Math.cos(Math.toRadians((double)(this.player_yaw + 90.0F)));
   }
}
