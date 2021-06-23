package com.oldturok.turok.module.modules.render;

import com.oldturok.turok.gui.turok.TurokGUI;
import com.oldturok.turok.gui.turok.widgets.WidgetActiveModules;
import com.oldturok.turok.gui.turok.widgets.WidgetModuleFrame;
import com.oldturok.turok.module.Module;
import com.oldturok.turok.setting.Setting;
import com.oldturok.turok.setting.Settings;
import java.awt.Color;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;

@Module.Info(
   name = "TurokHUD",
   description = "Turok HUD, ask for Rina.",
   category = Module.Category.TUROK_RENDER
)
public class TurokHUD extends Module {
   private static RenderItem itemRender = Minecraft.func_71410_x().func_175599_af();
   private Setting<Boolean> effect_pinnable = this.register(Settings.b("Effect", true));
   private Setting<Boolean> wattermark_hud = this.register(Settings.b("Wattermark", true));
   private Setting<Boolean> users_hud = this.register(Settings.b("Users", true));
   private Setting<Boolean> coords_hud = this.register(Settings.b("Coords", true));
   private Setting<Boolean> totem_hud = this.register(Settings.b("Totems", true));
   private Setting<Boolean> gapple_hud = this.register(Settings.b("Gapples", true));
   private Setting<Boolean> crystal_hud = this.register(Settings.b("Crystals", true));
   private Setting<Boolean> exp_hud = this.register(Settings.b("BottlesXp", true));
   private Setting<Boolean> armor_hud = this.register(Settings.b("ArmorHUD", true));
   public Setting<Boolean> array_rgb = this.register(Settings.b("ArrayRGB", true));
   public Setting<Integer> array_r = this.register(Settings.integerBuilder("ArrayRed").withMinimum(1).withMaximum(255).withValue((int)255));
   public Setting<Integer> array_g = this.register(Settings.integerBuilder("ArrayGreen").withMinimum(1).withMaximum(255).withValue((int)255));
   public Setting<Integer> array_b = this.register(Settings.integerBuilder("ArrayBlue").withMinimum(1).withMaximum(255).withValue((int)255));
   boolean wattermark_enable;
   boolean users_enable;
   boolean coords_enable;
   boolean totem_enable;
   boolean gapple_enable;
   boolean crystal_enable;
   boolean exp_enable;
   int wattermark_tick;
   int users_tick;
   int coords_tick;
   int totem_tick;
   int gapple_tick;
   int crystal_tick;
   int exp_tick;

   public void onDisable() {
      this.enable();
   }

