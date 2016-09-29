package slimeknights.harvesttweaks.items;

import com.google.common.reflect.TypeToken;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import slimeknights.harvesttweaks.config.ConfigFile;
import slimeknights.mantle.configurate.commented.CommentedConfigurationNode;
import slimeknights.mantle.configurate.objectmapping.ObjectMappingException;
import slimeknights.mantle.configurate.objectmapping.Setting;
import slimeknights.mantle.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class ItemConfig extends ConfigFile {

  // registry tools
  @Setting
  Map<Item, Map<String, Integer>> tools = new HashMap<>();

  public ItemConfig() {
    super("Tools");
  }

  @Override
  public void insertDefaults() {
    for(Item item : Item.REGISTRY) {
      ItemStack stack = new ItemStack(item);
      Set<String> classes = item.getToolClasses(stack);
      classes.forEach(toolclass -> {
        try {
          tools.computeIfAbsent(item, x -> new HashMap<>()).putIfAbsent(toolclass, item.getHarvestLevel(stack, toolclass, null, null));
        } catch(Exception e) {
          // in case something happens in getHarvestLevel
        }
      });
    }
  }
}
