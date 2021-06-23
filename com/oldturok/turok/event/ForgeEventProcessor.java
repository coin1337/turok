package com.oldturok.turok.event;

import com.oldturok.turok.TurokMod;
import com.oldturok.turok.event.events.DisplaySizeChangedEvent;
import com.oldturok.turok.gui.UIRenderer;
import com.oldturok.turok.gui.rgui.component.container.use.Frame;
import com.oldturok.turok.gui.turok.TurokGUI;
import com.oldturok.turok.module.ModuleManager;
import com.oldturok.turok.util.TurokTessellator;
import com.oldturok.turok.util.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.client.event.PlayerSPPushOutOfBlocksEvent;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Post;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Pre;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent.Start;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.LeftClickBlock;
import net.minecraftforge.event.world.ChunkEvent.Load;
import net.minecraftforge.event.world.ChunkEvent.Unload;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class ForgeEventProcessor {
   private int displayWidth;
   private int displayHeight;

   @SubscribeEvent
   public void onUpdate(LivingUpdateEvent event) {
      if (!event.isCanceled()) {
         if (Minecraft.func_71410_x().field_71443_c != this.displayWidth || Minecraft.func_71410_x().field_71440_d != this.displayHeight) {
            TurokMod.EVENT_BUS.post(new DisplaySizeChangedEvent());
            this.displayWidth = Minecraft.func_71410_x().field_71443_c;
            this.displayHeight = Minecraft.func_71410_x().field_71440_d;
            TurokMod.get_instance().get_gui_manager().getChildren().stream().filter((component) -> {
               return component instanceof Frame;
            }).forEach((component) -> {
               TurokGUI.dock((Frame)component);
            });
         }

      }
   }

   @SubscribeEvent
   public void onTick(ClientTickEvent event) {
      if (Wrapper.getPlayer() != null) {
         ModuleManager.onUpdate();
         TurokMod.get_instance().get_gui_manager().callTick(TurokMod.get_instance().get_gui_manager());
      }
   }

   @SubscribeEvent
   public void onWorldRender(RenderWorldLastEvent event) {
      if (!event.isCanceled()) {
         ModuleManager.onWorldRender(event);
      }
   }

   @SubscribeEvent
   public void onRenderPre(Pre event) {
      if (event.getType() == ElementType.BOSSINFO && ModuleManager.isModuleEnabled("BossStack")) {
         event.setCanceled(true);
      }

   }

   @SubscribeEvent
   public void onRender(Post event) {
      if (!event.isCanceled()) {
         ElementType target = ElementType.EXPERIENCE;
         if (!Wrapper.getPlayer().func_184812_l_() && Wrapper.getPlayer().func_184187_bx() instanceof AbstractHorse) {
            target = ElementType.HEALTHMOUNT;
         }

         if (event.getType() == target) {
            ModuleManager.onRender();
            GL11.glPushMatrix();
            UIRenderer.renderAndUpdateFrames();
            GL11.glPopMatrix();
            TurokTessellator.releaseGL();
         }

      }
   }

   @SubscribeEvent(
      priority = EventPriority.NORMAL,
      receiveCanceled = true
   )
   public void onKeyInput(KeyInputEvent event) {
      if (Keyboard.getEventKeyState()) {
         ModuleManager.onBind(Keyboard.getEventKey());
      }

   }

   @SubscribeEvent(
      priority = EventPriority.HIGHEST
   )
   public void onPlayerDrawn(net.minecraftforge.client.event.RenderPlayerEvent.Pre event) {
      TurokMod.EVENT_BUS.post(event);
   }

   @SubscribeEvent(
      priority = EventPriority.HIGHEST
   )
   public void onPlayerDrawn(net.minecraftforge.client.event.RenderPlayerEvent.Post event) {
      TurokMod.EVENT_BUS.post(event);
   }

   @SubscribeEvent
   public void onChunkLoaded(Load event) {
      TurokMod.EVENT_BUS.post(event);
   }

   @SubscribeEvent
   public void onChunkLoaded(Unload event) {
      TurokMod.EVENT_BUS.post(event);
   }

   @SubscribeEvent
   public void onInputUpdate(InputUpdateEvent event) {
      TurokMod.EVENT_BUS.post(event);
   }

   @SubscribeEvent
   public void onLivingEntityUseItemEventTick(Start entityUseItemEvent) {
      TurokMod.EVENT_BUS.post(entityUseItemEvent);
   }

   @SubscribeEvent
   public void onLivingDamageEvent(LivingDamageEvent event) {
      TurokMod.EVENT_BUS.post(event);
   }

   @SubscribeEvent
   public void onEntityJoinWorldEvent(EntityJoinWorldEvent entityJoinWorldEvent) {
      TurokMod.EVENT_BUS.post(entityJoinWorldEvent);
   }

   @SubscribeEvent
   public void onPlayerPush(PlayerSPPushOutOfBlocksEvent event) {
      TurokMod.EVENT_BUS.post(event);
   }

   @SubscribeEvent
   public void onLeftClickBlock(LeftClickBlock event) {
      TurokMod.EVENT_BUS.post(event);
   }

   @SubscribeEvent
   public void onAttackEntity(AttackEntityEvent entityEvent) {
      TurokMod.EVENT_BUS.post(entityEvent);
   }

   @SubscribeEvent
   public void onRenderBlockOverlay(RenderBlockOverlayEvent event) {
      TurokMod.EVENT_BUS.post(event);
   }
}
