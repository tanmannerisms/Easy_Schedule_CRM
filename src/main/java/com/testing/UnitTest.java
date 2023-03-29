package com.testing;
import com.controllers.AppointmentManagement;
import com.easyschedule.Instance;
import com.people.Customer;
import com.people.User;
import com.utils.JDBC;
import com.utils.Query;
import de.saxsys.javafx.test.JfxRunner;
import javafx.event.ActionEvent;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;

import static org.junit.Assert.*;

@RunWith(JfxRunner.class)
public class UnitTest {
    @Test
    public void inBusinessHours() {

        AppointmentManagement testController = new AppointmentManagement();

        LocalDate today = LocalDate.now();
        LocalTime startTime = LocalTime.of(9, 0);
        ZonedDateTime startDateTime = ZonedDateTime.of(today, startTime, Instance.BUSINESSZONEID);

        LocalTime endTime = LocalTime.of(10,0);
        ZonedDateTime endDateTime = ZonedDateTime.of(today, endTime, Instance.BUSINESSZONEID);

        boolean result = testController.inBusinessHours(startDateTime, endDateTime, new ActionEvent());

        assertTrue(result);
    }
    @Test
    @DisplayName("Disallow appointment outside business hours.")
    public void notInBusinessHours() {
        AppointmentManagement testController = new AppointmentManagement();

        LocalDate today = LocalDate.now();
        LocalTime startTime = LocalTime.of(7, 0);
        ZonedDateTime startDateTime = ZonedDateTime.of(today, startTime, Instance.BUSINESSZONEID);

        LocalTime endTime = LocalTime.of(8,0);
        ZonedDateTime endDateTime = ZonedDateTime.of(today, endTime, Instance.BUSINESSZONEID);

        boolean result = testController.inBusinessHours(startDateTime, endDateTime, new ActionEvent());

        assertFalse(result);

    }

    @Test
    public void addValidateRemoveCustomerFromDB() {
        boolean result = true;

        JDBC.openConnection();

        Customer customer = new Customer("Tanner", "Somewhere in Ohio", "123456", "123-456-7890", 1);
        Instance.setActiveUser(new User(0, "unittest", "unittest"));
        boolean addSuccess = Instance.addCustomer(customer);

        if (addSuccess) {
            ResultSet resultSet = Query.selectConditional("customer_name, address, phone, postal_code, division_ID", "customers", "created_by = ", "unittest");
            try {
                while (resultSet.isFirst()) {
                    if (resultSet.getString(1) != customer.getName()) result = false;
                    if (resultSet.getString(2) != customer.getAddress()) result = false;
                    if (resultSet.getString(3) != customer.getPhoneNumber()) result = false;
                    if (resultSet.getString(4) != customer.getPostalCode()) result = false;
                    if (resultSet.getInt(5) != customer.getDivisionId()) result = false;
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
            Instance.deleteCustomer(customer);
        }
        else result = false;

        JDBC.closeConnection();

        assertTrue(result);
    }
}
