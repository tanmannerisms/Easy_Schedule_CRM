package com.controllers;

import com.easyschedule.Instance;
import com.location.Country;
import com.location.Division;
import com.location.Place;
import com.people.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.Iterator;
import java.util.ResourceBundle;

public class CustomerForm extends Controller implements Initializable {
    private boolean customerImported;
    protected Customer customer;
    @FXML
    private TextField idField, nameField, addressField, postalCodeField, phoneNumberField;
    @FXML
    private ComboBox<String> divisionSelector;
    @FXML
    private ComboBox<String> countrySelector;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        countrySelector.setItems(getCountryNames(Instance.getAllCountries()));
        customerImported = false;
        countrySelector.setValue(Instance.getCountry(1).getName());
        setDivisionSelectorOptions();
    }
    @FXML
    private void onSaveClick(ActionEvent actionEvent) {
        if (customerImported) {
            customer.setName(nameField.getText());
            customer.setPhoneNumber(phoneNumberField.getText());
            customer.setAddress(addressField.getText());
            customer.setPostalCode(postalCodeField.getText());
            customer.setDivisionId(Instance.getDivision(divisionSelector.getValue()));
        }
        else {

        }
    }
    @FXML
    private void setDivisionSelectorOptions() {
        Country country = Instance.getCountry(countrySelector.getValue());
        divisionSelector.setItems(getDivisionNames(Instance.getDivision(country)));
    }
    public void setCustomer(Customer customer) {
        this.customer = customer;
        customerImported = true;
        idField.setText(String.valueOf(customer.getId()));
        nameField.setText(customer.getName());
        addressField.setText(customer.getAddress());
        postalCodeField.setText(customer.getPostalCode());
        phoneNumberField.setText(customer.getPhoneNumber());
        countrySelector.setValue(Instance.getCountry(customer.getDivision().getCountryId()).getName());
        setDivisionSelectorOptions();
        divisionSelector.setValue(Instance.getDivision(customer.getDivisionId()).getName());
    }
    private ObservableList<String> getCountryNames(ObservableList<Country> countries) {
        ObservableList<String> names = FXCollections.observableArrayList();
        Iterator<Country> listIterator = countries.listIterator();
        while (listIterator.hasNext()) {
            names.add(listIterator.next().getName());
        }
        return names;
    }
    private ObservableList<String> getDivisionNames(ObservableList<Division> divisions) {
        ObservableList<String> names = FXCollections.observableArrayList();
        Iterator<Division> listIterator = divisions.listIterator();
        while (listIterator.hasNext()) {
            names.add(listIterator.next().getName());
        }
        return names;
    }
}
