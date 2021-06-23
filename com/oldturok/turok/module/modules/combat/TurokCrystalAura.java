package com.oldturok.turok.module.modules.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.oldturok.turok.TurokFriends;
import com.oldturok.turok.TurokMessage;
import com.oldturok.turok.event.events.PacketEvent;
import com.oldturok.turok.event.events.RenderEvent;
import com.oldturok.turok.module.Module;
import com.oldturok.turok.module.ModuleManager;
import com.oldturok.turok.module.modules.chat.AutoGG;
import com.oldturok.turok.setting.Setting;
import com.oldturok.turok.setting.Settings;
import com.oldturok.turok.util.EntityUtil;
import com.oldturok.turok.util.TurokTessellator;
import com.oldturok.turok.util.Wrapper;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.potion.Potion;
import net.minecraft.util.CombatRules;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;

@Module.Info(
   name = "TurokCrystalAura",
   description = "A aura for break crystals, fast and good for high ping.",
   category = Module.Category.TUROK_COMBAT
)
public class TurokCrystalAura extends Module {
   private Setting<Boolean> rgb = this.register(Settings.b("Color RGB", true));
   private Setting<Boolean> ant_fraqueza = this.register(Settings.b("Anti Weakness", true));
   private Setting<Boolean> auto_switch = this.register(Settings.b("Auto Switch"));
   private Setting<Boolean> ray_trace = this.register(Settings.b("Ray Trace", true));
   private Setting<Boolean> dois_cristais = this.register(Settings.b("Dual Place", true));
   private Setting<Integer> dano_minimo = this.register(Settings.integerBuilder("Damage for Place").withMinimum(0).withMaximum(10).withValue((int)2));
   private Setting<Integer> colocar_distancia = this.register(Settings.integerBuilder("Place Range").withMinimum(0).withMaximum(6).withValue((int)5));
   private Setting<Integer> quebrar_distancia = this.register(Settings.integerBuilder("Break Range").withMinimum(0).withMaximum(16).withValue((int)6));
   private Setting<Integer> tempo_de_ataque = this.register(Settings.integerBuilder("Break Tick").withMinimum(0).withMaximum(10).withValue((int)0));
   private Setting<Integer> cor_red = this.register(Settings.integerBuilder("Color Red").withMinimum(0).withMaximum(255).withValue((int)200));
   private Setting<Integer> cor_green = this.register(Settings.integerBuilder("Color Green").withMinimum(0).withMaximum(255).withValue((int)200));
   private Setting<Integer> cor_blue = this.register(Settings.integerBuilder("Color Blue").withMinimum(0).withMaximum(255).withValue((int)200));
   private Setting<Integer> cor_alfa = this.register(Settings.integerBuilder("Alpha Color").withMinimum(0).withMaximum(255).withValue((int)70));
   private Setting<TurokCrystalAura.TypeDraw> type;
   private static boolean is_spoofing_angles;
   private static boolean toggle_pitch;
   private static double player_yaw;
   private static double player_pitch;
   private Entity render_ent;
   private BlockPos render;
   private boolean switch_cooldown;
   private boolean attack;
   private int old_slot;
   private int new_slot;
   private int places;
   private AutoGG target_autogg;
   public static String player_target;
   int prepare;
   int mask;
   int r;
   int g;
   int b;
   Boolean type_;
   private long system_time;
   @EventHandler
   private Listener<PacketEvent.Send> packetListener;

   public TurokCrystalAura() {
      this.type = this.register(Settings.e("Type Draw", TurokCrystalAura.TypeDraw.OUTLINE));
      this.switch_cooldown = false;
      this.attack = false;
      this.old_slot = -1;
      this.target_autogg = (AutoGG)ModuleManager.getModuleByName("AutoGG");
      this.system_time = -1L;
      this.packetListener = new Listener((event) -> {
         Packet packet = event.getPacket();
         if (packet instanceof CPacketPlayer && is_spoofing_angles) {
            ((CPacketPlayer)packet).field_149476_e = (float)player_yaw;
            ((CPacketPlayer)packet).field_149473_f = (float)player_pitch;
         }

      }, (Predicate[])(new Predicate[0]));
   }

   public void onEnable() {
      TurokMessage.send_msg("TurokCrystalAura -> " + ChatFormatting.GREEN + "ON");
   }

   public void onDisable() {
      this.render = null;
      this.render_ent = null;
      reset_rotation();
      TurokMessage.send_msg("TurokCrystalAura <- " + ChatFormatting.RED + "OFF");
   }

