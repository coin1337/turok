package com.oldturok.turok.gui.turok.widgets;

import com.oldturok.turok.gui.rgui.component.container.Container;
import com.oldturok.turok.gui.rgui.component.use.Slider;
import com.oldturok.turok.gui.rgui.render.AbstractComponentUI;
import com.oldturok.turok.gui.rgui.render.font.FontRenderer;
import com.oldturok.turok.gui.turok.RenderHelper;
import com.oldturok.turok.gui.turok.RootSmallFontRenderer;
import com.oldturok.turok.util.TurokGL;

public class WidgetSlider extends AbstractComponentUI<Slider> {
   RootSmallFontRenderer smallFontRenderer = new RootSmallFontRenderer();

   public void renderComponent(Slider component, FontRenderer aa) {
      TurokGL.refresh_color(255.0F, 255.0F, 255.0F, component.getOpacity());
      int height = component.getHeight();
      double value = component.getValue();
      double w = (double)component.getWidth() * ((value - component.getMinimum()) / (component.getMaximum() - component.getMinimum()));
      float downscale = 1.1F;
      float w_ = (float)((int)w);
      TurokGL.refresh_color(WidgetModuleFrame.color_pinned_r, 0.0F, 0.0F, 150.0F);
      RenderHelper.drawFilledRectangle(0.0F, 0.0F, w_, (float)height / downscale);
      TurokGL.refresh_color(WidgetModuleFrame.color_pinned_r, 0.0F, 0.0F, 150.0F);
      RenderHelper.drawRectangle(0.0F, 0.0F, (float)component.getWidth(), (float)height / downscale);
      String s = value + "";
      if (component.isPressed()) {
         w_ -= (float)(this.smallFontRenderer.getStringWidth(s) / 2);
         w_ = Math.max(0.0F, Math.min(w_, (float)(component.getWidth() - this.smallFontRenderer.getStringWidth(s))));
         this.smallFontRenderer.drawString((int)w_, 2, s);
      } else {
         this.smallFontRenderer.drawString(2, 2, component.getText());
         this.smallFontRenderer.drawString(component.getWidth() - this.smallFontRenderer.getStringWidth(s) - 2, 2, s);
      }

      TurokGL.FixRefreshColor();
   }

   public void handleAddComponent(Slider component, Container container) {
      component.setHeight(component.getTheme().getFontRenderer().getFontHeight() + 2);
      component.setWidth(this.smallFontRenderer.getStringWidth(component.getText()) + this.smallFontRenderer.getStringWidth(component.getMaximum() + "") + 3);
   }
}
