package ru.deelter.xraydetector.player;

import com.google.common.cache.CacheLoader;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class XrayPlayerCacheLoader extends CacheLoader<UUID, XrayPlayer> {

	@Override
	public @NotNull XrayPlayer load(@NotNull UUID key) {
		return new XrayPlayer(key);
	}
}
