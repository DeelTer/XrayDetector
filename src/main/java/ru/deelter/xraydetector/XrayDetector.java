package ru.deelter.xraydetector;

import org.bukkit.plugin.java.JavaPlugin;
import ru.deelter.xraydetector.blocks.BlockLimitManager;
import ru.deelter.xraydetector.commands.XrayDetectorCommand;
import ru.deelter.xraydetector.hooks.CoreProtectHook;
import ru.deelter.xraydetector.blocks.BlockBreakListener;
import ru.deelter.xraydetector.menus.XrayMenuManager;
import ru.deelter.xraydetector.player.XrayPlayerManager;
import ru.deelter.xraydetector.player.suspected.SuspectedPlayersDatabase;

public final class XrayDetector extends JavaPlugin {
	
	private static XrayDetector instance;

	public static XrayDetector getInstance() {
		return instance;
	}

	@Override
	public void onLoad() {
		instance = this;
		saveDefaultConfig();
		SuspectedPlayersDatabase.setupTables();
	}

	@Override
	public void onEnable() {
		CoreProtectHook.load(this);
		BlockLimitManager.load(this);
		XrayPlayerManager.setup(this);
		BlockBreakListener.init(this);
		XrayDetectorCommand.load(this);
		XrayMenuManager.load(this);
		this.getLogger().info("Plugin successfully loaded!");
	}

	@Override
	public void onDisable() {
		XrayPlayerManager.savePlayersToDatabase();
	}
}
