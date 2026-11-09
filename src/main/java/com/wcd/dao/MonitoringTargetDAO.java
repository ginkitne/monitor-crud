package com.wcd.dao;

import com.wcd.model.MonitoringTarget;
import com.wcd.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for {@link MonitoringTarget}.
 */
public class MonitoringTargetDAO {

    private static final String SELECT_ALL =
        "SELECT id, url, check_interval, css_selector, active, created_at " +
        "FROM monitoring_target ORDER BY id";

    private static final String SELECT_BY_ID =
        "SELECT id, url, check_interval, css_selector, active, created_at " +
        "FROM monitoring_target WHERE id = ?";

    private static final String INSERT =
        "INSERT INTO monitoring_target (url, check_interval, css_selector, active) " +
        "VALUES (?, ?, ?, ?)";

    private static final String UPDATE =
        "UPDATE monitoring_target SET url = ?, check_interval = ?, " +
        "css_selector = ?, active = ? WHERE id = ?";

    private static final String DELETE =
        "DELETE FROM monitoring_target WHERE id = ?";

    public List<MonitoringTarget> findAll() throws SQLException {
        List<MonitoringTarget> results = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                results.add(mapRow(rs));
            }
        }
        return results;
    }

    public MonitoringTarget findById(long id) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    public void insert(MonitoringTarget target) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, target.getUrl());
            ps.setString(2, target.getCheckInterval());
            ps.setString(3, target.getCssSelector());
            ps.setBoolean(4, target.isActive());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    target.setId(keys.getLong(1));
                }
            }
        }
    }

    public void update(MonitoringTarget target) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE)) {
            ps.setString(1, target.getUrl());
            ps.setString(2, target.getCheckInterval());
            ps.setString(3, target.getCssSelector());
            ps.setBoolean(4, target.isActive());
            ps.setLong(5, target.getId());
            ps.executeUpdate();
        }
    }

    public void delete(long id) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    private MonitoringTarget mapRow(ResultSet rs) throws SQLException {
        return new MonitoringTarget(
            rs.getLong("id"),
            rs.getString("url"),
            rs.getString("check_interval"),
            rs.getString("css_selector"),
            rs.getBoolean("active"),
            rs.getTimestamp("created_at").toLocalDateTime()
        );
    }
}
