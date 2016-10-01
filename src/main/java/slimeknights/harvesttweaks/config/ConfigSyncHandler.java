package slimeknights.harvesttweaks.config;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import slimeknights.harvesttweaks.HarvestTweaks;

public class ConfigSyncHandler {

  @SideOnly(Side.CLIENT)
  private static boolean needsRestart;

  @SubscribeEvent
  @SideOnly(Side.SERVER)
  public void playerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
    if(event.player instanceof EntityPlayerMP && FMLCommonHandler.instance().getSide().isServer()) {
      HarvestTweakConfigSyncPacket packet = new HarvestTweakConfigSyncPacket();
      HarvestTweaks.NETWORK.network.sendTo(packet, (EntityPlayerMP) event.player);
    }
  }

  @SubscribeEvent
  @SideOnly(Side.CLIENT)
  public void playerJoinedWorld(TickEvent.ClientTickEvent event) {
    EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
    if(needsRestart) {
      player.addChatMessage(new TextComponentString("[TConstruct] " + I18n.translateToLocal("config.synced.restart")));
    }
    else {
      player.addChatMessage(new TextComponentString("[TConstruct] " + I18n.translateToLocal("config.synced.ok")));
    }
    MinecraftForge.EVENT_BUS.unregister(this);
  }
}
