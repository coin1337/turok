package com.oldturok.turok.module.modules.combat;

import com.oldturok.turok.module.Module;
import com.oldturok.turok.setting.Setting;
import com.oldturok.turok.setting.Settings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemEndCrystal;
import net.minecraft.item.ItemExpBottle;

@Module.Info(
   name = "FastCE",
   description = "Fast Crystal and Exp Bottles.",
   category = Module.Category.TUROK_COMBAT
)
public class FastCE extends Module {
   private Setting<Boolean> crystal = this.register(Settings.b("Crystal", true));
   private Setting<Boolean> expbottle = this.register(Settings.b("Exp Bottles", true));

   public void onUpdate() {
      Item itemMainHand = mc.field_71439_g.func_184614_ca().func_77973_b();
      Item itemONotMainHand = mc.field_71439_g.func_184592_cb().func_77973_b();
      boolean expInMainHand = itemMainHand instanceof ItemExpBottle;
      boolean expNotInMainHand = itemONotMainHand instanceof ItemExpBottle;
      boolean crystalInMainHand = itemMainHand instanceof ItemEndCrystal;
      boolean crystalNotInMainHand = itemONotMainHand instanceof ItemEndCrystal;
      if ((Boolean)this.crystal.getValue() && crystalInMainHand | crystalNotInMainHand) {
         mc.field_71467_ac = 0;
      }

      if ((Boolean)this.expbottle.getValue() && expInMainHand | expNotInMainHand) {
         mc.field_71467_ac = 0;
      }

   }
}
