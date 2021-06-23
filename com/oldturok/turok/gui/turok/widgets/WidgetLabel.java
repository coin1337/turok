package com.oldturok.turok.gui.turok.widgets;

import com.oldturok.turok.gui.rgui.component.AlignedComponent;
import com.oldturok.turok.gui.rgui.component.use.Label;
import com.oldturok.turok.gui.rgui.render.AbstractComponentUI;
import com.oldturok.turok.gui.rgui.render.font.FontRenderer;
import org.lwjgl.opengl.GL11;

public class WidgetLabel<T extends Label> extends AbstractComponentUI<Label> {
   public void renderComponent(Label component, FontRenderer a) {
      a = component.getFontRenderer();
      String[] lines = component.getLines();
      int y = 0;
      boolean shadow = component.isShadow();
      String[] var6 = lines;
      int var7 = lines.length;

      for(int var8 = 0; var8 < var7; ++var8) {
         String str = var6[var8];
         int x = 0;
         if (component.getAlignment() == AlignedComponent.Alignment.CENTER) {
            x = component.getWidth() / 2 - a.getStringWidth(str) / 2;
         } else if (component.getAlignment() == AlignedComponent.Alignment.RIGHT) {
            x = component.getWidth() - a.getStringWidth(str);
         }

         if (shadow) {
            a.drawStringWithShadow(x, y, 255, 255, 255, str);
         } else {
            a.drawString(x, y, str);
         }

         y += a.getFontHeight() + 3;
      }

      GL11.glDisable(3553);
      GL11.glDisable(3042);
   }

   public void handleSizeComponent(Label component) {
      String[] lines = component.getLines();
      int y = 0;
      int w = 0;
      String[] var5 = lines;
      int var6 = lines.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         String s = var5[var7];
         w = Math.max(w, component.getFontRenderer().getStringWidth(s));
         y += component.getFontRenderer().getFontHeight() + 3;
      }

      component.setWidth(w);
      component.setHeight(y);
   }
}
