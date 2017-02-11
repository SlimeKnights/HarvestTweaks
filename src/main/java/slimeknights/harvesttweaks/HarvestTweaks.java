package slimeknights.harvesttweaks;

import com.google.common.collect.ImmutableList;

import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

import slimeknights.harvesttweaks.blocks.BlockPulse;
import slimeknights.harvesttweaks.config.Config;
import slimeknights.harvesttweaks.config.ConfigSyncHandler;
import slimeknights.harvesttweaks.config.HarvestTweakConfigSyncPacket;
import slimeknights.harvesttweaks.items.ItemPulse;
import slimeknights.harvesttweaks.tinkers.TinkerPulse;
import slimeknights.mantle.config.AbstractConfigFile;
import slimeknights.mantle.network.NetworkWrapper;
import slimeknights.mantle.pulsar.config.IConfiguration;
import slimeknights.mantle.pulsar.control.PulseManager;
import slimeknights.mantle.pulsar.pulse.PulseMeta;

@Mod(modid = HarvestTweaks.MODID,
    version = HarvestTweaks.VERSION,
    dependencies = "required-after:Forge@[12.18.1.2073,);" +
                   "required-after:mantle@[1.10.2-1.1.5,);" +
                   "after:*",
    acceptedMinecraftVersions = "[1.10.2, 1.11)"
//    guiFactory = "slimeknights.harvesttweaks.config.ConfigGui$GuiFactory"
)
public class HarvestTweaks {

  public static final String MODID = "harvesttweaks";
  public static final String VERSION = "${version}";

  public static Config CONFIG;

  private static List<IPulse> pulses = ImmutableList.<IPulse>builder()
      .add(BlockPulse.INSTANCE)
      .add(ItemPulse.INSTANCE)
      .add(TinkerPulse.INSTANCE)
      .build();

  public static final NetworkWrapper NETWORK = new NetworkWrapper(MODID + ":sync");

  private static PulseManager pulseManager;

  static {
    pulseManager = new PulseManager(new PulseConfiguration());
    pulses.forEach(pulseManager::registerPulse);
  }

  @EventHandler
  public void preInit(FMLPreInitializationEvent event) {
    // ensure forges harvestlevel stuff has been statically initialized
    new ForgeHooks();

    AbstractConfigFile.init();
    CONFIG = new Config();
    Config.setConfigDirectory(event.getModConfigurationDirectory());

    NETWORK.registerPacketClient(HarvestTweakConfigSyncPacket.class);
  }

  @EventHandler
  public void init(FMLInitializationEvent event) {
  }

  @EventHandler
  public void postInit(FMLPostInitializationEvent event) {
    CONFIG.save();

    MinecraftForge.EVENT_BUS.register(this);
    MinecraftForge.EVENT_BUS.register(new ExtraHarvestLevelHandler());
    if(event.getSide().isServer()) {
      MinecraftForge.EVENT_BUS.register(ConfigSyncHandler.INSTANCE);
    }
  }


  @SubscribeEvent
  public void update(ConfigChangedEvent.OnConfigChangedEvent event) {
    if(Config.configID.equals(event.getConfigID())) {
      pulses.forEach(iPulse -> iPulse.getLogic().applyChanges());
    }
  }

  public static String bigLogString(String text) {
    String line = new String(new char[20]).replace('\0', '=');
    return line + "- " + text + " -" + line;
  }

  // dummy config because we don't want a module config
  private static class PulseConfiguration implements IConfiguration {

    @Override
    public void load() {
      // nope
    }

    @Override
    public boolean isModuleEnabled(PulseMeta meta) {
      return true;
    }

    @Override
    public void flush() {
      // nope
    }
  }
}
