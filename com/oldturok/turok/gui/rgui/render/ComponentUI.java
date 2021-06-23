package com.oldturok.turok.gui.rgui.render;

import com.oldturok.turok.gui.rgui.component.Component;
import com.oldturok.turok.gui.rgui.component.container.Container;
import com.oldturok.turok.gui.rgui.render.font.FontRenderer;

public interface ComponentUI<T extends Component> {
   void renderComponent(T var1, FontRenderer var2);

   void handleMouseDown(T var1, int var2, int var3, int var4);

   void handleMouseRelease(T var1, int var2, int var3, int var4);

   void handleMouseDrag(T var1, int var2, int var3, int var4);

   void handleScroll(T var1, int var2, int var3, int var4, boolean var5);

   void handleKeyDown(T var1, int var2);

   void handleKeyUp(T var1, int var2);

   void handleAddComponent(T var1, Container var2);

   void handleSizeComponent(T var1);

   Class<? extends Component> getHandledClass();
}