   public void onUpdate() {
      switch((TurokCrystalAura.TypeDraw)this.type.getValue()) {
      case OUTLINE:
         this.prepare = 1;
         this.mask = 63;
         this.type_ = true;
         break;
      case BOX:
         this.prepare = 7;
         this.mask = 63;
         this.type_ = false;
      }

      EntityEnderCrystal crystal = (EntityEnderCrystal)mc.field_71441_e.field_72996_f.stream().filter((entity) -> {
         return entity instanceof EntityEnderCrystal;
      }).map((entity) -> {
         return entity;
      }).min(Comparator.comparing((dx) -> {
         return mc.field_71439_g.func_70032_d(dx);
      })).orElse((Object)null);
      int crystal_slot;
      if (crystal != null && mc.field_71439_g.func_70032_d(crystal) <= (float)(Integer)this.quebrar_distancia.getValue()) {
         if (System.nanoTime() / 1000000L - this.system_time >= (long)(Integer)this.tempo_de_ataque.getValue()) {
            if ((Boolean)this.ant_fraqueza.getValue() && mc.field_71439_g.func_70644_a(MobEffects.field_76437_t)) {
               if (!this.attack) {
                  this.old_slot = Wrapper.getPlayer().field_71071_by.field_70461_c;
                  this.attack = true;
               }

               this.new_slot = -1;

               for(crystal_slot = 0; crystal_slot < 9; ++crystal_slot) {
                  ItemStack stack = Wrapper.getPlayer().field_71071_by.func_70301_a(crystal_slot);
                  if (stack != ItemStack.field_190927_a) {
                     if (stack.func_77973_b() instanceof ItemSword) {
                        this.new_slot = crystal_slot;
                        break;
                     }

                     if (stack.func_77973_b() instanceof ItemTool) {
                        this.new_slot = crystal_slot;
                        break;
                     }
                  }
               }

               if (this.new_slot != -1) {
                  Wrapper.getPlayer().field_71071_by.field_70461_c = this.new_slot;
                  this.switch_cooldown = true;
               }
            }

            this.lookAtPacket(crystal.field_70165_t, crystal.field_70163_u, crystal.field_70161_v, mc.field_71439_g);
            mc.field_71442_b.func_78764_a(mc.field_71439_g, crystal);
            mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
            this.system_time = System.nanoTime() / 1000000L;
            ++this.places;
         }

         if ((Boolean)this.dois_cristais.getValue() && this.places >= 2) {
            reset_rotation();
            this.places = 0;
            return;
         }

         if (!(Boolean)this.dois_cristais.getValue() && this.places >= 1) {
            reset_rotation();
            this.places = 0;
            return;
         }
      } else {
         reset_rotation();
         if (this.old_slot != -1) {
            Wrapper.getPlayer().field_71071_by.field_70461_c = this.old_slot;
            this.old_slot = -1;
         }

         this.attack = false;
      }

      crystal_slot = mc.field_71439_g.func_184614_ca().func_77973_b() == Items.field_185158_cP ? mc.field_71439_g.field_71071_by.field_70461_c : -1;
      if (crystal_slot == -1) {
         for(int l = 0; l < 9; ++l) {
            if (mc.field_71439_g.field_71071_by.func_70301_a(l).func_77973_b() == Items.field_185158_cP) {
               crystal_slot = l;
               break;
            }
         }
      }

      boolean off_hand = false;
      if (mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_185158_cP) {
         off_hand = true;
      } else if (crystal_slot == -1) {
         return;
      }

      List<BlockPos> blocks = this.find_crystal_blocks();
      List<Entity> entities = new ArrayList();
      entities.addAll((Collection)mc.field_71441_e.field_73010_i.stream().filter((entityPlayer) -> {
         return !TurokFriends.is_friend(entityPlayer.func_70005_c_());
      }).collect(Collectors.toList()));
      BlockPos q = null;
      double damage = 0.0D;
      Iterator var9 = entities.iterator();

      label164:
      while(true) {
         Entity player;
         do {
            do {
               do {
                  if (!var9.hasNext()) {
                     if (damage == 0.0D) {
                        player_target = null;
                        this.render = null;
                        this.render_ent = null;
                        reset_rotation();
                        return;
                     }

                     this.render = q;
                     if (!off_hand && mc.field_71439_g.field_71071_by.field_70461_c != crystal_slot) {
                        if ((Boolean)this.auto_switch.getValue()) {
                           mc.field_71439_g.field_71071_by.field_70461_c = crystal_slot;
                           reset_rotation();
                           this.switch_cooldown = true;
                        }

                        return;
                     }

                     this.lookAtPacket((double)q.field_177962_a + 0.5D, (double)q.field_177960_b - 0.5D, (double)q.field_177961_c + 0.5D, mc.field_71439_g);
                     EnumFacing f;
                     if ((Boolean)this.ray_trace.getValue()) {
                        RayTraceResult result = mc.field_71441_e.func_72933_a(new Vec3d(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + (double)mc.field_71439_g.func_70047_e(), mc.field_71439_g.field_70161_v), new Vec3d((double)q.field_177962_a + 0.5D, (double)q.field_177960_b - 0.5D, (double)q.field_177961_c + 0.5D));
                        if (result != null && result.field_178784_b != null) {
                           f = result.field_178784_b;
                        } else {
                           f = EnumFacing.UP;
                        }
                     } else {
                        f = EnumFacing.DOWN;
                     }

                     if (this.switch_cooldown) {
                        this.switch_cooldown = false;
                        return;
                     }

                     mc.field_71439_g.field_71174_a.func_147297_a(new CPacketPlayerTryUseItemOnBlock(q, f, off_hand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0F, 0.0F, 0.0F));
                     if (is_spoofing_angles) {
                        EntityPlayerSP var10000;
                        if (toggle_pitch) {
                           var10000 = mc.field_71439_g;
                           var10000.field_70125_A += 4.0E-4F;
                           toggle_pitch = false;
                        } else {
                           var10000 = mc.field_71439_g;
                           var10000.field_70125_A -= 4.0E-4F;
                           toggle_pitch = true;
                        }
                     }

                     return;
                  }

                  player = (Entity)var9.next();
               } while(player == mc.field_71439_g);
            } while(((EntityLivingBase)player).func_110143_aJ() <= 0.0F);
         } while(((EntityLivingBase)player).field_70128_L);

         Iterator var11 = blocks.iterator();

         while(true) {
            BlockPos blockPos;
            double d;
            double self;
            do {
               do {
                  double b;
                  do {
                     if (!var11.hasNext()) {
                        continue label164;
                     }

                     blockPos = (BlockPos)var11.next();
                     b = player.func_174818_b(blockPos);
                  } while(b >= 169.0D);

                  d = (double)calculate_damage((double)blockPos.field_177962_a + 0.5D, (double)(blockPos.field_177960_b + 1), (double)blockPos.field_177961_c + 0.5D, player);
               } while(d <= damage);

               self = (double)calculate_damage((double)blockPos.field_177962_a + 0.5D, (double)(blockPos.field_177960_b + 1), (double)blockPos.field_177961_c + 0.5D, mc.field_71439_g);
            } while(self > d && d >= (double)((EntityLivingBase)player).func_110143_aJ());

            if (!(self - 0.5D > (double)mc.field_71439_g.func_110143_aJ()) && !(d < (double)(Integer)this.dano_minimo.getValue())) {
               player_target = ((EntityLivingBase)player).func_70005_c_();
               damage = d;
               q = blockPos;
               this.render_ent = player;
            }
         }
      }
   }

