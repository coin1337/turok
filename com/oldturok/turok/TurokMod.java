package com.oldturok.turok;

import com.google.common.base.Converter;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.oldturok.TurokRPC;
import com.oldturok.turok.commands.TurokChatCommand;
import com.oldturok.turok.event.ForgeEventProcessor;
import com.oldturok.turok.gui.rgui.component.AlignedComponent;
import com.oldturok.turok.gui.rgui.component.Component;
import com.oldturok.turok.gui.rgui.component.container.use.Frame;
import com.oldturok.turok.gui.rgui.util.ContainerHelper;
import com.oldturok.turok.gui.rgui.util.Docking;
import com.oldturok.turok.gui.turok.TurokGUI;
import com.oldturok.turok.module.Module;
import com.oldturok.turok.module.ModuleManager;
import com.oldturok.turok.setting.Setting;
import com.oldturok.turok.setting.Settings;
import com.oldturok.turok.setting.SettingsRegister;
import com.oldturok.turok.util.LagCompensator;
import com.oldturok.turok.util.TurokChatManager;
import com.oldturok.turok.util.Wrapper;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.stream.Stream;
import me.zero.alpine.EventBus;
import me.zero.alpine.EventManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(
   modid = "turok",
   name = "ᴛᴜʀѳᴋ",
   version = "0.5"
)
public class TurokMod {
   public static final String TUROK_MOD_ID = "turok";
   public static final String TUROK_MOD_NAME = "ᴛᴜʀѳᴋ";
   public static final String TUROK_MOD_VERSION = "0.5";
   public static final int TUROK_GUI_BUTTON = 25;
   public static String TUROK_CHAT_PREFIX = "-";
   private static final String TUROK_CONFIG_FRAMES = "HUD.json";
   private static final String TUROK_CONFIG_BINDS = "FriendAndConfigs.json";
   private static final String TUROK_FRIENDS_LIST = "Friends.json";
   private static final String TUROK_CONFIG_FOLDER = "Turok/";
   public static final Logger turok_log = LogManager.getLogger("turok");
   public static final EventBus EVENT_BUS = new EventManager();
   public TurokChatCommand turok_chat_manager;
   public TurokGUI gui_manager;
   public Setting<JsonObject> frames_data = Settings.custom("frames", new JsonObject(), new Converter<JsonObject, JsonObject>() {
      protected JsonObject doForward(JsonObject jsonObject) {
         return jsonObject;
      }

      protected JsonObject doBackward(JsonObject jsonObject) {
         return jsonObject;
      }
   }).buildAndRegister("");
   @Instance
   private static TurokMod INSTANCE;

   @EventHandler
   public void init(FMLInitializationEvent event) {
      turok_log.info("\n\n----------- Turok Loading Configs -----------");
      ModuleManager.initialize();
      turok_log.info("Starting modules and util loader.");
      turok_log.info("Loading: Modules util. - State: Init.");
      Stream var10000 = ModuleManager.getModules().stream().filter((module) -> {
         return module.alwaysListening;
      });
      EventBus var10001 = EVENT_BUS;
      var10000.forEach(var10001::subscribe);
      MinecraftForge.EVENT_BUS.register(new ForgeEventProcessor());
      turok_log.info("Loading: ForgeEvents util. - State: Loaded.");
      SettingsRegister.register("chatprefix", TurokChatManager.prefix);
      MinecraftForge.EVENT_BUS.register(this.turok_chat_manager = new TurokChatCommand());
      turok_log.info("Loading: TurokChatManager util. - State: Loaded.");
      LagCompensator.INSTANCE = new LagCompensator();
      turok_log.info("Loading: LagCompensator util. - State: Loaded.");
      Wrapper.init();
      turok_log.info("Loading: Wrapper util. - State: Loaded.");
      this.gui_manager = new TurokGUI();
      this.gui_manager.initializeGUI();
      turok_log.info("Loading: TurokGUI util. - State: Loaded.");
      TurokFriends.init_friends_list();
      turok_log.info("Loading: TurokFriends util. - State: Loaded.");
      load_config();
      turok_log.info("Loading: TurokConfig util. - State: Loaded.");
      TurokChatCommand.turok_update_commands();
      turok_log.info("Finishing load: TurokChatManager util. - State: Loaded.");
      ModuleManager.updateLookup();
      ModuleManager.getModules().stream().filter(Module::isEnabled).forEach(Module::enable);
      turok_log.info("Finishing load: Modules util. - State: Loaded.");
      TurokRPC.start();
      turok_log.info("Loading: TurokRPC util. - State: Loaded.");
      ModuleManager.getModuleByName("FreeCamera").disable();
      if (ModuleManager.getModuleByName("TurokHUD").isDisabled()) {
         ModuleManager.getModuleByName("TurokHUD").enable();
      }

      turok_log.info("Disabling buggy modules.");
      turok_log.info("TurokLoadUtil loaded 10.");
      turok_log.info("Turok started no problems.");
   }

