package slimeknights.harvesttweaks.config;

import com.google.common.collect.ImmutableList;

import net.minecraftforge.fml.client.config.ConfigGuiType;
import net.minecraftforge.fml.client.config.DummyConfigElement;
import net.minecraftforge.fml.client.config.GuiConfigEntries;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.util.Map;

public class ConfigFieldEntry extends DummyConfigElement {

  private final Object instance;
  private final Map<Object, Object> parent;

  public ConfigFieldEntry(String name, String langKey, Class<?> clazz, Object instance) {
    this(name, langKey, clazz, instance, null);
  }

  public ConfigFieldEntry(String name, String langKey, Class<?> clazz, Object instance, Map<Object, Object> parent) {
    super(name, null, getType(clazz), langKey);

    this.instance = instance;
    this.parent = parent;

    if(Map.class.isAssignableFrom(clazz)) {
      this.isProperty = false;
      ImmutableList.Builder<IConfigElement> builder = ImmutableList.builder();
      Map<Object, Object> map = ((Map<Object,Object>)instance);
      map.forEach((o, o2) -> builder.add(new ConfigFieldEntry(o.toString(), o.toString() + ".lang", o2.getClass(), o2, map)));
      this.childElements = builder.build();
      this.configEntryClass = GuiConfigEntries.CategoryEntry.class;
    }
    else {
      this.defaultValue = instance.toString();
      this.setToDefault();
    }
  }

  private static ConfigGuiType getType(Class<?> type) {
    if(Integer.class.isAssignableFrom(type)) {
      return ConfigGuiType.INTEGER;
    }
    if(String.class.isAssignableFrom(type)) {
      return ConfigGuiType.STRING;
    }
    if(Double.class.isAssignableFrom(type)) {
      return ConfigGuiType.DOUBLE;
    }
    if(Boolean.class.isAssignableFrom(type)) {
      return ConfigGuiType.BOOLEAN;
    }
    return null;
  }

  @Override
  public void set(Object value) {
    this.value = value;
    if(parent != null) {
      parent.put(this.name, this.value);
    }
  }
}
