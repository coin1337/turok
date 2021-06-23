package com.oldturok.turok.module.modules.render;

import com.oldturok.turok.event.events.RenderEvent;
import com.oldturok.turok.module.Module;
import com.oldturok.turok.setting.Setting;
import com.oldturok.turok.setting.Settings;
import com.oldturok.turok.util.TurokTessellator;
import java.awt.Color;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;

@Module.Info(
   name = "BlockHighlight",
   description = "For you see better the block on split.",
   category = Module.Category.TUROK_RENDER
)
public class BlockHighlight extends Module {
   private Setting<Integer> color_r = this.register(Settings.integerBuilder("Color Red").withMinimum(0).withMaximum(255).withValue((int)200));
   private Setting<Integer> color_g = this.register(Settings.integerBuilder("Color Green").withMinimum(0).withMaximum(255).withValue((int)200));
   private Setting<Integer> color_b = this.register(Settings.integerBuilder("Color Blue").withMinimum(0).withMaximum(255).withValue((int)200));
   private Setting<Integer> color_a = this.register(Settings.integerBuilder("Alpha Color").withMinimum(0).withMaximum(255).withValue((int)70));
   private Setting<Boolean> rgb = this.register(Settings.b("RGB", true));
   private Setting<BlockHighlight.TypeDraw> type;
   public RayTraceResult result;
   int r;
   int g;
   int b;
   int prepare;
   int mask;
   boolean type_;

   public BlockHighlight() {
      this.type = this.register(Settings.e("Type Draw", BlockHighlight.TypeDraw.BOX));
   }

   public void onUpdate() {
      float[] tick_color = new float[]{(float)(System.currentTimeMillis() % 11520L) / 11520.0F};
      int color_rgb = Color.HSBtoRGB(tick_color[0], 1.0F, 1.0F);
      tick_color[0] += 0.1F;
      if ((Boolean)this.rgb.getValue()) {
         this.r = color_rgb >> 16 & 255;
         this.g = color_rgb >> 8 & 255;
         this.b = color_rgb & 255;
      } else {
         this.r = (Integer)this.color_r.getValue();
         this.g = (Integer)this.color_g.getValue();
         this.b = (Integer)this.color_b.getValue();
      }

      if (mc.field_71441_e != null && mc.field_71439_g != null) {
         this.result = mc.field_71476_x;
         if (this.result != null) {
            switch((BlockHighlight.TypeDraw)this.type.getValue()) {
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

         }
      }
   }

   public void onWorldRender(RenderEvent event) {
      if (mc.field_71441_e != null && mc.field_71439_g != null) {
         if (this.result != null) {
            if (this.result.field_72313_a == Type.BLOCK) {
               BlockPos pos = this.result.func_178782_a();
               IBlockState block_state = mc.field_71441_e.func_180495_p(pos);
               TurokTessellator.prepare(this.prepare);
               if (this.type_) {
                  TurokTessellator.drawLines(pos, this.r, this.g, this.b, (Integer)this.color_a.getValue(), this.mask);
               } else {
                  TurokTessellator.drawBox(pos, this.r, this.g, this.b, (Integer)this.color_a.getValue(), this.mask);
               }

               TurokTessellator.release();
            }

         }
      }
   }

   public static enum TypeDraw {
      OUTLINE,
      BOX;
   }
}
