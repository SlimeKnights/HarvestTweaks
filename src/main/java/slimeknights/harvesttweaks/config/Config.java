package slimeknights.harvesttweaks.config;

import java.io.File;

import slimeknights.mantle.config.AbstractConfig;
import slimeknights.tconstruct.library.Util;

public class Config extends AbstractConfig {

  public static String configID = Util.MODID + ".config";
  public static File configDirectory;

  public static void setConfigDirectory(File modConfigDirectory) {
    configDirectory = new File(modConfigDirectory, "HarvestTweaks");
    configDirectory.mkdirs();
  }

}
