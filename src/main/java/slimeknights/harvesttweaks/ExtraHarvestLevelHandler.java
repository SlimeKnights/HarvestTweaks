package slimeknights.harvesttweaks;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.function.Predicate;

import slimeknights.harvesttweaks.tinkers.TinkerPulse;


// This class handles harvest levels on the same block but where some metadata requires a tool,
// and some metadatas don't
public class ExtraHarvestLevelHandler {

  private static Predicate<ItemStack> ticIsBrokenSupport;

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

    // The tool does NOT require a tool, but has a harvest level for a tool set
    // we now manually check if this requiremnet is fulfilled

    String tool = block.getHarvestTool(state);
    ItemStack itemStack = player.getHeldItemMainhand();

    if(!itemStack.isEmpty()) {
      if(itemStack.getItem().getHarvestLevel(itemStack, tool, player, state) >= hlvl
         || ticIsBrokenSupport.test(itemStack))
      // everything ok, correct tool
      {
        return;
      }
    }

    // block requires a harvest level, but does the material require a tool?
    if(!state.getMaterial().isToolNotRequired()) {
      // a tool is required, if it's too weak ensure that the breakspeed is low
      event.setNewSpeed(1f);
    }
    else {
      // we require a tool, but no tool would be required by default. we prevent any breaking
      event.setCanceled(true);
    }
  }

  public static void initTicSupport() {
    if(HarvestTweaks.pulseManager.isPulseLoaded(TinkerPulse.MODID)) {
      ticIsBrokenSupport = slimeknights.tconstruct.library.utils.ToolHelper::isBroken;
    }
    else {
      ticIsBrokenSupport = o -> true;
    }
  }
}
