package com.oldturok.turok.gui.turok.widgets;

import com.oldturok.turok.gui.rgui.component.container.Container;
import com.oldturok.turok.gui.rgui.component.use.InputField;
import com.oldturok.turok.gui.rgui.render.AbstractComponentUI;
import com.oldturok.turok.gui.rgui.render.font.FontRenderer;

public class WidgetInputField<T extends InputField> extends AbstractComponentUI<InputField> {
   public void renderComponent(InputField component, FontRenderer fontRenderer) {
   }

   public void handleAddComponent(InputField component, Container container) {
      component.setWidth(200);
      component.setHeight(component.getTheme().getFontRenderer().getFontHeight());
   }
}
