package com.controllers;

import com.easyschedule.Appointment;
import com.easyschedule.Instance;
import com.location.Country;
import com.location.Division;
import com.people.Customer;
import com.utils.Query;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZonedDateTime;
import java.util.ResourceBundle;

public class CustomerLocator extends Controller implements Initializable {
    private final ObservableList<Customer> ALLCUSTOMERS = Instance.getAllCustomers();
    private ObservableList<Customer> tableCustomers = FXCollections.observableArrayList();
    private ObservableList<Division> divisonList = FXCollections.observableArrayList();
    @FXML
    private TableView<Customer> customersTable;
    @FXML
    private ComboBox<Division> divisionSelector;
    @FXML
    private ComboBox<Country> countrySelector;
    @FXML
    private TableColumn<Customer, Integer> idColumn;
    @FXML
    private TableColumn<Customer, String> nameColumn, phoneColumn, addressColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setTableColumns();
        countrySelector.setItems(Instance.getAllCountries());
        countrySelector.setValue(Instance.getCountry(1));
        selectCountry(new ActionEvent());
    }
    private void setTableColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("Id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("PhoneNumber"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("Address"));
    }
    @FXML
    private void selectCountry(ActionEvent actionEvent) {
        divisonList.clear();
        int selectedCountry = countrySelector.getValue().getId();
        for (Division division : Instance.getAllDivisions()) {
            if (division.getCountryId() == selectedCountry) {
                divisonList.add(division);
            }
        }
        divisionSelector.setItems(divisonList);
        actionEvent.consume();
    }

    @FXML
    private void selectDivision(ActionEvent actionEvent) {
        if (countrySelector.getValue() != null && divisionSelector.getValue() != null) {
            updateTable(actionEvent);
            return;
        }
        actionEvent.consume();
    }
    private void updateTable(ActionEvent actionEvent) {
        tableCustomers.clear();
        int divId = divisionSelector.getValue().getId();
        for (Customer customer : ALLCUSTOMERS) {
            if (customer.getDivisionId() == divId) {
                tableCustomers.add(customer);
            }
        }
        customersTable.setItems(tableCustomers);
        actionEvent.consume();
    }
}

