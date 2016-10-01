package slimeknights.harvesttweaks.config;

import slimeknights.mantle.config.AbstractConfigFile;
import slimeknights.mantle.configurate.objectmapping.Setting;
import slimeknights.mantle.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public abstract class ConfigFile extends AbstractConfigFile {

  @Setting(comment = "If true, all changes made by this file will be logged")
  public boolean logChanges = false;

  public ConfigFile(String name) {
    super(Config.configDirectory, name);
  }

  public abstract void insertDefaults();

}
