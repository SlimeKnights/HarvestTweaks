package slimeknights.harvesttweaks.blocks;

import com.google.common.eventbus.Subscribe;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.oredict.OreDictionary;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.stream.IntStream;

import slimeknights.harvesttweaks.HarvestTweaks;
import slimeknights.harvesttweaks.IPulse;
import slimeknights.mantle.pulsar.pulse.Pulse;

/** Sets block harvest levels through registry names */
@Pulse(id = "blocks", forced = true)
public class BlockPulse implements IPulse {

  private static Logger log = LogManager.getLogger("HarvestTweaks-Blocks");

  private BlockConfig config;

  @Subscribe
  public void init(FMLInitializationEvent event) {
    config = HarvestTweaks.CONFIG.load(new BlockConfig(), BlockConfig.class);
    applyChanges();
  }

  @Override
  public void applyChanges() {
    // oredict before block, since block is more specific and can overwrite therefore
    //applyOredictChanges();
    applyBlockChanges();
  }

  private void applyOredictChanges() {
    config.oredict.forEach(
        (tool, oreHarvestLevels) -> oreHarvestLevels.forEach(
            (ore, level) -> applyChangeToOredict(ore, tool, level)
        )
    );
  }

  private void applyChangeToOredict(String ore, String tool, int level) {
    OreDictionary.getOres(ore).forEach(stack -> modifyBlock(stack, tool, level));
  }

  private void applyBlockChanges() {
    config.blocks.forEach(
        (tool, blockHarvestLevels) -> blockHarvestLevels.forEach(
          (blockMeta, level) -> modifyBlock(new ItemStack(blockMeta.block, 1, blockMeta.metadata), tool, level)
    ));
  }

  private void modifyBlock(ItemStack stack, String tool, int harvestLevel) {
    Block block = Block.getBlockFromItem(stack.getItem());
    if(block != null) {
      modifyBlock(block, stack.getItemDamage(), tool, harvestLevel);
    }
  }

  private void modifyBlock(Block block, int metadata, String tool, int harvestLevel) {
    IntStream metas;
    if(metadata == OreDictionary.WILDCARD_VALUE) {
      metas = IntStream.range(0, 16);
    }
    else {
      metas = IntStream.of(metadata);
    }

    metas.forEach(meta -> {
      try {
        IBlockState state = block.getStateFromMeta(meta);

        // only modify if there is something to modify
        if(!tool.equals(block.getHarvestTool(state)) || block.getHarvestLevel(state) != harvestLevel) {
          if(config.logChanges) {
            String oldTool = block.getHarvestTool(state);
            int oldLevel = block.getHarvestLevel(state);
            log.info(String.format("Changed Harvest Level of %s:%d from %s: %d to %s %d",
                                   block.getRegistryName(), meta,
                                   oldTool, oldLevel,
                                   tool, harvestLevel));
          }

          block.setHarvestLevel(tool, harvestLevel, state);

          if(block.getHarvestLevel(state) != harvestLevel || !tool.equals(block.getHarvestTool(state))) {
            log.warn("Changing of harvest capabilities is not supported by block " + block.getRegistryName() + ":" + meta);
          }
        }
      } catch(Exception e) {
        // exception can occur if stuff does weird things metadatas
        log.warn("An error occurred when trying to change harvest capabilities of block " + block.getRegistryName() + " with meta " + meta);
      }
    });
  }
}
