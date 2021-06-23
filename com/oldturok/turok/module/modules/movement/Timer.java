package com.oldturok.turok.module.modules.movement;

import com.oldturok.turok.module.Module;
import com.oldturok.turok.setting.Setting;
import com.oldturok.turok.setting.Settings;

@Module.Info(
   name = "Timer",
   description = "For modify the timer.",
   category = Module.Category.TUROK_MOVEMENT
)
public class Timer extends Module {
   private Setting<Integer> ticks_speed_value = this.register(Settings.integerBuilder("Time Value").withMinimum(1).withValue((int)4).withMaximum(20));

   public void onDisable() {
      mc.field_71428_T.field_194149_e = 50.0F;
   }

   public void onUpdate() {
      mc.field_71428_T.field_194149_e = 50.0F / (float)(Integer)this.ticks_speed_value.getValue();
   }

   public String getHudInfo() {
      return String.valueOf(this.ticks_speed_value.getValue());
   }
}
