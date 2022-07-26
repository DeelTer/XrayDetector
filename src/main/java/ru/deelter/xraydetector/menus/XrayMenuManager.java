package ru.deelter.xraydetector.menus;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import ru.deelter.xraydetector.XrayDetector;

public class XrayMenuManager {

	private static final XrayPlayersMenu MENU = new XrayPlayersMenu();

	public static void openWindow(@NotNull Player player) {
		Inventory inventory = MENU.getInventory();
		player.openInventory(inventory);
	}

	public static void load(XrayDetector instance) {
		Bukkit.getScheduler().runTaskTimerAsynchronously(instance, () -> {
			MENU.clearPlayers();
			MENU.loadPlayers();
		}, 0, 600 * 20L);
		XrayMenuListener.init(instance);
	}
}
