package com.oldturok.turok.module.modules.movement;

import com.oldturok.turok.module.Module;
import com.oldturok.turok.setting.Setting;
import com.oldturok.turok.setting.Settings;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketEntityAction.Action;
import net.minecraft.util.math.MathHelper;

@Module.Info(
   name = "TurokElytraFly",
   description = "A Turok module for get fly into elytra.",
   category = Module.Category.TUROK_MOVEMENT
)
public class TurokElytraFly extends Module {
   private Setting<TurokElytraFly.Enum_fly_mode> fly_mode;

   public TurokElytraFly() {
      this.fly_mode = this.register(Settings.e("Mode Fly", TurokElytraFly.Enum_fly_mode.BOOST));
   }

   public void onUpdate() {
      if (mc.field_71439_g.field_71075_bZ.field_75100_b) {
         mc.field_71439_g.func_70016_h(0.0D, -0.003D, 0.0D);
         mc.field_71439_g.field_71075_bZ.func_75092_a(0.915F);
      }

      if (mc.field_71439_g.field_70122_E) {
         mc.field_71439_g.field_71075_bZ.field_75101_c = false;
      }

      if (mc.field_71439_g.func_184613_cA()) {
         switch((TurokElytraFly.Enum_fly_mode)this.fly_mode.getValue()) {
         case BOOST:
            if (mc.field_71439_g.func_70090_H()) {
               mc.func_147114_u().func_147297_a(new CPacketEntityAction(mc.field_71439_g, Action.START_FALL_FLYING));
               return;
            }

            EntityPlayerSP player8;
            if (mc.field_71474_y.field_74314_A.func_151470_d()) {
               player8 = mc.field_71439_g;
               player8.field_70181_x += 0.08D;
            } else if (mc.field_71474_y.field_74311_E.func_151470_d()) {
               player8 = mc.field_71439_g;
               player8.field_70181_x -= 0.04D;
            }

            EntityPlayerSP player11;
            EntityPlayerSP player12;
            float yaw;
            if (mc.field_71474_y.field_74351_w.func_151470_d()) {
               yaw = (float)Math.toRadians((double)mc.field_71439_g.field_70177_z);
               player11 = mc.field_71439_g;
               player11.field_70159_w -= (double)(MathHelper.func_76126_a(yaw) * 0.05F);
               player12 = mc.field_71439_g;
               player12.field_70179_y += (double)(MathHelper.func_76134_b(yaw) * 0.05F);
            } else if (mc.field_71474_y.field_74368_y.func_151470_d()) {
               yaw = (float)Math.toRadians((double)mc.field_71439_g.field_70177_z);
               player11 = mc.field_71439_g;
               player11.field_70159_w += (double)(MathHelper.func_76126_a(yaw) * 0.05F);
               player12 = mc.field_71439_g;
               player12.field_70179_y -= (double)(MathHelper.func_76134_b(yaw) * 0.05F);
            }
            break;
         case FLY:
            mc.field_71439_g.field_71075_bZ.func_75092_a(0.915F);
            mc.field_71439_g.field_71075_bZ.field_75100_b = true;
            if (mc.field_71439_g.field_71075_bZ.field_75098_d) {
               return;
            }

            mc.field_71439_g.field_71075_bZ.field_75101_c = true;
         }

      }
   }

   protected void onDisable() {
      mc.field_71439_g.field_71075_bZ.field_75100_b = false;
      mc.field_71439_g.field_71075_bZ.func_75092_a(0.05F);
      if (!mc.field_71439_g.field_71075_bZ.field_75098_d) {
         mc.field_71439_g.field_71075_bZ.field_75101_c = false;
      }
   }

   private static enum Enum_fly_mode {
      BOOST,
      FLY;
   }
}
