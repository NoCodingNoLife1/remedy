package com.mlms.model;

public class Test {
    private int id;
    private String testNumber;
    private String testName;
    private double testCost;

    public Test() {}

    public Test(int id, String testNumber, String testName, double testCost) {
        this.id = id;
        this.testNumber = testNumber;
        this.testName = testName;
        this.testCost = testCost;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTestNumber() {
        return testNumber;
    }

    public void setTestNumber(String testNumber) {
        this.testNumber = testNumber;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public double getTestCost() {
        return testCost;
    }

    public void setTestCost(double testCost) {
        this.testCost = testCost;
    }
}
