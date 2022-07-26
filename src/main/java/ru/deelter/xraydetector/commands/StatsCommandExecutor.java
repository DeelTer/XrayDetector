package ru.deelter.xraydetector.commands;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import ru.deelter.xraydetector.logs.XrayLoggerManager;
import ru.deelter.xraydetector.player.suspected.SuspectedPlayer;
import ru.deelter.xraydetector.player.suspected.SuspectedPlayersDatabase;

import java.util.List;

public class StatsCommandExecutor {

	public static void execute(CommandSender sender, String @NotNull [] args) {
		if (args.length < 2) {
			sender.sendMessage("Введите дополнительную информацию [all]");
			return;
		}
		String secondArg = args[1];
		if (secondArg.equalsIgnoreCase("all")) {
			List<SuspectedPlayer> suspectedPlayers = SuspectedPlayersDatabase.loadAllSuspected();
			if (suspectedPlayers.isEmpty()) {
				sender.sendMessage("Лист подозреваемых пуст");
				return;
			}
			sender.sendMessage("Сохраняем статистику в JSON файл");
			XrayLoggerManager.logSuspected(suspectedPlayers);
		}
	}
}
