package repository;

import java.sql.*;
import models.*;
import utils.*;

public class MinumanRepo {

    private static final String ADD_MINUMAN = "INSERT INTO minuman (id, name, category, harga, is_ready) VALUES (?, ?, ?, ?, ?)";
    private static final String GET_BY_ID = "SELECT * FROM minuman WHERE id = ?";
    private static final String GET_ALL = "SELECT * FROM minuman";
    private static final String UPDATE_MINUMAN = "UPDATE minuman SET name = ?, category = ?, harga = ?, is_ready = ? WHERE id = ?";
    private static final String DELETE_MINUMAN = "DELETE FROM minuman WHERE id = ?";
    private static final int MAX_MINUMAN = 100;

    public Minuman addMinuman(Minuman newMinuman) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(ADD_MINUMAN)) {

            stmt.setString(1, newMinuman.getId());
            stmt.setString(2, newMinuman.getName());
            stmt.setString(3, newMinuman.getCategory());
            stmt.setInt(4, newMinuman.getHarga());
            stmt.setBoolean(5, newMinuman.getIsReady());

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0 ? newMinuman : null;

        } catch (SQLException e) {
            FormatUtil.logError("MinumanRepo", "addMinuman", e);
            return null;
        }
    }

    public Minuman getMinumanById(String id) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_BY_ID)) {

            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            return rs.next() ? mapResultSetToMinuman(rs) : null;

        } catch (SQLException e) {
            FormatUtil.logError("MinumanRepo", "getMinumanById", e);
            return null;
        }
    }

    public Minuman[] getAllMinuman() {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_ALL);
             ResultSet rs = stmt.executeQuery()) {

            Minuman[] list = new Minuman[MAX_MINUMAN];
            int index = 0;

            while (rs.next() && index < MAX_MINUMAN) {
                list[index++] = mapResultSetToMinuman(rs);
            }

            return list;

        } catch (SQLException e) {
            FormatUtil.logError("MinumanRepo", "getAllMinuman", e);
            return null;
        }
    }

    public Minuman updateMinuman(Minuman updatedMinuman) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_MINUMAN)) {

            stmt.setString(1, updatedMinuman.getName());
            stmt.setString(2, updatedMinuman.getCategory());
            stmt.setInt(3, updatedMinuman.getHarga());
            stmt.setBoolean(4, updatedMinuman.getIsReady());
            stmt.setString(5, updatedMinuman.getId());

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0 ? updatedMinuman : null;

        } catch (SQLException e) {
            FormatUtil.logError("MinumanRepo", "updateMinuman", e);
            return null;
        }
    }

    public boolean deleteMinuman(String id) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_MINUMAN)) {

            stmt.setString(1, id);
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;

        } catch (SQLException e) {
            FormatUtil.logError("MinumanRepo", "deleteMinuman", e);
            return false;
        }
    }

    private Minuman mapResultSetToMinuman(ResultSet rs) throws SQLException {
        String id = rs.getString("id");
        String name = rs.getString("name");
        String category = rs.getString("category");
        int harga = rs.getInt("harga");
        boolean isReady = rs.getBoolean("is_ready");

        return new Minuman(id, name, category, harga, isReady);
    }
}
