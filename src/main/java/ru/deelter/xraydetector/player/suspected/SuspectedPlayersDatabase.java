package ru.deelter.xraydetector.player.suspected;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.jetbrains.annotations.NotNull;
import ru.deelter.xraydetector.XrayDetector;
import ru.deelter.xraydetector.player.XrayPlayer;
import ru.deelter.xraydetector.utils.database.SQLite;
import ru.deelter.xraydetector.utils.XrayStats;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SuspectedPlayersDatabase {

	private static final SQLite DATABASE = new SQLite("suspected", XrayDetector.getInstance());

	public static void setupTables() {
		String sql = "CREATE TABLE IF NOT EXISTS `players`("
				+ "`uuid` VARCHAR,"
				+ "`name` TEXT,"
				+ "`stats` TEXT DEFAULT '[]',"
				+ "`logged_time` TIME"
				+ ");";
		try (Connection con = DATABASE.openConnection();
			 PreparedStatement sqlStatement = con.prepareStatement(sql)
		) {
			sqlStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static synchronized void savePlayer(@NotNull XrayPlayer player) {
		JsonArray dataStats = player.getStatisticAsData();
		String sql = "INSERT OR REPLACE INTO players (" +
				"uuid, " +
				"name, " +
				"stats," +
				"logged_time" +
				") VALUES (?, ?, ?, ?);";
		try (Connection con = DATABASE.openConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, player.getUuid().toString());
			ps.setString(2, player.getName());
			ps.setString(3, dataStats.toString());
			ps.setTime(4, new Time(System.currentTimeMillis()));
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static synchronized @NotNull List<SuspectedPlayer> loadAllSuspected() {
		List<SuspectedPlayer> suspectedPlayers = new ArrayList<>();
		String sql = "SELECT * FROM players;";
		try (Connection con = DATABASE.openConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				List<XrayStats> stats = new ArrayList<>();
				JsonElement statsArray = JsonParser.parseString(rs.getString("stats"));
				for (JsonElement element : statsArray.getAsJsonArray()) {
					XrayStats statsData = new XrayStats(element.getAsJsonObject());
					stats.add(statsData);
				}
				UUID uuid = UUID.fromString(rs.getString("uuid"));
				String name = rs.getString("name");

				SuspectedPlayer suspected = new SuspectedPlayer(uuid, name, stats);
				suspectedPlayers.add(suspected);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return suspectedPlayers;
	}

}
