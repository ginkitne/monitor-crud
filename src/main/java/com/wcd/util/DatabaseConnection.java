package com.wcd.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Centralised JDBC connection provider using an embedded H2 in-memory database.
 */
public final class DatabaseConnection {

    private static final String JDBC_URL = "jdbc:h2:mem:wcd;DB_CLOSE_DELAY=-1";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    private static boolean initialised = false;

    /*
    * Explicitly load the H2 driver.
    *
    * Tomcat's system classloader (which loads DriverManager) does not scan
    * jars inside WEB-INF/lib for JDBC drivers. Calling Class.forName here
    * forces the webapp classloader to load org.h2.Driver, which triggers
    * its static initializer and registers it with DriverManager.
    */
    static {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError(
                "H2 JDBC driver not found on classpath. " +
                "Ensure the h2 dependency is present in the WAR's WEB-INF/lib.");
        }
    }

    private DatabaseConnection() {
    }

    public static Connection getConnection() throws SQLException {
        ensureInitialised();
        return DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
    }

    private static synchronized void ensureInitialised() {
        if (initialised) {
            return;
        }
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {

            stmt.execute(
                "CREATE TABLE IF NOT EXISTS monitoring_target (" +
                "  id             BIGINT AUTO_INCREMENT PRIMARY KEY," +
                "  url            VARCHAR(512) NOT NULL," +
                "  check_interval VARCHAR(32)  NOT NULL," +
                "  css_selector   VARCHAR(256)," +
                "  active         BOOLEAN      NOT NULL DEFAULT TRUE," +
                "  created_at     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP" +
                ")"
            );

            try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM monitoring_target")) {
                rs.next();
                if (rs.getInt(1) == 0) {
                    stmt.execute(
                        "INSERT INTO monitoring_target (url, check_interval, css_selector, active) " +
                        "VALUES ('https://example.com/news', 'daily', 'article', TRUE)"
                    );
                    stmt.execute(
                        "INSERT INTO monitoring_target (url, check_interval, css_selector, active) " +
                        "VALUES ('https://example.org/pricing', 'hourly', '.pricing-table', TRUE)"
                    );
                }
            }
            initialised = true;
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to initialise database schema", e);
        }
    }
}
