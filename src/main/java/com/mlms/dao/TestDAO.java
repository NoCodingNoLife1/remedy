package com.mlms.dao;

import com.mlms.model.Test;
import com.mlms.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TestDAO {

    public List<Test> getAllTests() {
        List<Test> tests = new ArrayList<>();
        String query = "SELECT * FROM tests";
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Test t = new Test();
                t.setId(rs.getInt("id"));
                t.setTestNumber(rs.getString("test_number"));
                t.setTestName(rs.getString("test_name"));
                t.setTestCost(rs.getDouble("test_cost"));
                tests.add(t);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tests;
    }

    public void addTest(Test t) {
        String query = "INSERT INTO tests(test_number, test_name, test_cost) VALUES(?,?,?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, t.getTestNumber());
            pstmt.setString(2, t.getTestName());
            pstmt.setDouble(3, t.getTestCost());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateTest(Test t) {
        String query = "UPDATE tests SET test_number=?, test_name=?, test_cost=? WHERE id=?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, t.getTestNumber());
            pstmt.setString(2, t.getTestName());
            pstmt.setDouble(3, t.getTestCost());
            pstmt.setInt(4, t.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteTest(int id) {
        String query = "DELETE FROM tests WHERE id=?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Test getTestById(int id) {
        String query = "SELECT * FROM tests WHERE id=?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Test t = new Test();
                t.setId(rs.getInt("id"));
                t.setTestNumber(rs.getString("test_number"));
                t.setTestName(rs.getString("test_name"));
                t.setTestCost(rs.getDouble("test_cost"));
                return t;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
