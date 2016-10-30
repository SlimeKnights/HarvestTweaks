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
import slimeknights.harvesttweaks.IPulseLogic;
import slimeknights.mantle.pulsar.pulse.Pulse;

/** Sets block harvest levels through registry names */
@Pulse(id = "blocks", forced = true)
public class BlockPulse implements IPulse {

  public static final BlockPulse INSTANCE = new BlockPulse();

  static Logger log = LogManager.getLogger("HarvestTweaks-Blocks");

  public BlockConfig config;
  private BlockPulseLogic logic;

  @Subscribe
  public void init(FMLInitializationEvent event) {
    config = HarvestTweaks.CONFIG.load(new BlockConfig(), BlockConfig.class);
    logic = new BlockPulseLogic(config);
    logic.applyChanges();
  }

  @Override
  public IPulseLogic getLogic() {
    return logic;
  }

}
