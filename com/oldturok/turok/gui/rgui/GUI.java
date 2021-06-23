package com.oldturok.turok.gui.rgui;

import com.oldturok.turok.gui.rgui.component.Component;
import com.oldturok.turok.gui.rgui.component.container.AbstractContainer;
import com.oldturok.turok.gui.rgui.component.container.Container;
import com.oldturok.turok.gui.rgui.component.listen.KeyListener;
import com.oldturok.turok.gui.rgui.component.listen.MouseListener;
import com.oldturok.turok.gui.rgui.render.theme.Theme;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public abstract class GUI extends AbstractContainer {
   Component focus = null;
   boolean press = false;
   int x = 0;
   int y = 0;
   int button = 0;
   int mx = 0;
   int my = 0;
   long lastMS = System.currentTimeMillis();

   public GUI(Theme theme) {
      super(theme);
   }

   public abstract void initializeGUI();

   public abstract void destroyGUI();

   public void updateGUI() {
      this.catchMouse();
      this.catchKey();
   }

   public void handleKeyDown(int key) {
      if (this.focus != null) {
         this.focus.getTheme().getUIForComponent(this.focus).handleKeyDown(this.focus, key);
         ArrayList<Component> l = new ArrayList();

         for(Object p = this.focus; p != null; p = ((Component)p).getParent()) {
            l.add(0, p);
         }

         KeyListener.KeyEvent event = new KeyListener.KeyEvent(key);
         Iterator var5 = l.iterator();

         while(var5.hasNext()) {
            Component a = (Component)var5.next();
            a.getKeyListeners().forEach((keyListener) -> {
               keyListener.onKeyDown(event);
            });
         }

      }
   }

   public void handleKeyUp(int key) {
      if (this.focus != null) {
         this.focus.getTheme().getUIForComponent(this.focus).handleKeyUp(this.focus, key);
         ArrayList<Component> l = new ArrayList();

         for(Object p = this.focus; p != null; p = ((Component)p).getParent()) {
            l.add(0, p);
         }

         KeyListener.KeyEvent event = new KeyListener.KeyEvent(key);
         Iterator var5 = l.iterator();

         while(var5.hasNext()) {
            Component a = (Component)var5.next();
            a.getKeyListeners().forEach((keyListener) -> {
               keyListener.onKeyUp(event);
            });
         }

      }
   }

   public void catchKey() {
      if (this.focus != null) {
         while(Keyboard.next()) {
            if (Keyboard.getEventKeyState()) {
               this.handleKeyDown(Keyboard.getEventKey());
            } else {
               this.handleKeyUp(Keyboard.getEventKey());
            }
         }

      }
   }

   public void handleMouseDown(int x, int y) {
      Component c = this.getComponentAt(x, y);
      int[] real = calculateRealPosition(c);
      if (this.focus != null) {
         this.focus.setFocussed(false);
      }

      this.focus = c;
      if (!c.equals(this)) {
         Object upperParent;
         for(upperParent = c; !this.hasChild((Component)upperParent); upperParent = ((Component)upperParent).getParent()) {
         }

         this.children.remove(upperParent);
         this.children.add(upperParent);
         Collections.sort(this.children, new Comparator<Component>() {
            public int compare(Component o1, Component o2) {
               return o1.getPriority() - o2.getPriority();
            }
         });
      }

      this.focus.setFocussed(true);
      this.press = true;
      this.x = x;
      this.y = y;
      this.button = Mouse.getEventButton();
      this.getTheme().getUIForComponent(c).handleMouseDown(c, x - real[0], y - real[1], Mouse.getEventButton());
      ArrayList<Component> l = new ArrayList();

      for(Object p = this.focus; p != null; p = ((Component)p).getParent()) {
         l.add(0, p);
      }

      MouseListener.MouseButtonEvent event = new MouseListener.MouseButtonEvent(x, y, this.button, this.focus);
      Iterator var10 = l.iterator();

      while(var10.hasNext()) {
         Component a = (Component)var10.next();
         event.setX(event.getX() - a.getX());
         event.setY(event.getY() - a.getY());
         if (a instanceof Container) {
            event.setX(event.getX() - ((Container)a).getOriginOffsetX());
            event.setY(event.getY() - ((Container)a).getOriginOffsetY());
         }

         a.getMouseListeners().forEach((listener) -> {
            listener.onMouseDown(event);
         });
         if (event.isCancelled()) {
            break;
         }
      }

   }

   public void handleMouseRelease(int x, int y) {
      int button = Mouse.getEventButton();
      if (this.focus != null && button != -1) {
         int[] real = calculateRealPosition(this.focus);
         this.getTheme().getUIForComponent(this.focus).handleMouseRelease(this.focus, x - real[0], y - real[1], button);
         ArrayList<Component> l = new ArrayList();

         for(Object p = this.focus; p != null; p = ((Component)p).getParent()) {
            l.add(0, p);
         }

         MouseListener.MouseButtonEvent event = new MouseListener.MouseButtonEvent(x, y, button, this.focus);
         Iterator var16 = l.iterator();

         while(var16.hasNext()) {
            Component a = (Component)var16.next();
            event.setX(event.getX() - a.getX());
            event.setY(event.getY() - a.getY());
            if (a instanceof Container) {
               event.setX(event.getX() - ((Container)a).getOriginOffsetX());
               event.setY(event.getY() - ((Container)a).getOriginOffsetY());
            }

            a.getMouseListeners().forEach((listener) -> {
               listener.onMouseRelease(event);
            });
            if (event.isCancelled()) {
               break;
            }
         }

         this.press = false;
      } else {
         if (button != -1) {
            Component c = this.getComponentAt(x, y);
            int[] real = calculateRealPosition(c);
            this.getTheme().getUIForComponent(c).handleMouseRelease(c, x - real[0], y - real[1], button);
            ArrayList<Component> l = new ArrayList();

            for(Object p = c; p != null; p = ((Component)p).getParent()) {
               l.add(0, p);
            }

            MouseListener.MouseButtonEvent event = new MouseListener.MouseButtonEvent(x, y, button, c);
            Iterator var11 = l.iterator();

            while(var11.hasNext()) {
               Component a = (Component)var11.next();
               event.setX(event.getX() - a.getX());
               event.setY(event.getY() - a.getY());
               if (a instanceof Container) {
                  event.setX(event.getX() - ((Container)a).getOriginOffsetX());
                  event.setY(event.getY() - ((Container)a).getOriginOffsetY());
               }

               a.getMouseListeners().forEach((listener) -> {
                  listener.onMouseRelease(event);
               });
               if (event.isCancelled()) {
                  break;
               }
            }

            this.press = false;
         }

      }
   }

   public void handleWheel(int x, int y, int step) {
      if (step != 0) {
         Component c = this.getComponentAt(x, y);
         int[] real = calculateRealPosition(c);
         this.getTheme().getUIForComponent(c).handleScroll(c, x - real[0], y - real[1], step, step > 0);
         ArrayList<Component> l = new ArrayList();

         for(Object p = c; p != null; p = ((Component)p).getParent()) {
            l.add(0, p);
         }

         MouseListener.MouseScrollEvent event = new MouseListener.MouseScrollEvent(x, y, step > 0, c);
         Iterator var12 = l.iterator();

         while(var12.hasNext()) {
            Component a = (Component)var12.next();
            event.setX(event.getX() - a.getX());
            event.setY(event.getY() - a.getY());
            if (a instanceof Container) {
               event.setX(event.getX() - ((Container)a).getOriginOffsetX());
               event.setY(event.getY() - ((Container)a).getOriginOffsetY());
            }

            a.getMouseListeners().forEach((listener) -> {
               listener.onScroll(event);
            });
            if (event.isCancelled()) {
               break;
            }
         }

      }
   }

   public void handleMouseDrag(int x, int y) {
      int[] real = calculateRealPosition(this.focus);
      int ex = x - real[0];
      int ey = y - real[1];
      this.getTheme().getUIForComponent(this.focus).handleMouseDrag(this.focus, ex, ey, this.button);
      ArrayList<Component> l = new ArrayList();

      for(Object p = this.focus; p != null; p = ((Component)p).getParent()) {
         l.add(0, p);
      }

      MouseListener.MouseButtonEvent event = new MouseListener.MouseButtonEvent(x, y, this.button, this.focus);
      Iterator var9 = l.iterator();

      while(var9.hasNext()) {
         Component a = (Component)var9.next();
         event.setX(event.getX() - a.getX());
         event.setY(event.getY() - a.getY());
         if (a instanceof Container) {
            event.setX(event.getX() - ((Container)a).getOriginOffsetX());
            event.setY(event.getY() - ((Container)a).getOriginOffsetY());
         }

         a.getMouseListeners().forEach((listener) -> {
            listener.onMouseDrag(event);
         });
         if (event.isCancelled()) {
            break;
         }
      }

   }

   private void catchMouse() {
      while(Mouse.next()) {
         int x = Mouse.getX();
         int y = Mouse.getY();
         y = Display.getHeight() - y;
         if (this.press && this.focus != null && (this.x != x || this.y != y)) {
            this.handleMouseDrag(x, y);
         }

         if (Mouse.getEventButtonState()) {
            this.handleMouseDown(x, y);
         } else {
            this.handleMouseRelease(x, y);
         }

         if (Mouse.hasWheel()) {
            this.handleWheel(x, y, Mouse.getDWheel());
         }
      }

   }

   public void callTick(Container container) {
      container.getTickListeners().forEach((tickListener) -> {
         tickListener.onTick();
      });
      Iterator var2 = container.getChildren().iterator();

      while(var2.hasNext()) {
         Component c = (Component)var2.next();
         if (c instanceof Container) {
            this.callTick((Container)c);
         } else {
            c.getTickListeners().forEach((tickListener) -> {
               tickListener.onTick();
            });
         }
      }

   }

   public void update() {
      if (System.currentTimeMillis() - this.lastMS > 50L) {
         this.callTick(this);
         this.lastMS = System.currentTimeMillis();
      }

   }

   public void drawGUI() {
      this.renderChildren();
   }

   public Component getFocus() {
      return this.focus;
   }

   public static int[] calculateRealPosition(Component c) {
      int realX = c.getX();
      int realY = c.getY();
      if (c instanceof Container) {
         realX += ((Container)c).getOriginOffsetX();
         realY += ((Container)c).getOriginOffsetY();
      }

      for(Container parent = c.getParent(); parent != null; parent = parent.getParent()) {
         realX += parent.getX();
         realY += parent.getY();
         if (parent instanceof Container) {
            realX += ((Container)parent).getOriginOffsetX();
            realY += ((Container)parent).getOriginOffsetY();
         }
      }

      return new int[]{realX, realY};
   }
}
