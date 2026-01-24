package edu.aitu.oop3.db.IDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgresDatabase implements IDatabase {

    private static final String URL =
            "jdbc:postgresql://aws-1-ap-south-1.pooler.supabase.com:5432/postgres?sslmode=require";
    private static final String USER = "postgres.eccfvjinhgldnywflozx";
    private static final String PASSWORD = "123.Kairattt";

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

