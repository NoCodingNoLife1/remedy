package com.mlms.controller;

import com.mlms.dao.PatientDAO;
import com.mlms.dao.ResultDAO;
import com.mlms.dao.TestDAO;
import com.mlms.model.Patient;
import com.mlms.model.Result;
import com.mlms.model.Test;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

public class ReportsController {

    @FXML private StackPane reportContainer;
    @FXML private Button btnPrev;
    @FXML private Button btnNext;
    @FXML private Label reportCounterLabel;

    private List<Node> generatedReports = new ArrayList<>();
    private List<LabReportController> generatedControllers = new ArrayList<>();
    private int currentIndex = -1;

    private PatientDAO patientDAO = new PatientDAO();
    private ResultDAO resultDAO = new ResultDAO();
    private TestDAO testDAO = new TestDAO();

    @FXML
    public void initialize() {
        generateReports();
        updateView();
    }

    private void generateReports() {
        generatedReports.clear();
        generatedControllers.clear();

        List<Result> allResults = resultDAO.getAllResults();
        
        // Fetch test details for quick lookup
        List<Test> allTests = testDAO.getAllTests();
        Map<String, String> testNameMap = allTests.stream()
                .collect(Collectors.toMap(Test::getTestNumber, Test::getTestName, (a, b) -> a));
        
        // Group by Patient ID and Date
        Map<String, List<Result>> groupedResults = new HashMap<>();
        for (Result r : allResults) {
            String key = r.getPatientId() + "_" + (r.getResultDate() != null ? r.getResultDate().toString() : "unknown");
            groupedResults.computeIfAbsent(key, k -> new ArrayList<>()).add(r);
        }

        // For each group, create a report FXML instance
        for (Map.Entry<String, List<Result>> entry : groupedResults.entrySet()) {
            List<Result> group = entry.getValue();
            if (group.isEmpty()) continue;

            Patient patient = patientDAO.getPatientById(group.get(0).getPatientId());
            if (patient == null) continue;

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mlms/fxml/LabReport.fxml"));
                Node reportNode = loader.load();
                LabReportController controller = loader.getController();

                // Calculate Age
                String age = "N/A";
                if (patient.getDob() != null) {
                    age = Period.between(patient.getDob(), LocalDate.now()).getYears() + " years old";
                }

                String dateStr = group.get(0).getResultDate() != null ? group.get(0).getResultDate().toString() : "N/A";
                
                // We'll use the result number of the first test as the Test ID
                String testId = group.get(0).getResultNumber();

                controller.setPatientDetails(
                    patient.getName(),
                    dateStr,
                    patient.getPatientNumber(),
                    age,
                    patient.getGender(),
                    testId
                );

                for (Result r : group) {
                    String testName = testNameMap.getOrDefault(r.getTestNumber(), r.getTestNumber());
                    controller.addResultRow(testName, r.getTestResult());
                }

                generatedReports.add(reportNode);
                generatedControllers.add(controller);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (!generatedReports.isEmpty()) {
            currentIndex = 0;
        }
    }

    private void updateView() {
        if (generatedReports.isEmpty()) {
            reportContainer.getChildren().clear();
            reportContainer.getChildren().add(new Label("No completed test reports found."));
            reportCounterLabel.setText("0 / 0");
            btnPrev.setDisable(true);
            btnNext.setDisable(true);
            return;
        }

        reportContainer.getChildren().setAll(generatedReports.get(currentIndex));
        reportCounterLabel.setText((currentIndex + 1) + " / " + generatedReports.size());
        
        btnPrev.setDisable(currentIndex == 0);
        btnNext.setDisable(currentIndex == generatedReports.size() - 1);
    }

    @FXML
    public void showPreviousReport() {
        if (currentIndex > 0) {
            currentIndex--;
            updateView();
        }
    }

    @FXML
    public void showNextReport() {
        if (currentIndex < generatedReports.size() - 1) {
            currentIndex++;
            updateView();
        }
    }

    @FXML
    public void printCurrentReport() {
        if (currentIndex >= 0 && currentIndex < generatedReports.size()) {
            Node reportNode = generatedReports.get(currentIndex);
            LabReportController controller = generatedControllers.get(currentIndex);
            controller.printReport(reportNode, reportContainer.getScene().getWindow());
        }
    }
}
