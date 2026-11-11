package com.mlms.dao;

import com.mlms.model.Patient;
import com.mlms.util.DatabaseUtil;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PatientDAO {

    public List<Patient> getAllPatients() {
        List<Patient> patients = new ArrayList<>();
        String query = "SELECT * FROM patients";
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Patient p = new Patient();
                p.setId(rs.getInt("id"));
                p.setPatientNumber(rs.getString("patient_number"));
                p.setName(rs.getString("name"));
                
                String dobStr = rs.getString("dob");
                if (dobStr != null && !dobStr.isEmpty()) {
                    p.setDob(LocalDate.parse(dobStr));
                }

                p.setGender(rs.getString("gender"));
                p.setPhone(rs.getString("phone"));
                p.setAddress(rs.getString("address"));

                String dateStr = rs.getString("registered_date");
                if (dateStr != null && !dateStr.isEmpty()) {
                    p.setRegisteredDate(LocalDate.parse(dateStr));
                }
                patients.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patients;
    }

    public void addPatient(Patient p) {
        String query = "INSERT INTO patients(patient_number, name, dob, gender, phone, address, registered_date) VALUES(?,?,?,?,?,?,?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, p.getPatientNumber());
            pstmt.setString(2, p.getName());
            pstmt.setString(3, p.getDob() != null ? p.getDob().toString() : null);
            pstmt.setString(4, p.getGender());
            pstmt.setString(5, p.getPhone());
            pstmt.setString(6, p.getAddress());
            pstmt.setString(7, p.getRegisteredDate() != null ? p.getRegisteredDate().toString() : null);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updatePatient(Patient p) {
        String query = "UPDATE patients SET patient_number=?, name=?, dob=?, gender=?, phone=?, address=?, registered_date=? WHERE id=?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, p.getPatientNumber());
            pstmt.setString(2, p.getName());
            pstmt.setString(3, p.getDob() != null ? p.getDob().toString() : null);
            pstmt.setString(4, p.getGender());
            pstmt.setString(5, p.getPhone());
            pstmt.setString(6, p.getAddress());
            pstmt.setString(7, p.getRegisteredDate() != null ? p.getRegisteredDate().toString() : null);
            pstmt.setInt(8, p.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletePatient(int id) {
        String query = "DELETE FROM patients WHERE id=?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Patient getPatientById(int id) {
        String query = "SELECT * FROM patients WHERE id=?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Patient p = new Patient();
                p.setId(rs.getInt("id"));
                p.setPatientNumber(rs.getString("patient_number"));
                p.setName(rs.getString("name"));
                
                String dobStr = rs.getString("dob");
                if (dobStr != null && !dobStr.isEmpty()) {
                    p.setDob(LocalDate.parse(dobStr));
                }

                p.setGender(rs.getString("gender"));
                p.setPhone(rs.getString("phone"));
                p.setAddress(rs.getString("address"));

                String dateStr = rs.getString("registered_date");
                if (dateStr != null && !dateStr.isEmpty()) {
                    p.setRegisteredDate(LocalDate.parse(dateStr));
                }
                return p;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
