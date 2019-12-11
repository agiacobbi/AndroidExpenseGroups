package com.example.ledger;

import java.io.Serializable;

public class Cost implements Serializable {
    private double amountCost;
    private String costDescription;
    private String user;
    private String userId;

    public Cost(){
        this.amountCost = 0.0;
        this.costDescription ="blank";
    }
    public Cost(double amountCost, String costDescription) {
        this.amountCost = amountCost;
        this.costDescription = costDescription;
    }

    public Cost(double amountCost, String costDescription, String user, String userId) {
        this.amountCost = amountCost;
        this.costDescription = costDescription;
        this.user = user;
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Cost(double amountCost, String costDescription, String user) {
        this.amountCost = amountCost;
        this.costDescription = costDescription;
        this.user = user;
    }

    public String getCostDescription() {
        return costDescription;
    }

    public void setCostDescription(String costDescription) {
        this.costDescription = costDescription;
    }

    public double getAmountCost() {
        return amountCost;
    }

    public void setAmountCost(double paramAmountCost) {
        this.amountCost = paramAmountCost;
    }
}
