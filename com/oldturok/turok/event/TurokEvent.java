package com.oldturok.turok.event;

import com.oldturok.turok.util.Wrapper;
import me.zero.alpine.type.Cancellable;

public class TurokEvent extends Cancellable {
   private TurokEvent.Era era;
   private final float partialTicks;

   public TurokEvent() {
      this.era = TurokEvent.Era.PRE;
      this.partialTicks = Wrapper.getMinecraft().func_184121_ak();
   }

   public TurokEvent.Era getEra() {
      return this.era;
   }

   public float getPartialTicks() {
      return this.partialTicks;
   }

   public static enum Era {
      PRE,
      PERI,
      POST;
   }
}
