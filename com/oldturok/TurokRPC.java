package com.oldturok;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import com.oldturok.turok.module.ModuleManager;
import com.oldturok.turok.module.modules.combat.TurokCrystalAura;
import net.minecraft.client.Minecraft;

public class TurokRPC {
   public static DiscordRichPresence discord_presence;
   private static boolean discord_started;
   private static final DiscordRPC discord_rpc;
   public static String detail;
   public static String state;
   private static String name;
   private static String separe;
   private static String server;
   private static String event_1;
   private static String event_extra;
   public static TurokCrystalAura crystalfunction = (TurokCrystalAura)ModuleManager.getModuleByName("TurokCrystalAura");
   public static Minecraft mc = Minecraft.func_71410_x();

   public static void start() {
      discord_presence = new DiscordRichPresence();
      DiscordEventHandlers handler_ = new DiscordEventHandlers();
      discord_rpc.Discord_Initialize("683841698778185818", handler_, true, "");
      discord_presence.startTimestamp = System.currentTimeMillis() / 1000L;
      discord_presence.largeImageKey = "splash";
      discord_presence.largeImageText = "Turok Client 0.5";
      (new Thread(() -> {
         while(!Thread.currentThread().isInterrupted()) {
            try {
               if (mc.field_71439_g != null) {
                  name = mc.field_71439_g.func_70005_c_();
                  separe = " - ";
               } else {
                  name = "";
                  separe = " ";
                  event_extra = "";
                  event_1 = "";
               }

               if (mc.field_71441_e != null) {
                  if (mc.func_71387_A()) {
                     server = "just offline";
                     event_1 = "relaxing in " + mc.field_71441_e.func_180494_b(mc.field_71439_g.func_180425_c()).func_185359_l();
                  } else if (mc.func_147104_D() != null) {
                     server = mc.func_147104_D().field_78845_b;
                     event_extra = "";
                     if (mc.field_71439_g.func_110143_aJ() < 4.0F) {
                        if (mc.field_71439_g.field_70128_L) {
                           event_1 = "died";
                        } else {
                           event_1 = "low health";
                        }
                     } else if (ModuleManager.getModuleByName("TurokCrystalAura").isEnabled()) {
                        TurokCrystalAura var10000 = crystalfunction;
                        if (TurokCrystalAura.player_target == null) {
                           event_1 = "TurokCrystalAura on...";
                        } else {
                           StringBuilder var3 = (new StringBuilder()).append("crystaling ");
                           TurokCrystalAura var10001 = crystalfunction;
                           event_1 = var3.append(TurokCrystalAura.player_target).toString();
                        }
                     } else {
                        event_1 = "relaxing in " + mc.field_71441_e.func_180494_b(mc.field_71439_g.func_180425_c()).func_185359_l();
                     }
                  }
               } else {
                  server = "main menu";
                  separe = " ";
                  event_extra = "...";
                  event_1 = "";
               }

               detail = name + separe + server;
               state = event_1 + event_extra;
               discord_rpc.Discord_RunCallbacks();
               discord_presence.details = detail;
               discord_presence.state = state;
               discord_rpc.Discord_UpdatePresence(discord_presence);
            } catch (Exception var2) {
               var2.printStackTrace();
            }

            try {
               Thread.sleep(4000L);
            } catch (InterruptedException var1) {
               var1.printStackTrace();
            }
         }

      }, "RPC-Callback-Handler")).start();
   }

   static {
      discord_rpc = DiscordRPC.INSTANCE;
      discord_presence = new DiscordRichPresence();
   }
}
