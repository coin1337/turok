package com.oldturok.turok.module.modules.movement;

import com.oldturok.turok.module.Module;

@Module.Info(
   name = "Sprint",
   description = "Make you run!",
   category = Module.Category.TUROK_PLAYER
)
public class Sprint extends Module {
   public void onUpdate() {
      if ((mc.field_71474_y.field_74351_w.func_151470_d() || mc.field_71474_y.field_74368_y.func_151470_d() || mc.field_71474_y.field_74370_x.func_151470_d() || mc.field_71474_y.field_74366_z.func_151470_d()) && !mc.field_71439_g.func_70093_af() && !mc.field_71439_g.field_70123_F && !((float)mc.field_71439_g.func_71024_bL().func_75116_a() <= 6.0F)) {
         mc.field_71439_g.func_70031_b(true);
      }

   }
}
