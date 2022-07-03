package ru.deelter.xraydetector.logs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import ru.deelter.xraydetector.XrayDetector;
import ru.deelter.xraydetector.player.suspected.SuspectedPlayer;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class XrayLoggerManager {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private XrayLoggerManager() {
        throw new IllegalStateException("Utility class");
    }

    private static final SimpleDateFormat GLOBAL_TIME_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
    private static final SimpleDateFormat LOCAL_TIME_FORMAT = new SimpleDateFormat("hh:mm:ss");

    public static void log(@NotNull String info) {
        File folder = new File(XrayDetector.getInstance().getDataFolder() + "/logs");
        if (!folder.exists()) folder.mkdir();

        Date date = new Date();
        String formattedDate = GLOBAL_TIME_FORMAT.format(date);
        String formattedTime = LOCAL_TIME_FORMAT.format(date);

        String fileName = formattedDate + ".txt";
        File logFile = new File(folder, fileName);
        try {
            if(!logFile.exists())
                logFile.createNewFile();

            FileWriter writer = new FileWriter(logFile.getPath(), true);
            BufferedWriter bufferWriter = new BufferedWriter(writer);
            bufferWriter.append(String.format("\n[%s] %s", formattedTime, info));
            bufferWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void logSuspected(List<SuspectedPlayer> suspectedPlayers) {
        File folder = new File(XrayDetector.getInstance().getDataFolder() + "/output");
        if (!folder.exists()) folder.mkdir();

        JsonArray array = new JsonArray();
        for (SuspectedPlayer player : suspectedPlayers) {
            JsonObject data = player.toData();
            array.add(data);
        }
        File[] files = folder.listFiles();
        int index = files == null ? 0 : files.length;
        String fileName = "saved" + index + ".json";

        try (Writer writer = new FileWriter(folder.getPath() + File.separator + fileName)) {
            GSON.toJson(array, writer);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
