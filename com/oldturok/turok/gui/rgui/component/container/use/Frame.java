package com.oldturok.turok.gui.rgui.component.container.use;

import com.oldturok.turok.gui.rgui.component.Component;
import com.oldturok.turok.gui.rgui.component.container.OrganisedContainer;
import com.oldturok.turok.gui.rgui.component.listen.MouseListener;
import com.oldturok.turok.gui.rgui.component.listen.RenderListener;
import com.oldturok.turok.gui.rgui.component.listen.UpdateListener;
import com.oldturok.turok.gui.rgui.layout.Layout;
import com.oldturok.turok.gui.rgui.layout.UselessLayout;
import com.oldturok.turok.gui.rgui.poof.PoofInfo;
import com.oldturok.turok.gui.rgui.poof.use.FramePoof;
import com.oldturok.turok.gui.rgui.poof.use.Poof;
import com.oldturok.turok.gui.rgui.render.theme.Theme;
import com.oldturok.turok.gui.rgui.util.Docking;
import com.oldturok.turok.gui.turok.DisplayGuiScreen;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class Frame extends OrganisedContainer {
   String title;
   int trueheight;
   int truemaxheight;
   int dx;
   int dy;
   boolean doDrag;
   boolean startDrag;
   boolean isMinimized;
   boolean isMinimizeable;
   boolean isCloseable;
   boolean isPinned;
   boolean isPinneable;
   boolean isLayoutWorking;
   Docking docking;
   HashMap<Component, Boolean> visibilityMap;

   public Frame(Theme theme, String title) {
      this(theme, new UselessLayout(), title);
   }

   public Frame(Theme theme, final Layout layout, String title) {
      super(theme, layout);
      this.trueheight = 0;
      this.truemaxheight = 0;
      this.dx = 0;
      this.dy = 0;
      this.doDrag = false;
      this.startDrag = false;
      this.isMinimized = false;
      this.isMinimizeable = true;
      this.isCloseable = true;
      this.isPinned = false;
      this.isPinneable = false;
      this.isLayoutWorking = false;
      this.docking = Docking.NONE;
      this.visibilityMap = new HashMap();
      this.title = title;
      this.addPoof(new FramePoof<Frame, FramePoof.FramePoofInfo>() {
         public void execute(Frame component, FramePoof.FramePoofInfo info) {
            switch(info.getAction()) {
            case MINIMIZE:
               if (Frame.this.isMinimizeable) {
                  Frame.this.setMinimized(true);
               }
               break;
            case MAXIMIZE:
               if (Frame.this.isMinimizeable) {
                  Frame.this.setMinimized(false);
               }
               break;
            case CLOSE:
               if (Frame.this.isCloseable) {
                  Frame.this.getParent().removeChild(Frame.this);
               }
            }

         }
      });
      this.addUpdateListener(new UpdateListener() {
         public void updateSize(Component component, int oldWidth, int oldHeight) {
            if (!Frame.this.isLayoutWorking) {
               if (!component.equals(Frame.this)) {
                  Frame.this.isLayoutWorking = true;
                  layout.organiseContainer(Frame.this);
                  Frame.this.isLayoutWorking = false;
               }

            }
         }

         public void updateLocation(Component component, int oldX, int oldY) {
            if (!Frame.this.isLayoutWorking) {
               if (!component.equals(Frame.this)) {
                  Frame.this.isLayoutWorking = true;
                  layout.organiseContainer(Frame.this);
                  Frame.this.isLayoutWorking = false;
               }

            }
         }
      });
      this.addRenderListener(new RenderListener() {
         public void onPreRender() {
            if (Frame.this.startDrag) {
               Frame.FrameDragPoof.DragInfo info = new Frame.FrameDragPoof.DragInfo(DisplayGuiScreen.mouseX - Frame.this.dx, DisplayGuiScreen.mouseY - Frame.this.dy);
               Frame.this.callPoof(Frame.FrameDragPoof.class, info);
               Frame.this.setX(info.getX());
               Frame.this.setY(info.getY());
            }

         }

         public void onPostRender() {
         }
      });
      this.addMouseListener(new Frame.HeterosMouseListener());
   }

   public void setCloseable(boolean closeable) {
      this.isCloseable = closeable;
   }

   public void setMinimizeable(boolean minimizeable) {
      this.isMinimizeable = minimizeable;
   }

   public boolean isMinimizeable() {
      return this.isMinimizeable;
   }

   public boolean isMinimized() {
      return this.isMinimized;
   }

   public void setMinimized(boolean minimized) {
      Iterator var2;
      if (minimized && !this.isMinimized) {
         this.trueheight = this.getHeight();
         this.truemaxheight = this.getMaximumHeight();
         this.setHeight(0);
         this.setMaximumHeight(this.getOriginOffsetY());
         var2 = this.getChildren().iterator();

         while(var2.hasNext()) {
            Component c = (Component)var2.next();
            this.visibilityMap.put(c, c.isVisible());
            c.setVisible(false);
         }
      } else if (!minimized && this.isMinimized) {
         this.setMaximumHeight(this.truemaxheight);
         this.setHeight(this.trueheight - this.getOriginOffsetY());
         var2 = this.visibilityMap.entrySet().iterator();

         while(var2.hasNext()) {
            Entry<Component, Boolean> entry = (Entry)var2.next();
            ((Component)entry.getKey()).setVisible((Boolean)entry.getValue());
         }
      }

      this.isMinimized = minimized;
   }

   public boolean isCloseable() {
      return this.isCloseable;
   }

   public boolean isPinneable() {
      return this.isPinneable;
   }

   public boolean isPinned() {
      return this.isPinned;
   }

   public void setPinneable(boolean pinneable) {
      this.isPinneable = pinneable;
   }

   public void setPinned(boolean pinned) {
      this.isPinned = pinned && this.isPinneable;
   }

   public String getTitle() {
      return this.title;
   }

   public Docking getDocking() {
      return this.docking;
   }

   public void setDocking(Docking docking) {
      this.docking = docking;
   }

   public abstract static class FrameDragPoof<T extends Frame, S extends Frame.FrameDragPoof.DragInfo> extends Poof<T, S> {
      public static class DragInfo extends PoofInfo {
         int x;
         int y;

         public DragInfo(int x, int y) {
            this.x = x;
            this.y = y;
         }

         public int getX() {
            return this.x;
         }

         public int getY() {
            return this.y;
         }

         public void setX(int x) {
            this.x = x;
         }

         public void setY(int y) {
            this.y = y;
         }
      }
   }

   public class HeterosMouseListener implements MouseListener {
      public void onMouseDown(MouseListener.MouseButtonEvent event) {
         Frame.this.dx = event.getX() + Frame.this.getOriginOffsetX();
         Frame.this.dy = event.getY() + Frame.this.getOriginOffsetY();
         if (Frame.this.dy <= Frame.this.getOriginOffsetY() && event.getButton() == 0 && Frame.this.dy > 0) {
            Frame.this.doDrag = true;
         } else {
            Frame.this.doDrag = false;
         }

         if (Frame.this.isMinimized && event.getY() > Frame.this.getOriginOffsetY()) {
            event.cancel();
         }

      }

      public void onMouseRelease(MouseListener.MouseButtonEvent event) {
         Frame.this.doDrag = false;
         Frame.this.startDrag = false;
      }

      public void onMouseDrag(MouseListener.MouseButtonEvent event) {
         if (Frame.this.doDrag) {
            Frame.this.startDrag = true;
         }
      }

      public void onMouseMove(MouseListener.MouseMoveEvent event) {
      }

      public void onScroll(MouseListener.MouseScrollEvent event) {
      }
   }
}
