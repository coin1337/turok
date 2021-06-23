package com.oldturok.turok.module.modules.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.oldturok.turok.TurokFriends;
import com.oldturok.turok.TurokMessage;
import com.oldturok.turok.module.Module;
import com.oldturok.turok.module.ModuleManager;
import com.oldturok.turok.setting.Setting;
import com.oldturok.turok.setting.Settings;
import com.oldturok.turok.util.BlockInteractionHelper;
import com.oldturok.turok.util.EntityUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketEntityAction.Action;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameType;

@Module.Info(
   name = "FastAutoTrap",
   description = "AutoTrap based on KAMI.",
   category = Module.Category.TUROK_COMBAT
)
public class FastAutoTrap extends Module {
   private Setting<Double> range = this.register(Settings.doubleBuilder("Range").withMinimum(3.5D).withValue((Number)5.0D).withMaximum(10.0D).build());
   private Setting<Integer> blocksPerTick = this.register(Settings.integerBuilder("BlocksPerTick").withMinimum(1).withValue((int)7).withMaximum(23).build());
   private Setting<Integer> tickDelay = this.register(Settings.integerBuilder("TickDelay").withMinimum(0).withValue((int)2).withMaximum(10).build());
   private Setting<FastAutoTrap.Cage> cage;
   private Setting<Boolean> rotate;
   private Setting<Boolean> noGlitchBlocks;
   private Setting<Boolean> activeInFreecam;
   private Setting<Boolean> infoMessage;
   private EntityPlayer closestTarget;
   private String lastTargetName;
   private int playerHotbarSlot;
   private int lastHotbarSlot;
   private boolean isSneaking;
   private int delayStep;
   private int offsetStep;
   private boolean firstRun;
   private boolean missingObiDisable;

   public FastAutoTrap() {
      this.cage = this.register(Settings.e("Cage", FastAutoTrap.Cage.CRYSTALFULL));
      this.rotate = this.register(Settings.b("Rotate", true));
      this.noGlitchBlocks = this.register(Settings.b("NoGlitchBlocks", true));
      this.activeInFreecam = this.register(Settings.b("Active In Freecam", true));
      this.infoMessage = this.register(Settings.b("Debug", false));
      this.playerHotbarSlot = -1;
      this.lastHotbarSlot = -1;
      this.isSneaking = false;
      this.delayStep = 0;
      this.offsetStep = 0;
      this.missingObiDisable = false;
   }

   private static EnumFacing get_placeable_side(BlockPos pos) {
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
      TurokMessage.send_msg("FastAutoTrap -> " + ChatFormatting.GREEN + "ON");
      if (mc.field_71439_g == null) {
         this.disable();
      } else {
         this.firstRun = true;
         this.playerHotbarSlot = mc.field_71439_g.field_71071_by.field_70461_c;
         this.lastHotbarSlot = -1;
      }
   }

