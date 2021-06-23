package com.oldturok.turok.gui.rgui.render.theme;

import com.oldturok.turok.gui.rgui.component.Component;
import com.oldturok.turok.gui.rgui.render.ComponentUI;
import com.oldturok.turok.gui.rgui.render.font.FontRenderer;

public interface Theme {
   ComponentUI getUIForComponent(Component var1);

   FontRenderer getFontRenderer();
}
