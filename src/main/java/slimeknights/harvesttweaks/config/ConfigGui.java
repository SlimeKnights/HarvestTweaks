package slimeknights.harvesttweaks.config;

import com.google.common.collect.ImmutableList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.util.List;
import java.util.Set;

import slimeknights.harvesttweaks.HarvestTweaks;
import slimeknights.harvesttweaks.blocks.BlockConfig;
import slimeknights.harvesttweaks.blocks.BlockPulse;

public class ConfigGui extends GuiConfig {

  public ConfigGui(GuiScreen parentScreen) {
    super(parentScreen, getGuiElements(), HarvestTweaks.MODID, Config.configID, false, false, "HarvestTweaks Configuration");
  }

  private static List<IConfigElement> getGuiElements() {
    return ImmutableList.of(new ConfigClassCategory("Blocks", "blocks.lang", BlockConfig.class, BlockPulse.INSTANCE.config));
  }

  public static class GuiFactory implements IModGuiFactory {

    @Override
    public void initialize(Minecraft minecraftInstance) {

    }

    @Override
    public boolean hasConfigGui() {
      return false;
    }

    @Override
    public GuiScreen createConfigGui(GuiScreen parentScreen) {
      return null;
    }

    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
      // dead code, never called
      return null;
    }
  }
}
