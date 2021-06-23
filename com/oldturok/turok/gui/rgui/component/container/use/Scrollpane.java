package com.oldturok.turok.gui.rgui.component.container.use;

import com.oldturok.turok.gui.rgui.GUI;
import com.oldturok.turok.gui.rgui.component.Component;
import com.oldturok.turok.gui.rgui.component.container.Container;
import com.oldturok.turok.gui.rgui.component.container.OrganisedContainer;
import com.oldturok.turok.gui.rgui.component.listen.MouseListener;
import com.oldturok.turok.gui.rgui.component.listen.RenderListener;
import com.oldturok.turok.gui.rgui.component.listen.UpdateListener;
import com.oldturok.turok.gui.rgui.layout.Layout;
import com.oldturok.turok.gui.rgui.render.theme.Theme;
import com.oldturok.turok.gui.turok.DisplayGuiScreen;
import java.util.Iterator;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

public class Scrollpane extends OrganisedContainer {
   int scrolledX;
   int maxScrollX;
   int scrolledY;
   int maxScrollY;
   boolean doScrollX = false;
   boolean doScrollY = true;
   boolean canScrollX = false;
   boolean canScrollY = false;
   boolean lockWidth = false;
   boolean lockHeight = false;
   int step = 22;

   public Scrollpane(Theme theme, Layout layout, int width, int height) {
      super(theme, layout);
      this.setWidth(width);
      this.setHeight(height);
      this.scrolledX = 0;
      this.scrolledY = 0;
      this.addRenderListener(new RenderListener() {
         int translatex;
         int translatey;

         public void onPreRender() {
            this.translatex = Scrollpane.this.scrolledX;
            this.translatey = Scrollpane.this.scrolledY;
            int[] real = GUI.calculateRealPosition(Scrollpane.this);
            int scale = DisplayGuiScreen.getScale();
            GL11.glScissor(Scrollpane.this.getX() * scale + real[0] * scale - Scrollpane.this.getParent().getOriginOffsetX() - 1, Display.getHeight() - Scrollpane.this.getHeight() * scale - real[1] * scale - 1, Scrollpane.this.getWidth() * scale + Scrollpane.this.getParent().getOriginOffsetX() * scale + 1, Scrollpane.this.getHeight() * scale + 1);
            GL11.glEnable(3089);
         }

         public void onPostRender() {
            GL11.glDisable(3089);
         }
      });
      this.addMouseListener(new MouseListener() {
         public void onMouseDown(MouseListener.MouseButtonEvent event) {
            if (event.getY() > Scrollpane.this.getHeight() || event.getX() > Scrollpane.this.getWidth() || event.getX() < 0 || event.getY() < 0) {
               event.cancel();
            }

         }

         public void onMouseRelease(MouseListener.MouseButtonEvent event) {
         }

         public void onMouseDrag(MouseListener.MouseButtonEvent event) {
         }

         public void onMouseMove(MouseListener.MouseMoveEvent event) {
         }

         public void onScroll(MouseListener.MouseScrollEvent event) {
            if (Scrollpane.this.canScrollY() && (!Scrollpane.this.canScrollX() || Scrollpane.this.scrolledX == 0 || !Scrollpane.this.isDoScrollX()) && Scrollpane.this.isDoScrollY()) {
               if (event.isUp() && Scrollpane.this.getScrolledY() > 0) {
                  Scrollpane.this.setScrolledY(Math.max(0, Scrollpane.this.getScrolledY() - Scrollpane.this.step));
                  return;
               }

               if (!event.isUp() && Scrollpane.this.getScrolledY() < Scrollpane.this.getMaxScrollY()) {
                  Scrollpane.this.setScrolledY(Math.min(Scrollpane.this.getMaxScrollY(), Scrollpane.this.getScrolledY() + Scrollpane.this.step));
                  return;
               }
            }

            if (Scrollpane.this.canScrollX() && Scrollpane.this.isDoScrollX()) {
               if (event.isUp() && Scrollpane.this.getScrolledX() > 0) {
                  Scrollpane.this.setScrolledX(Math.max(0, Scrollpane.this.getScrolledX() - Scrollpane.this.step));
                  return;
               }

               if (!event.isUp() && Scrollpane.this.getScrolledX() < Scrollpane.this.getMaxScrollX()) {
                  Scrollpane.this.setScrolledX(Math.min(Scrollpane.this.getMaxScrollX(), Scrollpane.this.getScrolledX() + Scrollpane.this.step));
                  return;
               }
            }

         }
      });
      this.addUpdateListener(new UpdateListener() {
         public void updateSize(Component component, int oldWidth, int oldHeight) {
            Scrollpane.this.performCalculations();
         }

         public void updateLocation(Component component, int oldX, int oldY) {
            Scrollpane.this.performCalculations();
         }
      });
   }

   public void setWidth(int width) {
      if (!this.lockWidth) {
         super.setWidth(width);
      }

   }

   public void setHeight(int height) {
      if (!this.lockHeight) {
         super.setHeight(height);
      }

   }

   public Container addChild(Component... component) {
      super.addChild(component);
      this.performCalculations();
      return this;
   }

   private void performCalculations() {
      int farX = 0;
      int farY = 0;

      Component c;
      for(Iterator var3 = this.getChildren().iterator(); var3.hasNext(); farY = Math.max(this.getScrolledY() + c.getY() + c.getHeight(), farY)) {
         c = (Component)var3.next();
         farX = Math.max(this.getScrolledX() + c.getX() + c.getWidth(), farX);
      }

      this.canScrollX = farX > this.getWidth();
      this.maxScrollX = farX - this.getWidth();
      this.canScrollY = farY > this.getHeight();
      this.maxScrollY = farY - this.getHeight();
   }

   public boolean canScrollX() {
      return this.canScrollX;
   }

   public boolean canScrollY() {
      return this.canScrollY;
   }

   public boolean isDoScrollX() {
      return this.doScrollX;
   }

   public boolean isDoScrollY() {
      return this.doScrollY;
   }

   public void setDoScrollY(boolean doScrollY) {
      this.doScrollY = doScrollY;
   }

   public void setDoScrollX(boolean doScrollX) {
      this.doScrollX = doScrollX;
   }

   public void setScrolledX(int scrolledX) {
      int a = this.getScrolledX();
      this.scrolledX = scrolledX;
      int dif = this.getScrolledX() - a;
      Iterator var4 = this.getChildren().iterator();

      while(var4.hasNext()) {
         Component component = (Component)var4.next();
         component.setX(component.getX() - dif);
      }

   }

   public void setScrolledY(int scrolledY) {
      int a = this.getScrolledY();
      this.scrolledY = scrolledY;
      int dif = this.getScrolledY() - a;
      Iterator var4 = this.getChildren().iterator();

      while(var4.hasNext()) {
         Component component = (Component)var4.next();
         component.setY(component.getY() - dif);
      }

   }

   public int getScrolledX() {
      return this.scrolledX;
   }

   public int getScrolledY() {
      return this.scrolledY;
   }

   public int getMaxScrollX() {
      return this.maxScrollX;
   }

   public int getMaxScrollY() {
      return this.maxScrollY;
   }

   public Scrollpane setLockHeight(boolean lockHeight) {
      this.lockHeight = lockHeight;
      return this;
   }

   public Scrollpane setLockWidth(boolean lockWidth) {
      this.lockWidth = lockWidth;
      return this;
   }

   public boolean penetrateTest(int x, int y) {
      return x > 0 && x < this.getWidth() && y > 0 && y < this.getHeight();
   }
}
