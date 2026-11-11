package com.mlms.controller;

import com.mlms.dao.PatientDAO;
import com.mlms.dao.ResultDAO;
import com.mlms.dao.TestDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class OverviewController {
    @FXML private Label totalPatientsLabel;
    @FXML private Label totalTestsLabel;
    @FXML private Label completedTestsLabel;

    private PatientDAO patientDAO = new PatientDAO();
    private TestDAO testDAO = new TestDAO();
    private ResultDAO resultDAO = new ResultDAO();

    @FXML
    public void initialize() {
        loadStatistics();
    }

    private void loadStatistics() {
        int totalPatients = patientDAO.getAllPatients().size();
        int totalTests = testDAO.getAllTests().size();
        int completedTests = resultDAO.getAllResults().size();

        totalPatientsLabel.setText(String.valueOf(totalPatients));
        totalTestsLabel.setText(String.valueOf(totalTests));
        completedTestsLabel.setText(String.valueOf(completedTests));
    }
}
