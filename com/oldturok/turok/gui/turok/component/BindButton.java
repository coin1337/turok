package com.oldturok.turok.gui.turok.component;

import com.oldturok.turok.gui.rgui.component.listen.KeyListener;
import com.oldturok.turok.gui.rgui.component.listen.MouseListener;
import com.oldturok.turok.module.Module;
import com.oldturok.turok.util.TurokBind;

public class BindButton extends EnumButton {
   static String[] lookingFor = new String[]{"..."};
   static String[] none = new String[]{"None"};
   boolean waiting = false;
   Module m;

   public BindButton(String name, final Module m) {
      super(name, none);
      this.m = m;
      TurokBind bind = m.getBind();
      this.modes = new String[]{bind.toString()};
      this.addKeyListener(new KeyListener() {
         public void onKeyDown(KeyListener.KeyEvent event) {
            if (BindButton.this.waiting) {
               int key = event.getKey();
               if (key == 14) {
                  m.getBind().set_key(-1);
                  BindButton.this.modes = new String[]{m.getBind().toString()};
                  BindButton.this.waiting = false;
               } else if (key == 211) {
                  if (!m.getBind().is_empty()) {
                     m.getBind().set_key(-1);
                     BindButton.this.modes = new String[]{m.getBind().toString()};
                     BindButton.this.waiting = false;
                  }
               } else {
                  m.getBind().set_key(key);
                  BindButton.this.modes = new String[]{m.getBind().toString()};
                  BindButton.this.waiting = false;
               }

            }
         }

         public void onKeyUp(KeyListener.KeyEvent event) {
         }
      });
      this.addMouseListener(new MouseListener() {
         public void onMouseDown(MouseListener.MouseButtonEvent event) {
            BindButton.this.setModes(BindButton.lookingFor);
            BindButton.this.waiting = true;
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
}
