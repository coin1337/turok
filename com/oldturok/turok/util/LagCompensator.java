package com.oldturok.turok.util;

import com.oldturok.turok.TurokMod;
import com.oldturok.turok.event.events.PacketEvent;
import java.util.Arrays;
import java.util.EventListener;
import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraft.util.math.MathHelper;

public class LagCompensator implements EventListener {
   public static LagCompensator INSTANCE;
   private final float[] tickRates = new float[20];
   private int nextIndex = 0;
   private long timeLastTimeUpdate;
   @EventHandler
   Listener<PacketEvent.Receive> packetEventListener = new Listener((event) -> {
      if (event.getPacket() instanceof SPacketTimeUpdate) {
         INSTANCE.onTimeUpdate();
      }

   }, new Predicate[0]);

   public LagCompensator() {
      TurokMod.EVENT_BUS.subscribe((Object)this);
      this.reset();
   }

   public void reset() {
      this.nextIndex = 0;
      this.timeLastTimeUpdate = -1L;
      Arrays.fill(this.tickRates, 0.0F);
   }

   public float getTickRate() {
      float numTicks = 0.0F;
      float sumTickRates = 0.0F;
      float[] var3 = this.tickRates;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         float tickRate = var3[var5];
         if (tickRate > 0.0F) {
            sumTickRates += tickRate;
            ++numTicks;
         }
      }

      return MathHelper.func_76131_a(sumTickRates / numTicks, 0.0F, 20.0F);
   }

   public void onTimeUpdate() {
      if (this.timeLastTimeUpdate != -1L) {
         float timeElapsed = (float)(System.currentTimeMillis() - this.timeLastTimeUpdate) / 1000.0F;
         this.tickRates[this.nextIndex % this.tickRates.length] = MathHelper.func_76131_a(20.0F / timeElapsed, 0.0F, 20.0F);
         ++this.nextIndex;
      }

      this.timeLastTimeUpdate = System.currentTimeMillis();
   }
}
