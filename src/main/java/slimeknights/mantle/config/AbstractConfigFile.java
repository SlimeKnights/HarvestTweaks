package slimeknights.mantle.config;

import com.google.common.reflect.TypeToken;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameData;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;

import java.io.File;
import java.io.IOException;

import slimeknights.mantle.configurate.ConfigurationNode;
import slimeknights.mantle.configurate.ConfigurationOptions;
import slimeknights.mantle.configurate.commented.CommentedConfigurationNode;
import slimeknights.mantle.configurate.hocon.HoconConfigurationLoader;
import slimeknights.mantle.configurate.loader.ConfigurationLoader;
import slimeknights.mantle.configurate.objectmapping.ObjectMappingException;
import slimeknights.mantle.configurate.objectmapping.serialize.TypeSerializer;
import slimeknights.mantle.configurate.objectmapping.serialize.TypeSerializers;

public class AbstractConfigFile {

  private static boolean initialized = false;

  private final File file;
  private final ConfigurationLoader<CommentedConfigurationNode> loader;

  public AbstractConfigFile(File configFolder, String name) {
    this(new File(configFolder, name + ".cfg"));
  }

  public AbstractConfigFile(File configFile) {
    configFile.getParentFile().mkdirs();
    file = configFile;
    loader = HoconConfigurationLoader.builder().setFile(file).build();
  }

  public CommentedConfigurationNode load() throws IOException {
    return loader.load(ConfigurationOptions.defaults().setShouldCopyDefaults(true));
  }

  public void save(ConfigurationNode node) throws IOException {
    loader.save(node);
  }

  public static void init() {
    if(initialized) {
      return;
    }

    // item and block serializer/deserializer
    TypeSerializers.getDefaultSerializers().registerType(TypeToken.of(Block.class), new RegistrySerializer<Block>() {
      @Override
      IForgeRegistry<Block> getRegistry() {
        return GameData.getBlockRegistry();
      }
    });

    TypeSerializers.getDefaultSerializers().registerType(TypeToken.of(Item.class), new RegistrySerializer<Item>() {
      @Override
      IForgeRegistry<Item> getRegistry() {
        return GameData.getItemRegistry();
      }
    });

    TypeSerializers.getDefaultSerializers().registerType(TypeToken.of(BlockMeta.class), BlockMeta.SERIALIZER);

    initialized = true;
  }

  private static abstract class RegistrySerializer<T extends IForgeRegistryEntry<T>> implements TypeSerializer<T> {

    // done at runtime so registry changes from joining servers take effect
    abstract IForgeRegistry<T> getRegistry();

    @Override
    public T deserialize(TypeToken<?> typeToken, ConfigurationNode configurationNode) throws ObjectMappingException {
      return getRegistry().getValue(new ResourceLocation(configurationNode.getString()));
    }

    @Override
    public void serialize(TypeToken<?> typeToken, T t, ConfigurationNode configurationNode)
        throws ObjectMappingException {
      configurationNode.setValue(t.getRegistryName());
    }
  }
}
