package com.oldturok.turok.gui;

import com.oldturok.turok.TurokMod;
import com.oldturok.turok.gui.rgui.component.Component;
import com.oldturok.turok.gui.rgui.component.container.use.Frame;
import com.oldturok.turok.gui.rgui.component.listen.RenderListener;
import com.oldturok.turok.gui.turok.DisplayGuiScreen;
import com.oldturok.turok.gui.turok.TurokGUI;
import com.oldturok.turok.util.Wrapper;
import java.util.Iterator;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

public class UIRenderer {
   public static void renderAndUpdateFrames() {
      if (!(Wrapper.getMinecraft().field_71462_r instanceof DisplayGuiScreen) && !Wrapper.getMinecraft().field_71474_y.field_74330_P) {
         TurokGUI gui = TurokMod.get_instance().get_gui_manager();
         GL11.glDisable(3553);
         Iterator var1 = gui.getChildren().iterator();

         while(var1.hasNext()) {
            Component c = (Component)var1.next();
            if (c instanceof Frame) {
               GlStateManager.func_179094_E();
               Frame child = (Frame)c;
               if (child.isPinned() && child.isVisible()) {
                  boolean slide = child.getOpacity() != 0.0F;
                  GL11.glTranslated((double)child.getX(), (double)child.getY(), 0.0D);
                  child.getRenderListeners().forEach((renderListener) -> {
                     renderListener.onPreRender();
                  });
                  child.getTheme().getUIForComponent(child).renderComponent(child, child.getTheme().getFontRenderer());
                  int translateX = 0;
                  int translateY = 0;
                  if (slide) {
                     translateX += child.getOriginOffsetX();
                     translateY += child.getOriginOffsetY();
                  } else {
                     if (child.getDocking().isBottom()) {
                        translateY += child.getOriginOffsetY();
                     }

                     if (child.getDocking().isRight()) {
                        translateX += child.getOriginOffsetX();
                        if (child.getChildren().size() > 0) {
                           translateX += (child.getWidth() - ((Component)child.getChildren().get(0)).getX() - ((Component)child.getChildren().get(0)).getWidth()) / DisplayGuiScreen.getScale();
                        }
                     }

                     if (child.getDocking().isLeft() && child.getChildren().size() > 0) {
                        translateX -= ((Component)child.getChildren().get(0)).getX();
                     }

                     if (child.getDocking().isTop() && child.getChildren().size() > 0) {
                        translateY -= ((Component)child.getChildren().get(0)).getY();
                     }
                  }

                  GL11.glTranslated((double)translateX, (double)translateY, 0.0D);
                  child.getRenderListeners().forEach(RenderListener::onPostRender);
                  child.renderChildren();
                  GL11.glTranslated((double)(-translateX), (double)(-translateY), 0.0D);
                  GL11.glTranslated((double)(-child.getX()), (double)(-child.getY()), 0.0D);
               }

               GlStateManager.func_179121_F();
            }
         }

         GL11.glEnable(3553);
         GL11.glEnable(3042);
         GlStateManager.func_179147_l();
      }
   }
}
