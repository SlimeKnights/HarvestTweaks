package slimeknights.harvesttweaks.tinkers;

import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import slimeknights.harvesttweaks.IPulseLogic;
import slimeknights.tconstruct.library.events.MaterialEvent;
import slimeknights.tconstruct.library.materials.HeadMaterialStats;

public class TinkerPulseLogic implements IPulseLogic {

  @Override
  public void applyChanges() {
    // nothing to do, since we handle everything via events
  }

  @SubscribeEvent
  public void onStatRegister(MaterialEvent.StatRegisterEvent<HeadMaterialStats> statRegisterEvent) {
    if(statRegisterEvent.stats instanceof HeadMaterialStats) {
      HeadMaterialStats oldStats = statRegisterEvent.stats;
      statRegisterEvent.newStats = new HeadMaterialStats(oldStats.durability, oldStats.miningspeed, oldStats.attack, 9);
      statRegisterEvent.setResult(Event.Result.ALLOW);
    }
  }
}