   public void onWorldRender(RenderEvent event) {
      float[] tick_color = new float[]{(float)(System.currentTimeMillis() % 11520L) / 11520.0F};
      int color_rgb = Color.HSBtoRGB(tick_color[0], 1.0F, 1.0F);
      tick_color[0] += 0.1F;
      if (this.render != null && player_target != null) {
         TurokTessellator.prepare(this.prepare);
         if ((Boolean)this.rgb.getValue()) {
            this.r = color_rgb >> 16 & 255;
            this.g = color_rgb >> 8 & 255;
            this.b = color_rgb & 255;
         } else {
            this.r = (Integer)this.cor_red.getValue();
            this.g = (Integer)this.cor_green.getValue();
            this.b = (Integer)this.cor_blue.getValue();
         }

         Color type_color = new Color(this.r, this.g, this.b, (Integer)this.cor_alfa.getValue());
         if (this.type_) {
            TurokTessellator.drawLines(this.render, type_color.getRGB(), this.mask);
         } else {
            TurokTessellator.drawBox(this.render, type_color.getRGB(), this.mask);
         }

         TurokTessellator.release();
      }

   }

   private void lookAtPacket(double px, double py, double pz, EntityPlayer me) {
      double[] v = EntityUtil.calculateLookAt(px, py, pz, me);
      set_yaw_and_pitch((float)v[0], (float)v[1]);
   }

