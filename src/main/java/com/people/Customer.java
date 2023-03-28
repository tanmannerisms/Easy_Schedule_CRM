package com.people;

import com.easyschedule.Appointment;
import com.easyschedule.Instance;
import com.location.Division;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Customer extends Person{
    private int divisionId;
    private String address, phoneNumber, postalCode;
    private ObservableList<Appointment> associatedAppointments = FXCollections.observableArrayList();

    public Customer (String name, String address, String postalCode, String phoneNumber, int divisionId) {
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.phoneNumber = phoneNumber;
        this.divisionId = divisionId;
    }
    public Customer(int id, String name, String address, String postalCode, String phoneNumber, int divisionId) {
        this(name, address, postalCode, phoneNumber, divisionId);
        this.id = id;
        associatedAppointments = Instance.getCustomerAppointments(this);
    }

    public int getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }
    public String  getDivisionName() {
        return Instance.getDivision(divisionId).getName();
    }
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public void printCustomer() {
        System.out.println(getName() + ":\n" + getAddress() + " " + getDivisionId() + ", " + getPostalCode() + "\n\n");
    }

    public ObservableList<Appointment> getAssociatedAppointments() {
        return associatedAppointments;
    }
    public void addAssociatedAppointments(Appointment appointment) {
        associatedAppointments.add(appointment);
    }
}
