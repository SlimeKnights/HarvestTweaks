package slimeknights.harvesttweaks.config;

import slimeknights.harvesttweaks.HarvestTweaks;
import slimeknights.mantle.config.AbstractConfig;
import slimeknights.mantle.config.ConfigSyncPacket;

public class HarvestTweakConfigSyncPacket extends ConfigSyncPacket {

  @Override
  protected AbstractConfig getConfig() {
    return HarvestTweaks.CONFIG;
  }
}
