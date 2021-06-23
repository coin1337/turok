package com.oldturok.turok.module.modules.movement;

import com.oldturok.turok.module.Module;
import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.util.MovementInput;
import net.minecraftforge.client.event.InputUpdateEvent;

@Module.Info(
   name = "WalkEat",
   description = "Walk eating.",
   category = Module.Category.TUROK_MOVEMENT
)
public class WalkEat extends Module {
   @EventHandler
   private Listener<InputUpdateEvent> eventListener = new Listener((event) -> {
      if (mc.field_71439_g.func_184587_cr() && !mc.field_71439_g.func_184218_aH()) {
         MovementInput var10000 = event.getMovementInput();
         var10000.field_78902_a *= 5.0F;
         var10000 = event.getMovementInput();
         var10000.field_192832_b *= 5.0F;
      }

   }, new Predicate[0]);
}
