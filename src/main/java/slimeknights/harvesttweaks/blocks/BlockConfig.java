package slimeknights.harvesttweaks.blocks;

import com.google.common.reflect.TypeToken;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import slimeknights.harvesttweaks.config.BlockMeta;
import slimeknights.harvesttweaks.config.ConfigFile;
import slimeknights.mantle.configurate.objectmapping.Setting;
import slimeknights.mantle.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class BlockConfig extends ConfigFile {

  // key = tool, Value = Block:Meta + Harvestlevel
  @Setting
  Map<String, Map<BlockMeta, Integer>> blocks = new HashMap<>();

  // key = tool, Value = Oredictionary-Entry + HarvestLevel
  @Setting
  Map<String, Map<String, Integer>> oredict = new HashMap<>();

  public BlockConfig() {
    super("Blocks");
  }

  @Override
  public void insertDefaults() {
    insertBlockDefaults();
    insertOredictDefaults();
  }

  private void insertBlockDefaults() {
    for(Block block : Block.REGISTRY) {
      List<IBlockState> validStates = new LinkedList<>();

      // check if all states have the same tool and harvest level
      String tool = block.getHarvestTool(block.getDefaultState());
      int level = block.getHarvestLevel(block.getDefaultState());
      boolean singleEntry = getValidBlockstates(block, validStates, tool, level);
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
  }

  private void insertOredictDefaults() {
    for(String ore : OreDictionary.getOreNames()) {
      for(ItemStack oreEntry : OreDictionary.getOres(ore)) {
        Block block = Block.getBlockFromItem(oreEntry.getItem());
        if(block == null) {
          continue;
        }

        String tool = block.getHarvestTool(block.getDefaultState());
        int level = block.getHarvestLevel(block.getDefaultState());
        boolean singleEntry = getValidBlockstates(block, new ArrayList<>(), tool, level);

        if(tool != null && singleEntry) {
          oredict.computeIfAbsent(tool, x -> new HashMap<>()).putIfAbsent(ore, block.getHarvestLevel(block.getDefaultState()));
          break;
        }
      }
    }
  }

  public boolean getValidBlockstates(Block block, List<IBlockState> validStates, String tool, int level) {
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

    return singleEntry;
  }
}
