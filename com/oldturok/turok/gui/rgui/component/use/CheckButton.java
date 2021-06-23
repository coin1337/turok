package com.oldturok.turok.gui.rgui.component.use;

import com.oldturok.turok.gui.rgui.component.listen.MouseListener;
import com.oldturok.turok.gui.rgui.poof.PoofInfo;
import com.oldturok.turok.gui.rgui.poof.use.Poof;

public class CheckButton extends Button {
   boolean toggled;

   public CheckButton(String name) {
      this(name, 0, 0);
   }

   public CheckButton(String name, int x, int y) {
      super(name, x, y);
      this.addMouseListener(new MouseListener() {
         public void onMouseDown(MouseListener.MouseButtonEvent event) {
            if (event.getButton() == 0) {
               CheckButton.this.toggled = !CheckButton.this.toggled;
               CheckButton.this.callPoof(CheckButton.CheckButtonPoof.class, new CheckButton.CheckButtonPoof.CheckButtonPoofInfo(CheckButton.CheckButtonPoof.CheckButtonPoofInfo.CheckButtonPoofInfoAction.TOGGLE));
               if (CheckButton.this.toggled) {
                  CheckButton.this.callPoof(CheckButton.CheckButtonPoof.class, new CheckButton.CheckButtonPoof.CheckButtonPoofInfo(CheckButton.CheckButtonPoof.CheckButtonPoofInfo.CheckButtonPoofInfoAction.ENABLE));
               } else {
                  CheckButton.this.callPoof(CheckButton.CheckButtonPoof.class, new CheckButton.CheckButtonPoof.CheckButtonPoofInfo(CheckButton.CheckButtonPoof.CheckButtonPoofInfo.CheckButtonPoofInfoAction.DISABLE));
               }

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
   }

   public void setToggled(boolean toggled) {
      this.toggled = toggled;
   }

   public boolean isToggled() {
      return this.toggled;
   }

   public abstract static class CheckButtonPoof<T extends CheckButton, S extends CheckButton.CheckButtonPoof.CheckButtonPoofInfo> extends Poof<T, S> {
      CheckButton.CheckButtonPoof.CheckButtonPoofInfo info;

      public static class CheckButtonPoofInfo extends PoofInfo {
         CheckButton.CheckButtonPoof.CheckButtonPoofInfo.CheckButtonPoofInfoAction action;

         public CheckButtonPoofInfo(CheckButton.CheckButtonPoof.CheckButtonPoofInfo.CheckButtonPoofInfoAction action) {
            this.action = action;
         }

         public CheckButton.CheckButtonPoof.CheckButtonPoofInfo.CheckButtonPoofInfoAction getAction() {
            return this.action;
         }

         public static enum CheckButtonPoofInfoAction {
            TOGGLE,
            ENABLE,
            DISABLE;
         }
      }
   }
}
