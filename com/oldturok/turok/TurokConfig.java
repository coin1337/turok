package com.oldturok.turok;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.oldturok.turok.setting.Setting;
import com.oldturok.turok.setting.SettingsRegister;
import com.oldturok.turok.setting.converter.Convertable;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Map.Entry;

public class TurokConfig {
   public static JsonObject load(Boolean type) {
      return type ? create_config_frames(SettingsRegister.ROOT) : create_config_binds(SettingsRegister.ROOT);
   }

   public static JsonObject create_config_frames(SettingsRegister setting) {
      JsonObject object = new JsonObject();
      Iterator var2 = setting.settingHashMap.entrySet().iterator();

      while(var2.hasNext()) {
         Entry<String, Setting> entry = (Entry)var2.next();
         Setting setting_register = (Setting)entry.getValue();
         if (setting_register instanceof Convertable && setting_register.getName().equals("frames")) {
            object.add((String)entry.getKey(), (JsonElement)setting_register.converter().convert(setting_register.getValue()));
         }
      }

      return object;
   }

   public static JsonObject create_config_binds(SettingsRegister setting) {
      JsonObject object = new JsonObject();
      Iterator var2 = setting.registerHashMap.entrySet().iterator();

      Entry entry;
      while(var2.hasNext()) {
         entry = (Entry)var2.next();
         object.add((String)entry.getKey(), create_config_binds((SettingsRegister)entry.getValue()));
      }

      var2 = setting.settingHashMap.entrySet().iterator();

      while(var2.hasNext()) {
         entry = (Entry)var2.next();
         Setting setting_register = (Setting)entry.getValue();
         if (setting_register instanceof Convertable) {
            object.add((String)entry.getKey(), (JsonElement)setting_register.converter().convert(setting_register.getValue()));
            if (setting_register.getName().equals("frames")) {
               object.remove((String)entry.getKey());
            }
         }
      }

      return object;
   }

   public static void start_save(OutputStream stream, boolean type) throws IOException {
      Gson gson_gson = (new GsonBuilder()).setPrettyPrinting().create();
      String string_json = "";
      if (type) {
         string_json = gson_gson.toJson(load(type));
      } else {
         string_json = gson_gson.toJson(load(type));
      }

      BufferedWriter buffer_file = new BufferedWriter(new OutputStreamWriter(stream));
      buffer_file.write(string_json);
      buffer_file.close();
   }

   public static void save_frames(Path path, boolean type) throws IOException {
      start_save(Files.newOutputStream(path), type);
   }

   public static void save_binds(Path path, boolean type) throws IOException {
      start_save(Files.newOutputStream(path), type);
   }

   public static void load_frames(Path path) throws IOException {
      InputStream stream = Files.newInputStream(path);
      load_config_frames(stream);
      stream.close();
   }

   public static void load_binds(Path path) throws IOException {
      InputStream stream = Files.newInputStream(path);
      load_config_binds(stream);
      stream.close();
   }

   public static void load_config_frames(InputStream stream) {
      try {
         start_load((new JsonParser()).parse(new InputStreamReader(stream)).getAsJsonObject());
      } catch (IllegalStateException var2) {
         TurokMod.turok_log.error("Failed to load the frames file. Try delet it.");
         start_load(new JsonObject());
      }

   }

   public static void load_config_binds(InputStream stream) {
      try {
         start_load((new JsonParser()).parse(new InputStreamReader(stream)).getAsJsonObject());
      } catch (IllegalStateException var2) {
         TurokMod.turok_log.error("Failed to load the binds file. Try delet it.");
         start_load(new JsonObject());
      }

   }

   public static void start_load(JsonObject json) {
      load_configs(SettingsRegister.ROOT, json);
   }

   public static void load_configs(SettingsRegister setting, JsonObject json) {
      Iterator var2 = json.entrySet().iterator();

      while(var2.hasNext()) {
         Entry<String, JsonElement> entry = (Entry)var2.next();
         String key = (String)entry.getKey();
         JsonElement element = (JsonElement)entry.getValue();
         if (setting.registerHashMap.containsKey(key)) {
            load_configs(setting.subregister(key), element.getAsJsonObject());
         } else {
            Setting setting_register = setting.getSetting(key);
            if (setting_register != null) {
               setting_register.setValue(setting_register.converter().reverse().convert(element));
            }
         }
      }

   }
}
