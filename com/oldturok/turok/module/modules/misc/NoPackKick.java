package com.oldturok.turok.module.modules.misc;

import com.oldturok.turok.module.Module;

@Module.Info(
   name = "NoPackKick",
   category = Module.Category.TUROK_HIDDEN
)
public class NoPackKick {
   private static NoPackKick INSTANCE;

   public NoPackKick() {
      INSTANCE = this;
   }

   public static boolean isEnabled() {
      NoPackKick var10000 = INSTANCE;
      return isEnabled();
   }
}
