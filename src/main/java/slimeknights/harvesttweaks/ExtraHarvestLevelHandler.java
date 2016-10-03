package slimeknights.harvesttweaks;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


// This class handles harvest levels on the same block but where some metadata requires a tool,
// and some metadatas don't
public class ExtraHarvestLevelHandler {

  @SubscribeEvent
  public void breakSpeed(PlayerEvent.BreakSpeed event) {
    EntityPlayer player = event.getEntityPlayer();
    if(player == null) {
      return;
    }

    if(event.getState() == null || event.getState().getBlock().isAir(event.getState(), null, null)) {
      return;
    }

    IBlockState state = event.getState();
    Block block = state.getBlock();
    int hlvl = block.getHarvestLevel(state);

    // does the block require a tool?
    if(hlvl < 0) {
      // no, nothing to do
      return;
    }

    // block requires a harvest level, but does the material require a tool?
    if(!state.getMaterial().isToolNotRequired()) {
      // a tool is required, we don't have to do anything
      return;
    }

    // The tool does NOT require a tool, but has a harvest level for a tool set
    // we now manually check if this requiremnet is fulfilled

    String tool = block.getHarvestTool(state);
    ItemStack itemStack = player.getHeldItemMainhand();

    if(itemStack != null && itemStack.getItem() != null) {
      if(itemStack.getItem().getHarvestLevel(itemStack, tool, player, state) >= hlvl)
      // everything ok, correct tool
      {
        return;
      }
    }

    // we require a tool, but no fitting tool is present. we prevent any breaking
    event.setCanceled(true);
  }
}
