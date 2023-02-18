package com.controllers;

import com.people.Customer;
import com.utils.CustomerQuery;
import com.window.Window;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CustomerMenu extends Controller implements Initializable {
    private ObservableList<Customer> customers;
    @FXML
    private TextField customerSearchField;
    @FXML
    private TableView customerTable;
    @FXML
    private TableColumn<Customer, Integer> idColumn;
    @FXML
    private TableColumn<Customer, String> nameColumn, addressColumn, divisionColumn, phoneColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setTableColumns();
        customers = FXCollections.observableArrayList();
        try {
            customers = CustomerQuery.queryAllCustomers();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // Does a customer need to change for the event listener to fire or does adding a customer fire the listener?
        customers.addListener((ListChangeListener<? super Customer>) change -> customerTable.setItems(customers));
        customerTable.setItems(customers);


        // To remove
        System.out.println(customers);

    }
    @FXML
    private void onViewClick(ActionEvent actionEvent) {

    }
    @FXML
    private void onAddClick(ActionEvent actionEvent) {
        Window addCustomer = new Window("add-customer.fxml", "Add Customer");
        addCustomer.showWindowAndWait(actionEvent);
    }
    @FXML
    private void onModifyClick(ActionEvent actionEvent) {
        Window modifyCustomer = new Window("modify-customer.fxml", "Edit Customer");
        modifyCustomer.showWindowAndWait(actionEvent);
    }
    @FXML
    private void onDeleteClick(ActionEvent actionEvent) {

    }
    private void setTableColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("Id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("Address"));
        divisionColumn.setCellValueFactory(new PropertyValueFactory<>("Division"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("PhoneNumber"));
    }
}
