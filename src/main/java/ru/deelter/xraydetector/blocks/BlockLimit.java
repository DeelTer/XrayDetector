package ru.deelter.xraydetector.blocks;

import org.bukkit.Material;

public class BlockLimit {

	private final Material material;
	private final int amount;
	private final int minutes;

	public BlockLimit(Material material, int amount, int minutes) {
		this.material = material;
		this.amount = amount;
		this.minutes = minutes;
	}

	public Material getMaterial() {
		return material;
	}

	public int getAmount() {
		return amount;
	}

	public int getMinutes() {
		return minutes;
	}
}
