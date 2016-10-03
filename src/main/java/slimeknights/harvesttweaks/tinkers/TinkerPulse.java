package slimeknights.harvesttweaks.tinkers;

import com.google.common.eventbus.Subscribe;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import slimeknights.harvesttweaks.HarvestTweaks;
import slimeknights.harvesttweaks.IPulse;
import slimeknights.mantle.pulsar.pulse.Pulse;

/** Sets harvest levels of tinker materials */
@Pulse(id = TinkerPulse.MODID, modsRequired = TinkerPulse.MODID, forced = true)
public class TinkerPulse implements IPulse {

  public static final TinkerPulse INSTANCE = new TinkerPulse();

  static final String MODID = "tconstruct";

  private TinkerConfig config;
  private TinkerPulseLogic logic;

  @Subscribe
  public void preInit(FMLPreInitializationEvent event) {
    config = HarvestTweaks.CONFIG.load(new TinkerConfig(), TinkerConfig.class);
    logic = new TinkerPulseLogic(config);
    MinecraftForge.EVENT_BUS.register(logic);
  }

  @Subscribe
  public void init(FMLInitializationEvent event) {
    // load defaults again, since the stats are added after the initial load
    config.insertDefaults();
  }

  @Override
  public TinkerPulseLogic getLogic() {
    return logic;
  }
}
