package com.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class Customer extends Controller implements Initializable {
    private Customer customer;
    @FXML
    private TextField idField, nameField, addressField, postalCodeField, phoneNumberField;
    @FXML
    private ComboBox<Division> divisionSelector;
    @FXML
    private ComboBox<Country> countrySelector;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    @FXML
    private void onSaveClick(ActionEvent actionEvent) {

    }
    @FXML
    private void setDivisions() {

    }
}
