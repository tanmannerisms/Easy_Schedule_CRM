package com.location;

public class Division extends Place {
    private int countryId;
    public Division(int divisionId, int countryId, String divisionName) {
        setId(divisionId);
        setCountryId(countryId);
        setName(divisionName);
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public int getCountryId() {
        return countryId;
    }
}
