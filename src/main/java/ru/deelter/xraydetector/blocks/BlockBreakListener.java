package ru.deelter.xraydetector.blocks;

import net.coreprotect.CoreProtectAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.jetbrains.annotations.NotNull;
import ru.deelter.xraydetector.XrayDetector;
import ru.deelter.xraydetector.hooks.CoreProtectHook;
import ru.deelter.xraydetector.player.XrayPlayer;
import ru.deelter.xraydetector.player.XrayPlayerManager;
import ru.deelter.xraydetector.utils.XrayStats;

import java.util.List;
import java.util.UUID;

public class BlockBreakListener implements Listener {

	public static void init(XrayDetector instance) {
		Bukkit.getPluginManager().registerEvents(new BlockBreakListener(), instance);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onBreak(@NotNull BlockBreakEvent event) {
		Block block = event.getBlock();
		Material material = block.getType();
		BlockLimit blockLimit = BlockLimitManager.getLimit(material);
		if (blockLimit == null) return; // No tracking
		if (CoreProtectHook.isLoaded()) {
			// TODO Refactoring. Huge optimization-dead code
			CoreProtectAPI coreProtect = CoreProtectHook.getInstance();
			List<String[]> lookup = coreProtect.blockLookup(block, 2592000); // 30 days
			if (!lookup.isEmpty())
				return;
		}

		UUID uuid = event.getPlayer().getUniqueId();
		XrayPlayer xrayPlayer = XrayPlayerManager.getPlayer(uuid);
		XrayStats stats = xrayPlayer.getStatistic(material);
		if (stats.isSessionExpired()) {
			stats.setBreakAmount(1);
			return;
		}
		stats.increaseBreakAmount(1);
		int breakAmount = stats.getBreakAmount();
		if (breakAmount < blockLimit.getAmount()) return;
		if (xrayPlayer.isSuspected())
			return;
		// Alarm script
		xrayPlayer.applySuspected();
	}
}
