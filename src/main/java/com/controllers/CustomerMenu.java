package com.controllers;

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

    }
    @FXML
    private void onModifyClick(ActionEvent actionEvent) {

    }
    @FXML
    private void onDeleteClick(ActionEvent actionEvent) {

    }
}
