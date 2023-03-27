package com;
import com.controllers.AppointmentManagement;
import com.easyschedule.Instance;
import de.saxsys.javafx.test.JfxRunner;
import javafx.event.ActionEvent;
import javafx.scene.control.DatePicker;
import org.junit.Test;
import org.junit.runner.RunWith;

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
        LocalTime startTime = LocalTime.of(8, 0);
        ZonedDateTime startDateTime = ZonedDateTime.of(today, startTime, Instance.BUSINESSZONEID);

        LocalTime endTime = LocalTime.of(10,0);
        ZonedDateTime endDateTime = ZonedDateTime.of(today, endTime, Instance.BUSINESSZONEID);

        testController.startDatePicker = new DatePicker();
        testController.startDatePicker.setValue(today);

        boolean result = testController.inBusinessHours(startDateTime, endDateTime, new ActionEvent());

        assertTrue(result);
    }
    @Test
    public void notInBusinessHours() {
        AppointmentManagement testController = new AppointmentManagement();

        LocalDate today = LocalDate.now();
        LocalTime startTime = LocalTime.of(7, 59);
        ZonedDateTime startDateTime = ZonedDateTime.of(today, startTime, Instance.BUSINESSZONEID);

        LocalTime endTime = LocalTime.of(8,0);
        ZonedDateTime endDateTime = ZonedDateTime.of(today, endTime, Instance.BUSINESSZONEID);

        testController.startDatePicker = new DatePicker();
        testController.startDatePicker.setValue(today);

        boolean result = testController.inBusinessHours(startDateTime, endDateTime, new ActionEvent());

        assertFalse(result);

    }
}
