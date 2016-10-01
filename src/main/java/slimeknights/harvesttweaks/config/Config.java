package slimeknights.harvesttweaks.config;

import com.google.common.reflect.TypeToken;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import slimeknights.harvesttweaks.HarvestTweaks;
import slimeknights.harvesttweaks.IPulse;
import slimeknights.mantle.configurate.commented.CommentedConfigurationNode;
import slimeknights.mantle.configurate.objectmapping.ObjectMappingException;
import slimeknights.tconstruct.library.Util;

public class Config {

  public static String configID = Util.MODID + ".config";
  public static File configDirectory;

  private List<Function<?, ?>> configFiles = new ArrayList<>();

  public static void setConfigDirectory(File modConfigDirectory) {
    configDirectory = new File(modConfigDirectory, "HarvestTweaks");
    configDirectory.mkdirs();
  }

  public void save() {
    configFiles.forEach(function -> function.apply(null));
  }

  public <T extends ConfigFile> T load(T configFile, Class<T> clazz) {
    try {
      CommentedConfigurationNode node = configFile.load();

      T val = node.getValue(TypeToken.of(clazz), configFile);
      val.insertDefaults();


      configFiles.add(configFile1 -> {
        this.save(val, clazz);
        return true;
      });

      return val;
    } catch(IOException | ObjectMappingException e) {
      e.printStackTrace();
    }
    return configFile;
  }

  public <T extends ConfigFile> void save(T configFile, Class<T> clazz) {
    try {
      CommentedConfigurationNode node = configFile.load();

      node.setValue(TypeToken.of(clazz), configFile);
      configFile.save(node);
    } catch(IOException | ObjectMappingException e) {
      e.printStackTrace();
    }
  }
}
