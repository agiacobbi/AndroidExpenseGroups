package com.example.ledger;

import java.io.Serializable;

/**
 * Cost data structure
 *
 */
public class Cost implements Serializable {
    private double amountCost;
    private String costDescription;
    private String user;
    private String userId;

    /**
     * DVC for cost class
     */
    public Cost(){
        this.amountCost = 0.0;
        this.costDescription ="blank";
    }

    /**
     * EVC
     * @param amountCost
     * @param costDescription
     */
    public Cost(double amountCost, String costDescription) {
        this.amountCost = amountCost;
        this.costDescription = costDescription;
    }

    /**
     * EVC
     * @param amountCost
     * @param costDescription
     * @param user
     * @param userId
     */
    public Cost(double amountCost, String costDescription, String user, String userId) {
        this.amountCost = amountCost;
        this.costDescription = costDescription;
        this.user = user;
        this.userId = userId;
    }

    /**
     *
     * @return String userID
     */
    public String getUserId() {
        return userId;
    }

    /**
     * sets userId to for instance of cost
     * @param userId
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     *
     * @return String user
     */
    public String getUser() {
        return user;
    }

    /**
     * sets user info
     * @param user used to contain the users information
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * EVC
     * @param amountCost
     * @param costDescription
     * @param user
     */
    public Cost(double amountCost, String costDescription, String user) {
        this.amountCost = amountCost;
        this.costDescription = costDescription;
        this.user = user;
    }

    /**
     *
     * @return
     */
    public String getCostDescription() {
        return costDescription;
    }

    /**
     * sets cost description
     * @param costDescription user description of what cost was for
     */
    public void setCostDescription(String costDescription) {
        this.costDescription = costDescription;
    }

    /**
     *
     * @return amount of the cost
     */
    public double getAmountCost() {
        return amountCost;
    }

    /**
     * sets amount cost
     * @param paramAmountCost user input for amount cost
     */
    public void setAmountCost(double paramAmountCost) {
        this.amountCost = paramAmountCost;
    }
}
