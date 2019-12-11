package com.example.ledger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Group implements Serializable {
    private String groupName;
    private List<User> userArrayList;

    public Group() {
        this.userArrayList = new ArrayList<>();
    }

    public Group(String groupName, List<User> userArrayList) {
        this.groupName = groupName;
        this.userArrayList = userArrayList;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<User> getUserArrayList() {
        return userArrayList;
    }

    public void setUserArrayList(List<User> userArrayList) {
        this.userArrayList = userArrayList;
    }
}
