package com.oldturok.turok.module.modules.combat;

import com.oldturok.turok.module.Module;
import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.client.CPacketPlayerDigging.Action;
import net.minecraft.util.math.BlockPos;

@Module.Info(
   name = "FastBow",
   description = "For fast bow.",
   category = Module.Category.TUROK_COMBAT
)
public class FastBow extends Module {
   public void onUpdate() {
      if (mc.field_71439_g.field_71071_by.func_70448_g().func_77973_b() instanceof ItemBow && mc.field_71439_g.func_184587_cr() && mc.field_71439_g.func_184612_cw() >= 3) {
         mc.field_71439_g.field_71174_a.func_147297_a(new CPacketPlayerDigging(Action.RELEASE_USE_ITEM, BlockPos.field_177992_a, mc.field_71439_g.func_174811_aO()));
         mc.field_71439_g.field_71174_a.func_147297_a(new CPacketPlayerTryUseItem(mc.field_71439_g.func_184600_cs()));
         mc.field_71439_g.func_184597_cx();
      }

   }
}
