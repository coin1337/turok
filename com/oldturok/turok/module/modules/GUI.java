package com.oldturok.turok.module.modules;

import com.oldturok.turok.gui.turok.DisplayGuiScreen;
import com.oldturok.turok.gui.turok.RenderHelper;
import com.oldturok.turok.module.Module;
import com.oldturok.turok.util.TurokGL;

@Module.Info(
   name = "GUI",
   category = Module.Category.TUROK_HIDDEN
)
public class GUI extends Module {
   public GUI() {
      this.getBind().set_key(25);
   }

   protected void onEnable() {
      if (!(mc.field_71462_r instanceof DisplayGuiScreen)) {
         TurokGL.refresh_color(190.0F, 190.0F, 190.0F, 50.0F);
         RenderHelper.drawFilledRectangle(0.0F, 0.0F, (float)mc.field_71440_d, (float)mc.field_71443_c);
         mc.func_147108_a(new DisplayGuiScreen(mc.field_71462_r));
      }

      this.disable();
   }
}
