package com.oldturok.turok.module.modules.combat;

import com.oldturok.turok.module.Module;
import com.oldturok.turok.module.ModuleManager;
import com.oldturok.turok.setting.Setting;
import com.oldturok.turok.setting.Settings;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;

@Module.Info(
   name = "AutoGapple",
   description = "Auto off hand gapple.",
   category = Module.Category.TUROK_COMBAT
)
public class AutoGapple extends Module {
   private Setting<Boolean> totem_disable = this.register(Settings.b("Totem Disable", true));
   private Setting<Boolean> totem_safe = this.register(Settings.b("Auto Safe", true));
   int count;
   boolean item = false;
   boolean move = false;

   public void onEnable() {
      if ((Boolean)this.totem_disable.getValue()) {
         ModuleManager.getModuleByName("AutoTotem").disable();
      }
   }

   public void onDisable() {
      if ((Boolean)this.totem_disable.getValue()) {
         ModuleManager.getModuleByName("AutoTotem").enable();
      }
   }

   public void onUpdate() {
      if ((Boolean)this.totem_safe.getValue() && mc.field_71439_g.func_110143_aJ() < 4.0F) {
         this.disable();
      }

      if (!(mc.field_71462_r instanceof GuiContainer)) {
         int _item;
         int item_;
         if (this.item) {
            _item = -1;

            for(item_ = 0; item_ < 45; ++item_) {
               if (mc.field_71439_g.field_71071_by.func_70301_a(item_).field_190928_g) {
                  _item = item_;
                  break;
               }
            }

            if (_item == -1) {
               return;
            }

            mc.field_71442_b.func_187098_a(0, _item < 9 ? _item + 36 : _item, 0, ClickType.PICKUP, mc.field_71439_g);
            this.item = false;
         }

         this.count = mc.field_71439_g.field_71071_by.field_70462_a.stream().filter((itemStack) -> {
            return itemStack.func_77973_b() == Items.field_151153_ao;
         }).mapToInt(ItemStack::func_190916_E).sum();
         if (mc.field_71439_g.func_184592_cb().func_77973_b() != Items.field_151153_ao) {
            if (this.move) {
               mc.field_71442_b.func_187098_a(0, 45, 0, ClickType.PICKUP, mc.field_71439_g);
               this.move = false;
               if (!mc.field_71439_g.field_71071_by.field_70457_g.func_190926_b()) {
                  this.item = true;
               }

            } else {
               if (mc.field_71439_g.field_71071_by.field_70457_g.func_190926_b()) {
                  if (this.count == 0) {
                     return;
                  }

                  _item = -1;

                  for(item_ = 0; item_ < 45; ++item_) {
                     if (mc.field_71439_g.field_71071_by.func_70301_a(item_).func_77973_b() == Items.field_151153_ao) {
                        _item = item_;
                        break;
                     }
                  }

                  if (_item == -1) {
                     return;
                  }

                  mc.field_71442_b.func_187098_a(0, _item < 9 ? _item + 36 : _item, 0, ClickType.PICKUP, mc.field_71439_g);
                  this.move = true;
               }

            }
         }
      }
   }
}
