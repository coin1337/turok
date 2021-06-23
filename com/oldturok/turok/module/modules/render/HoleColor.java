package com.oldturok.turok.module.modules.render;

import com.oldturok.turok.event.events.RenderEvent;
import com.oldturok.turok.module.Module;
import com.oldturok.turok.module.ModuleManager;
import com.oldturok.turok.module.modules.combat.TurokCrystalAura;
import com.oldturok.turok.setting.Setting;
import com.oldturok.turok.setting.Settings;
import com.oldturok.turok.util.TurokTessellator;
import java.awt.Color;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

@Module.Info(
   name = "HoleColor",
   description = "For draw block into holes.",
   category = Module.Category.TUROK_RENDER
)
public class HoleColor extends Module {
   private ConcurrentHashMap<BlockPos, Boolean> safe_holes;
   private final BlockPos[] barrier_ = new BlockPos[]{new BlockPos(0, -1, 0), new BlockPos(0, 0, -1), new BlockPos(1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(-1, 0, 0)};
   private Setting<Double> range = this.register(Settings.d("Range", 10.0D));
   private Setting<Integer> color_r = this.register(Settings.integerBuilder("Color Red").withMinimum(0).withMaximum(255).withValue((int)0));
   private Setting<Integer> color_g = this.register(Settings.integerBuilder("Color Green").withMinimum(0).withMaximum(255).withValue((int)0));
   private Setting<Integer> color_b = this.register(Settings.integerBuilder("Color Blue").withMinimum(0).withMaximum(255).withValue((int)0));
   private Setting<Integer> a = this.register(Settings.integerBuilder("Alpha Color").withMinimum(0).withMaximum(255).withValue((int)255));
   private Setting<Boolean> rgb = this.register(Settings.b("RGB", true));
   private Setting<HoleColor.TypeHole> type;
   public int prepare;
   public int mask;
   public boolean type_;

   public HoleColor() {
      this.type = this.register(Settings.e("Hole Type", HoleColor.TypeHole.OUTLINE));
   }

   public void onUpdate() {
      switch((HoleColor.TypeHole)this.type.getValue()) {
      case OUTLINE:
         this.prepare = 1;
         this.mask = 63;
         this.type_ = true;
         break;
      case BOX:
         this.prepare = 7;
         this.mask = 63;
         this.type_ = false;
      }

      if (this.safe_holes == null) {
         this.safe_holes = new ConcurrentHashMap();
      } else {
         this.safe_holes.clear();
      }

      int range_ = (int)Math.ceil((Double)this.range.getValue());
      TurokCrystalAura crystal_function = (TurokCrystalAura)ModuleManager.getModuleByName("TurokCrystalAura");
      List<BlockPos> block_pos = crystal_function.get_sphere(TurokCrystalAura.player_pos(), (float)range_, range_, false, true, 0);
      Iterator var4 = block_pos.iterator();

      while(true) {
         BlockPos pos;
         do {
            do {
               do {
                  if (!var4.hasNext()) {
                     return;
                  }

                  pos = (BlockPos)var4.next();
               } while(!mc.field_71441_e.func_180495_p(pos).func_177230_c().equals(Blocks.field_150350_a));
            } while(!mc.field_71441_e.func_180495_p(pos.func_177982_a(0, 1, 0)).func_177230_c().equals(Blocks.field_150350_a));
         } while(!mc.field_71441_e.func_180495_p(pos.func_177982_a(0, 2, 0)).func_177230_c().equals(Blocks.field_150350_a));

         boolean safe = true;
         boolean bedrock = true;
         BlockPos[] var8 = this.barrier_;
         int var9 = var8.length;

         for(int var10 = 0; var10 < var9; ++var10) {
            BlockPos offset = var8[var10];
            Block block = mc.field_71441_e.func_180495_p(pos.func_177971_a(offset)).func_177230_c();
            if (block != Blocks.field_150357_h) {
               bedrock = false;
            }

            if (block != Blocks.field_150357_h && block != Blocks.field_150343_Z && block != Blocks.field_150477_bB && block != Blocks.field_150467_bQ) {
               safe = false;
               break;
            }
         }

         if (safe) {
            this.safe_holes.put(pos, bedrock);
         }
      }
   }

   public void onWorldRender(RenderEvent event) {
      if (mc.field_71439_g != null && this.safe_holes != null) {
         if (!this.safe_holes.isEmpty()) {
            TurokTessellator.prepare(this.prepare);
            this.safe_holes.forEach((block_pos, bedrock) -> {
               int rx = false;
               int gx = false;
               int bx = false;
               int r;
               int g;
               int b;
               if ((Boolean)this.rgb.getValue()) {
                  float[] tick_color = new float[]{(float)(System.currentTimeMillis() % 11520L) / 11520.0F};
                  int color_rgb = Color.HSBtoRGB(tick_color[0], 1.0F, 1.0F);
                  tick_color[0] += 0.1F;
                  r = color_rgb >> 16 & 255;
                  g = color_rgb >> 8 & 255;
                  b = color_rgb & 255;
               } else {
                  r = (Integer)this.color_r.getValue();
                  g = (Integer)this.color_g.getValue();
                  b = (Integer)this.color_b.getValue();
               }

               this.draw(block_pos, r, g, b);
               this.draw(block_pos, r, g, b);
               this.draw(block_pos, r, g, b);
            });
            TurokTessellator.release();
         }
      }
   }

   private void draw(BlockPos block_pos, int r, int g, int b) {
      Color color = new Color(r, g, b, (Integer)this.a.getValue());
      if (this.type_) {
         TurokTessellator.drawLines(block_pos, color.getRGB(), this.mask);
      } else {
         TurokTessellator.drawBox(block_pos, color.getRGB(), this.mask);
      }

   }

   public static enum TypeHole {
      OUTLINE,
      BOX;
   }
}
