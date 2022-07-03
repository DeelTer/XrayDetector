package ru.deelter.xraydetector.utils.database;

import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class Database {

    private final String databaseName;

    public Database(@NotNull String databaseName) {
        this.databaseName = databaseName;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public Connection getConnection() throws SQLException {
        return openConnection();
    }

    public abstract Connection openConnection() throws SQLException;

    public abstract void closeConnection();
}