   public static void load_config() {
      try {
         load_configs();
      } catch (IOException var1) {
         var1.printStackTrace();
      }

   }

   public static void load_configs() throws IOException {
      String turok_frames_name = "Turok/HUD.json";
      String turok_binds_name = "Turok/FriendAndConfigs.json";
      String turok_frinds_name = "Turok/Friends.json";
      Path turok_config_frames = Paths.get(turok_frames_name);
      Path turok_config_binds = Paths.get(turok_binds_name);
      Path turok_friends_list = Paths.get(turok_frinds_name);
      Path folder_bind = Paths.get("Turok/");
      if (!Files.exists(folder_bind, new LinkOption[0])) {
         Files.createDirectories(folder_bind);
      }

      if (!Files.exists(turok_config_frames, new LinkOption[0])) {
         Files.createFile(turok_config_frames);
      }

      if (!Files.exists(turok_config_binds, new LinkOption[0])) {
         Files.createFile(turok_config_binds);
      }

      TurokConfig.load_frames(turok_config_frames);
      TurokConfig.load_binds(turok_config_binds);
      JsonObject gui = (JsonObject)INSTANCE.frames_data.getValue();
      Iterator var8 = gui.entrySet().iterator();

      while(var8.hasNext()) {
         Entry<String, JsonElement> entry = (Entry)var8.next();
         Optional<Component> optional = INSTANCE.gui_manager.getChildren().stream().filter((component) -> {
            return component instanceof Frame;
         }).filter((component) -> {
            return ((Frame)component).getTitle().equals(entry.getKey());
         }).findFirst();
         if (optional.isPresent()) {
            JsonObject object = ((JsonElement)entry.getValue()).getAsJsonObject();
            Frame frame = (Frame)optional.get();
            frame.setX(object.get("x").getAsInt());
            frame.setY(object.get("y").getAsInt());
            Docking docking = Docking.values()[object.get("docking").getAsInt()];
            if (docking.isLeft()) {
               ContainerHelper.setAlignment(frame, AlignedComponent.Alignment.LEFT);
            } else if (docking.isRight()) {
               ContainerHelper.setAlignment(frame, AlignedComponent.Alignment.RIGHT);
            } else if (docking.isCenterVertical()) {
               ContainerHelper.setAlignment(frame, AlignedComponent.Alignment.CENTER);
            }

            frame.setDocking(docking);
            frame.setMinimized(object.get("minimized").getAsBoolean());
            frame.setPinned(object.get("pinned").getAsBoolean());
         } else {
            System.err.println("Found GUI config entry for " + (String)entry.getKey() + ", but found no frame with that name");
         }
      }

      get_instance().get_gui_manager().getChildren().stream().filter((component) -> {
         return component instanceof Frame && ((Frame)component).isPinneable() && component.isVisible();
      }).forEach((component) -> {
         component.setOpacity(0.0F);
      });
   }

   public static void save_config() {
      try {
         save_unsafe();
      } catch (IOException var1) {
         var1.printStackTrace();
      }

   }

   public static void save_unsafe() throws IOException {
      JsonObject object = new JsonObject();
      INSTANCE.gui_manager.getChildren().stream().filter((component) -> {
         return component instanceof Frame;
      }).map((component) -> {
         return (Frame)component;
      }).forEach((frame) -> {
         JsonObject frame_object = new JsonObject();
         frame_object.add("x", new JsonPrimitive(frame.getX()));
         frame_object.add("y", new JsonPrimitive(frame.getY()));
         frame_object.add("docking", new JsonPrimitive(Arrays.asList(Docking.values()).indexOf(frame.getDocking())));
         frame_object.add("minimized", new JsonPrimitive(frame.isMinimized()));
         frame_object.add("pinned", new JsonPrimitive(frame.isPinned()));
         object.add(frame.getTitle(), frame_object);
      });
      INSTANCE.frames_data.setValue(object);
      Path file_frames = Paths.get("Turok/HUD.json");
      Path file_bind = Paths.get("Turok/FriendAndConfigs.json");
      TurokConfig.save_frames(file_frames, true);
      TurokConfig.save_binds(file_bind, false);
      ModuleManager.getModules().forEach(Module::destroy);
   }

   public static boolean name_valid(String file_) {
      File file = new File(file_);

      try {
         file.getCanonicalPath();
         return true;
      } catch (IOException var3) {
         return false;
      }
   }

   public static TurokMod get_instance() {
      return INSTANCE;
   }

   public TurokGUI get_gui_manager() {
      return this.gui_manager;
   }
}
