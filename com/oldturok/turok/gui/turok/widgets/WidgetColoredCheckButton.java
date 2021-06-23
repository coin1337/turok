package com.oldturok.turok.gui.turok.widgets;

import com.oldturok.turok.gui.rgui.component.use.CheckButton;
import com.oldturok.turok.gui.rgui.render.font.FontRenderer;
import com.oldturok.turok.gui.turok.RootSmallFontRenderer;
import com.oldturok.turok.gui.turok.TurokGUI;
import com.oldturok.turok.gui.turok.component.ColorizedCheckButton;
import java.awt.Color;
import org.lwjgl.opengl.GL11;

public class WidgetColoredCheckButton extends WidgetCheckButton<ColorizedCheckButton> {
   RootSmallFontRenderer ff = new RootSmallFontRenderer();

   public WidgetColoredCheckButton() {
      this.backgroundColour = new Color(200, this.backgroundColour.getGreen(), this.backgroundColour.getBlue());
      this.backgroundColourHover = new Color(255, this.backgroundColourHover.getGreen(), this.backgroundColourHover.getBlue());
      this.downColourNormal = new Color(190, 190, 190);
   }

   public void renderComponent(CheckButton component, FontRenderer aa) {
      GL11.glColor4f((float)this.backgroundColour.getRed() / 255.0F, (float)this.backgroundColour.getGreen() / 255.0F, (float)this.backgroundColour.getBlue() / 255.0F, component.getOpacity());
      if (component.isHovered() || component.isPressed()) {
         GL11.glColor4f((float)this.backgroundColourHover.getRed() / 255.0F, (float)this.backgroundColourHover.getGreen() / 255.0F, (float)this.backgroundColourHover.getBlue() / 255.0F, component.getOpacity());
      }

      if (component.isToggled()) {
         GL11.glColor3f((float)this.backgroundColour.getRed() / 255.0F, (float)this.backgroundColour.getGreen() / 255.0F, (float)this.backgroundColour.getBlue() / 255.0F);
      }

      GL11.glLineWidth(2.5F);
      GL11.glBegin(1);
      GL11.glVertex2d(0.0D, (double)component.getHeight());
      GL11.glVertex2d((double)component.getWidth(), (double)component.getHeight());
      GL11.glEnd();
      Color idleColour = component.isToggled() ? this.idleColourToggle : this.idleColourNormal;
      Color downColour = component.isToggled() ? this.downColourToggle : this.downColourNormal;
      GL11.glColor3f(1.0F, 1.0F, 1.0F);
      GL11.glEnable(3553);
      this.ff.drawString(component.getWidth() / 2 - TurokGUI.fontRenderer.getStringWidth(component.getName()) / 2, 0, component.isPressed() ? downColour : idleColour, component.getName());
      GL11.glDisable(3553);
   }
}
