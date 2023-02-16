package com.controllers;

import com.window.Window;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class CustomerMenu extends Controller implements Initializable {
    @FXML
    private TextField customerSearchField;
    @FXML
    private TableView customerTable;
    @FXML
    private TableColumn idColumn, nameColumn, addressColumn, divisionColumn, phoneColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        populateTable();
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
}
