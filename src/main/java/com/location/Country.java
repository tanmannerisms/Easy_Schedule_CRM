package com.location;

public class Country extends Place{
    public Country(int id, String countryName) {
        setId(id);
        setName(countryName);
    }
    @Override
    public String toString() {
        return this.name;
    }
}
