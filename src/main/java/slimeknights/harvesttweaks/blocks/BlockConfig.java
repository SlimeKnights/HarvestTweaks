package slimeknights.harvesttweaks.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import slimeknights.harvesttweaks.config.ConfigFile;
import slimeknights.mantle.config.BlockMeta;
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
    if(blocks.isEmpty()) {
      insertBlockDefaults();
    }
    if(oredict.isEmpty()) {
      insertOredictDefaults();
    }
  }

  private void insertBlockDefaults() {
    for(Block block : Block.REGISTRY) {
      List<IBlockState> validStates = new LinkedList<>();

      // check if all states have the same tool and harvest level
      String tool = block.getHarvestTool(block.getDefaultState());
      int level = getActualHarvestLevel(block.getDefaultState());
      boolean singleEntry = getValidBlockstates(block, validStates, tool, level);
      if(singleEntry) {
        if(tool != null) {
          blocks.computeIfAbsent(tool, x -> new HashMap<>()).computeIfAbsent(new BlockMeta(block, -1), blockMeta -> {
            setNeedsSaving();
            return level;
          });
        }
      }
      else {
        validStates.forEach(state -> {
          String harvestTool = block.getHarvestTool(state);
          blocks.computeIfAbsent(harvestTool, x -> new HashMap<>()).computeIfAbsent(BlockMeta.of(state), blockMeta -> {
            setNeedsSaving();
            return getActualHarvestLevel(state);
          });
        });
      }
    }
  }

  private void insertOredictDefaults() {
    for(String ore : OreDictionary.getOreNames()) {
      String matchedTool = null;
      Map<String, Integer> toolSingleEntry = new HashMap<>();
      for(ItemStack oreEntry : OreDictionary.getOres(ore)) {
        Block block = Block.getBlockFromItem(oreEntry.getItem());
        if(block == null) {
          continue;
        }

        String tool = block.getHarvestTool(block.getDefaultState());
        int level = getActualHarvestLevel(block.getDefaultState());

        level = toolSingleEntry.getOrDefault(matchedTool, level);

        if((matchedTool != null && !matchedTool.equals(tool)) || !getValidBlockstates(block, new ArrayList<>(), tool, level)) {
          // -2 indicates that it's not a single entry
          toolSingleEntry.put(tool, -2);
          toolSingleEntry.put(matchedTool, -2);
          break;
        }
        else {
          toolSingleEntry.put(tool, level);
          matchedTool = tool;
        }
      }

      toolSingleEntry.forEach((tool, level) -> {
        if(level > -2) {
          oredict.computeIfAbsent(tool, x -> new HashMap<>()).putIfAbsent(ore, level);
        }
      });
    }
  }

  private int getActualHarvestLevel(IBlockState state) {
    if(state.getMaterial().isToolNotRequired()) {
      return -1;
    }
    return state.getBlock().getHarvestLevel(state);
  }

  private boolean getValidBlockstates(Block block, List<IBlockState> validStates, String tool, int level) {
      if(tool == null) {
      return false;
    }
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

        if(!Objects.equals(tool, block.getHarvestTool(state)) || level != getActualHarvestLevel(state)) {
          singleEntry = false;
        }
      }
    }

    return singleEntry;
  }
}
