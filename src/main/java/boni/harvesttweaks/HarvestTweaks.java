package boni.harvesttweaks;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.List;

import boni.harvesttweaks.blocks.BlocksModule;
import boni.harvesttweaks.tconstruct.TinkerModule;
import boni.harvesttweaks.vanilla.ItemsModule;

@Mod(modid = HarvestTweaks.MODID,
    version = HarvestTweaks.VERSION,
    dependencies = "required-after:Forge@[12.18.1.2073,);",
    acceptedMinecraftVersions = "[1.10.2, 1.11)"
)
public class HarvestTweaks
{
    public static final String MODID = "harvesttweaks";
    public static final String VERSION = "${version}";

    private List<Module> modules;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        modules.add(new BlocksModule());
        modules.add(new ItemsModule());

        if(Loader.isModLoaded("tconstruct")) {
            modules.add(new TinkerModule());
        }

        modules.forEach(module -> module.preInit(event));
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        modules.forEach(module -> module.init(event));
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        modules.forEach(module -> module.postInit(event));
    }

}
