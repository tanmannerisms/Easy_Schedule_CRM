package com.location;

public class Division {
    private int divisionId, countryId;
    private String divisionName;
    public Division(int divisionId, int countryId, String divisionName) {
        setDivisionId(divisionId);
        setCountryId(countryId);
        setDivisionName(divisionName);
    }

    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    public int getDivisionId() {
        return divisionId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }

    public String getDivisionName() {
        return divisionName;
    }
}
