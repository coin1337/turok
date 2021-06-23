package com.oldturok.turok.module;

import com.google.common.base.Converter;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.oldturok.turok.TurokMod;
import com.oldturok.turok.event.events.RenderEvent;
import com.oldturok.turok.setting.Setting;
import com.oldturok.turok.setting.Settings;
import com.oldturok.turok.setting.builder.SettingBuilder;
import com.oldturok.turok.util.TurokBind;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

public class Module {
   private final String originalName = this.getAnnotation().name();
   private final Setting<String> name;
   private final String description;
   private final Module.Category category;
   private Setting<TurokBind> bind;
   private Setting<Boolean> enabled;
   public boolean alwaysListening;
   protected static final Minecraft mc = Minecraft.func_71410_x();
   public List<Setting> settingList;

   public Module() {
      this.name = this.register(Settings.s("Name", this.originalName));
      this.description = this.getAnnotation().description();
      this.category = this.getAnnotation().category();
      this.bind = this.register(Settings.custom("Bind", TurokBind.none(), new Module.BindConverter()).build());
      this.enabled = this.register(Settings.booleanBuilder("Enabled").withVisibility((aBoolean) -> {
         return false;
      }).withValue(false).build());
      this.settingList = new ArrayList();
      this.alwaysListening = this.getAnnotation().alwaysListening();
      this.registerAll(this.bind, this.enabled);
   }

   private Module.Info getAnnotation() {
      if (this.getClass().isAnnotationPresent(Module.Info.class)) {
         return (Module.Info)this.getClass().getAnnotation(Module.Info.class);
      } else {
         throw new IllegalStateException("No Annotation on class " + this.getClass().getCanonicalName() + "!");
      }
   }

   public void onUpdate() {
   }

   public void onRender() {
   }

   public void onWorldRender(RenderEvent event) {
   }

   public TurokBind getBind() {
      return (TurokBind)this.bind.getValue();
   }

   public String getBindName() {
      return ((TurokBind)this.bind.getValue()).toString();
   }

   public void setName(String name) {
      this.name.setValue(name);
      ModuleManager.updateLookup();
   }

   public String getOriginalName() {
      return this.originalName;
   }

   public String getName() {
      return (String)this.name.getValue();
   }

   public String getDescription() {
      return this.description;
   }

   public Module.Category getCategory() {
      return this.category;
   }

   public boolean isEnabled() {
      return (Boolean)this.enabled.getValue();
   }

   protected void onEnable() {
   }

   protected void onDisable() {
   }

   public void toggle() {
      this.setEnabled(!this.isEnabled());
   }

   public void enable() {
      this.enabled.setValue(true);
      this.onEnable();
      if (!this.alwaysListening) {
         TurokMod.EVENT_BUS.subscribe((Object)this);
      }

   }

   public void disable() {
      this.enabled.setValue(false);
      this.onDisable();
      if (!this.alwaysListening) {
         TurokMod.EVENT_BUS.unsubscribe((Object)this);
      }

   }

   public boolean isDisabled() {
      return !this.isEnabled();
   }

   public void setEnabled(boolean enabled) {
      boolean prev = (Boolean)this.enabled.getValue();
      if (prev != enabled) {
         if (enabled) {
            this.enable();
         } else {
            this.disable();
         }
      }

   }

   public String getHudInfo() {
      return null;
   }

   protected final void setAlwaysListening(boolean alwaysListening) {
      this.alwaysListening = alwaysListening;
      if (alwaysListening) {
         TurokMod.EVENT_BUS.subscribe((Object)this);
      }

      if (!alwaysListening && this.isDisabled()) {
         TurokMod.EVENT_BUS.unsubscribe((Object)this);
      }

   }

   public void destroy() {
   }

   protected void registerAll(Setting... settings) {
      Setting[] var2 = settings;
      int var3 = settings.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Setting setting = var2[var4];
         this.register(setting);
      }

   }

   protected <T> Setting<T> register(Setting<T> setting) {
      if (this.settingList == null) {
         this.settingList = new ArrayList();
      }

      this.settingList.add(setting);
      return SettingBuilder.register(setting, "modules." + this.originalName);
   }

   protected <T> Setting<T> register(SettingBuilder<T> builder) {
      if (this.settingList == null) {
         this.settingList = new ArrayList();
      }

      Setting<T> setting = builder.buildAndRegister("modules." + this.name);
      this.settingList.add(setting);
      return setting;
   }

   private class BindConverter extends Converter<TurokBind, JsonElement> {
      private BindConverter() {
      }

      protected JsonElement doForward(TurokBind bind) {
         return new JsonPrimitive(bind.toString());
      }

      protected TurokBind doBackward(JsonElement jsonElement) {
         String s = jsonElement.getAsString();
         if (s.equalsIgnoreCase("None")) {
            return TurokBind.none();
         } else {
            int key = -1;

            try {
               key = Keyboard.getKeyIndex(s.toUpperCase());
            } catch (Exception var5) {
            }

            return key == 0 ? TurokBind.none() : new TurokBind(key);
         }
      }

      // $FF: synthetic method
      BindConverter(Object x1) {
         this();
      }
   }

   @Retention(RetentionPolicy.RUNTIME)
   public @interface Info {
      String name();

      String description() default "Descriptionless";

      Module.Category category();

      boolean alwaysListening() default false;
   }

   public static enum Category {
      TUROK_COMBAT("Turok Combat", false),
      TUROK_MISC("Turok Misc", false),
      TUROK_MOVEMENT("Turok Movement", false),
      TUROK_PLAYER("Turok Player", false),
      TUROK_RENDER("Turok Render", false),
      TUROK_CHAT("Turok Chat", false),
      TUROK_HIDDEN("null", true);

      boolean hidden;
      String name;

      private Category(String name, boolean hidden) {
         this.name = name;
         this.hidden = hidden;
      }

      public boolean isHidden() {
         return this.hidden;
      }

      public String getName() {
         return this.name;
      }
   }
}
