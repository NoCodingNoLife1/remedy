package com.mlms.controller;

import com.mlms.dao.PatientDAO;
import com.mlms.dao.ResultDAO;
import com.mlms.dao.TestDAO;
import com.mlms.model.Patient;
import com.mlms.model.Result;
import com.mlms.model.Test;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class ResultController {

    @FXML private TableView<Result> resultTable;
    @FXML private TableColumn<Result, String> patientColumn;

    private ResultDAO resultDAO = new ResultDAO();
    private PatientDAO patientDAO = new PatientDAO();
    private TestDAO testDAO = new TestDAO();
    
    private ObservableList<Result> resultList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        patientColumn.setCellValueFactory(cellData -> {
            int pId = cellData.getValue().getPatientId();
            Patient p = patientDAO.getPatientById(pId);
            return new javafx.beans.property.SimpleStringProperty(p != null ? p.getPatientNumber() : String.valueOf(pId));
        });
        loadData();
    }

    private void loadData() {
        resultList.clear();
        resultList.addAll(resultDAO.getAllResults());
        resultTable.setItems(resultList);
    }

    private TextFormatter<String> createNumberFormatter(int maxLength) {
        Pattern pattern = Pattern.compile("\\d{0," + maxLength + "}");
        UnaryOperator<TextFormatter.Change> filter = change -> {
            if (pattern.matcher(change.getControlNewText()).matches()) {
                return change;
            }
            return null;
        };
        return new TextFormatter<>(filter);
    }

    private TextFormatter<String> createLettersFormatter(int maxLength) {
        Pattern pattern = Pattern.compile("[a-zA-Z\\s]{0," + maxLength + "}");
        UnaryOperator<TextFormatter.Change> filter = change -> {
            if (pattern.matcher(change.getControlNewText()).matches()) {
                return change;
            }
            return null;
        };
        return new TextFormatter<>(filter);
    }

    @FXML
    public void addResult() {
        Dialog<Result> dialog = new Dialog<>();
        dialog.setTitle("Add New Result");
        
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
        
        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        
        TextField numberField = new TextField();
        numberField.setTextFormatter(createNumberFormatter(4));
        
        ComboBox<Patient> patientComboBox = new ComboBox<>();
        patientComboBox.setItems(FXCollections.observableArrayList(patientDAO.getAllPatients()));
        patientComboBox.setConverter(new StringConverter<Patient>() {
            @Override
            public String toString(Patient p) {
                return p != null ? p.getPatientNumber() + " - " + p.getName() : "";
            }
            @Override
            public Patient fromString(String string) { return null; }
        });
        
        ComboBox<Test> testComboBox = new ComboBox<>();
        testComboBox.setItems(FXCollections.observableArrayList(testDAO.getAllTests()));
        testComboBox.setConverter(new StringConverter<Test>() {
            @Override
            public String toString(Test t) {
                return t != null ? t.getTestNumber() + " - " + t.getTestName() : "";
            }
            @Override
            public Test fromString(String string) { return null; }
        });
        
        TextField costField = new TextField();
        costField.setEditable(false);
        costField.setDisable(true);

        testComboBox.setOnAction(e -> {
            Test selected = testComboBox.getSelectionModel().getSelectedItem();
            if (selected != null) {
                long cost = (long) selected.getTestCost();
                costField.setText(String.valueOf(cost));
            } else {
                costField.setText("");
            }
        });
        
        TextField resultField = new TextField();
        resultField.setTextFormatter(createLettersFormatter(35));
        
        DatePicker datePicker = new DatePicker();
        
        grid.add(new Label("Result Number (4 digits):"), 0, 0); grid.add(numberField, 1, 0);
        grid.add(new Label("Patient:"), 0, 1); grid.add(patientComboBox, 1, 1);
        grid.add(new Label("Lab Test:"), 0, 2); grid.add(testComboBox, 1, 2);
        grid.add(new Label("Test Cost:"), 0, 3); grid.add(costField, 1, 3);
        grid.add(new Label("Test Result (Max 35 chars):"), 0, 4); grid.add(resultField, 1, 4);
        grid.add(new Label("Result Date:"), 0, 5); grid.add(datePicker, 1, 5);
        
        dialog.getDialogPane().setContent(grid);
        
        final Button btOk = (Button) dialog.getDialogPane().lookupButton(saveButtonType);
        btOk.addEventFilter(ActionEvent.ACTION, event -> {
            if (!validateResult(numberField, patientComboBox, testComboBox, resultField, datePicker)) {
                event.consume();
            }
        });

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                Patient p = patientComboBox.getValue();
                Test t = testComboBox.getValue();
                double cost = t.getTestCost();
                return new Result(0, numberField.getText().trim(), p.getId(), t.getTestNumber(), resultField.getText().trim(), datePicker.getValue(), cost);
            }
            return null;
        });
        
        dialog.showAndWait().ifPresent(res -> {
            resultDAO.addResult(res);
            loadData();
        });
    }

    @FXML
    public void editResult() {
        Result selected = resultTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        Dialog<Result> dialog = new Dialog<>();
        dialog.setTitle("Edit Result");
        
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
        
        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        
        TextField numberField = new TextField(selected.getResultNumber());
        numberField.setTextFormatter(createNumberFormatter(4));
        
        List<Patient> patients = patientDAO.getAllPatients();
        ComboBox<Patient> patientComboBox = new ComboBox<>(FXCollections.observableArrayList(patients));
        patientComboBox.setConverter(new StringConverter<Patient>() {
            @Override
            public String toString(Patient p) { return p != null ? p.getPatientNumber() + " - " + p.getName() : ""; }
            @Override
            public Patient fromString(String string) { return null; }
        });
        patientComboBox.setValue(patients.stream().filter(p -> p.getId() == selected.getPatientId()).findFirst().orElse(null));
        
        List<Test> tests = testDAO.getAllTests();
        ComboBox<Test> testComboBox = new ComboBox<>(FXCollections.observableArrayList(tests));
        testComboBox.setConverter(new StringConverter<Test>() {
            @Override
            public String toString(Test t) { return t != null ? t.getTestNumber() + " - " + t.getTestName() : ""; }
            @Override
            public Test fromString(String string) { return null; }
        });
        testComboBox.setValue(tests.stream().filter(t -> t.getTestNumber().equals(selected.getTestNumber())).findFirst().orElse(null));
        
        TextField costField = new TextField(String.valueOf((long)selected.getTestCost()));
        costField.setEditable(false);
        costField.setDisable(true);

        testComboBox.setOnAction(e -> {
            Test t = testComboBox.getSelectionModel().getSelectedItem();
            if (t != null) {
                costField.setText(String.valueOf((long)t.getTestCost()));
            } else {
                costField.setText("");
            }
        });
        
        TextField resultField = new TextField(selected.getTestResult());
        resultField.setTextFormatter(createLettersFormatter(35));
        
        DatePicker datePicker = new DatePicker(selected.getResultDate());
        
        grid.add(new Label("Result Number (4 digits):"), 0, 0); grid.add(numberField, 1, 0);
        grid.add(new Label("Patient:"), 0, 1); grid.add(patientComboBox, 1, 1);
        grid.add(new Label("Lab Test:"), 0, 2); grid.add(testComboBox, 1, 2);
        grid.add(new Label("Test Cost:"), 0, 3); grid.add(costField, 1, 3);
        grid.add(new Label("Test Result (Max 35 chars):"), 0, 4); grid.add(resultField, 1, 4);
        grid.add(new Label("Result Date:"), 0, 5); grid.add(datePicker, 1, 5);
        
        dialog.getDialogPane().setContent(grid);
        
        final Button btOk = (Button) dialog.getDialogPane().lookupButton(saveButtonType);
        btOk.addEventFilter(ActionEvent.ACTION, event -> {
            if (!validateResult(numberField, patientComboBox, testComboBox, resultField, datePicker)) {
                event.consume();
            }
        });

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                selected.setResultNumber(numberField.getText().trim());
                selected.setPatientId(patientComboBox.getValue().getId());
                selected.setTestNumber(testComboBox.getValue().getTestNumber());
                selected.setTestCost(testComboBox.getValue().getTestCost());
                selected.setTestResult(resultField.getText().trim());
                selected.setResultDate(datePicker.getValue());
                return selected;
            }
            return null;
        });
        
        dialog.showAndWait().ifPresent(res -> {
            resultDAO.updateResult(res);
            loadData();
        });
    }

    private boolean validateResult(TextField numberField, ComboBox<Patient> patientComboBox, ComboBox<Test> testComboBox, TextField resultField, DatePicker datePicker) {
        String num = numberField.getText().trim();
        String resultTxt = resultField.getText().trim();

        if (num.length() != 4) {
            showAlert("Validation Error", "Result Number must be exactly 4 digits.");
            return false;
        }
        if (patientComboBox.getValue() == null) {
            showAlert("Validation Error", "Please select a patient.");
            return false;
        }
        if (testComboBox.getValue() == null) {
            showAlert("Validation Error", "Please select a lab test.");
            return false;
        }
        if (resultTxt.isEmpty()) {
            showAlert("Validation Error", "Test Result cannot be empty.");
            return false;
        }
        if (datePicker.getValue() == null) {
            showAlert("Validation Error", "Please select a result date.");
            return false;
        }
        
        return true;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void deleteResult() {
        Result selected = resultTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            resultDAO.deleteResult(selected.getId());
            loadData();
        }
    }
}
