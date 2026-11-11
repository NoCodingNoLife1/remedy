package com.mlms.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class DashboardController {

    @FXML private StackPane contentArea;
    
    @FXML private Button btnDashboard;
    @FXML private Button btnPatients;
    @FXML private Button btnTests;
    @FXML private Button btnResults;
    @FXML private Button btnReports;
    @FXML private Button btnSettings;

    @FXML
    public void initialize() {
        showDashboard();
    }
    
    private void setActiveTab(Button activeBtn) {
        if (btnDashboard != null) {
            Button[] btns = {btnDashboard, btnPatients, btnTests, btnResults, btnReports, btnSettings};
            for (Button btn : btns) {
                btn.getStyleClass().remove("sidebar-item-active");
            }
            activeBtn.getStyleClass().add("sidebar-item-active");
        }
    }

    private void loadView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent view = loader.load();
            contentArea.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void showDashboard() {
        setActiveTab(btnDashboard);
        loadView("/com/mlms/fxml/Overview.fxml");
    }

    @FXML
    public void showPatients() {
        setActiveTab(btnPatients);
        loadView("/com/mlms/fxml/PatientView.fxml");
    }

    @FXML
    public void showTests() {
        setActiveTab(btnTests);
        loadView("/com/mlms/fxml/LabTestView.fxml");
    }

    @FXML
    public void showResults() {
        setActiveTab(btnResults);
        loadView("/com/mlms/fxml/ResultView.fxml");
    }

    @FXML
    public void showReports() {
        setActiveTab(btnReports);
        loadView("/com/mlms/fxml/ReportsView.fxml");
    }

    @FXML
    public void showSettings() {
        setActiveTab(btnSettings);
        loadView("/com/mlms/fxml/SettingsView.fxml");
    }
}
