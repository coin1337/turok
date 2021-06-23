package com.oldturok.turok.gui.turok.widgets;

import com.oldturok.turok.gui.rgui.render.AbstractComponentUI;
import com.oldturok.turok.gui.rgui.render.font.FontRenderer;
import com.oldturok.turok.gui.turok.RenderHelper;
import com.oldturok.turok.gui.turok.component.SettingsPanel;
import com.oldturok.turok.util.TurokGL;

public class WidgetModuleSettings extends AbstractComponentUI<SettingsPanel> {
   public void renderComponent(SettingsPanel component, FontRenderer fontRenderer) {
      super.renderComponent(component, fontRenderer);
      TurokGL.refresh_color(0.0F, 0.0F, 0.0F, 150.0F);
      RenderHelper.drawFilledRectangle(0.0F, 0.0F, (float)(component.getWidth() + 4), (float)component.getHeight());
   }
}