   public void onUpdate() {
      float[] tick_color = new float[]{(float)(System.currentTimeMillis() % 11520L) / 11520.0F};
      int color_rgb = Color.HSBtoRGB(tick_color[0], 1.0F, 1.0F);
      tick_color[0] += 0.1F;
      if ((Boolean)this.array_rgb.getValue()) {
         WidgetActiveModules.r = color_rgb >> 16 & 255;
         WidgetActiveModules.g = color_rgb >> 8 & 255;
         WidgetActiveModules.b = color_rgb & 255;
      } else {
         WidgetActiveModules.r = (Integer)this.array_r.getValue();
         WidgetActiveModules.g = (Integer)this.array_g.getValue();
         WidgetActiveModules.b = (Integer)this.array_b.getValue();
      }

      if ((Boolean)this.effect_pinnable.getValue()) {
         WidgetModuleFrame.effect = true;
      } else {
         WidgetModuleFrame.effect = false;
      }

      if (this.wattermark_enable) {
         ++this.wattermark_tick;
         if (this.wattermark_tick >= 5) {
            this.wattermark_tick = 6;
         } else {
            TurokGUI.frame_wattermark.setX(0);
            TurokGUI.frame_wattermark.setY(10);
         }
      } else {
         this.wattermark_tick = 0;
         TurokGUI.frame_wattermark.setX(12000);
         TurokGUI.frame_wattermark.setY(12000);
      }

      if (this.users_enable) {
         ++this.users_tick;
         if (this.users_tick >= 5) {
            this.users_tick = 6;
         } else {
            TurokGUI.frame_users.setX(0);
            TurokGUI.frame_users.setY(50);
         }
      } else {
         this.users_tick = 0;
         TurokGUI.frame_users.setX(12000);
         TurokGUI.frame_users.setY(12000);
      }

      if (this.coords_enable) {
         ++this.coords_tick;
         if (this.coords_tick >= 5) {
            this.coords_tick = 6;
         } else {
            TurokGUI.frame_coords.setX(0);
            TurokGUI.frame_coords.setY(100);
         }
      } else {
         this.coords_tick = 0;
         TurokGUI.frame_coords.setX(12000);
         TurokGUI.frame_coords.setY(12000);
      }

      if (this.totem_enable) {
         ++this.totem_tick;
         if (this.totem_tick >= 5) {
            this.totem_tick = 6;
         } else {
            TurokGUI.frame_counts_totem.setX(0);
            TurokGUI.frame_counts_totem.setY(200);
         }
      } else {
         this.totem_tick = 0;
         TurokGUI.frame_counts_totem.setX(12000);
         TurokGUI.frame_counts_totem.setY(12000);
      }

      if (this.gapple_enable) {
         ++this.gapple_tick;
         if (this.gapple_tick >= 5) {
            this.gapple_tick = 6;
         } else {
            TurokGUI.frame_counts_gapple.setX(0);
            TurokGUI.frame_counts_gapple.setY(300);
         }
      } else {
         this.gapple_tick = 0;
         TurokGUI.frame_counts_gapple.setX(12000);
         TurokGUI.frame_counts_gapple.setY(12000);
      }

      if (this.crystal_enable) {
         ++this.crystal_tick;
         if (this.crystal_tick >= 5) {
            this.crystal_tick = 6;
         } else {
            TurokGUI.frame_counts_crystal.setX(0);
            TurokGUI.frame_counts_crystal.setY(400);
         }
      } else {
         this.crystal_tick = 0;
         TurokGUI.frame_counts_crystal.setX(12000);
         TurokGUI.frame_counts_crystal.setY(12000);
      }

      if (this.exp_enable) {
         ++this.exp_tick;
         if (this.exp_tick >= 5) {
            this.exp_tick = 6;
         } else {
            TurokGUI.frame_counts_exp.setX(0);
            TurokGUI.frame_counts_exp.setY(500);
         }
      } else {
         this.exp_tick = 0;
         TurokGUI.frame_counts_exp.setX(12000);
         TurokGUI.frame_counts_exp.setY(12000);
      }

      this.compare();
      this.enable();
   }

   public void compare() {
      if ((Boolean)this.wattermark_hud.getValue()) {
         this.wattermark_enable = true;
      } else {
         this.wattermark_enable = false;
      }

      if ((Boolean)this.users_hud.getValue()) {
         this.users_enable = true;
      } else {
         this.users_enable = false;
      }

      if ((Boolean)this.coords_hud.getValue()) {
         this.coords_enable = true;
      } else {
         this.coords_enable = false;
      }

      if ((Boolean)this.totem_hud.getValue()) {
         this.totem_enable = true;
      } else {
         this.totem_enable = false;
      }

      if ((Boolean)this.gapple_hud.getValue()) {
         this.gapple_enable = true;
      } else {
         this.gapple_enable = false;
      }

      if ((Boolean)this.crystal_hud.getValue()) {
         this.crystal_enable = true;
      } else {
         this.crystal_enable = false;
      }

      if ((Boolean)this.exp_hud.getValue()) {
         this.exp_enable = true;
      } else {
         this.exp_enable = false;
      }

   }

   public void onRender() {
      if ((Boolean)this.armor_hud.getValue()) {
         GlStateManager.func_179098_w();
         ScaledResolution resolution = new ScaledResolution(mc);
         int i = resolution.func_78326_a() / 2;
         int iteration = 0;
         int y = resolution.func_78328_b() - 55 - (mc.field_71439_g.func_70090_H() ? 10 : 0);
         Iterator var5 = mc.field_71439_g.field_71071_by.field_70460_b.iterator();

         while(var5.hasNext()) {
            ItemStack is = (ItemStack)var5.next();
            ++iteration;
            if (!is.func_190926_b()) {
               int x = i - 90 + (9 - iteration) * 20 + 2;
               GlStateManager.func_179126_j();
               itemRender.field_77023_b = 200.0F;
               itemRender.func_180450_b(is, x, y);
               itemRender.func_180453_a(mc.field_71466_p, is, x, y, "");
               itemRender.field_77023_b = 0.0F;
               GlStateManager.func_179098_w();
               GlStateManager.func_179140_f();
               GlStateManager.func_179097_i();
               String s = is.func_190916_E() > 1 ? is.func_190916_E() + "" : "";
               mc.field_71466_p.func_175063_a(s, (float)(x + 19 - 2 - mc.field_71466_p.func_78256_a(s)), (float)(y + 9), 16777215);
            }
         }

         GlStateManager.func_179126_j();
         GlStateManager.func_179140_f();
      }

   }
}
