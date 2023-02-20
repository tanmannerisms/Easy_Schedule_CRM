package com.controllers;

import com.easyschedule.Instance;
import com.location.Country;
import com.location.Division;
import com.people.Customer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class CustomerForm extends Controller implements Initializable {
    private boolean customerImported;
    private Customer customer;
    @FXML
    private TextField idField, nameField, addressField, postalCodeField, phoneNumberField;
    @FXML
    private ComboBox<Division> divisionSelector;
    @FXML
    private ComboBox<Country> countrySelector;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        countrySelector.setItems(Instance.getAllCountries());
        setDivisions();
        if (customer != null){
            customerImported = true;
            idField.setText(String.valueOf(customer.getId()));
            nameField.setText(customer.getName());
            addressField.setText(customer.getAddress());
            postalCodeField.setText(customer.getPostalCode());
            phoneNumberField.setText(customer.getPhoneNumber());
//            divisionSelector.selectionModelProperty().
//            countrySelector.selectionModelProperty().setValue();
        }
        else {

        }
    }
    @FXML
    private void onSaveClick(ActionEvent actionEvent) {

    }
    @FXML
    private void setDivisions() {

    }
}
