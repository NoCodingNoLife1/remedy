package com.mlms.dao;

import com.mlms.model.Result;
import com.mlms.util.DatabaseUtil;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ResultDAO {

    public List<Result> getAllResults() {
        List<Result> results = new ArrayList<>();
        String query = "SELECT * FROM results";
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Result r = new Result();
                r.setId(rs.getInt("id"));
                r.setResultNumber(rs.getString("result_number"));
                r.setPatientId(rs.getInt("patient_id"));
                r.setTestNumber(rs.getString("test_number"));
                r.setTestResult(rs.getString("test_result"));
                
                String dateStr = rs.getString("result_date");
                if (dateStr != null && !dateStr.isEmpty()) {
                    r.setResultDate(LocalDate.parse(dateStr));
                }
                
                r.setTestCost(rs.getDouble("test_cost"));
                results.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    public void addResult(Result r) {
        String query = "INSERT INTO results(result_number, patient_id, test_number, test_result, result_date, test_cost) VALUES(?,?,?,?,?,?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, r.getResultNumber());
            pstmt.setInt(2, r.getPatientId());
            pstmt.setString(3, r.getTestNumber());
            pstmt.setString(4, r.getTestResult());
            pstmt.setString(5, r.getResultDate() != null ? r.getResultDate().toString() : null);
            pstmt.setDouble(6, r.getTestCost());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateResult(Result r) {
        String query = "UPDATE results SET result_number=?, patient_id=?, test_number=?, test_result=?, result_date=?, test_cost=? WHERE id=?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, r.getResultNumber());
            pstmt.setInt(2, r.getPatientId());
            pstmt.setString(3, r.getTestNumber());
            pstmt.setString(4, r.getTestResult());
            pstmt.setString(5, r.getResultDate() != null ? r.getResultDate().toString() : null);
            pstmt.setDouble(6, r.getTestCost());
            pstmt.setInt(7, r.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteResult(int id) {
        String query = "DELETE FROM results WHERE id=?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
