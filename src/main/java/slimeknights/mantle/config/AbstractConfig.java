package slimeknights.mantle.config;

import com.google.common.reflect.TypeToken;

import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import slimeknights.mantle.configurate.commented.CommentedConfigurationNode;
import slimeknights.mantle.configurate.objectmapping.ObjectMappingException;

public abstract class AbstractConfig {

  List<AbstractConfigFile> configFileList = new ArrayList<>();
  private List<Function<?, ?>> configFiles = new ArrayList<>();

  public void save() {
    configFiles.forEach(function -> function.apply(null));
  }

  public <T extends AbstractConfigFile> T load(T configFile, Class<T> clazz) {
    try {
      CommentedConfigurationNode node = configFile.load();

      T val = node.getValue(TypeToken.of(clazz), configFile);
      val.insertDefaults();

      configFileList.add(val);
      // note: this is a workaround for generics because the generic info has to match the same class
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

  public <T extends AbstractConfigFile> void save(T configFile, Class<T> clazz) {
    if(configFile.needsSaving()) {
      try {
        CommentedConfigurationNode node = configFile.load();

        node.setValue(TypeToken.of(clazz), configFile);
        configFile.save(node);
      } catch(IOException | ObjectMappingException e) {
        e.printStackTrace();
      }
    }
    configFile.clearNeedsSaving();
  }
}
