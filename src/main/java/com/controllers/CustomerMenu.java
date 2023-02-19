package com.controllers;

import com.easyschedule.Instance;
import com.people.Customer;
import com.utils.Query;
import com.window.Window;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class CustomerMenu extends Controller implements Initializable {
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
        // Does a customer need to change for the event listener to fire or does adding a customer fire the listener?
        Instance.allCustomers.addListener((ListChangeListener<? super Customer>) change -> customerTable.setItems(Instance.allCustomers));
        customerTable.setItems(Instance.allCustomers);

        // To remove
        System.out.println(Instance.allCustomers);

    }
    @FXML
    private void onSearchClick(ActionEvent actionEvent) {
        ObservableList<Customer> searchResults;
        searchResults = searchCustomer();
        if (searchResults != null) {
            customerTable.setItems(searchResults);
        }
        else openNotifyWindow("No customers found.", actionEvent);
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
    private ObservableList<Customer> searchCustomer() {
        try {
            int customerId = Integer.parseInt(customerSearchField.getText());
            return FXCollections.observableArrayList(Instance.lookupCustomer(customerId));
        } catch (NumberFormatException e) {
            return Instance.lookupCustomer(customerSearchField.getText());
        }
    }
}
