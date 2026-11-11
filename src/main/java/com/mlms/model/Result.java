package com.mlms.model;

import java.time.LocalDate;

public class Result {
    private int id;
    private String resultNumber;
    private int patientId;
    private String testNumber;
    private String testResult;
    private LocalDate resultDate;
    private double testCost;

    public Result() {}

    public Result(int id, String resultNumber, int patientId, String testNumber, String testResult, LocalDate resultDate, double testCost) {
        this.id = id;
        this.resultNumber = resultNumber;
        this.patientId = patientId;
        this.testNumber = testNumber;
        this.testResult = testResult;
        this.resultDate = resultDate;
        this.testCost = testCost;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getResultNumber() {
        return resultNumber;
    }

    public void setResultNumber(String resultNumber) {
        this.resultNumber = resultNumber;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getTestNumber() {
        return testNumber;
    }

    public void setTestNumber(String testNumber) {
        this.testNumber = testNumber;
    }

    public String getTestResult() {
        return testResult;
    }

    public void setTestResult(String testResult) {
        this.testResult = testResult;
    }

    public LocalDate getResultDate() {
        return resultDate;
    }

    public void setResultDate(LocalDate resultDate) {
        this.resultDate = resultDate;
    }

    public double getTestCost() {
        return testCost;
    }

    public void setTestCost(double testCost) {
        this.testCost = testCost;
    }
}
