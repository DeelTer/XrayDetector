package ru.deelter.xraydetector.hooks;

import net.coreprotect.CoreProtect;
import net.coreprotect.CoreProtectAPI;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import ru.deelter.xraydetector.XrayDetector;

public class CoreProtectHook {

	private static CoreProtectAPI instance;

	public static CoreProtectAPI getInstance() {
		return instance;
	}

	public static boolean isLoaded() {
		return instance != null;
	}

	public static void load(@NotNull XrayDetector instance) {
		FileConfiguration config = instance.getConfig();
		if (!config.getBoolean("hooks.coreprotect")) return;

		Plugin plugin = Bukkit.getPluginManager().getPlugin("CoreProtect");
		if (!(plugin instanceof CoreProtect coreProtect)) {
			instance.getLogger().warning("Can't hook CoreProtect plugin!");
			return;
		}

		// Check that the API is enabled
		CoreProtectAPI apiInstance = coreProtect.getAPI();
		if (!apiInstance.isEnabled()) {
			instance.getLogger().warning("Can't hook CoreProtect because plugin disabled!");
			return;
		}
		// Check that a compatible version of the API is loaded
		if (apiInstance.APIVersion() < 9) {
			instance.getLogger().warning("Can't hook CoreProtect. API version lower than 9");
			return;
		}
		CoreProtectHook.instance = apiInstance;
	}
}
