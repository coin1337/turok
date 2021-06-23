package com.turok_mixins;

import com.oldturok.turok.TurokMod;
import com.oldturok.turok.event.events.EntityEvent;
import com.oldturok.turok.module.modules.movement.SafeWalk;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({Entity.class})
public class TurokEntity {
   @Redirect(
      method = {"applyEntityCollision"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/entity/Entity;addVelocity(DDD)V"
)
   )
   public void addVelocity(Entity entity, double x, double y, double z) {
      EntityEvent.EntityCollision entityCollisionEvent = new EntityEvent.EntityCollision(entity, x, y, z);
      TurokMod.EVENT_BUS.post(entityCollisionEvent);
      if (!entityCollisionEvent.isCancelled()) {
         entity.field_70159_w += x;
         entity.field_70181_x += y;
         entity.field_70179_y += z;
         entity.field_70160_al = true;
      }
   }

   @Redirect(
      method = {"move"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/entity/Entity;isSneaking()Z"
)
   )
   public boolean isSneaking(Entity entity) {
      return SafeWalk.shouldSafewalk() || entity.func_70093_af();
   }
}
