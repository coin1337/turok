package com.oldturok.turok.module.modules.combat;

import com.oldturok.turok.module.Module;
import com.oldturok.turok.setting.Setting;
import com.oldturok.turok.setting.Settings;
import com.oldturok.turok.util.Pair;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.block.Block;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

@Module.Info(
   name = "AutoReplace",
   description = "Auto replace crystals stacks or any item.",
   category = Module.Category.TUROK_COMBAT
)
public class AutoReplace extends Module {
   private Setting<Integer> stack_value = this.register(Settings.integerBuilder("Replace in").withMinimum(1).withValue((int)16).withMaximum(63).build());
   int tick = 0;

   public void onUpdate() {
      if (mc.field_71439_g != null) {
         if (!(mc.field_71462_r instanceof GuiContainer)) {
            if (this.tick < 2) {
               ++this.tick;
            } else {
               this.tick = 0;
               Pair<Integer, Integer> slots = this.slots_replace();
               if (slots != null) {
                  int inventory_slots_replaced = (Integer)slots.getKey();
                  int inventory_hotbar_replaced = (Integer)slots.getValue();
                  mc.field_71442_b.func_187098_a(0, inventory_slots_replaced, 0, ClickType.PICKUP, mc.field_71439_g);
                  mc.field_71442_b.func_187098_a(0, inventory_hotbar_replaced, 0, ClickType.PICKUP, mc.field_71439_g);
                  mc.field_71442_b.func_187098_a(0, inventory_slots_replaced, 0, ClickType.PICKUP, mc.field_71439_g);
               }
            }
         }
      }
   }

   private Pair<Integer, Integer> slots_replace() {
      Pair<Integer, Integer> pair = null;
      Iterator var2 = hotbar().entrySet().iterator();

      while(var2.hasNext()) {
         Entry<Integer, ItemStack> slot = (Entry)var2.next();
         ItemStack stack = (ItemStack)slot.getValue();
         if (!stack.field_190928_g && stack.func_77973_b() != Items.field_190931_a && stack.func_77985_e() && stack.field_77994_a < stack.func_77976_d() && stack.field_77994_a <= (Integer)this.stack_value.getValue()) {
            int inventory_slot = this.compative_replace(stack);
            if (inventory_slot != -1) {
               pair = new Pair(inventory_slot, slot.getKey());
            }
         }
      }

      return pair;
   }

   private int compative_replace(ItemStack stack) {
      int slot = -1;
      int bit_stack = 999;
      Iterator var4 = inventory().entrySet().iterator();

      while(var4.hasNext()) {
         Entry<Integer, ItemStack> entry = (Entry)var4.next();
         ItemStack stack_replace = (ItemStack)entry.getValue();
         if (!stack_replace.field_190928_g && stack_replace.func_77973_b() != Items.field_190931_a && this.is_compative(stack, stack_replace)) {
            int current_stack = ((ItemStack)mc.field_71439_g.field_71069_bz.func_75138_a().get((Integer)entry.getKey())).field_77994_a;
            if (bit_stack > current_stack) {
               bit_stack = current_stack;
               slot = (Integer)entry.getKey();
            }
         }
      }

      return slot;
   }

   private boolean is_compative(ItemStack stack, ItemStack stack_) {
      if (!stack.func_77973_b().equals(stack_.func_77973_b())) {
         return false;
      } else {
         if (stack.func_77973_b() instanceof ItemBlock && stack_.func_77973_b() instanceof ItemBlock) {
            Block block_1 = ((ItemBlock)stack_.func_77973_b()).func_179223_d();
            Block block_2 = ((ItemBlock)stack_.func_77973_b()).func_179223_d();
            if (!block_1.field_149764_J.equals(block_2.field_149764_J)) {
               return false;
            }
         }

         if (!stack.func_82833_r().equals(stack_.func_82833_r())) {
            return false;
         } else {
            return stack.func_77952_i() == stack_.func_77952_i();
         }
      }
   }

   private static Map<Integer, ItemStack> inventory() {
      return inventory_slots(9, 35);
   }

   private static Map<Integer, ItemStack> hotbar() {
      return inventory_slots(36, 44);
   }

   private static Map<Integer, ItemStack> inventory_slots(int current, int last) {
      HashMap full_inventory;
      for(full_inventory = new HashMap(); current <= last; ++current) {
         full_inventory.put(current, mc.field_71439_g.field_71069_bz.func_75138_a().get(current));
      }

      return full_inventory;
   }

   public String getHudInfo() {
      return String.valueOf(this.stack_value.getValue());
   }
}
