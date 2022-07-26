package ru.deelter.xraydetector.player.suspected;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SuspectedPlayerManager {

	public static @NotNull List<SuspectedPlayer> getAllPlayers() {
		return SuspectedPlayersDatabase.loadAllSuspected();
	}

	public static @NotNull List<SuspectedPlayer> getPlayers(int count) {
		return SuspectedPlayersDatabase.loadLastSuspected(count);
	}
}