   protected void onDisable() {
      TurokMessage.send_msg("FastAutoTrap <- " + ChatFormatting.RED + "OFF");
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
      if (mc.field_71439_g != null) {
         if ((Boolean)this.activeInFreecam.getValue() || !ModuleManager.isModuleEnabled("Freecam")) {
            if (this.firstRun) {
               if (this.find_obi_in_hotbar() == -1) {
                  if ((Boolean)this.infoMessage.getValue()) {
                     TurokMessage.send_msg("FastAutoTrap <- " + ChatFormatting.RED + "Disabled" + ChatFormatting.RESET + ", Obsidian missing!");
                  }

                  this.disable();
                  return;
               }
            } else {
               if (this.delayStep < (Integer)this.tickDelay.getValue()) {
                  ++this.delayStep;
                  return;
               }

               this.delayStep = 0;
            }

            this.findClosestTarget();
            if (this.closestTarget != null) {
               if (this.firstRun) {
                  this.firstRun = false;
                  this.lastTargetName = this.closestTarget.func_70005_c_();
               } else if (!this.lastTargetName.equals(this.closestTarget.func_70005_c_())) {
                  this.offsetStep = 0;
                  this.lastTargetName = this.closestTarget.func_70005_c_();
               }

               List<Vec3d> placeTargets = new ArrayList();
               if (((FastAutoTrap.Cage)this.cage.getValue()).equals(FastAutoTrap.Cage.TRAP)) {
                  Collections.addAll(placeTargets, FastAutoTrap.Offsets.TRAP);
               }

               if (((FastAutoTrap.Cage)this.cage.getValue()).equals(FastAutoTrap.Cage.CRYSTALEXA)) {
                  Collections.addAll(placeTargets, FastAutoTrap.Offsets.CRYSTALEXA);
               }

               if (((FastAutoTrap.Cage)this.cage.getValue()).equals(FastAutoTrap.Cage.CRYSTALFULL)) {
                  Collections.addAll(placeTargets, FastAutoTrap.Offsets.CRYSTALFULL);
               }

               int blocksPlaced;
               for(blocksPlaced = 0; blocksPlaced < (Integer)this.blocksPerTick.getValue(); ++this.offsetStep) {
                  if (this.offsetStep >= placeTargets.size()) {
                     this.offsetStep = 0;
                     break;
                  }

                  BlockPos offsetPos = new BlockPos((Vec3d)placeTargets.get(this.offsetStep));
                  BlockPos targetPos = (new BlockPos(this.closestTarget.func_174791_d())).func_177977_b().func_177982_a(offsetPos.field_177962_a, offsetPos.field_177960_b, offsetPos.field_177961_c);
                  if (this.placeBlockInRange(targetPos, (Double)this.range.getValue())) {
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

               if (this.missingObiDisable) {
                  this.missingObiDisable = false;
                  if ((Boolean)this.infoMessage.getValue()) {
                     TurokMessage.send_msg("FastAutoTrap <- " + ChatFormatting.RED + "Disabled" + ChatFormatting.RESET + ", Obsidian missing!");
                  }

                  this.disable();
               }

            }
         }
      }
   }

   private boolean placeBlockInRange(BlockPos pos, double range) {
      Block block = mc.field_71441_e.func_180495_p(pos).func_177230_c();
      if (!(block instanceof BlockAir) && !(block instanceof BlockLiquid)) {
         return false;
      } else {
         Iterator var5 = mc.field_71441_e.func_72839_b((Entity)null, new AxisAlignedBB(pos)).iterator();

         while(var5.hasNext()) {
            Entity entity = (Entity)var5.next();
            if (!(entity instanceof EntityItem) && !(entity instanceof EntityXPOrb)) {
               return false;
            }
         }

         EnumFacing side = get_placeable_side(pos);
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
               if (mc.field_71439_g.func_174791_d().func_72438_d(hitVec) > range) {
                  return false;
               } else {
                  int obiSlot = this.find_obi_in_hotbar();
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
                     if ((Boolean)this.noGlitchBlocks.getValue() && !mc.field_71442_b.func_178889_l().equals(GameType.CREATIVE)) {
                        mc.field_71439_g.field_71174_a.func_147297_a(new CPacketPlayerDigging(net.minecraft.network.play.client.CPacketPlayerDigging.Action.START_DESTROY_BLOCK, neighbour, opposite));
                     }

                     return true;
                  }
               }
            }
         }
      }
   }

   private int find_obi_in_hotbar() {
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

   private void findClosestTarget() {
      List<EntityPlayer> playerList = mc.field_71441_e.field_73010_i;
      this.closestTarget = null;
      Iterator var2 = playerList.iterator();

      while(var2.hasNext()) {
         EntityPlayer target = (EntityPlayer)var2.next();
         if (target != mc.field_71439_g && !((double)mc.field_71439_g.func_70032_d(target) > (Double)this.range.getValue() + 3.0D) && EntityUtil.isLiving(target) && !(target.func_110143_aJ() <= 0.0F) && !TurokFriends.is_friend(target.func_70005_c_())) {
            if (this.closestTarget == null) {
               this.closestTarget = target;
            } else if (mc.field_71439_g.func_70032_d(target) < mc.field_71439_g.func_70032_d(this.closestTarget)) {
               this.closestTarget = target;
            }
         }
      }

   }

   public String getHudInfo() {
      return this.closestTarget != null ? this.closestTarget.func_70005_c_().toUpperCase() : "NO TARGET";
   }

   private static class Offsets {
      private static final Vec3d[] TRAP = new Vec3d[]{new Vec3d(0.0D, 0.0D, -1.0D), new Vec3d(1.0D, 0.0D, 0.0D), new Vec3d(0.0D, 0.0D, 1.0D), new Vec3d(-1.0D, 0.0D, 0.0D), new Vec3d(0.0D, 1.0D, -1.0D), new Vec3d(1.0D, 1.0D, 0.0D), new Vec3d(0.0D, 1.0D, 1.0D), new Vec3d(-1.0D, 1.0D, 0.0D), new Vec3d(0.0D, 2.0D, -1.0D), new Vec3d(1.0D, 2.0D, 0.0D), new Vec3d(0.0D, 2.0D, 1.0D), new Vec3d(-1.0D, 2.0D, 0.0D), new Vec3d(0.0D, 3.0D, -1.0D), new Vec3d(0.0D, 3.0D, 0.0D)};
      private static final Vec3d[] CRYSTALEXA = new Vec3d[]{new Vec3d(0.0D, 0.0D, -1.0D), new Vec3d(0.0D, 1.0D, -1.0D), new Vec3d(0.0D, 2.0D, -1.0D), new Vec3d(1.0D, 2.0D, 0.0D), new Vec3d(0.0D, 2.0D, 1.0D), new Vec3d(-1.0D, 2.0D, 0.0D), new Vec3d(-1.0D, 2.0D, -1.0D), new Vec3d(1.0D, 2.0D, 1.0D), new Vec3d(1.0D, 2.0D, -1.0D), new Vec3d(-1.0D, 2.0D, 1.0D), new Vec3d(0.0D, 3.0D, -1.0D), new Vec3d(0.0D, 3.0D, 0.0D)};
      private static final Vec3d[] CRYSTALFULL = new Vec3d[]{new Vec3d(0.0D, 0.0D, -1.0D), new Vec3d(1.0D, 0.0D, 0.0D), new Vec3d(0.0D, 0.0D, 1.0D), new Vec3d(-1.0D, 0.0D, 0.0D), new Vec3d(-1.0D, 0.0D, 1.0D), new Vec3d(1.0D, 0.0D, -1.0D), new Vec3d(-1.0D, 0.0D, -1.0D), new Vec3d(1.0D, 0.0D, 1.0D), new Vec3d(-1.0D, 1.0D, -1.0D), new Vec3d(1.0D, 1.0D, 1.0D), new Vec3d(-1.0D, 1.0D, 1.0D), new Vec3d(1.0D, 1.0D, -1.0D), new Vec3d(0.0D, 2.0D, -1.0D), new Vec3d(1.0D, 2.0D, 0.0D), new Vec3d(0.0D, 2.0D, 1.0D), new Vec3d(-1.0D, 2.0D, 0.0D), new Vec3d(-1.0D, 2.0D, 1.0D), new Vec3d(1.0D, 2.0D, -1.0D), new Vec3d(0.0D, 3.0D, -1.0D), new Vec3d(0.0D, 3.0D, 0.0D)};
   }

   private static enum Cage {
      TRAP,
      CRYSTALEXA,
      CRYSTALFULL;
   }
}
