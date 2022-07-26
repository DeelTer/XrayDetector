package ru.deelter.xraydetector.menus;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import ru.deelter.xraydetector.XrayDetector;
import ru.deelter.xraydetector.hooks.CoreProtectHook;
import ru.deelter.xraydetector.player.suspected.SuspectedPlayer;

public class XrayMenuListener implements Listener {

	public static void init(XrayDetector instance) {
		Bukkit.getPluginManager().registerEvents(new XrayMenuListener(), instance);
	}

	@EventHandler
	public void onMenuClick(@NotNull InventoryClickEvent event) {
		if (!(event.getView().getTopInventory().getHolder() instanceof XrayPlayersMenu xrayMenu)) return;
		if (!(event.getWhoClicked() instanceof Player player)) return;

		ItemStack icon = event.getCurrentItem();
		if (icon == null || icon.getType() == Material.AIR) {
			event.setCancelled(true);
			return;
		}

		if (icon.getType() == Material.PLAYER_HEAD) {
			int slot = event.getRawSlot();
			SuspectedPlayer suspected = xrayMenu.getPlayerBySlot(slot);
			if (suspected == null) {
				event.setCancelled(true);
				return;
			}
			if (event.isRightClick()) {
				player.closeInventory();
				if (!CoreProtectHook.isLoaded()) {
					player.sendMessage("CoreProtect не подключен, Вы не можете просмотреть историю");
					event.setCancelled(true);
					return;
				}
				String command = String.format("/coreprotect lookup action:-block time:2days user:%s", suspected.getName());
				player.performCommand(command);
			}
			else if (event.isLeftClick()) {
				xrayMenu.removePlayerBySlot(slot);
				player.sendMessage("Человек удалён из списка подозреваемых");
				player.closeInventory();
			}
		}
		event.setCancelled(true);
	}
}
