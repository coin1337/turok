package com.oldturok.turok.gui.turok;

import com.oldturok.turok.gui.rgui.component.Component;
import com.oldturok.turok.gui.rgui.component.container.Container;
import com.oldturok.turok.gui.rgui.layout.Layout;
import java.util.ArrayList;
import java.util.Iterator;

public class Stretcherlayout implements Layout {
   public int COMPONENT_OFFSET_X = 10;
   public int COMPONENT_OFFSET_Y = 4;
   int blocks;
   int maxrows = -1;

   public Stretcherlayout(int blocks) {
      this.blocks = blocks;
   }

   public Stretcherlayout(int blocks, int fixrows) {
      this.blocks = blocks;
      this.maxrows = fixrows;
   }

   public void organiseContainer(Container container) {
      int width = 0;
      int height = 0;
      int i = 0;
      int w = 0;
      int h = 0;
      ArrayList<Component> children = container.getChildren();
      Iterator var8 = children.iterator();

      while(var8.hasNext()) {
         Component c = (Component)var8.next();
         if (c.doAffectLayout()) {
            w += c.getWidth() + this.COMPONENT_OFFSET_X;
            h = Math.max(h, c.getHeight());
            ++i;
            if (i >= this.blocks) {
               width = Math.max(width, w);
               height += h + this.COMPONENT_OFFSET_Y;
               i = 0;
               h = 0;
               w = 0;
            }
         }
      }

      int x = 0;
      int y = 0;
      Iterator var10 = children.iterator();

      Component c;
      while(var10.hasNext()) {
         c = (Component)var10.next();
         if (c.doAffectLayout()) {
            c.setX(x + this.COMPONENT_OFFSET_X / 3);
            c.setY(y + this.COMPONENT_OFFSET_Y / 3);
            h = Math.max(c.getHeight(), h);
            x += width / this.blocks;
            if (x >= width) {
               y += h + this.COMPONENT_OFFSET_Y;
               x = 0;
            }
         }
      }

      container.setWidth(width);
      container.setHeight(height);
      width -= this.COMPONENT_OFFSET_X;
      var10 = children.iterator();

      while(var10.hasNext()) {
         c = (Component)var10.next();
         if (!c.doAffectLayout()) {
            return;
         }

         c.setWidth(width);
      }

   }

   public void setComponentOffsetWidth(int componentOffset) {
      this.COMPONENT_OFFSET_X = componentOffset;
   }

   public void setComponentOffsetHeight(int componentOffset) {
      this.COMPONENT_OFFSET_Y = componentOffset;
   }
}
