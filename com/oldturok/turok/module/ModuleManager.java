package com.oldturok.turok.module;

import com.oldturok.turok.TurokMod;
import com.oldturok.turok.event.events.RenderEvent;
import com.oldturok.turok.module.modules.GUI;
import com.oldturok.turok.util.ClassFinder;
import com.oldturok.turok.util.EntityUtil;
import com.oldturok.turok.util.TurokTessellator;
import com.oldturok.turok.util.Wrapper;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;

public class ModuleManager {
   public static ArrayList<Module> modules = new ArrayList();
   static HashMap<String, Module> lookup = new HashMap();

   public static void updateLookup() {
      lookup.clear();
      Iterator var0 = modules.iterator();

      while(var0.hasNext()) {
         Module m = (Module)var0.next();
         lookup.put(m.getName().toLowerCase(), m);
      }

   }

   public static void initialize() {
      Set<Class> classList = ClassFinder.findClasses(GUI.class.getPackage().getName(), Module.class);
      classList.forEach((aClass) -> {
         try {
            Module module = (Module)aClass.getConstructor().newInstance();
            modules.add(module);
         } catch (InvocationTargetException var2) {
            var2.getCause().printStackTrace();
            System.err.println("Couldn't initiate module " + aClass.getSimpleName() + "! Err: " + var2.getClass().getSimpleName() + ", message: " + var2.getMessage());
         } catch (Exception var3) {
            var3.printStackTrace();
            System.err.println("Couldn't initiate module " + aClass.getSimpleName() + "! Err: " + var3.getClass().getSimpleName() + ", message: " + var3.getMessage());
         }

      });
      TurokMod.turok_log.info("Modules initialised");
      getModules().sort(Comparator.comparing(Module::getName));
   }

   public static void onUpdate() {
      modules.stream().filter((module) -> {
         return module.alwaysListening || module.isEnabled();
      }).forEach((module) -> {
         module.onUpdate();
      });
   }

   public static void onRender() {
      modules.stream().filter((module) -> {
         return module.alwaysListening || module.isEnabled();
      }).forEach((module) -> {
         module.onRender();
      });
   }

   public static void onWorldRender(RenderWorldLastEvent event) {
      Minecraft.func_71410_x().field_71424_I.func_76320_a("turok");
      Minecraft.func_71410_x().field_71424_I.func_76320_a("setup");
      GlStateManager.func_179090_x();
      GlStateManager.func_179147_l();
      GlStateManager.func_179118_c();
      GlStateManager.func_179120_a(770, 771, 1, 0);
      GlStateManager.func_179103_j(7425);
      GlStateManager.func_179097_i();
      GlStateManager.func_187441_d(1.0F);
      Vec3d renderPos = EntityUtil.getInterpolatedPos(Wrapper.getPlayer(), event.getPartialTicks());
      RenderEvent e = new RenderEvent(TurokTessellator.INSTANCE, renderPos);
      e.resetTranslation();
      Minecraft.func_71410_x().field_71424_I.func_76319_b();
      modules.stream().filter((module) -> {
         return module.alwaysListening || module.isEnabled();
      }).forEach((module) -> {
         Minecraft.func_71410_x().field_71424_I.func_76320_a(module.getName());
         module.onWorldRender(e);
         Minecraft.func_71410_x().field_71424_I.func_76319_b();
      });
      Minecraft.func_71410_x().field_71424_I.func_76320_a("release");
      GlStateManager.func_187441_d(1.0F);
      GlStateManager.func_179103_j(7424);
      GlStateManager.func_179084_k();
      GlStateManager.func_179141_d();
      GlStateManager.func_179098_w();
      GlStateManager.func_179126_j();
      GlStateManager.func_179089_o();
      TurokTessellator.releaseGL();
      Minecraft.func_71410_x().field_71424_I.func_76319_b();
      Minecraft.func_71410_x().field_71424_I.func_76319_b();
   }

   public static void onBind(int eventKey) {
      if (eventKey != 0) {
         modules.forEach((module) -> {
            if (module.getBind().keydown(eventKey)) {
               module.toggle();
            }

         });
      }
   }

   public static ArrayList<Module> getModules() {
      return modules;
   }

   public static Module getModuleByName(String name) {
      return (Module)lookup.get(name.toLowerCase());
   }

   public static boolean isModuleEnabled(String moduleName) {
      Module m = getModuleByName(moduleName);
      return m == null ? false : m.isEnabled();
   }
}
