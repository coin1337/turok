package com.oldturok.turok.gui.turok.widgets;

import com.oldturok.turok.gui.rgui.component.AlignedComponent;
import com.oldturok.turok.gui.rgui.component.Component;
import com.oldturok.turok.gui.rgui.component.container.Container;
import com.oldturok.turok.gui.rgui.component.container.use.Frame;
import com.oldturok.turok.gui.rgui.component.listen.MouseListener;
import com.oldturok.turok.gui.rgui.component.listen.UpdateListener;
import com.oldturok.turok.gui.rgui.render.AbstractComponentUI;
import com.oldturok.turok.gui.rgui.render.font.FontRenderer;
import com.oldturok.turok.gui.rgui.util.ContainerHelper;
import com.oldturok.turok.gui.rgui.util.Docking;
import com.oldturok.turok.gui.turok.DisplayGuiScreen;
import com.oldturok.turok.gui.turok.RenderHelper;
import com.oldturok.turok.gui.turok.RootFontRenderer;
import com.oldturok.turok.gui.turok.TurokGUI;
import com.oldturok.turok.util.ColourHolder;
import com.oldturok.turok.util.TurokColor;
import com.oldturok.turok.util.TurokGL;
import com.oldturok.turok.util.Wrapper;

public class WidgetModuleFrame<T extends Frame> extends AbstractComponentUI<Frame> {
   ColourHolder frameColour;
   ColourHolder outlineColour;
   Component yLineComponent;
   Component xLineComponent;
   Component centerXComponent;
   Component centerYComponent;
   boolean centerX;
   boolean centerY;
   boolean effect_pinned_one;
   Boolean effect_pinned_r;
   boolean effect_module_one;
   Boolean effect_module_r;
   public static boolean effect = true;
   public static float color_pinned_r = 105.0F;
   public static int color_module_r = 105;
   public static int speed_effect = 1;
   private static final RootFontRenderer ff = new RootFontRenderer(0.9F);

   public WidgetModuleFrame() {
      this.frameColour = TurokGUI.primaryColour.setA(100);
      this.outlineColour = this.frameColour.darker();
      this.yLineComponent = null;
      this.xLineComponent = null;
      this.centerXComponent = null;
      this.centerYComponent = null;
      this.centerX = false;
      this.centerY = false;
      this.effect_pinned_one = true;
      this.effect_pinned_r = false;
      this.effect_module_one = true;
      this.effect_module_r = false;
   }

   public void renderComponent(Frame component, FontRenderer fontRenderer) {
      if (component.getOpacity() != 0.0F) {
         if (this.effect_pinned_r) {
            color_pinned_r += 0.25F;
         } else {
            color_pinned_r -= 0.25F;
         }

         if (color_pinned_r >= 150.0F) {
            this.effect_pinned_r = false;
         }

         if (color_pinned_r <= 50.0F) {
            this.effect_pinned_r = true;
         }

         TurokGL.DisableGL(3553);
         if (fontRenderer.getStringWidth(component.getTitle()) > component.getWidth()) {
            component.setWidth(fontRenderer.getStringWidth(component.getTitle()) + 1);
         }

         if (component.isPinneable()) {
            this.draw_pinned(component);
         } else {
            this.draw_module(component, fontRenderer);
         }

         TurokGL.FixRefreshColor();
      }
   }

   public void draw_module(Frame component, FontRenderer fontRenderer) {
      if (this.effect_module_one) {
         if (this.effect_module_r) {
            ++color_module_r;
         } else {
            --color_module_r;
         }

         if (color_module_r >= 255) {
            this.effect_module_r = false;
         }

         if (color_module_r <= 105) {
            this.effect_module_r = true;
         }
      }

      TurokGL.refresh_color(0.0F, 0.0F, 0.0F, 150.0F);
      RenderHelper.drawFilledRectangle(0.0F, 0.0F, (float)component.getWidth(), (float)component.getHeight());
      TurokGL.refresh_color(0.0F, 0.0F, 0.0F, 255.0F);
      RenderHelper.drawFilledRectangle(0.0F, 0.0F, (float)component.getWidth(), (float)(ff.getStringHeight(component.getTitle()) + 2));
      TurokColor color = new TurokColor(color_module_r, 0, 0);
      fontRenderer.drawString(1, 1, color.hex(), component.getTitle());
   }

   public void draw_pinned(Frame component) {
      if (component.isPinned()) {
         TurokGL.refresh_color(effect ? color_pinned_r : 0.0F, 0.0F, 0.0F, 150.0F);
      } else {
         TurokGL.refresh_color(0.0F, 0.0F, 0.0F, 150.0F);
      }

      RenderHelper.drawFilledRectangle(0.0F, 0.0F, (float)component.getWidth(), (float)component.getHeight());
      ff.drawString(1, 1, component.getTitle());
   }

   public void handleMouseRelease(Frame component, int x, int y, int button) {
      this.yLineComponent = null;
      this.xLineComponent = null;
      this.centerXComponent = null;
      this.centerYComponent = null;
   }

   public void handleMouseDrag(Frame component, int x, int y, int button) {
      super.handleMouseDrag(component, x, y, button);
   }

