package com.oldturok.turok.gui.rgui.component;

import com.oldturok.turok.gui.rgui.GUI;
import com.oldturok.turok.gui.rgui.component.container.Container;
import com.oldturok.turok.gui.rgui.component.container.use.Frame;
import com.oldturok.turok.gui.rgui.component.listen.KeyListener;
import com.oldturok.turok.gui.rgui.component.listen.MouseListener;
import com.oldturok.turok.gui.rgui.component.listen.RenderListener;
import com.oldturok.turok.gui.rgui.component.listen.TickListener;
import com.oldturok.turok.gui.rgui.component.listen.UpdateListener;
import com.oldturok.turok.gui.rgui.poof.IPoof;
import com.oldturok.turok.gui.rgui.poof.PoofInfo;
import com.oldturok.turok.gui.rgui.render.ComponentUI;
import com.oldturok.turok.gui.rgui.render.theme.Theme;
import com.oldturok.turok.gui.turok.DisplayGuiScreen;
import com.oldturok.turok.setting.Setting;
import com.oldturok.turok.setting.Settings;
import java.util.ArrayList;
import java.util.Iterator;

public abstract class AbstractComponent implements Component {
   int x;
   int y;
   int width;
   int height;
   int minWidth = Integer.MIN_VALUE;
   int minHeight = Integer.MIN_VALUE;
   int maxWidth = Integer.MAX_VALUE;
   int maxHeight = Integer.MAX_VALUE;
   protected int priority = 0;
   private Setting<Boolean> visible = Settings.b("Visible", true);
   float opacity = 1.0F;
   private boolean focus = false;
   ComponentUI ui;
   Theme theme;
   Container parent;
   boolean hover = false;
   boolean press = false;
   boolean drag = false;
   boolean affectlayout = true;
   ArrayList<MouseListener> mouseListeners = new ArrayList();
   ArrayList<RenderListener> renderListeners = new ArrayList();
   ArrayList<KeyListener> keyListeners = new ArrayList();
   ArrayList<UpdateListener> updateListeners = new ArrayList();
   ArrayList<TickListener> tickListeners = new ArrayList();
   ArrayList<IPoof> poofs = new ArrayList();
   boolean workingy = false;
   boolean workingx = false;

   public AbstractComponent() {
      this.addMouseListener(new MouseListener() {
         public void onMouseDown(MouseListener.MouseButtonEvent event) {
            AbstractComponent.this.press = true;
         }

         public void onMouseRelease(MouseListener.MouseButtonEvent event) {
            AbstractComponent.this.press = false;
            AbstractComponent.this.drag = false;
         }

         public void onMouseDrag(MouseListener.MouseButtonEvent event) {
            AbstractComponent.this.drag = true;
         }

         public void onMouseMove(MouseListener.MouseMoveEvent event) {
         }

         public void onScroll(MouseListener.MouseScrollEvent event) {
         }
      });
   }

   public ComponentUI getUI() {
      if (this.ui == null) {
         this.ui = this.getTheme().getUIForComponent(this);
      }

      return this.ui;
   }

   public Container getParent() {
      return this.parent;
   }

   public void setParent(Container parent) {
      this.parent = parent;
   }

   public Theme getTheme() {
      return this.theme;
   }

   public void setTheme(Theme theme) {
      this.theme = theme;
   }

   public void setFocussed(boolean focus) {
      this.focus = focus;
   }

   public boolean isFocussed() {
      return this.focus;
   }

   public int getX() {
      return this.x;
   }

   public int getY() {
      return this.y;
   }

   public int getWidth() {
      return this.width;
   }

   public int getHeight() {
      return this.height;
   }

   public void setY(int y) {
      int oldX = this.getX();
      int oldY = this.getY();
      this.y = y;
      if (!this.workingy) {
         this.workingy = true;
         this.getUpdateListeners().forEach((listener) -> {
            listener.updateLocation(this, oldX, oldY);
         });
         if (this.getParent() != null) {
            this.getParent().getUpdateListeners().forEach((listener) -> {
               listener.updateLocation(this, oldX, oldY);
            });
         }

         this.workingy = false;
      }

   }

   public void setX(int x) {
      int oldX = this.getX();
      int oldY = this.getY();
      this.x = x;
      if (!this.workingx) {
         this.workingx = true;
         this.getUpdateListeners().forEach((listener) -> {
            listener.updateLocation(this, oldX, oldY);
         });
         if (this.getParent() != null) {
            this.getParent().getUpdateListeners().forEach((listener) -> {
               listener.updateLocation(this, oldX, oldY);
            });
         }

         this.workingx = false;
      }

   }

   public void setWidth(int width) {
      width = Math.max(this.getMinimumWidth(), Math.min(width, this.getMaximumWidth()));
      int oldWidth = this.getWidth();
      int oldHeight = this.getHeight();
      this.width = width;
      this.getUpdateListeners().forEach((listener) -> {
         listener.updateSize(this, oldWidth, oldHeight);
      });
      if (this.getParent() != null) {
         this.getParent().getUpdateListeners().forEach((listener) -> {
            listener.updateSize(this, oldWidth, oldHeight);
         });
      }

   }

