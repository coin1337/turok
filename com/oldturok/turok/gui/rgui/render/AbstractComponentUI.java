package com.oldturok.turok.gui.rgui.render;

import com.oldturok.turok.gui.rgui.component.Component;
import com.oldturok.turok.gui.rgui.component.container.Container;
import com.oldturok.turok.gui.rgui.render.font.FontRenderer;
import java.lang.reflect.ParameterizedType;

public abstract class AbstractComponentUI<T extends Component> implements ComponentUI<T> {
   private Class<T> persistentClass = (Class)((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];

   public void renderComponent(T component, FontRenderer fontRenderer) {
   }

   public void handleMouseDown(T component, int x, int y, int button) {
   }

   public void handleMouseRelease(T component, int x, int y, int button) {
   }

   public void handleMouseDrag(T component, int x, int y, int button) {
   }

   public void handleScroll(T component, int x, int y, int amount, boolean up) {
   }

   public void handleAddComponent(T component, Container container) {
   }

   public void handleKeyDown(T component, int key) {
   }

   public void handleKeyUp(T component, int key) {
   }

   public void handleSizeComponent(T component) {
   }

   public Class<? extends Component> getHandledClass() {
      return this.persistentClass;
   }
}
