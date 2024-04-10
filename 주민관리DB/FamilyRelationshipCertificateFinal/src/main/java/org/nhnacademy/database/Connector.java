package org.nhnacademy.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import lombok.Getter;

@Getter
public class Connector {
    private Logger loggger;
    private Connection conn;

    public Connector(String url, String user, String pw) {
        loggger = LogManager.getLogger(this.getClass().getName());
        try {
            conn = DriverManager.getConnection(url, user, pw);
            loggger.log(Level.INFO, "Successfully Connection!");
        } catch (SQLException e) {
            loggger.log(Level.ERROR, e.getMessage());
        }
    }

    public void executeQuery(String query) {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(query);
            loggger.log(Level.INFO, "< Execute Query >\n {}", query);
        } catch (SQLException e) {
            loggger.log(Level.ERROR, e.getMessage());
        }
    }
}
