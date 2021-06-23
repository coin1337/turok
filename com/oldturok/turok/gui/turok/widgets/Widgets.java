package com.oldturok.turok.gui.turok.widgets;

import com.oldturok.turok.gui.rgui.render.AbstractComponentUI;
import com.oldturok.turok.gui.rgui.render.font.FontRenderer;
import com.oldturok.turok.gui.rgui.render.theme.AbstractTheme;
import com.oldturok.turok.gui.turok.TurokGUI;

public class Widgets extends AbstractTheme {
   FontRenderer fontRenderer;

   public Widgets() {
      this.installUI(new WidgetButton());
      this.installUI(new Widgets.implement_gui());
      this.installUI(new WidgetGroupbox());
      this.installUI(new WidgetModuleFrame());
      this.installUI(new WidgetScrollpane());
      this.installUI(new WidgetInputField());
      this.installUI(new WidgetLabel());
      this.installUI(new WidgetChat());
      this.installUI(new WidgetCheckButton());
      this.installUI(new WidgetActiveModules());
      this.installUI(new WidgetModuleSettings());
      this.installUI(new WidgetSlider());
      this.installUI(new WidgetEnummButton());
      this.installUI(new WidgetColoredCheckButton());
      this.installUI(new WidgetUnboundSlider());
      this.fontRenderer = TurokGUI.fontRenderer;
   }

   public FontRenderer getFontRenderer() {
      return this.fontRenderer;
   }

   public class implement_gui extends AbstractComponentUI<TurokGUI> {
   }
}
