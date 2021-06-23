package com.oldturok.turok.gui.rgui.layout;

import com.oldturok.turok.gui.rgui.component.Component;
import com.oldturok.turok.gui.rgui.component.container.Container;
import java.util.ArrayList;
import java.util.Iterator;

public class GridbagLayout implements Layout {
   private static final int COMPONENT_OFFSET = 10;
   int blocks;
   int maxrows = -1;

   public GridbagLayout(int blocks) {
      this.blocks = blocks;
   }

   public GridbagLayout(int blocks, int fixrows) {
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
            w += c.getWidth() + 10;
            h = Math.max(h, c.getHeight());
            ++i;
            if (i >= this.blocks) {
               width = Math.max(width, w);
               height += h + 10;
               i = 0;
               h = 0;
               w = 0;
            }
         }
      }

      int x = 0;
      int y = 0;
      Iterator var10 = children.iterator();

      while(var10.hasNext()) {
         Component c = (Component)var10.next();
         if (c.doAffectLayout()) {
            c.setX(x + 3);
            c.setY(y + 3);
            h = Math.max(c.getHeight(), h);
            x += width / this.blocks;
            if (x >= width) {
               y += h + 10;
               x = 0;
            }
         }
      }

      container.setWidth(width);
      container.setHeight(height);
   }
}
