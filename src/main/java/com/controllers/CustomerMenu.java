package com.controllers;

import com.easyschedule.Instance;
import com.people.Customer;
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
    /**
     * Calls to update the list of all customers, set table columns and adds a lister to the list of customers in
     * the instance class to automatically update the table when a customer is added or removed.
     * @see Instance#updateCustomerList()
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Instance.updateCustomerList();
        setTableColumns();
        // Listener works for deleting Customers from allCustomers!!!
        // Does a customer need to change for the event listener to fire or does adding a customer fire the listener?
        Instance.getAllCustomers().addListener(
                (ListChangeListener<? super Customer>) change -> customerTable.setItems(Instance.getAllCustomers()));
        customerTable.setItems(Instance.getAllCustomers());
    }

    /**
     * Called when the search button is clicked. Used for finding a customer with a specific ID or name.
     * @param actionEvent used to open an error window.
     */
    @FXML
    private void onSearchClick(ActionEvent actionEvent) {
        ObservableList<Customer> searchResults;
        searchResults = searchCustomers();
        if (searchResults != null) {
            customerTable.setItems(searchResults);
        }
        else openNotifyWindow(actionEvent, "notify.searchCustomerFailed");
    }

    /**
     * Searches for a customer depending on if the input is an integer or a string.
     * @return a list of customers to set the table to.
     * @see Instance#lookupCustomer(String)
     * @see Instance#lookupCustomer(int)
     */
    private ObservableList<Customer> searchCustomers() {
        try {
            int customerId = Integer.parseInt(customerSearchField.getText());
            return FXCollections.observableArrayList(Instance.lookupCustomer(customerId));
        } catch (NumberFormatException e) {
            return Instance.lookupCustomer(customerSearchField.getText());
        }
    }

    /**
     * Opens up a window to view the schedule of the selected customer. Opens an error window if nothing selected.
     * @param actionEvent used to open error window.
     */
    @FXML
    private void onViewClick(ActionEvent actionEvent) {
        Window viewCustomer = new Window("calendar.fxml", "Customer Info");
        CalendarView controller = (CalendarView) viewCustomer.getController();
        controller.setCustomer(getSelectedCustomer(actionEvent));
        viewCustomer.showWindow();
    }

    /**
     * Opens a window to create a new customer.
     * @param actionEvent used to open the window.
     */
    @FXML
    private void onAddClick(ActionEvent actionEvent) {
        Window addCustomer = new Window("customer.fxml", "Add Customer");
        addCustomer.showWindowAndWait(actionEvent);
    }

    /**
     * Opens a window to edit the currently selected customer. Refreshes the table after window closes.
     * @param actionEvent used to open the window.
     * @see CustomerForm#setCustomer(Customer)
     */
    @FXML
    private void onModifyClick(ActionEvent actionEvent) {
        Window modifyCustomer = new Window("customer.fxml", "Edit Customer");
        CustomerForm controller = (CustomerForm) modifyCustomer.getController();
        controller.setCustomer(getSelectedCustomer(actionEvent));
        modifyCustomer.showWindowAndWait(actionEvent);
        customerTable.refresh();
    }

    /**
     * Trys to delete the customer from the database and opens a notify window detailing the success of the deletion.
     * @param actionEvent
     */
    @FXML
    private void onDeleteClick(ActionEvent actionEvent) {
        Customer customer = getSelectedCustomer(actionEvent);
        if (customer != null){
            if (hasAssociatedAppointments(customer)){
                openNotifyWindow(
                        actionEvent,
                        "notify.pleaseDelete1",
                        String.valueOf(customer.getId()),
                        "notify.pleaseDelete2"
                );
            }
            else if(!Instance.deleteCustomer(customer)) {
                openNotifyWindow(
                        actionEvent,
                        "Deletion of customer with ID ",
                        String.valueOf(customer.getId()),
                        "notify.fail");
            }
            else {
                openNotifyWindow(
                        actionEvent,
                        "Deletion of customer with ID ",
                        String.valueOf(customer.getId()),
                        "notify.success"
                );
            }
        }
    }

    /**
     * Changes the window back to the main menu.
     * @param actionEvent used to return to the previous window.
     */
    @FXML
    private void onBackClick(ActionEvent actionEvent) {
        Window.changeScene(actionEvent, "main-menu.fxml", "Main Menu");
    }

    /**
     * Sets the getters for the table columns.
     */
    private void setTableColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("Id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("Address"));
        divisionColumn.setCellValueFactory(new PropertyValueFactory<>("DivisionName"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("PhoneNumber"));
    }

    /**
     * Gets the customer that is selected in the table. Opens error window if nothing selected.
     * @param actionEvent used to open the error window.
     * @return the selected Customer.
     */
    private Customer getSelectedCustomer(ActionEvent actionEvent) {
        Customer selectedCustomer = (Customer) customerTable.getSelectionModel().getSelectedItem();
        if (selectedCustomer == null) {
            openNotifyWindow(actionEvent, "notify.selectCustomer");
        }
        actionEvent.consume();
        return selectedCustomer;
    }

    /**
     * Checks to see if the customer has any associated appointments or not. Used primarily to restrict the deletion
     * of a customer that has appointments.
     * @param customer the customer to check.
     * @return true if has appointments.
     */
    private boolean hasAssociatedAppointments(Customer customer) {
        if (customer.getAssociatedAppointments().isEmpty()) {
            return false;
        }
        else return true;
    }
}
