package ru.deelter.xraydetector.player;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.deelter.xraydetector.XrayDetector;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class XrayPlayerManager {

	private static final LoadingCache<UUID, XrayPlayer> PLAYERS = CacheBuilder.newBuilder()
			.maximumSize(1000)
			.expireAfterAccess(15, TimeUnit.MINUTES)
			.removalListener(new XrayPlayerCacheRemover())
			.build(new XrayPlayerCacheLoader());

	public static void setup(XrayDetector instance) {
		Bukkit.getScheduler().runTaskTimerAsynchronously(instance, PLAYERS::cleanUp, 5 * 20L, 0L);
	}

	public static @NotNull XrayPlayer getPlayer(@NotNull UUID uuid) {
		return PLAYERS.getUnchecked(uuid);
	}

	public static @NotNull XrayPlayer getPlayer(@NotNull Player player) {
		return getPlayer(player.getUniqueId());
	}

	public static void savePlayersToDatabase() {
		PLAYERS.invalidateAll();;
	}
}
