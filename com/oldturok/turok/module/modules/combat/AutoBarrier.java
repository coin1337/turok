package com.oldturok.turok.module.modules.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.oldturok.turok.TurokMessage;
import com.oldturok.turok.module.Module;
import com.oldturok.turok.module.ModuleManager;
import com.oldturok.turok.setting.Setting;
import com.oldturok.turok.setting.Settings;
import com.oldturok.turok.util.BlockInteractionHelper;
import java.util.Iterator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketEntityAction.Action;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

@Module.Info(
   name = "AutoBarrier",
   description = "Auto barrier using obsidians.",
   category = Module.Category.TUROK_COMBAT
)
public class AutoBarrier extends Module {
   private Setting<AutoBarrier.Mode> mode;
   private Setting<Boolean> triggerable;
   private Setting<Integer> timeoutTicks;
   private Setting<Integer> blocksPerTick;
   private Setting<Integer> tickDelay;
   private Setting<Boolean> rotate;
   private Setting<Boolean> infoMessage;
   private int offsetStep;
   private int delayStep;
   private int playerHotbarSlot;
   private int lastHotbarSlot;
   private boolean isSneaking;
   private int totalTicksRunning;
   private boolean firstRun;
   private boolean missingObiDisable;

   public AutoBarrier() {
      this.mode = this.register(Settings.e("Mode", AutoBarrier.Mode.FULL));
      this.triggerable = this.register(Settings.b("Triggerable", true));
      this.timeoutTicks = this.register(Settings.integerBuilder("TimeoutTicks").withMinimum(1).withValue((int)13).withMaximum(100).withVisibility((b) -> {
         return (Boolean)this.triggerable.getValue();
      }).build());
      this.blocksPerTick = this.register(Settings.integerBuilder("BlocksPerTick").withMinimum(1).withValue((int)4).withMaximum(9).build());
      this.tickDelay = this.register(Settings.integerBuilder("TickDelay").withMinimum(0).withValue((int)0).withMaximum(10).build());
      this.rotate = this.register(Settings.b("Rotate", true));
      this.infoMessage = this.register(Settings.b("InfoMessage", false));
      this.offsetStep = 0;
      this.delayStep = 0;
      this.playerHotbarSlot = -1;
      this.lastHotbarSlot = -1;
      this.isSneaking = false;
      this.totalTicksRunning = 0;
      this.missingObiDisable = false;
   }

   private static EnumFacing getPlaceableSide(BlockPos pos) {
      EnumFacing[] var1 = EnumFacing.values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         EnumFacing side = var1[var3];
         BlockPos neighbour = pos.func_177972_a(side);
         if (mc.field_71441_e.func_180495_p(neighbour).func_177230_c().func_176209_a(mc.field_71441_e.func_180495_p(neighbour), false)) {
            IBlockState blockState = mc.field_71441_e.func_180495_p(neighbour);
            if (!blockState.func_185904_a().func_76222_j()) {
               return side;
            }
         }
      }

