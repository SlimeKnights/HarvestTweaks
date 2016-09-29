package slimeknights.harvesttweaks.config;

import com.google.common.reflect.TypeToken;

import java.io.IOException;

import slimeknights.mantle.configurate.ConfigurationOptions;
import slimeknights.mantle.configurate.commented.CommentedConfigurationNode;
import slimeknights.mantle.configurate.hocon.HoconConfigurationLoader;
import slimeknights.mantle.configurate.loader.ConfigurationLoader;
import slimeknights.mantle.configurate.objectmapping.ObjectMappingException;

public class Config {

  // config files
  BlockConfig blockConfig;
  ToolConfig toolConfig;
  TinkerMaterialConfig tinkerMaterialConfig;

  public void load() {
    blockConfig = load(new BlockConfig(), BlockConfig.class);
    blockConfig.insertDefaults();
  }


  public void save() {
    save(blockConfig, BlockConfig.class);
  }

  private <T extends ConfigFile> T load(T configFile, Class<T> clazz) {
    try {
      CommentedConfigurationNode node = configFile.load();

      return node.getValue(TypeToken.of(clazz), configFile);
    } catch(IOException | ObjectMappingException e) {
      e.printStackTrace();
    }
    return configFile;
  }

  private <T extends ConfigFile> void save(T configFile, Class<T> clazz) {
    try {
      CommentedConfigurationNode node = configFile.load();

      node.setValue(TypeToken.of(clazz), configFile);
      configFile.save(node);
    } catch(IOException | ObjectMappingException e) {
      e.printStackTrace();
    }
  }
}
