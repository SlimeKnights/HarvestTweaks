package slimeknights.harvesttweaks.config;

import net.minecraftforge.common.MinecraftForge;

import slimeknights.harvesttweaks.HarvestTweaks;
import slimeknights.mantle.config.AbstractConfig;
import slimeknights.mantle.config.AbstractConfigSyncPacket;

public class HarvestTweakConfigSyncPacket extends AbstractConfigSyncPacket {

  @Override
  protected AbstractConfig getConfig() {
    return HarvestTweaks.CONFIG;
  }

  @Override
  protected boolean sync() {
    if(super.sync()) {
      // clientside register only
      MinecraftForge.EVENT_BUS.register(ConfigSyncHandler.INSTANCE);
      return true;
    }
    return false;
  }
}
