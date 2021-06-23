package com.oldturok.turok.gui.rgui.render.theme;

import com.oldturok.turok.gui.rgui.component.Component;
import com.oldturok.turok.gui.rgui.layout.Layout;
import com.oldturok.turok.gui.rgui.render.ComponentUI;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractTheme implements Theme {
   protected final Map<Class<? extends Component>, ComponentUI> uis = new HashMap();
   protected final Map<Class<? extends Layout>, Class<? extends Layout>> layoutMap = new HashMap();

   protected void installUI(ComponentUI<?> ui) {
      this.uis.put(ui.getHandledClass(), ui);
   }

   public ComponentUI getUIForComponent(Component component) {
      ComponentUI a = this.getComponentUIForClass(component.getClass());
      if (a == null) {
         throw new RuntimeException("No installed component UI for " + component.getClass().getName());
      } else {
         return a;
      }
   }

   public ComponentUI getComponentUIForClass(Class<? extends Component> componentClass) {
      if (this.uis.containsKey(componentClass)) {
         return (ComponentUI)this.uis.get(componentClass);
      } else if (componentClass == null) {
         return null;
      } else {
         Class[] var2 = componentClass.getInterfaces();
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            Class<?> componentInterface = var2[var4];
            ComponentUI ui = (ComponentUI)this.uis.get(componentInterface);
            if (ui != null) {
               return ui;
            }
         }

         return this.getComponentUIForClass(componentClass.getSuperclass());
      }
   }
}
