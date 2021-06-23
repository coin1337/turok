package com.oldturok.turok.gui.turok;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.oldturok.turok.gui.rgui.GUI;
import com.oldturok.turok.gui.rgui.component.Component;
import com.oldturok.turok.gui.rgui.component.container.use.Frame;
import com.oldturok.turok.gui.rgui.component.container.use.Scrollpane;
import com.oldturok.turok.gui.rgui.component.listen.MouseListener;
import com.oldturok.turok.gui.rgui.component.use.CheckButton;
import com.oldturok.turok.gui.rgui.component.use.Label;
import com.oldturok.turok.gui.rgui.render.theme.Theme;
import com.oldturok.turok.gui.rgui.util.ContainerHelper;
import com.oldturok.turok.gui.rgui.util.Docking;
import com.oldturok.turok.gui.turok.component.ActiveModules;
import com.oldturok.turok.gui.turok.component.SettingsPanel;
import com.oldturok.turok.gui.turok.widgets.Widgets;
import com.oldturok.turok.module.Module;
import com.oldturok.turok.module.ModuleManager;
import com.oldturok.turok.util.ColourHolder;
import com.oldturok.turok.util.Pair;
import com.oldturok.turok.util.Wrapper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class TurokGUI extends GUI {
   public static final RootFontRenderer fontRendererBig = new RootFontRenderer(1.0F);
   public static final RootFontRenderer fontRenderer = new RootFontRenderer(1.0F);
   public Theme theme = this.getTheme();
   public static ColourHolder primaryColour = new ColourHolder(29, 29, 29);
   public static ArrayList<Frame> frames;
   public static Frame frame_wattermark;
   public static Frame frame_users;
   public static Frame frame_coords;
   public static Frame frame_counts_totem;
   public static Frame frame_counts_gapple;
   public static Frame frame_counts_crystal;
   public static Frame frame_counts_exp;
   public static Frame frame_array;
   public static int x = 10;
   public static int y = 10;
   public static int nexty;
   public static boolean state_arrays;
   public static boolean state_counts;
   public static boolean state_coords;
   public static Boolean state_users;
   private static final int DOCK_OFFSET = 0;

   public TurokGUI() {
      super(new Widgets());
   }

   public void drawGUI() {
      super.drawGUI();
   }

   public void initializeGUI() {
      HashMap<Module.Category, Pair<Scrollpane, SettingsPanel>> categoryScrollpaneHashMap = new HashMap();
      Iterator var2 = ModuleManager.getModules().iterator();

      Scrollpane scrollpane;
      while(var2.hasNext()) {
         final Module module = (Module)var2.next();
         if (!module.getCategory().isHidden()) {
            Module.Category moduleCategory = module.getCategory();
            if (!categoryScrollpaneHashMap.containsKey(moduleCategory)) {
               Stretcherlayout stretcherlayout = new Stretcherlayout(1);
               stretcherlayout.setComponentOffsetWidth(0);
               scrollpane = new Scrollpane(this.getTheme(), stretcherlayout, 300, 260);
               scrollpane.setMaximumHeight(600);
               scrollpane.setMaximumWidth(600);
               categoryScrollpaneHashMap.put(moduleCategory, new Pair(scrollpane, new SettingsPanel(this.getTheme(), (Module)null)));
            }

            final Pair<Scrollpane, SettingsPanel> pair = (Pair)categoryScrollpaneHashMap.get(moduleCategory);
            scrollpane = (Scrollpane)pair.getKey();
            final CheckButton checkButton = new CheckButton(module.getName());
            checkButton.setToggled(module.isEnabled());
            int old_height = this.getHeight();
            checkButton.addTickListener(() -> {
               checkButton.setToggled(module.isEnabled());
               checkButton.setName(module.getName());
            });
            checkButton.addMouseListener(new MouseListener() {
               public void onMouseDown(MouseListener.MouseButtonEvent event) {
                  if (event.getButton() == 1) {
                     ((SettingsPanel)pair.getValue()).setModule(module);
                     ((SettingsPanel)pair.getValue()).setX(1);
                     ((SettingsPanel)pair.getValue()).setY(checkButton.getY() + TurokGUI.fontRenderer.getStringHeight(checkButton.getName()));
                  }

               }

               public void onMouseRelease(MouseListener.MouseButtonEvent event) {
               }

               public void onMouseDrag(MouseListener.MouseButtonEvent event) {
               }

               public void onMouseMove(MouseListener.MouseMoveEvent event) {
               }

               public void onScroll(MouseListener.MouseScrollEvent event) {
               }
            });
            checkButton.addPoof(new CheckButton.CheckButtonPoof<CheckButton, CheckButton.CheckButtonPoof.CheckButtonPoofInfo>() {
               public void execute(CheckButton component, CheckButton.CheckButtonPoof.CheckButtonPoofInfo info) {
                  if (info.getAction().equals(CheckButton.CheckButtonPoof.CheckButtonPoofInfo.CheckButtonPoofInfoAction.TOGGLE)) {
                     module.setEnabled(checkButton.isToggled());
                  }

               }
            });
            scrollpane.addChild(checkButton);
         }
      }

      x = 10;
      y = 10;
      nexty = y;
      var2 = categoryScrollpaneHashMap.entrySet().iterator();

      while(var2.hasNext()) {
         Entry<Module.Category, Pair<Scrollpane, SettingsPanel>> entry = (Entry)var2.next();
         Stretcherlayout stretcherlayout = new Stretcherlayout(1);
         stretcherlayout.COMPONENT_OFFSET_Y = 1;
         Frame frame = new Frame(this.getTheme(), stretcherlayout, ((Module.Category)entry.getKey()).getName());
         scrollpane = (Scrollpane)((Pair)entry.getValue()).getKey();
         frame.addChild(new Component[]{scrollpane});
         frame.addChild(new Component[]{(Component)((Pair)entry.getValue()).getValue()});
         scrollpane.setOriginOffsetY(0);
         scrollpane.setOriginOffsetX(0);
         frame.setCloseable(false);
         frame.setX(x);
         frame.setY(y);
         this.addChild(new Component[]{frame});
         nexty = Math.max(y + frame.getHeight() + 10, nexty);
         x += frame.getWidth() + 10;
         if ((float)x > (float)Wrapper.getMinecraft().field_71443_c / 1.2F) {
            y = nexty;
            nexty = y;
         }
      }

      this.addMouseListener(new MouseListener() {
         private boolean isBetween(int min, int val, int max) {
            return val <= max && val >= min;
         }

         public void onMouseDown(MouseListener.MouseButtonEvent event) {
            List<SettingsPanel> panels = ContainerHelper.getAllChildren(SettingsPanel.class, TurokGUI.this);
            Iterator var3 = panels.iterator();

            while(true) {
               SettingsPanel settingsPanel;
               int pX;
               int pY;
               do {
                  do {
                     if (!var3.hasNext()) {
                        return;
                     }

                     settingsPanel = (SettingsPanel)var3.next();
                  } while(!settingsPanel.isVisible());

                  int[] real = GUI.calculateRealPosition(settingsPanel);
                  pX = event.getX() - real[0];
                  pY = event.getY() - real[1];
               } while(this.isBetween(0, pX, settingsPanel.getWidth()) && this.isBetween(0, pY, settingsPanel.getHeight()));

               settingsPanel.setVisible(false);
            }
         }

         public void onMouseRelease(MouseListener.MouseButtonEvent event) {
         }

         public void onMouseDrag(MouseListener.MouseButtonEvent event) {
         }

         public void onMouseMove(MouseListener.MouseMoveEvent event) {
         }

         public void onScroll(MouseListener.MouseScrollEvent event) {
         }
      });
      frame_users = new Frame(this.getTheme(), new Stretcherlayout(1), ChatFormatting.RED + "Turok");
      frame_users.setCloseable(false);
      frame_users.setPinneable(true);
      frame_users.setMaximumWidth(600);
      Label users = new Label("");
      users.addTickListener(() -> {
         Minecraft mc = Minecraft.func_71410_x();
         String name = "";
         name = mc.field_71439_g.func_70005_c_();
         StringBuilder var10001 = (new StringBuilder()).append(name).append(ChatFormatting.RED).append(" - Turok ").append("0.5").append(" - ");
         Wrapper.getMinecraft();
         users.setText(var10001.append(Minecraft.field_71470_ab).toString());
      });
      frame_users.addChild(new Component[]{users});
      users.setFontRenderer(fontRendererBig);
      users.setShadow(false);
      frame_wattermark = new Frame(this.getTheme(), new Stretcherlayout(1), ChatFormatting.RED + "We are Turok");
      frame_wattermark.setCloseable(false);
      frame_wattermark.setPinneable(true);
      frame_wattermark.setMaximumWidth(600);
      Label wattermark = new Label("");
      wattermark.addTickListener(() -> {
         Minecraft mc = Minecraft.func_71410_x();
         String name = "";
         name = mc.field_71439_g.func_70005_c_();
         wattermark.setText(ChatFormatting.RED + "We Are Turok!");
      });
      frame_wattermark.addChild(new Component[]{wattermark});
      wattermark.setFontRenderer(fontRendererBig);
      wattermark.setShadow(false);
      frame_array = new Frame(this.getTheme(), new Stretcherlayout(1), ChatFormatting.RED + "Turok Modules Array");
      frame_array.setCloseable(false);
      frame_array.addChild(new Component[]{new ActiveModules()});
      frame_array.setPinneable(true);
      frame_array.setMaximumWidth(600);
      frame_coords = new Frame(this.getTheme(), new Stretcherlayout(1), ChatFormatting.RED + "Coordinates");
      frame_coords.setCloseable(false);
      frame_coords.setPinneable(true);
      frame_coords.setMaximumWidth(600);
      Label coords = new Label("");
      coords.addTickListener(() -> {
         Minecraft mc = Minecraft.func_71410_x();
         boolean on_nether = mc.field_71441_e.func_180494_b(mc.field_71439_g.func_180425_c()).func_185359_l().equals("Hell");
         float value = !on_nether ? 0.125F : 8.0F;
         int posX = (int)mc.field_71439_g.field_70165_t;
         int posY = (int)mc.field_71439_g.field_70163_u;
         int posZ = (int)mc.field_71439_g.field_70161_v;
         int poshX = (int)(mc.field_71439_g.field_70165_t * (double)value);
         int poshZ = (int)(mc.field_71439_g.field_70161_v * (double)value);
         coords.setText("");
         coords.addLine(ChatFormatting.RED + String.format(poshX + " - " + posY + " - " + poshZ));
         coords.addLine(ChatFormatting.BLUE + String.format(posX + " - " + posY + " - " + posZ));
      });
      frame_coords.addChild(new Component[]{coords});
      coords.setFontRenderer(fontRendererBig);
      coords.setShadow(false);
      frame_counts_totem = new Frame(this.getTheme(), new Stretcherlayout(1), ChatFormatting.RED + "Turok Info");
      frame_counts_totem.setCloseable(false);
      frame_counts_totem.setPinneable(true);
      frame_counts_totem.setMaximumWidth(600);
      Label count_totem = new Label("");
      count_totem.addTickListener(() -> {
         Minecraft mc = Minecraft.func_71410_x();
         int totems = 0;

         for(int items = 0; items < 45; ++items) {
            ItemStack itemStack = Wrapper.getMinecraft().field_71439_g.field_71071_by.func_70301_a(items);
            if (itemStack.func_77973_b() == Items.field_190929_cY) {
               totems += itemStack.field_77994_a;
            }
         }

         count_totem.setText(ChatFormatting.RED + "Totems " + ChatFormatting.GRAY + totems);
      });
      frame_counts_totem.addChild(new Component[]{count_totem});
      count_totem.setFontRenderer(fontRendererBig);
      count_totem.setShadow(false);
      frame_counts_gapple = new Frame(this.getTheme(), new Stretcherlayout(1), ChatFormatting.RED + "Turok Info");
      frame_counts_gapple.setCloseable(false);
      frame_counts_gapple.setPinneable(true);
      frame_counts_gapple.setMaximumWidth(600);
      Label count_gapple = new Label("");
      count_gapple.addTickListener(() -> {
         Minecraft mc = Minecraft.func_71410_x();
         int gapples = 0;

         for(int items = 0; items < 45; ++items) {
            ItemStack itemStack = Wrapper.getMinecraft().field_71439_g.field_71071_by.func_70301_a(items);
            if (itemStack.func_77973_b() == Items.field_151153_ao) {
               gapples += itemStack.field_77994_a;
            }
         }

         count_gapple.setText(ChatFormatting.RED + "Gapples " + ChatFormatting.GRAY + gapples);
      });
      frame_counts_gapple.addChild(new Component[]{count_gapple});
      count_gapple.setFontRenderer(fontRendererBig);
      count_gapple.setShadow(false);
      frame_counts_crystal = new Frame(this.getTheme(), new Stretcherlayout(1), ChatFormatting.RED + "Turok Info");
      frame_counts_crystal.setCloseable(false);
      frame_counts_crystal.setPinneable(true);
      frame_counts_crystal.setMaximumWidth(600);
      Label count_crystal = new Label("");
      count_crystal.addTickListener(() -> {
         Minecraft mc = Minecraft.func_71410_x();
         int crystals = 0;

         for(int items = 0; items < 45; ++items) {
            ItemStack itemStack = Wrapper.getMinecraft().field_71439_g.field_71071_by.func_70301_a(items);
            if (itemStack.func_77973_b() == Items.field_185158_cP) {
               crystals += itemStack.field_77994_a;
            }
         }

         count_crystal.setText(ChatFormatting.RED + "Crystals " + ChatFormatting.GRAY + crystals);
      });
      frame_counts_crystal.addChild(new Component[]{count_crystal});
      count_crystal.setFontRenderer(fontRendererBig);
      count_crystal.setShadow(false);
      frame_counts_exp = new Frame(this.getTheme(), new Stretcherlayout(1), ChatFormatting.RED + "Turok Info");
      frame_counts_exp.setCloseable(false);
      frame_counts_exp.setPinneable(true);
      frame_counts_exp.setMaximumWidth(600);
      Label count_exp = new Label("");
      count_exp.addTickListener(() -> {
         Minecraft mc = Minecraft.func_71410_x();
         int expbottles = 0;

         for(int items = 0; items < 45; ++items) {
            ItemStack itemStack = Wrapper.getMinecraft().field_71439_g.field_71071_by.func_70301_a(items);
            if (itemStack.func_77973_b() == Items.field_151062_by) {
               expbottles += itemStack.field_77994_a;
            }
         }

         count_exp.setText(ChatFormatting.RED + "EXPBottles " + ChatFormatting.GRAY + expbottles);
      });
      frame_counts_exp.addChild(new Component[]{count_exp});
      count_exp.setFontRenderer(fontRendererBig);
      count_exp.setShadow(false);
      frame_users.setY(60);
      frame_coords.setY(70);
      frame_array.setY(90);
      frame_wattermark.setY(100);
      frame_counts_totem.setY(10);
      frame_counts_crystal.setY(20);
      frame_counts_gapple.setY(30);
      frame_counts_exp.setY(40);
      this.addChild(new Component[]{frame_users});
      this.addChild(new Component[]{frame_coords});
      this.addChild(new Component[]{frame_array});
      this.addChild(new Component[]{frame_wattermark});
      this.addChild(new Component[]{frame_counts_totem});
      this.addChild(new Component[]{frame_counts_crystal});
      this.addChild(new Component[]{frame_counts_gapple});
      this.addChild(new Component[]{frame_counts_exp});
   }

   public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
      List<Entry<K, V>> list = new LinkedList(map.entrySet());
      Collections.sort(list, Comparator.comparing((o) -> {
         return (Comparable)o.getValue();
      }));
      Map<K, V> result = new LinkedHashMap();
      Iterator var3 = list.iterator();

      while(var3.hasNext()) {
         Entry<K, V> entry = (Entry)var3.next();
         result.put(entry.getKey(), entry.getValue());
      }

      return result;
   }

   public void destroyGUI() {
      this.kill();
   }

   public static void dock(Frame component) {
      Docking docking = component.getDocking();
      if (docking.isTop()) {
         component.setY(0);
      }

      if (docking.isBottom()) {
         component.setY(Wrapper.getMinecraft().field_71440_d / DisplayGuiScreen.getScale() - component.getHeight() - 0);
      }

      if (docking.isLeft()) {
         component.setX(0);
      }

      if (docking.isRight()) {
         component.setX(Wrapper.getMinecraft().field_71443_c / DisplayGuiScreen.getScale() - component.getWidth() - 0);
      }

      if (docking.isCenterHorizontal()) {
         component.setX(Wrapper.getMinecraft().field_71443_c / (DisplayGuiScreen.getScale() * 2) - component.getWidth() / 2);
      }

      if (docking.isCenterVertical()) {
         component.setY(Wrapper.getMinecraft().field_71440_d / (DisplayGuiScreen.getScale() * 2) - component.getHeight() / 2);
      }

   }

   static {
      nexty = y;
      state_arrays = true;
      state_counts = true;
      state_coords = true;
      state_users = false;
   }
}
