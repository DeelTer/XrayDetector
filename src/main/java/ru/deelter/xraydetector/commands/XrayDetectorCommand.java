package ru.deelter.xraydetector.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.jetbrains.annotations.NotNull;
import ru.deelter.xraydetector.XrayDetector;

public class XrayDetectorCommand implements CommandExecutor {

	public static void load(@NotNull XrayDetector instance) {
		PluginCommand command = instance.getCommand("xraydetector");
		if (command == null) {
			instance.getLogger().warning("Can't load main plugin command. Check plugin.yml please");
			return;
		}
		XrayDetectorCommand commandExecutor = new XrayDetectorCommand();
		command.setExecutor(commandExecutor);
	}

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (args.length < 1) {
			sender.sendMessage("Недостаточно аргументов");
			return true;
		}
		String firstArg = args[0];
		switch (firstArg) {
			case "stats" -> StatsCommand.execute(sender, args);
		}
		return true;
	}
}
