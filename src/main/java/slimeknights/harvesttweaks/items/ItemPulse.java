package slimeknights.harvesttweaks.items;

import com.google.common.eventbus.Subscribe;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import slimeknights.harvesttweaks.HarvestTweaks;
import slimeknights.harvesttweaks.IPulse;
import slimeknights.mantle.pulsar.pulse.Pulse;

/** Sets item harvest levels through registry names */
@Pulse(id = "items", forced = true)
public class ItemPulse implements IPulse {

  private static Logger log = LogManager.getLogger("HarvestTweaks-Tools");

  private ItemConfig config;

  @Subscribe
  public void init(FMLInitializationEvent event) {
    config = HarvestTweaks.CONFIG.load(new ItemConfig(), ItemConfig.class);
    applyChanges();
  }


  @Override
  public void applyChanges() {
    if(config.logChanges) {
      log.info(HarvestTweaks.bigLogString("Applying item changes"));
    }
    config.tools.forEach((toolclass, harvestInfo) -> harvestInfo.forEach((item, level) -> updateHarvestLevel(item, toolclass, level)));
  }

  private void updateHarvestLevel(Item item, String toolclass, int harvestLevel) {
    ItemStack stack = new ItemStack(item);
    try {

      int oldHarvestLevel = item.getHarvestLevel(stack, toolclass, null, null);
      if(oldHarvestLevel != harvestLevel) {
        if(config.logChanges) {
          log.info(String.format("Changing tool harvest level of %s for %s from %d to %d",
                                 item.getRegistryName(), toolclass, oldHarvestLevel, harvestLevel));
        }
        item.setHarvestLevel(toolclass, harvestLevel);

        if(item.getHarvestLevel(stack, toolclass, null, null) != harvestLevel) {
          log.warn("Changing of tool harvest capabilities is not supported by item " + item.getRegistryName());
        }
      }
    } catch(Exception e) {
      log.warn("An error occurred when trying to change harvest capabilities of item " + item.getRegistryName());
    }
  }
}
