package ru.deelter.xraydetector.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.deelter.xraydetector.menus.XrayMenuManager;

public class StatsMenuCommandExecutor {

	public static void execute(CommandSender sender, String @NotNull [] args) {
		if (!(sender instanceof Player player)) {
			sender.sendMessage("Вы не можете использовать эту команду вне игры");
			return;
		}
		if (!player.hasPermission("xray.menu")) {
			player.sendMessage("У вас нет прав для открытия меню (xray.menu)");
			return;
		}
		player.sendMessage("Открываем меню подозреваемых");
		XrayMenuManager.openWindow(player);
	}
}
