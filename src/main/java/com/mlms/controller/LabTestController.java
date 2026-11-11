package com.mlms.controller;

import com.mlms.dao.TestDAO;
import com.mlms.model.Test;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class LabTestController {

    @FXML private TableView<Test> testTable;
    @FXML private TextField searchField;

    private TestDAO testDAO = new TestDAO();
    private ObservableList<Test> testList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        loadData();
    }

    private void loadData() {
        testList.clear();
        testList.addAll(testDAO.getAllTests());
        testTable.setItems(testList);
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
    public void addTest() {
        Dialog<Test> dialog = new Dialog<>();
        dialog.setTitle("Add New Lab Test");
        
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
        
        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        
        TextField numberField = new TextField();
        numberField.setTextFormatter(createNumberFormatter(4));
        
        TextField nameField = new TextField();
        nameField.setTextFormatter(createLettersFormatter(35));
        
        TextField costField = new TextField();
        costField.setTextFormatter(createNumberFormatter(6));
        
        grid.add(new Label("Test Number (4 digits):"), 0, 0); grid.add(numberField, 1, 0);
        grid.add(new Label("Test Name (Max 35 chars):"), 0, 1); grid.add(nameField, 1, 1);
        grid.add(new Label("Test Cost (Max UGX 100,000):"), 0, 2); grid.add(costField, 1, 2);
        
        dialog.getDialogPane().setContent(grid);
        
        final Button btOk = (Button) dialog.getDialogPane().lookupButton(saveButtonType);
        btOk.addEventFilter(ActionEvent.ACTION, event -> {
            if (!validateTest(numberField, nameField, costField)) {
                event.consume();
            }
        });

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                double cost = costField.getText().trim().isEmpty() ? 0 : Double.parseDouble(costField.getText().trim());
                return new Test(0, numberField.getText().trim(), nameField.getText().trim(), cost);
            }
            return null;
        });
        
        dialog.showAndWait().ifPresent(test -> {
            testDAO.addTest(test);
            loadData();
        });
    }

    @FXML
    public void editTest() {
        Test selected = testTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        Dialog<Test> dialog = new Dialog<>();
        dialog.setTitle("Edit Lab Test");
        
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
        
        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        
        TextField numberField = new TextField(selected.getTestNumber());
        numberField.setTextFormatter(createNumberFormatter(4));
        
        TextField nameField = new TextField(selected.getTestName());
        nameField.setTextFormatter(createLettersFormatter(35));
        
        long costLong = (long) selected.getTestCost();
        TextField costField = new TextField(String.valueOf(costLong));
        costField.setTextFormatter(createNumberFormatter(6));
        
        grid.add(new Label("Test Number (4 digits):"), 0, 0); grid.add(numberField, 1, 0);
        grid.add(new Label("Test Name (Max 35 chars):"), 0, 1); grid.add(nameField, 1, 1);
        grid.add(new Label("Test Cost (Max UGX 100,000):"), 0, 2); grid.add(costField, 1, 2);
        
        dialog.getDialogPane().setContent(grid);
        
        final Button btOk = (Button) dialog.getDialogPane().lookupButton(saveButtonType);
        btOk.addEventFilter(ActionEvent.ACTION, event -> {
            if (!validateTest(numberField, nameField, costField)) {
                event.consume();
            }
        });

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                selected.setTestNumber(numberField.getText().trim());
                selected.setTestName(nameField.getText().trim());
                double cost = costField.getText().trim().isEmpty() ? 0 : Double.parseDouble(costField.getText().trim());
                selected.setTestCost(cost);
                return selected;
            }
            return null;
        });
        
        dialog.showAndWait().ifPresent(test -> {
            testDAO.updateTest(test);
            loadData();
        });
    }

    private boolean validateTest(TextField numberField, TextField nameField, TextField costField) {
        String num = numberField.getText().trim();
        String name = nameField.getText().trim();
        String costStr = costField.getText().trim();

        if (num.length() != 4) {
            showAlert("Validation Error", "Test Number must be exactly 4 digits.");
            return false;
        }
        if (name.isEmpty()) {
            showAlert("Validation Error", "Test Name cannot be empty.");
            return false;
        }
        if (costStr.isEmpty()) {
            showAlert("Validation Error", "Test Cost cannot be empty.");
            return false;
        }

        try {
            double cost = Double.parseDouble(costStr);
            if (cost > 100000) {
                showAlert("Validation Error", "Test Cost cannot exceed UGX 100,000.");
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert("Validation Error", "Cost must be a valid number.");
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
    public void deleteTest() {
        Test selected = testTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            testDAO.deleteTest(selected.getId());
            loadData();
        }
    }
}
