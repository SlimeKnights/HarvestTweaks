package slimeknights.harvesttweaks.tinkers;

import com.google.common.eventbus.Subscribe;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import slimeknights.harvesttweaks.IPulse;
import slimeknights.mantle.pulsar.pulse.Pulse;
import slimeknights.tconstruct.library.events.MaterialEvent;
import slimeknights.tconstruct.library.materials.HeadMaterialStats;

/** Sets harvest levels of tinker materials */
@Pulse(id = TinkerPulse.MODID, modsRequired = TinkerPulse.MODID, forced = true)
public class TinkerPulse implements IPulse {
  static final String MODID = "tconstruct";

  @Subscribe
  public void preInit(FMLPreInitializationEvent event) {
    MinecraftForge.EVENT_BUS.register(this);
  }

  @SubscribeEvent
  public void onStatRegister(MaterialEvent.StatRegisterEvent<HeadMaterialStats> statRegisterEvent) {
    if(statRegisterEvent.stats instanceof HeadMaterialStats) {
      HeadMaterialStats oldStats = statRegisterEvent.stats;
      statRegisterEvent.newStats = new HeadMaterialStats(oldStats.durability, oldStats.miningspeed, oldStats.attack, 9);
      statRegisterEvent.setResult(Event.Result.ALLOW);
    }
  }

  @Override
  public void applyChanges() {

  }
}
