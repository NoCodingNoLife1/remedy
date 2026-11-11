package com.mlms.model;

import java.time.LocalDate;
import java.time.Period;

public class Patient {
    private int id;
    private String patientNumber;
    private String name;
    private LocalDate dob;
    private String gender;
    private String phone;
    private String address;
    private LocalDate registeredDate;

    public Patient() {}

    public Patient(int id, String patientNumber, String name, LocalDate dob, String gender, String phone, String address, LocalDate registeredDate) {
        this.id = id;
        this.patientNumber = patientNumber;
        this.name = name;
        this.dob = dob;
        this.gender = gender;
        this.phone = phone;
        this.address = address;
        this.registeredDate = registeredDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPatientNumber() {
        return patientNumber;
    }

    public void setPatientNumber(String patientNumber) {
        this.patientNumber = patientNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public int getAge() {
        if (dob != null) {
            return Period.between(dob, LocalDate.now()).getYears();
        }
        return 0;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDate getRegisteredDate() {
        return registeredDate;
    }

    public void setRegisteredDate(LocalDate registeredDate) {
        this.registeredDate = registeredDate;
    }
}
