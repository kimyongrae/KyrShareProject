package com.example.dynamictest1;

import java.io.Serializable;

public class Cricketer implements Serializable {

    private String cricketerName;
    private String teamName;

    public Cricketer() {
    }

    public Cricketer(String cricketerName, String teamName) {
        this.cricketerName = cricketerName;
        this.teamName = teamName;
    }

    public String getCricketerName() {
        return cricketerName;
    }

    public void setCricketerName(String cricketerName) {
        this.cricketerName = cricketerName;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
}
