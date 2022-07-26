package ru.deelter.xraydetector.player;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.deelter.xraydetector.XrayDetector;
import ru.deelter.xraydetector.logs.XrayLoggerManager;
import ru.deelter.xraydetector.player.suspected.SuspectedPlayersDatabase;
import ru.deelter.xraydetector.utils.XrayStats;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class XrayPlayer {

	private boolean suspected;
	private final Map<Material, XrayStats> statistic = new HashMap<>();
	private final UUID uuid;

	protected XrayPlayer(UUID uuid) {
		this.uuid = uuid;
	}

	public UUID getUuid() {
		return uuid;
	}

	public String getName() {
		String name = Bukkit.getOfflinePlayer(uuid).getName();
		return name == null ? "unknown" : name;
	}

	@Nullable
	public Player getPlayer() {
		return Bukkit.getPlayer(this.uuid);
	}

	public boolean isSuspected() {
		return suspected;
	}

	public XrayStats getStatistic(Material material) {
		if (!statistic.containsKey(material)) {
			XrayStats stats = new XrayStats(material);
			statistic.put(material, stats);
			return stats;
		}
		return statistic.get(material);
	}

	public void setStatistic(@NotNull Material material, @Nullable XrayStats stats) {
		if (stats == null) {
			this.statistic.remove(material);
			return;
		}
		this.statistic.put(material, stats);
	}

	public void applySuspected() {
		this.suspected = true;

		String info = applySuspectLog();
		XrayDetector.getInstance().getLogger().warning(info);
		XrayLoggerManager.log(info);
	}

	private @NotNull String applySuspectLog() {
		String name = this.getName();
		StringBuilder sb = new StringBuilder(String.format("Suspected user %s [uuid: %s]", name, this.uuid));
		for (XrayStats stats : statistic.values())
			sb.append("\n- ").append(stats.toShortString());
		return sb.toString();
	}

	public JsonArray getStatisticAsData() {
		JsonArray array = new JsonArray();
		for (XrayStats stats : this.statistic.values()) {
			JsonObject data = stats.toData();
			array.add(data);
		}
		return array;
	}

	public void saveToDatabase() {
		SuspectedPlayersDatabase.savePlayer(this);
	}
}
