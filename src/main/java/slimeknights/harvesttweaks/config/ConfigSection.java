package slimeknights.harvesttweaks.config;

import java.util.HashMap;
import java.util.Map;

import slimeknights.mantle.configurate.objectmapping.Setting;
import slimeknights.mantle.configurate.objectmapping.serialize.ConfigSerializable;

public class ConfigSection<T> {

  @Setting
  Map<T, Integer> entries = new HashMap<>();
}
