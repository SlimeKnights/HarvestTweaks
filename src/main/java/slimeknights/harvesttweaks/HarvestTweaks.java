package slimeknights.harvesttweaks;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import slimeknights.harvesttweaks.blocks.BlockPulse;
import slimeknights.harvesttweaks.items.ItemPulse;
import slimeknights.harvesttweaks.tinkers.TinkerPulse;
import slimeknights.mantle.pulsar.config.IConfiguration;
import slimeknights.mantle.pulsar.control.PulseManager;
import slimeknights.mantle.pulsar.pulse.PulseMeta;

@Mod(modid = HarvestTweaks.MODID,
    version = HarvestTweaks.VERSION,
    dependencies = "required-after:Forge@[12.18.1.2073,);",
    acceptedMinecraftVersions = "[1.10.2, 1.11)"
)
public class HarvestTweaks
{
    public static final String MODID = "harvesttweaks";
    public static final String VERSION = "${version}";

    private static PulseManager pulseManager;
    static {
        pulseManager = new PulseManager(new PulseConfiguration());
        pulseManager.registerPulse(new BlockPulse());
        pulseManager.registerPulse(new ItemPulse());
        pulseManager.registerPulse(new TinkerPulse());
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {

    }

    @EventHandler
    public void init(FMLInitializationEvent event) {

    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {

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
