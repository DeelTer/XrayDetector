package ru.deelter.xraydetector.blocks;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.deelter.xraydetector.XrayDetector;

import java.util.HashMap;
import java.util.Map;

public class XrayBlockManager {

	private static final Map<Material, BlockLimit> LIMITS = new HashMap<>();

	public static void registerLimit(BlockLimit blockLimit) {
		LIMITS.put(blockLimit.getMaterial(), blockLimit);
	}

	@Nullable
	public static BlockLimit getLimit(Material material) {
		return LIMITS.get(material);
	}

	public static void load(@NotNull XrayDetector instance) {
		FileConfiguration config = instance.getConfig();
		ConfigurationSection section = config.getConfigurationSection("limits");
		if (section == null) {
			instance.getLogger().warning("No limit section");
			return;
		}
		for (String key : section.getKeys(false)) {
			ConfigurationSection section2 = section.getConfigurationSection(key);
			if (section2 == null) continue;

			int amount = section2.getInt("amount", -1);
			int minutes = section2.getInt("minutes", -1);
			if (amount <= 0 || minutes <= 0) {
				instance.getLogger().warning("'Amount' and 'minutes' values can't be lower or equals 0");
				continue;
			}
			Material material = Material.valueOf(section2.getString("material"));
			BlockLimit blockLimit = new BlockLimit(material, amount, minutes);
			registerLimit(blockLimit);
		}
	}
}
