package edu.aitu.oop3.db.IDB;


import java.sql.Connection;
import java.sql.SQLException;

public interface IDatabase {
    Connection getConnection() throws SQLException;
}

