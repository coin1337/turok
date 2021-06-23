package com.oldturok.turok.util;

import java.util.Iterator;
import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemExpBottle;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSnowball;
import net.minecraft.item.ItemSplashPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class TrajectoryCalculator {
   public static TrajectoryCalculator.ThrowingType getThrowType(EntityLivingBase entity) {
      if (entity.func_184586_b(EnumHand.MAIN_HAND).func_190926_b()) {
         return TrajectoryCalculator.ThrowingType.NONE;
      } else {
         ItemStack itemStack = entity.func_184586_b(EnumHand.MAIN_HAND);
         Item item = itemStack.func_77973_b();
         if (item instanceof ItemPotion) {
            if (itemStack.func_77973_b() instanceof ItemSplashPotion) {
               return TrajectoryCalculator.ThrowingType.POTION;
            }
         } else {
            if (item instanceof ItemBow && entity.func_184587_cr()) {
               return TrajectoryCalculator.ThrowingType.BOW;
            }

            if (item instanceof ItemExpBottle) {
               return TrajectoryCalculator.ThrowingType.EXPERIENCE;
            }

            if (item instanceof ItemSnowball || item instanceof ItemEgg || item instanceof ItemEnderPearl) {
               return TrajectoryCalculator.ThrowingType.NORMAL;
            }
         }

         return TrajectoryCalculator.ThrowingType.NONE;
      }
   }

   public static double[] interpolate(Entity entity) {
      double posX = interpolate(entity.field_70165_t, entity.field_70142_S) - Wrapper.getMinecraft().field_175616_W.field_78725_b;
      double posY = interpolate(entity.field_70163_u, entity.field_70137_T) - Wrapper.getMinecraft().field_175616_W.field_78726_c;
      double posZ = interpolate(entity.field_70161_v, entity.field_70136_U) - Wrapper.getMinecraft().field_175616_W.field_78723_d;
      return new double[]{posX, posY, posZ};
   }

   public static double interpolate(double now, double then) {
      return then + (now - then) * (double)Wrapper.getMinecraft().func_184121_ak();
   }

   public static Vec3d mult(Vec3d factor, float multiplier) {
      return new Vec3d(factor.field_72450_a * (double)multiplier, factor.field_72448_b * (double)multiplier, factor.field_72449_c * (double)multiplier);
   }

   public static Vec3d div(Vec3d factor, float divisor) {
      return new Vec3d(factor.field_72450_a / (double)divisor, factor.field_72448_b / (double)divisor, factor.field_72449_c / (double)divisor);
   }

   public static final class FlightPath {
      private EntityLivingBase shooter;
      public Vec3d position;
      private Vec3d motion;
      private float yaw;
      private float pitch;
      private AxisAlignedBB boundingBox;
      private boolean collided;
      private RayTraceResult target;
      private TrajectoryCalculator.ThrowingType throwingType;

      public FlightPath(EntityLivingBase entityLivingBase, TrajectoryCalculator.ThrowingType throwingType) {
         this.shooter = entityLivingBase;
         this.throwingType = throwingType;
         double[] ipos = TrajectoryCalculator.interpolate(this.shooter);
         this.setLocationAndAngles(ipos[0] + Wrapper.getMinecraft().func_175598_ae().field_78725_b, ipos[1] + (double)this.shooter.func_70047_e() + Wrapper.getMinecraft().func_175598_ae().field_78726_c, ipos[2] + Wrapper.getMinecraft().func_175598_ae().field_78723_d, this.shooter.field_70177_z, this.shooter.field_70125_A);
         Vec3d startingOffset = new Vec3d((double)(MathHelper.func_76134_b(this.yaw / 180.0F * 3.1415927F) * 0.16F), 0.1D, (double)(MathHelper.func_76126_a(this.yaw / 180.0F * 3.1415927F) * 0.16F));
         this.position = this.position.func_178788_d(startingOffset);
         this.setPosition(this.position);
         this.motion = new Vec3d((double)(-MathHelper.func_76126_a(this.yaw / 180.0F * 3.1415927F) * MathHelper.func_76134_b(this.pitch / 180.0F * 3.1415927F)), (double)(-MathHelper.func_76126_a(this.pitch / 180.0F * 3.1415927F)), (double)(MathHelper.func_76134_b(this.yaw / 180.0F * 3.1415927F) * MathHelper.func_76134_b(this.pitch / 180.0F * 3.1415927F)));
         this.setThrowableHeading(this.motion, this.getInitialVelocity());
      }

      public void onUpdate() {
         Vec3d prediction = this.position.func_178787_e(this.motion);
         RayTraceResult blockCollision = this.shooter.func_130014_f_().func_147447_a(this.position, prediction, false, true, false);
         if (blockCollision != null) {
            prediction = blockCollision.field_72307_f;
         }

         this.onCollideWithEntity(prediction, blockCollision);
         if (this.target != null) {
            this.collided = true;
            this.setPosition(this.target.field_72307_f);
         } else if (this.position.field_72448_b <= 0.0D) {
            this.collided = true;
         } else {
            this.position = this.position.func_178787_e(this.motion);
            float motionModifier = 0.99F;
            if (this.shooter.func_130014_f_().func_72875_a(this.boundingBox, Material.field_151586_h)) {
               motionModifier = this.throwingType == TrajectoryCalculator.ThrowingType.BOW ? 0.6F : 0.8F;
            }

            this.motion = TrajectoryCalculator.mult(this.motion, motionModifier);
            this.motion = this.motion.func_178786_a(0.0D, (double)this.getGravityVelocity(), 0.0D);
            this.setPosition(this.position);
         }
      }

      private void onCollideWithEntity(Vec3d prediction, RayTraceResult blockCollision) {
         Entity collidingEntity = null;
         double currentDistance = 0.0D;
         List<Entity> collisionEntities = this.shooter.field_70170_p.func_72839_b(this.shooter, this.boundingBox.func_72321_a(this.motion.field_72450_a, this.motion.field_72448_b, this.motion.field_72449_c).func_72321_a(1.0D, 1.0D, 1.0D));
         Iterator var7 = collisionEntities.iterator();

         while(true) {
            Entity entity;
            double distanceTo;
            do {
               RayTraceResult objectPosition;
               do {
                  do {
                     if (!var7.hasNext()) {
                        if (collidingEntity != null) {
                           this.target = new RayTraceResult(collidingEntity);
                        } else {
                           this.target = blockCollision;
                        }

                        return;
                     }

                     entity = (Entity)var7.next();
                  } while(!entity.func_70067_L() && entity != this.shooter);

                  float collisionSize = entity.func_70111_Y();
                  AxisAlignedBB expandedBox = entity.func_174813_aQ().func_72321_a((double)collisionSize, (double)collisionSize, (double)collisionSize);
                  objectPosition = expandedBox.func_72327_a(this.position, prediction);
               } while(objectPosition == null);

               distanceTo = this.position.func_72438_d(objectPosition.field_72307_f);
            } while(!(distanceTo < currentDistance) && currentDistance != 0.0D);

            collidingEntity = entity;
            currentDistance = distanceTo;
         }
      }

      private float getInitialVelocity() {
         Item item = this.shooter.func_184586_b(EnumHand.MAIN_HAND).func_77973_b();
         switch(this.throwingType) {
         case BOW:
            ItemBow bow = (ItemBow)item;
            int useDuration = bow.func_77626_a(this.shooter.func_184586_b(EnumHand.MAIN_HAND)) - this.shooter.func_184605_cv();
            float velocity = (float)useDuration / 20.0F;
            velocity = (velocity * velocity + velocity * 2.0F) / 3.0F;
            if (velocity > 1.0F) {
               velocity = 1.0F;
            }

            return velocity * 2.0F * 1.5F;
         case POTION:
            return 0.5F;
         case EXPERIENCE:
            return 0.7F;
         case NORMAL:
            return 1.5F;
         default:
            return 1.5F;
         }
      }

      private float getGravityVelocity() {
         switch(this.throwingType) {
         case BOW:
         case POTION:
            return 0.05F;
         case EXPERIENCE:
            return 0.07F;
         case NORMAL:
            return 0.03F;
         default:
            return 0.03F;
         }
      }

      private void setLocationAndAngles(double x, double y, double z, float yaw, float pitch) {
         this.position = new Vec3d(x, y, z);
         this.yaw = yaw;
         this.pitch = pitch;
      }

      private void setPosition(Vec3d position) {
         this.position = new Vec3d(position.field_72450_a, position.field_72448_b, position.field_72449_c);
         double entitySize = (this.throwingType == TrajectoryCalculator.ThrowingType.BOW ? 0.5D : 0.25D) / 2.0D;
         this.boundingBox = new AxisAlignedBB(position.field_72450_a - entitySize, position.field_72448_b - entitySize, position.field_72449_c - entitySize, position.field_72450_a + entitySize, position.field_72448_b + entitySize, position.field_72449_c + entitySize);
      }

      private void setThrowableHeading(Vec3d motion, float velocity) {
         this.motion = TrajectoryCalculator.div(motion, (float)motion.func_72433_c());
         this.motion = TrajectoryCalculator.mult(this.motion, velocity);
      }

      public boolean isCollided() {
         return this.collided;
      }

      public RayTraceResult getCollidingTarget() {
         return this.target;
      }
   }

   public static enum ThrowingType {
      NONE,
      BOW,
      EXPERIENCE,
      POTION,
      NORMAL;
   }
}
