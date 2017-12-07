package slimeknights.harvesttweaks.tinkers;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import slimeknights.harvesttweaks.IPulseLogic;
import slimeknights.tconstruct.library.events.MaterialEvent;
import slimeknights.tconstruct.library.materials.HeadMaterialStats;

public class TinkerPulseLogic implements IPulseLogic {

  private static Logger log = LogManager.getLogger("HarvestTweaks-TConstruct");

  private final TinkerConfig config;

  public TinkerPulseLogic(TinkerConfig config) {
    this.config = config;
  }

  @Override
  public void applyChanges() {
    // nothing to do, since we handle everything via events
  }

  @SubscribeEvent
  public void onStatRegister(MaterialEvent.StatRegisterEvent<HeadMaterialStats> statRegisterEvent) {
    if(statRegisterEvent.stats instanceof HeadMaterialStats) {
      HeadMaterialStats oldStats = statRegisterEvent.newStats != null ? statRegisterEvent.newStats : statRegisterEvent.stats;
      int newHarvestLevel = config.materials.getOrDefault(statRegisterEvent.material.getIdentifier(), oldStats.harvestLevel);

      if(newHarvestLevel != oldStats.harvestLevel) {
        if(config.logChanges) {
          log.info(String.format("Changing harvest level of material %s from %d to %d",
                                 statRegisterEvent.material.getIdentifier(),
                                 oldStats.harvestLevel,
                                 newHarvestLevel));
        }
        HeadMaterialStats newStats = new HeadMaterialStats(oldStats.durability, oldStats.miningspeed, oldStats.attack, newHarvestLevel);
        statRegisterEvent.overrideResult(newStats);
      }

    }
  }
}
