package com.oldturok.turok.gui.turok.widgets;

import com.oldturok.turok.gui.rgui.component.container.Container;
import com.oldturok.turok.gui.rgui.render.AbstractComponentUI;
import com.oldturok.turok.gui.rgui.render.font.FontRenderer;
import com.oldturok.turok.gui.turok.RootSmallFontRenderer;
import com.oldturok.turok.gui.turok.component.EnumButton;
import java.awt.Color;
import org.lwjgl.opengl.GL11;

public class WidgetEnummButton extends AbstractComponentUI<EnumButton> {
   RootSmallFontRenderer smallFontRenderer = new RootSmallFontRenderer();
   protected Color idleColour = new Color(0, 0, 163);
   protected Color downColour = new Color(0, 0, 255);
   EnumButton modeComponent;
   long lastMS = System.currentTimeMillis();

   public void renderComponent(EnumButton component, FontRenderer aa) {
      if (System.currentTimeMillis() - this.lastMS > 3000L && this.modeComponent != null) {
         this.modeComponent = null;
      }

      int c = component.isPressed() ? 11184810 : 14540253;
      if (component.isHovered()) {
         c = (c & 8355711) << 1;
      }

      GL11.glColor3f(1.0F, 1.0F, 1.0F);
      GL11.glEnable(3553);
      int parts = component.getModes().length;
      double step = (double)component.getWidth() / (double)parts;
      double startX = step * (double)component.getIndex();
      double endX = step * (double)(component.getIndex() + 1);
      int height = component.getHeight();
      float downscale = 1.1F;
      GL11.glDisable(3553);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.3F);
      GL11.glBegin(1);
      GL11.glVertex2d(startX, (double)((float)height / downscale));
      GL11.glVertex2d(endX, (double)((float)height / downscale));
      GL11.glEnd();
      if (this.modeComponent != null && this.modeComponent.equals(component)) {
         this.smallFontRenderer.drawString(component.getWidth() / 2 - this.smallFontRenderer.getStringWidth(component.getIndexMode()) / 2, 0, c, component.getIndexMode());
      } else {
         this.smallFontRenderer.drawString(0, 0, c, component.getName());
         this.smallFontRenderer.drawString(component.getWidth() - this.smallFontRenderer.getStringWidth(component.getIndexMode()), 0, c, component.getIndexMode());
      }

      GL11.glDisable(3042);
   }

   public void handleSizeComponent(EnumButton component) {
      int width = 0;
      String[] var3 = component.getModes();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         String s = var3[var5];
         width = Math.max(width, this.smallFontRenderer.getStringWidth(s));
      }

      component.setWidth(this.smallFontRenderer.getStringWidth(component.getName()) + width + 1);
      component.setHeight(this.smallFontRenderer.getFontHeight() + 2);
   }

   public void handleAddComponent(EnumButton component, Container container) {
      component.addPoof(new EnumButton.EnumbuttonIndexPoof<EnumButton, EnumButton.EnumbuttonIndexPoof.EnumbuttonInfo>() {
         public void execute(EnumButton component, EnumButton.EnumbuttonIndexPoof.EnumbuttonInfo info) {
            WidgetEnummButton.this.modeComponent = component;
            WidgetEnummButton.this.lastMS = System.currentTimeMillis();
         }
      });
   }
}
