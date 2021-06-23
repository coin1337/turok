package com.oldturok.turok.module.modules.render;

import com.oldturok.turok.module.Module;
import com.oldturok.turok.setting.Setting;
import com.oldturok.turok.setting.Settings;

@Module.Info(
   name = "CustomFOV",
   description = "Custom FOV?",
   category = Module.Category.TUROK_RENDER
)
public class CustomFov extends Module {
   private Setting<Float> custom_fov = this.register(Settings.floatBuilder("FOV").withMinimum(30.0F).withValue((Number)110.0F).withMaximum(180.0F));
   public static float old_fov;

   public void onDisable() {
      mc.field_71474_y.field_74334_X = old_fov;
   }

   public void onUpdate() {
      mc.field_71474_y.field_74334_X = (Float)this.custom_fov.getValue();
   }

   public String getHudInfo() {
      return String.valueOf(this.custom_fov.getValue());
   }

   static {
      old_fov = mc.field_71474_y.field_74334_X;
   }
}
