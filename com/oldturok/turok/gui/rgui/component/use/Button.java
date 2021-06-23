package com.oldturok.turok.gui.rgui.component.use;

import com.oldturok.turok.gui.rgui.component.AbstractComponent;
import com.oldturok.turok.gui.rgui.component.listen.MouseListener;
import com.oldturok.turok.gui.rgui.poof.PoofInfo;
import com.oldturok.turok.gui.rgui.poof.use.Poof;

public class Button extends AbstractComponent {
   private String name;

   public Button(String name) {
      this(name, 0, 0);
      this.addMouseListener(new MouseListener() {
         public void onMouseDown(MouseListener.MouseButtonEvent event) {
            Button.this.callPoof(Button.ButtonPoof.class, new Button.ButtonPoof.ButtonInfo(event.getButton(), event.getX(), event.getY()));
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
   }

   public Button(String name, int x, int y) {
      this.name = name;
      this.setX(x);
      this.setY(y);
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void kill() {
   }

   public abstract static class ButtonPoof<T extends Button, S extends Button.ButtonPoof.ButtonInfo> extends Poof<T, S> {
      Button.ButtonPoof.ButtonInfo info;

      public static class ButtonInfo extends PoofInfo {
         int button;
         int x;
         int y;

         public ButtonInfo(int button, int x, int y) {
            this.button = button;
            this.x = x;
            this.y = y;
         }

         public int getX() {
            return this.x;
         }

         public int getY() {
            return this.y;
         }

         public int getButton() {
            return this.button;
         }
      }
   }
}
