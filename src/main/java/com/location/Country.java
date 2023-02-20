package com.location;

public class Country {
    private int countryId;
    private String countryName;

    public Country(int id, String countryName) {
        setCountryId(id);
        setCountryName(countryName);
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }
}
