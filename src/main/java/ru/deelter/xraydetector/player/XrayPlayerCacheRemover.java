package ru.deelter.xraydetector.player;

import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class XrayPlayerCacheRemover implements RemovalListener<UUID, XrayPlayer> {

	@Override
	public void onRemoval(@NotNull RemovalNotification<UUID, XrayPlayer> notification) {
		XrayPlayer xrayPlayer = notification.getValue();
		if (xrayPlayer == null) return;
		if (!xrayPlayer.isSuspected()) return;
		xrayPlayer.saveToDatabase();
	}
}
