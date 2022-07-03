package ru.deelter.xraydetector.utils;

import com.google.gson.JsonObject;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import ru.deelter.xraydetector.XrayDetector;
import ru.deelter.xraydetector.blocks.BlockLimit;
import ru.deelter.xraydetector.blocks.BlockLimitManager;

public class XrayStats {

	private final Material material;
	private int breakAmount;
	private long lastAccessMillis;

	public XrayStats(Material material) {
		this.material = material;
		this.breakAmount = 0;
		this.updateLastAccess();
	}

	public XrayStats(@NotNull JsonObject data) {
		if (!data.has("material") || !data.has("break-amount")) {
			this.material = Material.AIR;
			XrayDetector.getInstance().getLogger().warning("Can't parse xray statistic from data");
		}
		else {
			this.material = Material.valueOf(data.get("material").getAsString());
			this.breakAmount = data.get("break-amount").getAsInt();
		}
		this.updateLastAccess();
	}

	public Material getMaterial() {
		return material;
	}

	public int getBreakAmount() {
		return breakAmount;
	}

	public boolean isSessionExpired() {
		BlockLimit blockLimit = BlockLimitManager.getLimit(this.material);
		if (blockLimit == null) return false;
		long differenceMinutes = (System.currentTimeMillis() - this.lastAccessMillis) / 1000 / 60;
		return differenceMinutes >= blockLimit.getMinutes();
	}

	public void increaseBreakAmount(int points) {
		this.setBreakAmount(this.breakAmount + points);
		this.updateLastAccess();
	}

	public void setBreakAmount(int breakAmount) {
		this.breakAmount = breakAmount;
		this.updateLastAccess();
	}

	public long getLastAccessMillis() {
		return lastAccessMillis;
	}

	public void updateLastAccess() {
		this.lastAccessMillis = System.currentTimeMillis();
	}

	public String toShortString() {
		return String.format("Material: %s, Break amount: %d", this.material, this.breakAmount);
	}

	public JsonObject toData() {
		JsonObject data = new JsonObject();
		data.addProperty("material", this.material.toString());
		data.addProperty("break-amount", this.breakAmount);
		return data;
	}
}
