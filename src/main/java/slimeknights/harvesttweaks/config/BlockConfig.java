package slimeknights.harvesttweaks.config;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import slimeknights.mantle.configurate.objectmapping.Setting;
import slimeknights.mantle.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class BlockConfig extends ConfigFile {

  // key = tool, Value = Block + Harvestlevel
  @Setting
  Map<String, Map<BlockMeta, Integer>> blocks = new HashMap<>();

  @Setting
  Map<String, Map<String, Integer>> oredict = new HashMap<>();

  public BlockConfig() {
    super("Blocks");
  }

  @Override
  public void insertDefaults() {
    // fill in blocks
    for(Block block : Block.REGISTRY) {
      List<IBlockState> validStates = new LinkedList<>();

      // check if all states have the same tool and harvest level
      String tool = block.getHarvestTool(block.getDefaultState());
      int level = block.getHarvestLevel(block.getDefaultState());
      boolean singleEntry = true;
      for(int i = 0; i < 16; i++) {
        IBlockState state;
        try {
          state = block.getStateFromMeta(i);
        } catch(Exception e) {
          // invalid state
          continue;
        }
        if(block.getMetaFromState(state) == i) {
          validStates.add(state);

          if(!Objects.equals(tool, block.getHarvestTool(state)) || level != block.getHarvestLevel(state)) {
            singleEntry = false;
          }
        }
      }

      if(singleEntry) {
        if(tool != null) {
          blocks.computeIfAbsent(tool, x -> new HashMap<>()).putIfAbsent(new BlockMeta(block, -1), level);
        }
      }
      else {
        validStates.forEach(state -> {
          String harvestTool = block.getHarvestTool(state);
          blocks.computeIfAbsent(harvestTool, x -> new HashMap<>()).putIfAbsent(BlockMeta.of(state), block.getHarvestLevel(state));
        });
      }
    }

    // fill in oredict
    for(String ore : OreDictionary.getOreNames()) {
      for(ItemStack oreEntry : OreDictionary.getOres(ore)) {
        Block block = Block.getBlockFromItem(oreEntry.getItem());
        if(block == null) {
          continue;
        }

        String tool = block.getHarvestTool(block.getDefaultState());
        if(tool != null) {
          oredict.computeIfAbsent(tool, x -> new HashMap<>()).putIfAbsent(ore, block.getHarvestLevel(block.getDefaultState()));
          break;
        }
      }
    }
  }
}