   public void setHeight(int height) {
      height = Math.max(this.getMinimumHeight(), Math.min(height, this.getMaximumHeight()));
      int oldWidth = this.getWidth();
      int oldHeight = this.getHeight();
      this.height = height;
      this.getUpdateListeners().forEach((listener) -> {
         listener.updateSize(this, oldWidth, oldHeight);
      });
      if (this.getParent() != null) {
         this.getParent().getUpdateListeners().forEach((listener) -> {
            listener.updateSize(this, oldWidth, oldHeight);
         });
      }

   }

   public void embedFrame(Frame frame) {
      frame.setHeight(500);
   }

   public boolean isVisible() {
      return (Boolean)this.visible.getValue();
   }

   public void setVisible(boolean visible) {
      this.visible.setValue(visible);
   }

   public int getPriority() {
      return this.priority;
   }

   public void kill() {
      this.setVisible(false);
   }

   private boolean isMouseOver() {
      int[] real = GUI.calculateRealPosition(this);
      int mx = DisplayGuiScreen.mouseX;
      int my = DisplayGuiScreen.mouseY;
      return real[0] <= mx && real[1] <= my && real[0] + this.getWidth() >= mx && real[1] + this.getHeight() >= my;
   }

   public boolean isHovered() {
      return this.isMouseOver() && !this.press;
   }

   public boolean isPressed() {
      return this.press;
   }

   public ArrayList<MouseListener> getMouseListeners() {
      return this.mouseListeners;
   }

   public void addMouseListener(MouseListener listener) {
      if (!this.mouseListeners.contains(listener)) {
         this.mouseListeners.add(listener);
      }

   }

   public ArrayList<RenderListener> getRenderListeners() {
      return this.renderListeners;
   }

   public void addRenderListener(RenderListener listener) {
      if (!this.renderListeners.contains(listener)) {
         this.renderListeners.add(listener);
      }

   }

   public ArrayList<KeyListener> getKeyListeners() {
      return this.keyListeners;
   }

   public void addKeyListener(KeyListener listener) {
      if (!this.keyListeners.contains(listener)) {
         this.keyListeners.add(listener);
      }

   }

   public ArrayList<UpdateListener> getUpdateListeners() {
      return this.updateListeners;
   }

   public void addUpdateListener(UpdateListener listener) {
      if (!this.updateListeners.contains(listener)) {
         this.updateListeners.add(listener);
      }

   }

   public ArrayList<TickListener> getTickListeners() {
      return this.tickListeners;
   }

   public void addTickListener(TickListener listener) {
      if (!this.tickListeners.contains(listener)) {
         this.tickListeners.add(listener);
      }

   }

   public void addPoof(IPoof poof) {
      this.poofs.add(poof);
   }

   public void callPoof(Class<? extends IPoof> target, PoofInfo info) {
      Iterator var3 = this.poofs.iterator();

      while(var3.hasNext()) {
         IPoof poof = (IPoof)var3.next();
         if (target.isAssignableFrom(poof.getClass()) && poof.getComponentClass().isAssignableFrom(this.getClass())) {
            poof.execute(this, info);
         }
      }

   }

   public boolean liesIn(Component container) {
      if (container.equals(this)) {
         return true;
      } else if (container instanceof Container) {
         Iterator var2 = ((Container)container).getChildren().iterator();

         boolean liesin;
         do {
            if (!var2.hasNext()) {
               return false;
            }

            Component component = (Component)var2.next();
            if (component.equals(this)) {
               return true;
            }

            liesin = false;
            if (component instanceof Container) {
               liesin = this.liesIn((Container)component);
            }
         } while(!liesin);

         return true;
      } else {
         return false;
      }
   }

   public float getOpacity() {
      return this.opacity;
   }

   public void setOpacity(float opacity) {
      this.opacity = opacity;
   }

   public int getMaximumHeight() {
      return this.maxHeight;
   }

   public int getMaximumWidth() {
      return this.maxWidth;
   }

   public int getMinimumHeight() {
      return this.minHeight;
   }

   public int getMinimumWidth() {
      return this.minWidth;
   }

   public Component setMaximumWidth(int width) {
      this.maxWidth = width;
      return this;
   }

   public Component setMaximumHeight(int height) {
      this.maxHeight = height;
      return this;
   }

   public Component setMinimumWidth(int width) {
      this.minWidth = width;
      return this;
   }

   public Component setMinimumHeight(int height) {
      this.minHeight = height;
      return this;
   }

   public boolean doAffectLayout() {
      return this.affectlayout;
   }

   public void setAffectLayout(boolean flag) {
      this.affectlayout = flag;
   }
}
