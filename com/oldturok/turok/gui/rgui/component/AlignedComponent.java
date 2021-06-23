package com.oldturok.turok.gui.rgui.component;

public class AlignedComponent extends AbstractComponent {
   AlignedComponent.Alignment alignment;

   public AlignedComponent.Alignment getAlignment() {
      return this.alignment;
   }

   public void setAlignment(AlignedComponent.Alignment alignment) {
      this.alignment = alignment;
   }

   public static enum Alignment {
      LEFT(0),
      CENTER(1),
      RIGHT(2);

      int index;

      private Alignment(int index) {
         this.index = index;
      }

      public int getIndex() {
         return this.index;
      }
   }
}
