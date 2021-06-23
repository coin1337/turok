package com.oldturok.turok.gui.turok.widgets;

import com.oldturok.turok.gui.rgui.component.container.Container;
import com.oldturok.turok.gui.rgui.component.use.Button;
import com.oldturok.turok.gui.rgui.render.AbstractComponentUI;
import com.oldturok.turok.gui.rgui.render.font.FontRenderer;
import com.oldturok.turok.gui.turok.RenderHelper;
import com.oldturok.turok.gui.turok.TurokGUI;
import java.awt.Color;
import org.lwjgl.opengl.GL11;

public class WidgetButton<T extends Button> extends AbstractComponentUI<Button> {
   protected Color idleColour = new Color(0, 0, 255);
   protected Color downColour = new Color(0, 0, 150);

   public void renderComponent(Button component, FontRenderer ff) {
      GL11.glColor3f(0.22F, 0.22F, 0.22F);
      if (component.isHovered() || component.isPressed()) {
         GL11.glColor3f(0.26F, 0.26F, 0.26F);
      }

      RenderHelper.drawRoundedRectangle(0.0F, 0.0F, (float)component.getWidth(), (float)component.getHeight(), 3.0F);
      GL11.glColor3f(1.0F, 1.0F, 1.0F);
      GL11.glEnable(3553);
      TurokGUI.fontRenderer.drawString(1, 0, component.isPressed() ? this.downColour : this.idleColour, component.getName());
      GL11.glDisable(3553);
      GL11.glDisable(3042);
   }

   public void handleAddComponent(Button component, Container container) {
      component.setWidth(TurokGUI.fontRenderer.getStringWidth(component.getName()) + 28);
      component.setHeight(TurokGUI.fontRenderer.getFontHeight() + 2);
   }
}
