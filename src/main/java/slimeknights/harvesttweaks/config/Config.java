package slimeknights.harvesttweaks.config;

import com.google.common.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import slimeknights.mantle.configurate.commented.CommentedConfigurationNode;
import slimeknights.mantle.configurate.objectmapping.ObjectMappingException;

public class Config {

  private List<Function<?, ?>> configFiles = new ArrayList<>();

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
