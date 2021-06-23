package com.oldturok.turok.module.modules.render;

import com.oldturok.turok.event.events.RenderEvent;
import com.oldturok.turok.module.Module;
import com.oldturok.turok.setting.Setting;
import com.oldturok.turok.setting.Settings;
import com.oldturok.turok.util.EntityUtil;
import com.oldturok.turok.util.TurokTessellator;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.RayTraceResult.Type;
import org.lwjgl.opengl.GL11;

@Module.Info(
   name = "EyeTrack",
   description = "A eye track or eye finder.",
   category = Module.Category.TUROK_RENDER
)
public class EyeTrack extends Module {
   private Setting<Integer> color_r = this.register(Settings.integerBuilder("Color Red").withMinimum(0).withMaximum(255).withValue((int)0));
   private Setting<Integer> color_g = this.register(Settings.integerBuilder("Color Green").withMinimum(0).withMaximum(255).withValue((int)0));
   private Setting<Integer> color_b = this.register(Settings.integerBuilder("Color Blue").withMinimum(0).withMaximum(255).withValue((int)255));
   private Setting<Boolean> rgb = this.register(Settings.b("RGB", false));
   int r;
   int g;
   int b;

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

   }

   public void onWorldRender(RenderEvent event) {
      mc.field_71441_e.field_72996_f.stream().filter(EntityUtil::isLiving).filter((entity) -> {
         return mc.field_71439_g != entity;
      }).map((entity) -> {
         return (EntityLivingBase)entity;
      }).filter((entityLivingBase) -> {
         return !entityLivingBase.field_70128_L;
      }).filter((entity) -> {
         return entity instanceof EntityPlayer;
      }).forEach(this::drawLine);
   }

   private void drawLine(EntityLivingBase event_) {
      RayTraceResult result = event_.func_174822_a(6.0D, Minecraft.func_71410_x().func_184121_ak());
      if (result != null) {
         Vec3d eyes = event_.func_174824_e(Minecraft.func_71410_x().func_184121_ak());
         GlStateManager.func_179126_j();
         GlStateManager.func_179090_x();
         GlStateManager.func_179140_f();
         double posX = eyes.field_72450_a - mc.func_175598_ae().field_78725_b;
         double posY = eyes.field_72448_b - mc.func_175598_ae().field_78726_c;
         double posZ = eyes.field_72449_c - mc.func_175598_ae().field_78723_d;
         double posX_ = result.field_72307_f.field_72450_a - mc.func_175598_ae().field_78725_b;
         double posY_ = result.field_72307_f.field_72448_b - mc.func_175598_ae().field_78726_c;
         double posZ_ = result.field_72307_f.field_72449_c - mc.func_175598_ae().field_78723_d;
         GL11.glColor4f((float)(this.r / 255), (float)(this.g / 255), (float)(this.b / 255), 1.0F);
         GlStateManager.func_187441_d(1.5F);
         GL11.glBegin(1);
         GL11.glVertex3d(posX, posY, posZ);
         GL11.glVertex3d(posX_, posY_, posZ_);
         GL11.glVertex3d(posX_, posY_, posZ_);
         GL11.glVertex3d(posX_, posY_, posZ_);
         GL11.glEnd();
         if (result.field_72313_a == Type.BLOCK) {
            TurokTessellator.prepare(7);
            GL11.glEnable(2929);
            BlockPos block_pos = result.func_178782_a();
            float x = (float)block_pos.field_177962_a - 0.01F;
            float y = (float)block_pos.field_177960_b - 0.01F;
            float z = (float)block_pos.field_177961_c - 0.01F;
            TurokTessellator.drawBox(TurokTessellator.getBufferBuilder(), x, y, z, 1.01F, 1.01F, 1.01F, this.r, this.g, this.b, 70, 63);
            TurokTessellator.release();
         }

         GlStateManager.func_179098_w();
         GlStateManager.func_179145_e();
      }
   }
}
