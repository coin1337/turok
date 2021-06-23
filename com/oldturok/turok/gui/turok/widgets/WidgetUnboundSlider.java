package com.oldturok.turok.gui.turok.widgets;

import com.oldturok.turok.gui.rgui.component.container.Container;
import com.oldturok.turok.gui.rgui.render.AbstractComponentUI;
import com.oldturok.turok.gui.rgui.render.font.FontRenderer;
import com.oldturok.turok.gui.turok.component.UnboundSlider;
import com.oldturok.turok.util.TurokGL;

public class WidgetUnboundSlider extends AbstractComponentUI<UnboundSlider> {
   public String text;

   public void renderComponent(UnboundSlider component, FontRenderer fontRenderer) {
      this.text = component.getText() + ": " + component.getValue();
      int color = component.isPressed() ? 14540253 : 14540253;
      float value_ = (float)component.getValue();
      if (component.isHovered()) {
         color = (color & 10339548) << 1;
      }

      TurokGL.refresh_color(255.0F, 0.0F, 0.0F, 255.0F);
      fontRenderer.drawString(1, component.getHeight() - fontRenderer.getFontHeight() / 2 - 4, color, this.text);
      TurokGL.FixRefreshColor();
      component.setWidth(component.getTheme().getFontRenderer().getStringWidth(this.text));
   }

   public void handleAddComponent(UnboundSlider component, Container container) {
      component.setHeight(component.getTheme().getFontRenderer().getFontHeight());
      component.setWidth(component.getTheme().getFontRenderer().getStringWidth(this.text));
   }
}
