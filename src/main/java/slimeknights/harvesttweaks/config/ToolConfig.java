package slimeknights.harvesttweaks.config;

import net.minecraft.item.Item;

import java.util.Map;

import slimeknights.mantle.configurate.commented.CommentedConfigurationNode;
import slimeknights.mantle.configurate.objectmapping.ObjectMappingException;
import slimeknights.mantle.configurate.objectmapping.Setting;
import slimeknights.mantle.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class ToolConfig extends ConfigFile {

  // registry tools
  //@Setting
  //Map<Item> tools;

  public ToolConfig() {
    super("Tools");
  }

  @Override
  public void insertDefaults() {

  }
}
