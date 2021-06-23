package com.oldturok.turok.gui.rgui.util;

import com.oldturok.turok.gui.rgui.GUI;
import com.oldturok.turok.gui.rgui.component.AlignedComponent;
import com.oldturok.turok.gui.rgui.component.Component;
import com.oldturok.turok.gui.rgui.component.container.Container;
import com.oldturok.turok.gui.rgui.render.theme.Theme;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ContainerHelper {
   public static void setTheme(Container parent, Theme newTheme) {
      Theme old = parent.getTheme();
      parent.setTheme(newTheme);
      Iterator var3 = parent.getChildren().iterator();

      while(var3.hasNext()) {
         Component c = (Component)var3.next();
         if (c.getTheme().equals(old)) {
            c.setTheme(newTheme);
         }
      }

   }

   public static void setAlignment(Container container, AlignedComponent.Alignment alignment) {
      Iterator var2 = container.getChildren().iterator();

      while(var2.hasNext()) {
         Component component = (Component)var2.next();
         if (component instanceof Container) {
            setAlignment((Container)component, alignment);
         }

         if (component instanceof AlignedComponent) {
            ((AlignedComponent)component).setAlignment(alignment);
         }
      }

   }

   public static AlignedComponent.Alignment getAlignment(Container container) {
      Iterator var1 = container.getChildren().iterator();

      Component component;
      do {
         if (!var1.hasNext()) {
            return AlignedComponent.Alignment.LEFT;
         }

         component = (Component)var1.next();
         if (component instanceof Container) {
            return getAlignment((Container)component);
         }
      } while(!(component instanceof AlignedComponent));

      return ((AlignedComponent)component).getAlignment();
   }

   public static Component getHighParent(Component child) {
      return !(child.getParent() instanceof GUI) && child.getParent() != null ? getHighParent(child.getParent()) : child;
   }

   public static <T extends Component> T getFirstParent(Class<? extends T> parentClass, Component component) {
      if (component.getClass().equals(parentClass)) {
         return component;
      } else {
         return component == null ? null : getFirstParent(parentClass, component.getParent());
      }
   }

   public static <S extends Component> List<S> getAllChildren(Class<? extends S> childClass, Container parent) {
      ArrayList<S> list = new ArrayList();
      Iterator var3 = parent.getChildren().iterator();

      while(var3.hasNext()) {
         Component c = (Component)var3.next();
         if (childClass.isAssignableFrom(c.getClass())) {
            list.add(c);
         }

         if (c instanceof Container) {
            list.addAll(getAllChildren(childClass, (Container)c));
         }
      }

      return list;
   }
}
