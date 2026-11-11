package com.mlms.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.stage.Window;
import javafx.geometry.Pos;

public class LabReportController {

    @FXML
    private Label patientNameLabel;
    @FXML
    private Label resultDateLabel;
    @FXML
    private Label patientNumberLabel;
    @FXML
    private Label ageLabel;
    @FXML
    private Label sexLabel;
    @FXML
    private Label testNumberLabel;
    @FXML
    private VBox resultsTableBody;

    /**
     * Initializes the report with patient details.
     */
    public void setPatientDetails(String name, String date, String patientNo, String age, String sex, String testId) {
        patientNameLabel.setText(name != null ? name : "N/A");
        resultDateLabel.setText(date != null ? date : "N/A");
        patientNumberLabel.setText(patientNo != null ? patientNo : "N/A");
        ageLabel.setText(age != null ? age : "N/A");
        sexLabel.setText(sex != null ? sex : "N/A");
        testNumberLabel.setText(testId != null ? testId : "N/A");
    }

    /**
     * Adds a category row to the report (e.g., "Complete Blood Count")
     */
    public void addCategoryRow(String categoryName) {
        HBox box = new HBox();
        box.getStyleClass().add("results-category-box");
        Label label = new Label(categoryName);
        label.getStyleClass().add("results-category-label");
        box.getChildren().add(label);
        resultsTableBody.getChildren().add(box);
    }

    /**
     * Adds a test result row to the report.
     */
    public void addResultRow(String testName, String result) {
        HBox box = new HBox();
        box.getStyleClass().add("results-row-box");
        
        Label testLabel = new Label(testName);
        testLabel.getStyleClass().add("results-row-test-label");
        testLabel.setPrefWidth(350);
        
        Label resultLabel = new Label(result);
        resultLabel.getStyleClass().add("results-row-value-label");
        resultLabel.setPrefWidth(250);
        resultLabel.setAlignment(Pos.CENTER);
        
        box.getChildren().addAll(testLabel, resultLabel);
        resultsTableBody.getChildren().add(box);
    }
    
    /**
     * Clears all results from the table.
     */
    public void clearResults() {
        resultsTableBody.getChildren().clear();
    }

    /**
     * Prints the report node.
     */
    public boolean printReport(Node reportNode, Window owner) {
        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null && job.showPrintDialog(owner)) {
            boolean success = job.printPage(reportNode);
            if (success) {
                job.endJob();
                return true;
            }
        }
        return false;
    }
}
