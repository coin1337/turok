package com.oldturok.turok.gui.turok.widgets;

import com.oldturok.turok.gui.rgui.component.container.Container;
import com.oldturok.turok.gui.rgui.component.use.CheckButton;
import com.oldturok.turok.gui.rgui.render.AbstractComponentUI;
import com.oldturok.turok.gui.rgui.render.font.FontRenderer;
import com.oldturok.turok.gui.turok.RenderHelper;
import com.oldturok.turok.gui.turok.TurokGUI;
import com.oldturok.turok.util.TurokGL;
import java.awt.Color;

public class WidgetCheckButton<T extends CheckButton> extends AbstractComponentUI<CheckButton> {
   protected Color backgroundColour = new Color(0, 0, 255);
   protected Color backgroundColourHover = new Color(0, 0, 255);
   protected Color idleColourNormal = new Color(0, 0, 255);
   protected Color downColourNormal = new Color(0, 0, 255);
   protected Color idleColourToggle = new Color(0, 0, 255);
   protected Color downColourToggle;

   public WidgetCheckButton() {
      this.downColourToggle = this.idleColourToggle.brighter();
   }

   public void renderComponent(CheckButton component, FontRenderer ff) {
      int color = component.isPressed() ? 14540253 : (component.isToggled() ? 14540253 : 14540253);
      if (component.isHovered()) {
         color = (color & 10339548) << 1;
      }

      if (component.isToggled()) {
         TurokGL.refresh_color(WidgetModuleFrame.color_pinned_r, 0.0F, 0.0F, 150.0F);
         RenderHelper.drawFilledRectangle(0.0F, 0.0F, (float)component.getWidth(), (float)component.getHeight());
         TurokGUI.fontRenderer.drawString(1, 1, color, component.getName());
      } else {
         TurokGUI.fontRenderer.drawString(1, 1, color, component.getName());
      }

      TurokGL.FixRefreshColor();
   }

   public void handleAddComponent(CheckButton component, Container container) {
      component.setWidth(TurokGUI.fontRenderer.getStringWidth(component.getName()) + 1);
      component.setHeight(TurokGUI.fontRenderer.getFontHeight() + 1);
   }
}
