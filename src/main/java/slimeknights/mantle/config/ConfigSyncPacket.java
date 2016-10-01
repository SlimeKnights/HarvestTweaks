package slimeknights.mantle.config;

import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.ArrayList;
import java.util.List;

import io.netty.buffer.ByteBuf;
import slimeknights.harvesttweaks.HarvestTweaks;
import slimeknights.mantle.network.AbstractPacket;

public abstract class ConfigSyncPacket extends AbstractPacket {

  private List<AbstractConfigFile> config;

  public ConfigSyncPacket() {
  }

  protected abstract AbstractConfig getConfig();

  @Override
  public IMessage handleClient(NetHandlerPlayClient netHandler) {
    ConfigSyncHandler.syncConfig(getConfig(), config);
    return null;
  }

  @Override
  public IMessage handleServer(NetHandlerPlayServer netHandler) {
    // We sync from server to client, not vice versa
    return null;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    config = new ArrayList<>();
    getConfig().configFileList.forEach(configFile -> {
      int length = buf.readInt();
      byte[] data = new byte[length];
      buf.readBytes(data);
      config.add(configFile.loadFromPacket(data));
    });
  }

  @Override
  public void toBytes(ByteBuf buf) {
    getConfig().configFileList.forEach(configFile -> {
      byte[] data = configFile.getPacketData();
      buf.writeInt(data.length);
      buf.writeBytes(data);
    });
  }
}
