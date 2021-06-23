package com.oldturok.turok.module.modules.render;

import com.oldturok.turok.module.Module;
import com.oldturok.turok.setting.Setting;
import com.oldturok.turok.setting.Settings;
import java.util.Stack;
import java.util.function.Function;

@Module.Info(
   name = "Brightness",
   description = "Makes everything brighter!",
   category = Module.Category.TUROK_RENDER
)
public class Brightness extends Module {
   private Setting<Boolean> transition = this.register(Settings.b("Transition", true));
   private Setting<Float> seconds = this.register(Settings.floatBuilder("Seconds").withMinimum(0.0F).withMaximum(10.0F).withValue((Number)1.0F).withVisibility((o) -> {
      return (Boolean)this.transition.getValue();
   }).build());
   private Setting<Brightness.Transition> mode;
   private Stack<Float> transitionStack;
   private static float currentBrightness = 0.0F;
   private static boolean inTransition = false;

   public Brightness() {
      this.mode = this.register(Settings.enumBuilder(Brightness.Transition.class).withName("Mode").withValue(Brightness.Transition.SINE).withVisibility((o) -> {
         return (Boolean)this.transition.getValue();
      }).build());
      this.transitionStack = new Stack();
   }

   private void addTransition(boolean isUpwards) {
      if ((Boolean)this.transition.getValue()) {
         int length = (int)((Float)this.seconds.getValue() * 20.0F);
         float[] values;
         switch((Brightness.Transition)this.mode.getValue()) {
         case LINEAR:
            values = this.linear(length, isUpwards);
            break;
         case SINE:
            values = this.sine(length, isUpwards);
            break;
         default:
            values = new float[]{0.0F};
         }

         float[] var4 = values;
         int var5 = values.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            float v = var4[var6];
            this.transitionStack.add(v);
         }

         inTransition = true;
      }

   }

   protected void onEnable() {
      super.onEnable();
      this.addTransition(true);
   }

   protected void onDisable() {
      this.setAlwaysListening(true);
      super.onDisable();
      this.addTransition(false);
   }

   public void onUpdate() {
      if (inTransition) {
         if (this.transitionStack.isEmpty()) {
            inTransition = false;
            this.setAlwaysListening(false);
            currentBrightness = this.isEnabled() ? 1.0F : 0.0F;
         } else {
            currentBrightness = (Float)this.transitionStack.pop();
         }
      }

   }

   private float[] createTransition(int length, boolean upwards, Function<Float, Float> function) {
      float[] transition = new float[length];

      for(int i = 0; i < length; ++i) {
         float v = (Float)function.apply((float)i / (float)length);
         if (upwards) {
            v = 1.0F - v;
         }

         transition[i] = v;
      }

      return transition;
   }

   private float[] linear(int length, boolean polarity) {
      return this.createTransition(length, polarity, (d) -> {
         return d;
      });
   }

   private float sine(float x) {
      return ((float)Math.sin(3.141592653589793D * (double)x - 1.5707963267948966D) + 1.0F) / 2.0F;
   }

   private float[] sine(int length, boolean polarity) {
      return this.createTransition(length, polarity, this::sine);
   }

   public static float getCurrentBrightness() {
      return currentBrightness;
   }

   public static boolean isInTransition() {
      return inTransition;
   }

   public static boolean shouldBeActive() {
      return isInTransition() || currentBrightness == 1.0F;
   }

   public static enum Transition {
      LINEAR,
      SINE;
   }
}
