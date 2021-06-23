package com.oldturok.turok.module.modules.combat;

import com.oldturok.turok.module.Module;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;

@Module.Info(
   name = "AutoTotem",
   description = "Auto off hand Totem.",
   category = Module.Category.TUROK_COMBAT
)
public class AutoTotem extends Module {
   int count;
   boolean item = false;
   boolean move = false;

   public void onUpdate() {
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
            return itemStack.func_77973_b() == Items.field_190929_cY;
         }).mapToInt(ItemStack::func_190916_E).sum();
         if (mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_190929_cY) {
            ++this.count;
         } else {
            if (this.move) {
               mc.field_71442_b.func_187098_a(0, 45, 0, ClickType.PICKUP, mc.field_71439_g);
               this.move = false;
               if (!mc.field_71439_g.field_71071_by.field_70457_g.func_190926_b()) {
                  this.item = true;
               }

               return;
            }

            if (mc.field_71439_g.field_71071_by.field_70457_g.func_190926_b()) {
               if (this.count == 0) {
                  return;
               }

               _item = -1;

               for(item_ = 0; item_ < 45; ++item_) {
                  if (mc.field_71439_g.field_71071_by.func_70301_a(item_).func_77973_b() == Items.field_190929_cY) {
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
