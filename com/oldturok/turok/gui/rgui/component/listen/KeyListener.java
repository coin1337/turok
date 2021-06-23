package com.oldturok.turok.gui.rgui.component.listen;

public interface KeyListener {
   void onKeyDown(KeyListener.KeyEvent var1);

   void onKeyUp(KeyListener.KeyEvent var1);

   public static class KeyEvent {
      int key;

      public KeyEvent(int key) {
         this.key = key;
      }

      public int getKey() {
         return this.key;
      }

      public void setKey(int key) {
         this.key = key;
      }
   }
}
