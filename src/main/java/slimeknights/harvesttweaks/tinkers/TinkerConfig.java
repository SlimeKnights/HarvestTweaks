package slimeknights.harvesttweaks.tinkers;

import java.util.HashMap;
import java.util.Map;

import slimeknights.harvesttweaks.config.ConfigFile;
import slimeknights.mantle.configurate.objectmapping.Setting;
import slimeknights.mantle.configurate.objectmapping.serialize.ConfigSerializable;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.HeadMaterialStats;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.materials.MaterialTypes;

@ConfigSerializable
public class TinkerConfig extends ConfigFile {

  // key = material id, value = harvestlevel
  @Setting
  Map<String, Integer> materials = new HashMap<>();

  public TinkerConfig() {
    super("TinkersConstruct");
  }

  @Override
  public void insertDefaults() {
    if(materials.isEmpty()) {
      for(Material material : TinkerRegistry.getAllMaterialsWithStats(MaterialTypes.HEAD)) {
        HeadMaterialStats stat = material.getStats(MaterialTypes.HEAD);
        materials.putIfAbsent(material.getIdentifier(), stat.harvestLevel);
      }
    }
  }
}