   public void handleAddComponent(final Frame component, Container container) {
      super.handleAddComponent(component, container);
      component.setOriginOffsetY(component.getTheme().getFontRenderer().getFontHeight() + 3);
      component.setOriginOffsetX(3);
      component.addMouseListener(new MouseListener() {
         public void onMouseDown(MouseListener.MouseButtonEvent event) {
            int y = event.getY();
            int x = event.getX();
            if (y < 0 && x < component.getWidth() && x > WidgetModuleFrame.ff.getStringWidth(component.getTitle()) - component.getWidth() && component.isPinneable()) {
               component.setPinned(!component.isPinned());
            }

         }

         public void onMouseRelease(MouseListener.MouseButtonEvent event) {
         }

         public void onMouseDrag(MouseListener.MouseButtonEvent event) {
         }

         public void onMouseMove(MouseListener.MouseMoveEvent event) {
         }

         public void onScroll(MouseListener.MouseScrollEvent event) {
         }
      });
      component.addUpdateListener(new UpdateListener() {
         public void updateSize(Component component, int oldWidth, int oldHeight) {
            if (component instanceof Frame) {
               TurokGUI.dock((Frame)component);
            }

         }

         public void updateLocation(Component component, int oldX, int oldY) {
         }
      });
      component.addPoof(new Frame.FrameDragPoof<Frame, Frame.FrameDragPoof.DragInfo>() {
         public void execute(Frame component, Frame.FrameDragPoof.DragInfo info) {
            int x = info.getX();
            int y = info.getY();
            WidgetModuleFrame.this.yLineComponent = null;
            WidgetModuleFrame.this.xLineComponent = null;
            component.setDocking(Docking.NONE);
            if (x < 5) {
               x = 0;
               ContainerHelper.setAlignment(component, AlignedComponent.Alignment.LEFT);
               component.setDocking(Docking.LEFT);
            }

            int diff = (x + component.getWidth()) * DisplayGuiScreen.getScale() - Wrapper.getMinecraft().field_71443_c;
            if (-diff < 5) {
               x = Wrapper.getMinecraft().field_71443_c / DisplayGuiScreen.getScale() - component.getWidth();
               ContainerHelper.setAlignment(component, AlignedComponent.Alignment.RIGHT);
               component.setDocking(Docking.RIGHT);
            }

            if (y < 5) {
               y = 0;
               if (component.getDocking().equals(Docking.RIGHT)) {
                  component.setDocking(Docking.TOPRIGHT);
               } else if (component.getDocking().equals(Docking.LEFT)) {
                  component.setDocking(Docking.TOPLEFT);
               } else {
                  component.setDocking(Docking.TOP);
               }
            }

            diff = (y + component.getHeight()) * DisplayGuiScreen.getScale() - Wrapper.getMinecraft().field_71440_d;
            if (-diff < 5) {
               y = Wrapper.getMinecraft().field_71440_d / DisplayGuiScreen.getScale() - component.getHeight();
               if (component.getDocking().equals(Docking.RIGHT)) {
                  component.setDocking(Docking.BOTTOMRIGHT);
               } else if (component.getDocking().equals(Docking.LEFT)) {
                  component.setDocking(Docking.BOTTOMLEFT);
               } else {
                  component.setDocking(Docking.BOTTOM);
               }
            }

            if (Math.abs((x + component.getWidth() / 2) * DisplayGuiScreen.getScale() * 2 - Wrapper.getMinecraft().field_71443_c) < 5) {
               WidgetModuleFrame.this.xLineComponent = null;
               WidgetModuleFrame.this.centerXComponent = component;
               WidgetModuleFrame.this.centerX = true;
               x = Wrapper.getMinecraft().field_71443_c / (DisplayGuiScreen.getScale() * 2) - component.getWidth() / 2;
               if (component.getDocking().isTop()) {
                  component.setDocking(Docking.CENTERTOP);
               } else if (component.getDocking().isBottom()) {
                  component.setDocking(Docking.CENTERBOTTOM);
               } else {
                  component.setDocking(Docking.CENTERVERTICAL);
               }

               ContainerHelper.setAlignment(component, AlignedComponent.Alignment.CENTER);
            } else {
               WidgetModuleFrame.this.centerX = false;
            }

            if (Math.abs((y + component.getHeight() / 2) * DisplayGuiScreen.getScale() * 2 - Wrapper.getMinecraft().field_71440_d) < 5) {
               WidgetModuleFrame.this.yLineComponent = null;
               WidgetModuleFrame.this.centerYComponent = component;
               WidgetModuleFrame.this.centerY = true;
               y = Wrapper.getMinecraft().field_71440_d / (DisplayGuiScreen.getScale() * 2) - component.getHeight() / 2;
               if (component.getDocking().isLeft()) {
                  component.setDocking(Docking.CENTERLEFT);
               } else if (component.getDocking().isRight()) {
                  component.setDocking(Docking.CENTERRIGHT);
               } else if (component.getDocking().isCenterHorizontal()) {
                  component.setDocking(Docking.CENTER);
               } else {
                  component.setDocking(Docking.CENTERHOIZONTAL);
               }
            } else {
               WidgetModuleFrame.this.centerY = false;
            }

            info.setX(x);
            info.setY(y);
         }
      });
   }
}
