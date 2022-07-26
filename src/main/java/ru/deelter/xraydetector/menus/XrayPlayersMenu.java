package ru.deelter.xraydetector.menus;

import com.destroystokyo.paper.profile.PlayerProfile;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.deelter.xraydetector.player.suspected.SuspectedPlayer;
import ru.deelter.xraydetector.player.suspected.SuspectedPlayerManager;
import ru.deelter.xraydetector.player.suspected.SuspectedPlayersDatabase;
import ru.deelter.xraydetector.utils.XrayStats;

import java.util.*;

public class XrayPlayersMenu implements InventoryHolder {

	private final List<ItemStack> icons = new ArrayList<>();
	private List<SuspectedPlayer> players = new ArrayList<>();
	private final Inventory inventory;

	public XrayPlayersMenu() {
		this.inventory = Bukkit.createInventory(this, 54, Component.text("Подозреваемые"));
		this.loadPlayers();
	}

	public void loadPlayers() {
		List<SuspectedPlayer> players = SuspectedPlayerManager.getPlayers(54);
		this.loadPlayers(players);
	}

	public void loadPlayers(@NotNull List<SuspectedPlayer> players) {
		this.players = players;
		if (players.isEmpty()) {
			ItemStack icon = this.createIcon(Material.FEATHER, "&cУпс..", List.of("&7Кажется, что тут нет", "&7ни одного подозреваемого"));
			this.icons.add(icon);
		}
		else {
			for (SuspectedPlayer player : players) {
				String name = "&c" + player.getName();
				List<String> lore = getStatisticAsLore(player.getStatistic());
				lore.add(" ");
				lore.add("&fЛКМ &7просмотр истории CoreProtect");
				lore.add("&cПКМ &7удалить из списка");
				lore.add(" ");
				lore.add("&7uuid: " + player.getUuid());

				ItemStack playerIcon = this.createIcon(Material.PLAYER_HEAD, name, lore);
				SkullMeta skullMeta = (SkullMeta) playerIcon.getItemMeta();

				PlayerProfile profile = Bukkit.createProfile(player.getUuid());
				skullMeta.setPlayerProfile(profile);

				playerIcon.setItemMeta(skullMeta);
				this.icons.add(playerIcon);
			}
		}
		this.updateInventory();
	}

	private @NotNull List<String> getStatisticAsLore(@NotNull List<XrayStats> statsList) {
		List<String> lore = new ArrayList<>();
		lore.add("&fНаходки:");
		for (XrayStats xrayStats : statsList) {
			String row = "&7- " + xrayStats.toShortString();
			lore.add(row);
		}
		return lore;
	}

	private @NotNull ItemStack createIcon(Material material, String name, @NotNull List<String> loreStrings) {
		List<String> lore = new ArrayList<>();
		for (String str : loreStrings) {
			str = ChatColor.translateAlternateColorCodes('&', str);
			lore.add(str);
		}
		name = ChatColor.translateAlternateColorCodes('&', name);

		ItemStack item = new ItemStack(material);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

	public List<SuspectedPlayer> getPlayers() {
		return players;
	}

	public @Nullable SuspectedPlayer getPlayerBySlot(int index) {
		return players.get(index);
	}

	@Override
	public @NotNull Inventory getInventory() {
		return inventory;
	}

	public void removePlayerBySlot(int index) {
		if (players.size() - 1 < index) {
			this.updateInventory();
			return;
		}
		final UUID uuid = players.get(index).getUuid();
		this.players.remove(index);
		this.icons.remove(index);
		this.updateInventory();
		SuspectedPlayersDatabase.deletePlayer(uuid);
	}

	private void updateInventory() {
		this.inventory.clear();
		this.icons.forEach(inventory::addItem);
	}

	public void clearPlayers() {
		this.icons.clear();
		this.players.clear();
		this.inventory.clear();
	}
}
