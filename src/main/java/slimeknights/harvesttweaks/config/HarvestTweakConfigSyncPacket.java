package slimeknights.harvesttweaks.config;

import slimeknights.harvesttweaks.HarvestTweaks;
import slimeknights.mantle.config.AbstractConfig;
import slimeknights.mantle.config.AbstractConfigSyncPacket;

public class HarvestTweakConfigSyncPacket extends AbstractConfigSyncPacket {

  @Override
  protected AbstractConfig getConfig() {
    return HarvestTweaks.CONFIG;
  }
}
