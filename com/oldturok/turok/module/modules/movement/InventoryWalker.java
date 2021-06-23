package com.oldturok.turok.module.modules.movement;

import com.oldturok.turok.event.events.GuiScreenEvent;
import com.oldturok.turok.module.Module;
import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.input.Keyboard;

@Module.Info(
   name = "InventoryWalker",
   description = "For walk in any gui.",
   category = Module.Category.TUROK_MOVEMENT
)
public class InventoryWalker extends Module {
   private static KeyBinding[] KEYS;
   int JUMP;
   @EventHandler
   public Listener<GuiScreenEvent.Displayed> listener;

   public InventoryWalker() {
      this.JUMP = mc.field_71474_y.field_74314_A.func_151463_i();
      this.listener = new Listener((event) -> {
         if (!(mc.field_71462_r instanceof GuiChat) && mc.field_71462_r != null) {
            this.walk();
         }
      }, new Predicate[0]);
   }

   public void onUpdate() {
      if (!(mc.field_71462_r instanceof GuiChat) && mc.field_71462_r != null) {
         EntityPlayerSP var10000 = mc.field_71439_g;
         var10000.field_70177_z += Keyboard.isKeyDown(205) ? 4.0F : (Keyboard.isKeyDown(203) ? -4.0F : 0.0F);
         mc.field_71439_g.field_70125_A = (float)((double)mc.field_71439_g.field_70125_A + (double)(Keyboard.isKeyDown(208) ? 4 : (Keyboard.isKeyDown(200) ? -4 : 0)) * 0.75D);
         mc.field_71439_g.field_70125_A = MathHelper.func_76131_a(mc.field_71439_g.field_70125_A, -90.0F, 90.0F);
         if (Keyboard.isKeyDown(this.JUMP)) {
            if (!mc.field_71439_g.func_180799_ab() && !mc.field_71439_g.func_70090_H()) {
               if (mc.field_71439_g.field_70122_E) {
                  mc.field_71439_g.func_70664_aZ();
               }
            } else {
               var10000 = mc.field_71439_g;
               var10000.field_70181_x += 0.3799999952316284D;
            }
         }

         this.walk();
      }
   }

   public void walk() {
      KeyBinding[] keys = KEYS;
      int keys_n = keys.length;

      for(int keys_n_2 = 0; keys_n_2 < keys_n; ++keys_n_2) {
         KeyBinding key_binding = keys[keys_n_2];
         if (Keyboard.isKeyDown(key_binding.func_151463_i())) {
            if (key_binding.getKeyConflictContext() != KeyConflictContext.UNIVERSAL) {
               key_binding.setKeyConflictContext(KeyConflictContext.UNIVERSAL);
            }

            KeyBinding.func_74510_a(key_binding.func_151463_i(), true);
         } else {
            KeyBinding.func_74510_a(key_binding.func_151463_i(), false);
         }
      }

   }

   static {
      KEYS = new KeyBinding[]{mc.field_71474_y.field_74351_w, mc.field_71474_y.field_74366_z, mc.field_71474_y.field_74368_y, mc.field_71474_y.field_74370_x, mc.field_71474_y.field_74314_A, mc.field_71474_y.field_151444_V};
   }
}
