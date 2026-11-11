package com.mlms.controller;

import com.mlms.dao.PatientDAO;
import com.mlms.model.Patient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.converter.DefaultStringConverter;

import java.time.LocalDate;
import java.time.Period;
import java.util.function.UnaryOperator;

public class PatientController {

    @FXML private TableView<Patient> patientTable;
    @FXML private TextField searchField;

    private PatientDAO patientDAO = new PatientDAO();
    private ObservableList<Patient> patientList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        loadData();
    }

    private void loadData() {
        patientList.clear();
        patientList.addAll(patientDAO.getAllPatients());
        patientTable.setItems(patientList);
    }

    private UnaryOperator<TextFormatter.Change> createLettersOnlyFilter(int maxLength) {
        return change -> {
            String newText = change.getControlNewText();
            if (newText.length() > maxLength) {
                return null;
            }
            if (newText.matches("[a-zA-Z\\s]*")) {
                return change;
            }
            return null;
        };
    }

    private UnaryOperator<TextFormatter.Change> createNumbersOnlyFilter(int maxLength) {
        return change -> {
            String newText = change.getControlNewText();
            if (newText.length() > maxLength) {
                return null;
            }
            if (newText.matches("\\d*")) {
                return change;
            }
            return null;
        };
    }

    private void setupDialogFields(
            GridPane grid, 
            TextField idField, 
            TextField givenNameField, 
            TextField surnameField, 
            ComboBox<String> genderBox, 
            TextField ageField, 
            TextField phoneField, 
            TextField addressField, 
            DatePicker dobPicker) {
        
        grid.setHgap(10); 
        grid.setVgap(10);
        
        // Limits & Validations
        givenNameField.setTextFormatter(new TextFormatter<>(new DefaultStringConverter(), "", createLettersOnlyFilter(35)));
        surnameField.setTextFormatter(new TextFormatter<>(new DefaultStringConverter(), "", createLettersOnlyFilter(35)));
        phoneField.setTextFormatter(new TextFormatter<>(new DefaultStringConverter(), "", createNumbersOnlyFilter(10)));
        
        genderBox.getItems().addAll("Male", "Female");
        
        ageField.setEditable(false);
        ageField.setStyle("-fx-background-color: #e9ecef; -fx-text-fill: #6c757d;"); // Make it look read-only
        
        dobPicker.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                int age = Period.between(newVal, LocalDate.now()).getYears();
                ageField.setText(String.valueOf(age));
            } else {
                ageField.clear();
            }
        });

        grid.add(new Label("Patient ID:"), 0, 0); grid.add(idField, 1, 0);
        grid.add(new Label("Given Name:"), 0, 1); grid.add(givenNameField, 1, 1);
        grid.add(new Label("Surname:"), 0, 2);    grid.add(surnameField, 1, 2);
        grid.add(new Label("Gender:"), 0, 3);     grid.add(genderBox, 1, 3);
        grid.add(new Label("Age:"), 0, 4);        grid.add(ageField, 1, 4);
        grid.add(new Label("Phone:"), 0, 5);      grid.add(phoneField, 1, 5);
        grid.add(new Label("Address:"), 0, 6);    grid.add(addressField, 1, 6);
        grid.add(new Label("Date of Birth:"), 0, 7); grid.add(dobPicker, 1, 7);
    }

    @FXML
    public void addPatient() {
        Dialog<Patient> dialog = new Dialog<>();
        dialog.setTitle("Add New Patient");
        
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
        
        GridPane grid = new GridPane();
        
        TextField idField = new TextField();
        TextField givenNameField = new TextField();
        TextField surnameField = new TextField();
        ComboBox<String> genderBox = new ComboBox<>();
        TextField ageField = new TextField();
        TextField phoneField = new TextField();
        TextField addressField = new TextField();
        DatePicker dobPicker = new DatePicker();
        
        setupDialogFields(grid, idField, givenNameField, surnameField, genderBox, ageField, phoneField, addressField, dobPicker);
        dialog.getDialogPane().setContent(grid);
        
        final Button btOk = (Button) dialog.getDialogPane().lookupButton(saveButtonType);
        btOk.addEventFilter(ActionEvent.ACTION, event -> {
            if (!validatePatientInput(idField, givenNameField, surnameField, genderBox, phoneField, dobPicker)) {
                event.consume();
            }
        });
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                String fullName = givenNameField.getText().trim() + " " + surnameField.getText().trim();
                return new Patient(
                    0, 
                    idField.getText().trim(),
                    fullName.trim(),
                    dobPicker.getValue(),
                    genderBox.getValue(),
                    phoneField.getText().trim(),
                    addressField.getText().trim(),
                    LocalDate.now()
                );
            }
            return null;
        });
        
        dialog.showAndWait().ifPresent(patient -> {
            patientDAO.addPatient(patient);
            loadData();
        });
    }

    @FXML
    public void editPatient() {
        Patient selected = patientTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        Dialog<Patient> dialog = new Dialog<>();
        dialog.setTitle("Edit Patient");
        
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
        
        GridPane grid = new GridPane();
        
        TextField idField = new TextField(selected.getPatientNumber() != null ? selected.getPatientNumber() : "");
        
        String given = "";
        String sur = "";
        if (selected.getName() != null) {
            String[] parts = selected.getName().split(" ", 2);
            given = parts[0];
            if (parts.length > 1) {
                sur = parts[1];
            }
        }
        
        TextField givenNameField = new TextField(given);
        TextField surnameField = new TextField(sur);
        ComboBox<String> genderBox = new ComboBox<>();
        TextField ageField = new TextField(String.valueOf(selected.getAge()));
        TextField phoneField = new TextField(selected.getPhone() != null ? selected.getPhone() : "");
        TextField addressField = new TextField(selected.getAddress() != null ? selected.getAddress() : "");
        DatePicker dobPicker = new DatePicker(selected.getDob());
        
        setupDialogFields(grid, idField, givenNameField, surnameField, genderBox, ageField, phoneField, addressField, dobPicker);
        
        if (selected.getGender() != null) {
            genderBox.setValue(selected.getGender());
        }
        
        dialog.getDialogPane().setContent(grid);
        
        final Button btOk = (Button) dialog.getDialogPane().lookupButton(saveButtonType);
        btOk.addEventFilter(ActionEvent.ACTION, event -> {
            if (!validatePatientInput(idField, givenNameField, surnameField, genderBox, phoneField, dobPicker)) {
                event.consume();
            }
        });
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                String fullName = givenNameField.getText().trim() + " " + surnameField.getText().trim();
                
                selected.setPatientNumber(idField.getText().trim());
                selected.setName(fullName.trim());
                selected.setDob(dobPicker.getValue());
                selected.setGender(genderBox.getValue());
                selected.setPhone(phoneField.getText().trim());
                selected.setAddress(addressField.getText().trim());
                return selected;
            }
            return null;
        });
        
        dialog.showAndWait().ifPresent(patient -> {
            patientDAO.updatePatient(patient);
            loadData();
        });
    }

    private boolean validatePatientInput(TextField idField, TextField givenNameField, TextField surnameField, ComboBox<String> genderBox, TextField phoneField, DatePicker dobPicker) {
        String idStr = idField.getText().trim();
        String given = givenNameField.getText().trim();
        String sur = surnameField.getText().trim();
        String phone = phoneField.getText().trim();

        if (idStr.isEmpty() || given.isEmpty() || sur.isEmpty() || genderBox.getValue() == null) {
            showAlert("Validation Error", "Patient ID, Given Name, Surname, and Gender are required.");
            return false;
        }

        if (dobPicker.getValue() == null) {
            showAlert("Validation Error", "Date of Birth is required.");
            return false;
        }
        
        if (dobPicker.getValue().isAfter(LocalDate.now())) {
            showAlert("Validation Error", "Date of Birth cannot be in the future.");
            return false;
        }

        if (!phone.isEmpty() && phone.length() != 10) {
            showAlert("Validation Error", "Phone number must be exactly 10 digits.");
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
    public void deletePatient() {
        Patient selected = patientTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            patientDAO.deletePatient(selected.getId());
            loadData();
        }
    }
}