      return null;
   }

   protected void onEnable() {
      if (mc.field_71439_g == null) {
         this.disable();
      } else {
         this.firstRun = true;
         this.playerHotbarSlot = mc.field_71439_g.field_71071_by.field_70461_c;
         this.lastHotbarSlot = -1;
      }
   }

   protected void onDisable() {
      if (mc.field_71439_g != null) {
         if (this.lastHotbarSlot != this.playerHotbarSlot && this.playerHotbarSlot != -1) {
            mc.field_71439_g.field_71071_by.field_70461_c = this.playerHotbarSlot;
         }

         if (this.isSneaking) {
            mc.field_71439_g.field_71174_a.func_147297_a(new CPacketEntityAction(mc.field_71439_g, Action.STOP_SNEAKING));
            this.isSneaking = false;
         }

         this.playerHotbarSlot = -1;
         this.lastHotbarSlot = -1;
         this.missingObiDisable = false;
      }
   }

   public void onUpdate() {
      if (mc.field_71439_g != null && !ModuleManager.isModuleEnabled("Freecam")) {
         if ((Boolean)this.triggerable.getValue() && this.totalTicksRunning >= (Integer)this.timeoutTicks.getValue()) {
            this.totalTicksRunning = 0;
            this.disable();
         } else {
            if (!this.firstRun) {
               if (this.delayStep < (Integer)this.tickDelay.getValue()) {
                  ++this.delayStep;
                  return;
               }

               this.delayStep = 0;
            }

            if (this.firstRun) {
               this.firstRun = false;
               if (this.findObiInHotbar() == -1) {
                  this.missingObiDisable = true;
               }
            }

            Vec3d[] offsetPattern = new Vec3d[0];
            int maxSteps = 0;
            if (((AutoBarrier.Mode)this.mode.getValue()).equals(AutoBarrier.Mode.FULL)) {
               offsetPattern = AutoBarrier.Offsets.FULL;
               maxSteps = AutoBarrier.Offsets.FULL.length;
            }

            if (((AutoBarrier.Mode)this.mode.getValue()).equals(AutoBarrier.Mode.SURROUND)) {
               offsetPattern = AutoBarrier.Offsets.SURROUND;
               maxSteps = AutoBarrier.Offsets.SURROUND.length;
            }

            int blocksPlaced;
            for(blocksPlaced = 0; blocksPlaced < (Integer)this.blocksPerTick.getValue(); ++this.offsetStep) {
               if (this.offsetStep >= maxSteps) {
                  this.offsetStep = 0;
                  break;
               }

               BlockPos offsetPos = new BlockPos(offsetPattern[this.offsetStep]);
               BlockPos targetPos = (new BlockPos(mc.field_71439_g.func_174791_d())).func_177982_a(offsetPos.field_177962_a, offsetPos.field_177960_b, offsetPos.field_177961_c);
               if (this.placeBlock(targetPos)) {
                  ++blocksPlaced;
               }
            }

            if (blocksPlaced > 0) {
               if (this.lastHotbarSlot != this.playerHotbarSlot && this.playerHotbarSlot != -1) {
                  mc.field_71439_g.field_71071_by.field_70461_c = this.playerHotbarSlot;
                  this.lastHotbarSlot = this.playerHotbarSlot;
               }

               if (this.isSneaking) {
                  mc.field_71439_g.field_71174_a.func_147297_a(new CPacketEntityAction(mc.field_71439_g, Action.STOP_SNEAKING));
                  this.isSneaking = false;
               }
            }

            ++this.totalTicksRunning;
            if (this.missingObiDisable) {
               this.missingObiDisable = false;
               if ((Boolean)this.infoMessage.getValue()) {
                  TurokMessage.send_msg("AutoBarrier <- " + ChatFormatting.RED + "OFF");
               }

               this.disable();
            }

         }
      }
   }

   private boolean placeBlock(BlockPos pos) {
      Block block = mc.field_71441_e.func_180495_p(pos).func_177230_c();
      if (!(block instanceof BlockAir) && !(block instanceof BlockLiquid)) {
         return false;
      } else {
         Iterator var3 = mc.field_71441_e.func_72839_b((Entity)null, new AxisAlignedBB(pos)).iterator();

         while(var3.hasNext()) {
            Entity entity = (Entity)var3.next();
            if (!(entity instanceof EntityItem) && !(entity instanceof EntityXPOrb)) {
               return false;
            }
         }

         EnumFacing side = getPlaceableSide(pos);
         if (side == null) {
            return false;
         } else {
            BlockPos neighbour = pos.func_177972_a(side);
            EnumFacing opposite = side.func_176734_d();
            if (!BlockInteractionHelper.canBeClicked(neighbour)) {
               return false;
            } else {
               Vec3d hitVec = (new Vec3d(neighbour)).func_72441_c(0.5D, 0.5D, 0.5D).func_178787_e((new Vec3d(opposite.func_176730_m())).func_186678_a(0.5D));
               Block neighbourBlock = mc.field_71441_e.func_180495_p(neighbour).func_177230_c();
               int obiSlot = this.findObiInHotbar();
               if (obiSlot == -1) {
                  this.missingObiDisable = true;
                  return false;
               } else {
                  if (this.lastHotbarSlot != obiSlot) {
                     mc.field_71439_g.field_71071_by.field_70461_c = obiSlot;
                     this.lastHotbarSlot = obiSlot;
                  }

                  if (!this.isSneaking && BlockInteractionHelper.blackList.contains(neighbourBlock) || BlockInteractionHelper.shulkerList.contains(neighbourBlock)) {
                     mc.field_71439_g.field_71174_a.func_147297_a(new CPacketEntityAction(mc.field_71439_g, Action.START_SNEAKING));
                     this.isSneaking = true;
                  }

                  if ((Boolean)this.rotate.getValue()) {
                     BlockInteractionHelper.faceVectorPacketInstant(hitVec);
                  }

                  mc.field_71442_b.func_187099_a(mc.field_71439_g, mc.field_71441_e, neighbour, opposite, hitVec, EnumHand.MAIN_HAND);
                  mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
                  mc.field_71467_ac = 4;
                  return true;
               }
            }
         }
      }
   }

   private int findObiInHotbar() {
      int slot = -1;

      for(int i = 0; i < 9; ++i) {
         ItemStack stack = mc.field_71439_g.field_71071_by.func_70301_a(i);
         if (stack != ItemStack.field_190927_a && stack.func_77973_b() instanceof ItemBlock) {
            Block block = ((ItemBlock)stack.func_77973_b()).func_179223_d();
            if (block instanceof BlockObsidian) {
               slot = i;
               break;
            }
         }
      }

      return slot;
   }

   private static class Offsets {
      private static final Vec3d[] SURROUND = new Vec3d[]{new Vec3d(1.0D, 0.0D, 0.0D), new Vec3d(0.0D, 0.0D, 1.0D), new Vec3d(-1.0D, 0.0D, 0.0D), new Vec3d(0.0D, 0.0D, -1.0D), new Vec3d(1.0D, -1.0D, 0.0D), new Vec3d(0.0D, -1.0D, 1.0D), new Vec3d(-1.0D, -1.0D, 0.0D), new Vec3d(0.0D, -1.0D, -1.0D)};
      private static final Vec3d[] FULL = new Vec3d[]{new Vec3d(1.0D, 0.0D, 0.0D), new Vec3d(0.0D, 0.0D, 1.0D), new Vec3d(-1.0D, 0.0D, 0.0D), new Vec3d(0.0D, 0.0D, -1.0D), new Vec3d(1.0D, -1.0D, 0.0D), new Vec3d(0.0D, -1.0D, 1.0D), new Vec3d(-1.0D, -1.0D, 0.0D), new Vec3d(0.0D, -1.0D, -1.0D), new Vec3d(0.0D, -1.0D, 0.0D)};
   }

   private static enum Mode {
      SURROUND,
      FULL;
   }
}
