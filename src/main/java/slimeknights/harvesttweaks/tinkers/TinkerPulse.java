package slimeknights.harvesttweaks.tinkers;

import com.google.common.eventbus.Subscribe;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import slimeknights.harvesttweaks.IPulse;
import slimeknights.mantle.pulsar.pulse.Pulse;

/** Sets harvest levels of tinker materials */
@Pulse(id = TinkerPulse.MODID, modsRequired = TinkerPulse.MODID, forced = true)
public class TinkerPulse implements IPulse {

  public static final TinkerPulse INSTANCE = new TinkerPulse();

  static final String MODID = "tconstruct";

  private TinkerPulseLogic logic;

  @Subscribe
  public void preInit(FMLPreInitializationEvent event) {
    logic = new TinkerPulseLogic();
    MinecraftForge.EVENT_BUS.register(logic);
  }

  @Override
  public TinkerPulseLogic getLogic() {
    return logic;
  }
}
