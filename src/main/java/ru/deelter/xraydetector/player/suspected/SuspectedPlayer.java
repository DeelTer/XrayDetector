package ru.deelter.xraydetector.player.suspected;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import ru.deelter.xraydetector.utils.XrayStats;

import java.util.List;
import java.util.UUID;

public class SuspectedPlayer {

	private final String name;
	private final UUID uuid;
	private final List<XrayStats> statistic;

	public SuspectedPlayer(UUID uuid, String name, List<XrayStats> statistic) {
		this.uuid = uuid;
		this.name = name;
		this.statistic = statistic;
	}

	public String getName() {
		return name;
	}

	public UUID getUuid() {
		return uuid;
	}

	public List<XrayStats> getStatistic() {
		return statistic;
	}

	public JsonObject toData() {
		JsonObject data = new JsonObject();
		data.addProperty("name", this.name);
		data.addProperty("uuid", this.uuid.toString());

		JsonArray arrayStats = new JsonArray();
		for (XrayStats stats : statistic) {
			arrayStats.add(stats.toData());
		}
		data.add("statistic", arrayStats);
		return data;
	}
}
