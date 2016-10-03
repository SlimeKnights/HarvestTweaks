package slimeknights.harvesttweaks;

/**
 * Called on load and when the configs change for integration stuff to actually do its stuff
 * Do NOT implement this on the pulse itself, since that'll crash on load if dependencies are not there
 * (unless you only require vanilla stuff. then it's ok.)
 */
public interface IPulseLogic {

  void applyChanges();
}
