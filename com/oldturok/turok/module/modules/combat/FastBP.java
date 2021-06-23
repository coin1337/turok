package com.oldturok.turok.module.modules.combat;

import com.oldturok.turok.module.Module;
import com.oldturok.turok.setting.Setting;
import com.oldturok.turok.setting.Settings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemEndCrystal;
import net.minecraft.item.ItemExpBottle;

@Module.Info(
   name = "FastBP",
   description = "Fast Break and Place",
   category = Module.Category.TUROK_COMBAT
)
public class FastBP extends Module {
   private Setting<Boolean> break_ = this.register(Settings.b("Break", true));
   private Setting<Boolean> place = this.register(Settings.b("Place", true));

   public void onUpdate() {
      Item itemMainHand = mc.field_71439_g.func_184614_ca().func_77973_b();
      Item itemONotMainHand = mc.field_71439_g.func_184592_cb().func_77973_b();
      boolean expInMainHand = itemMainHand instanceof ItemExpBottle;
      boolean expNotInMainHand = itemONotMainHand instanceof ItemExpBottle;
      boolean crystalInMainHand = itemMainHand instanceof ItemEndCrystal;
      boolean crystalNotInMainHand = itemONotMainHand instanceof ItemEndCrystal;
      if ((Boolean)this.place.getValue()) {
         if (crystalInMainHand) {
            mc.field_71467_ac = 0;
         } else {
            mc.field_71467_ac = 1;
         }

         if (expInMainHand) {
            mc.field_71467_ac = 1;
         } else {
            mc.field_71467_ac = 0;
         }
      }

      if ((Boolean)this.break_.getValue()) {
         mc.field_71442_b.field_78781_i = 0;
      }

   }
}
