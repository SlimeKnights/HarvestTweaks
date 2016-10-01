package slimeknights.harvesttweaks.config;

import com.google.common.collect.ImmutableList;

import net.minecraftforge.fml.client.config.DummyConfigElement;
import net.minecraftforge.fml.client.config.GuiConfigEntries;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.lang.reflect.Field;
import java.util.List;

public class ConfigClassCategory extends DummyConfigElement.DummyCategoryElement {

  public ConfigClassCategory(String name, String langKey, Class<?> clazz, Object instance) {
    super(name, langKey, getElementsInClass(clazz, instance), GuiConfigEntries.CategoryEntry.class);
  }

  private static List<IConfigElement> getElementsInClass(Class<?> clazz, Object instance) {
    ImmutableList.Builder<IConfigElement> elements = ImmutableList.builder();
    for(Field field : clazz.getDeclaredFields()) {
      try {
        if(!field.isAccessible()) {
          field.setAccessible(true);
        }
        elements.add(new ConfigFieldEntry(field.getName(), field.getName() + ".lang", field.getType(), field.get(instance)));
      } catch(IllegalAccessException e) {
        e.printStackTrace();
      }
    }

    return elements.build();
  }
}