   private boolean can_place_crystal(BlockPos blockPos) {
      BlockPos boost = blockPos.func_177982_a(0, 1, 0);
      BlockPos boost2 = blockPos.func_177982_a(0, 2, 0);
      return (mc.field_71441_e.func_180495_p(blockPos).func_177230_c() == Blocks.field_150357_h || mc.field_71441_e.func_180495_p(blockPos).func_177230_c() == Blocks.field_150343_Z) && mc.field_71441_e.func_180495_p(boost).func_177230_c() == Blocks.field_150350_a && mc.field_71441_e.func_180495_p(boost2).func_177230_c() == Blocks.field_150350_a && mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(boost)).isEmpty() && mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(boost2)).isEmpty();
   }

   public static BlockPos player_pos() {
      return new BlockPos(Math.floor(mc.field_71439_g.field_70165_t), Math.floor(mc.field_71439_g.field_70163_u), Math.floor(mc.field_71439_g.field_70161_v));
   }

   private List<BlockPos> find_crystal_blocks() {
      NonNullList<BlockPos> positions = NonNullList.func_191196_a();
      positions.addAll((Collection)this.get_sphere(player_pos(), ((Integer)this.colocar_distancia.getValue()).floatValue(), (Integer)this.colocar_distancia.getValue(), false, true, 0).stream().filter(this::can_place_crystal).collect(Collectors.toList()));
      return positions;
   }

   public List<BlockPos> get_sphere(BlockPos loc, float r, int h, boolean hollow, boolean sphere, int plus_y) {
      List<BlockPos> circleblocks = new ArrayList();
      int cx = loc.func_177958_n();
      int cy = loc.func_177956_o();
      int cz = loc.func_177952_p();

      for(int x = cx - (int)r; (float)x <= (float)cx + r; ++x) {
         for(int z = cz - (int)r; (float)z <= (float)cz + r; ++z) {
            for(int y = sphere ? cy - (int)r : cy; (float)y < (sphere ? (float)cy + r : (float)(cy + h)); ++y) {
               double dist = (double)((cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0));
               if (dist < (double)(r * r) && (!hollow || dist >= (double)((r - 1.0F) * (r - 1.0F)))) {
                  BlockPos l = new BlockPos(x, y + plus_y, z);
                  circleblocks.add(l);
               }
            }
         }
      }

      return circleblocks;
   }

   public static float calculate_damage(double posX, double posY, double posZ, Entity entity) {
      float doubleExplosionSize = 12.0F;
      double distancedsize = entity.func_70011_f(posX, posY, posZ) / (double)doubleExplosionSize;
      Vec3d vec3d = new Vec3d(posX, posY, posZ);
      double blockDensity = (double)entity.field_70170_p.func_72842_a(vec3d, entity.func_174813_aQ());
      double v = (1.0D - distancedsize) * blockDensity;
      float damage = (float)((int)((v * v + v) / 2.0D * 7.0D * (double)doubleExplosionSize + 1.0D));
      double finald = 1.0D;
      if (entity instanceof EntityLivingBase) {
         finald = (double)get_blast_reduction((EntityLivingBase)entity, get_damage_multiplied(damage), new Explosion(mc.field_71441_e, (Entity)null, posX, posY, posZ, 6.0F, false, true));
      }

      return (float)finald;
   }

   public static float get_blast_reduction(EntityLivingBase entity, float damage, Explosion explosion) {
      if (entity instanceof EntityPlayer) {
         EntityPlayer ep = (EntityPlayer)entity;
         DamageSource ds = DamageSource.func_94539_a(explosion);
         damage = CombatRules.func_189427_a(damage, (float)ep.func_70658_aO(), (float)ep.func_110148_a(SharedMonsterAttributes.field_189429_h).func_111126_e());
         int k = EnchantmentHelper.func_77508_a(ep.func_184193_aE(), ds);
         float f = MathHelper.func_76131_a((float)k, 0.0F, 20.0F);
         damage *= 1.0F - f / 25.0F;
         if (entity.func_70644_a(Potion.func_188412_a(11))) {
            damage -= damage / 4.0F;
         }

         damage = Math.max(damage, 0.0F);
         return damage;
      } else {
         damage = CombatRules.func_189427_a(damage, (float)entity.func_70658_aO(), (float)entity.func_110148_a(SharedMonsterAttributes.field_189429_h).func_111126_e());
         return damage;
      }
   }

   private static void reset_rotation() {
      if (is_spoofing_angles) {
         player_yaw = (double)mc.field_71439_g.field_70177_z;
         player_pitch = (double)mc.field_71439_g.field_70125_A;
         is_spoofing_angles = false;
      }

   }

   private static void set_yaw_and_pitch(float player_yaw_, float player_pitch_) {
      player_yaw = (double)player_yaw_;
      player_pitch = (double)player_pitch_;
      is_spoofing_angles = true;
   }

   public static float calculate_damage(EntityEnderCrystal crystal, Entity entity) {
      return calculate_damage(crystal.field_70165_t, crystal.field_70163_u, crystal.field_70161_v, entity);
   }

   private static float get_damage_multiplied(float damage) {
      int diff = mc.field_71441_e.func_175659_aa().func_151525_a();
      return damage * (diff == 0 ? 0.0F : (diff == 2 ? 1.0F : (diff == 1 ? 0.5F : 1.5F)));
   }

   public static enum TypeDraw {
      OUTLINE,
      BOX;
   }
}
