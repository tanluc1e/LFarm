package me.tanluc.starfarm.fileManager;

import me.tanluc.starfarm.StarsFarm;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class GuiManager {

    public static File[] loadGuiFileConfiguration() {
        File guiFolder = new File(StarsFarm.getInstance().getDataFolder(), "gui");
        return guiFolder.listFiles((dir, name) -> name.toLowerCase().endsWith(".yml"));
    }

    public static void loadItemsName() {
        File guiFolder = new File(StarsFarm.getInstance().getDataFolder(), "gui");
        File[] files = guiFolder.listFiles((dir, name) -> name.toLowerCase().endsWith(".yml"));

        if (files != null) {
            for (File file : files) {
                String guiName = file.getName().replace(".yml", "");
                FileConfiguration guiConfig = YamlConfiguration.loadConfiguration(file);

                Map<Material, String> blockNames = new HashMap<>();
                for (String key : guiConfig.getConfigurationSection("Menu").getKeys(false)) {
                    int modelData = guiConfig.getInt("Menu." + key + ".model_data");
                    String itemName = guiConfig.getString("Menu." + key + ".name");

                    Material material = Material.getMaterial(key.toUpperCase());
                    if (material != null) {
                        blockNames.put(material, itemName);
                    }
                }

                StarsFarm.materialsName.put(guiName, blockNames);
            }
        }
    }

    public void loadItemsSupport() {
        File guiFolder = new File(StarsFarm.getInstance().getDataFolder(), "gui");
        File[] files = guiFolder.listFiles((dir, name) -> name.toLowerCase().endsWith(".yml"));

        if (files != null) {
            for (File file : files) {
                FileConfiguration guiConfig = YamlConfiguration.loadConfiguration(file);
                ConfigurationSection blockSupport = guiConfig.getConfigurationSection("Menu");

                if (blockSupport != null) {
                    for (String blockKey : blockSupport.getKeys(false)) {
                        if (blockKey.toUpperCase().equals(blockKey)) {
                            Material material = Material.getMaterial(blockKey);
                            if (material != null) {
                                StarsFarm.materials.add(material);
                            }
                        }
                    }
                }
            }
        }
    }
}
