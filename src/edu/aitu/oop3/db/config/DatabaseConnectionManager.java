package edu.aitu.oop3.db.config;

import edu.aitu.oop3.db.IDB.IDatabase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DatabaseConnectionManager implements IDatabase {
    private static final DatabaseConnectionManager INSTANCE = new DatabaseConnectionManager();
    private final AppConfig config = AppConfig.getInstance();

    private DatabaseConnectionManager() {}

    public static DatabaseConnectionManager getInstance() {
        return INSTANCE;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                config.getUrl(),
                config.getUser(),
                config.getPassword()
        );
    }
}
