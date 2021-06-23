package com;

import com.oldturok.turok.TurokMod;
import java.util.Map;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

public class MixinLoaderForge implements IFMLLoadingPlugin {
   private static boolean isObfuscatedEnvironment = false;

   public MixinLoaderForge() {
      TurokMod.turok_log.info("Turok mixins initialized");
      MixinBootstrap.init();
      Mixins.addConfiguration("turok.mixins.json");
      MixinEnvironment.getDefaultEnvironment().setObfuscationContext("searge");
      TurokMod.turok_log.info(MixinEnvironment.getDefaultEnvironment().getObfuscationContext());
   }

   public String[] getASMTransformerClass() {
      return new String[0];
   }

   public String getModContainerClass() {
      return null;
   }

   public String getSetupClass() {
      return null;
   }

   public void injectData(Map<String, Object> data) {
      isObfuscatedEnvironment = (Boolean)data.get("runtimeDeobfuscationEnabled");
   }

   public String getAccessTransformerClass() {
      return null;
   }
}
