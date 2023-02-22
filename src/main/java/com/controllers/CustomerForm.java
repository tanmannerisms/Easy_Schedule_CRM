package com.controllers;

import com.easyschedule.Instance;
import com.location.Country;
import com.location.Division;
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
        countrySelector.setItems(null);
        if (customer != null){
            customerImported = true;
            idField.setText(String.valueOf(customer.getId()));
            nameField.setText(customer.getName());
            addressField.setText(customer.getAddress());
            postalCodeField.setText(customer.getPostalCode());
            phoneNumberField.setText(customer.getPhoneNumber());
            countrySelector.setValue(Instance.getCountry(customer.getDivision().getCountryId()).getCountryName());
            setDivisionSelectorOptions();
            divisionSelector.setValue(Instance.getDivision(customer.getDivisionId()).getDivisionName());
        }
        else {
            customerImported = false;
            countrySelector.setValue(Instance.getCountry(1).getCountryName());
            setDivisionSelectorOptions();
        }
    }
    @FXML
    private void onSaveClick(ActionEvent actionEvent) {

    }
    @FXML
    private void setDivisionSelectorOptions() {
        Country country = Instance.getCountry(countrySelector.getValue());
        divisionSelector.setItems(null);
    }
}
