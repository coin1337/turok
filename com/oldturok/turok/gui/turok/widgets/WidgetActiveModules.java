package com.oldturok.turok.gui.turok.widgets;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.oldturok.turok.gui.rgui.component.AlignedComponent;
import com.oldturok.turok.gui.rgui.render.AbstractComponentUI;
import com.oldturok.turok.gui.rgui.render.font.FontRenderer;
import com.oldturok.turok.gui.turok.RootFontRenderer;
import com.oldturok.turok.gui.turok.component.ActiveModules;
import com.oldturok.turok.module.Module;
import com.oldturok.turok.module.ModuleManager;
import com.oldturok.turok.util.Wrapper;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.lwjgl.opengl.GL11;

public class WidgetActiveModules extends AbstractComponentUI<ActiveModules> {
   public static int r = 255;
   public static int g = 255;
   public static int b = 255;

   public void renderComponent(ActiveModules component, FontRenderer f) {
      GL11.glDisable(2884);
      GL11.glEnable(3042);
      GL11.glEnable(3553);
      RootFontRenderer renderer = new RootFontRenderer(1.0F);
      List<Module> mods = (List)ModuleManager.getModules().stream().filter(Module::isEnabled).sorted(Comparator.comparing((module) -> {
         return renderer.getStringWidth(module.getName() + (module.getHudInfo() == null ? "" : module.getHudInfo() + " "));
      })).collect(Collectors.toList());
      int[] module_y = new int[]{2};
      if (component.getParent().getY() < 26 && Wrapper.getPlayer().func_70651_bq().size() > 0 && component.getParent().getOpacity() == 0.0F) {
         module_y[0] = Math.max(component.getParent().getY(), 26 - component.getParent().getY());
      }

      boolean lAlign = component.getAlignment() == AlignedComponent.Alignment.LEFT;
      Function xFunc;
      switch(component.getAlignment()) {
      case RIGHT:
         xFunc = (i) -> {
            return component.getWidth() - i;
         };
         break;
      case LEFT:
      default:
         xFunc = (i) -> {
            return 0;
         };
      }

      mods.stream().forEach((module) -> {
         String module_info = module.getHudInfo();
         String module_name = module.getName() + (module_info == null ? "" : " " + ChatFormatting.GRAY + module_info);
         int module_width = renderer.getStringWidth(module_name);
         int module_height = renderer.getFontHeight() + 1;
         renderer.drawStringWithShadow((Integer)xFunc.apply(module_width), module_y[0], r, g, b, module_name);
         module_y[0] += module_height;
      });
      component.setHeight(module_y[0]);
      GL11.glEnable(2884);
      GL11.glDisable(3042);
   }

   public void handleSizeComponent(ActiveModules component) {
      component.setWidth(100);
      component.setHeight(100);
   }
}
