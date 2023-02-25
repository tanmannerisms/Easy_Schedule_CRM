package com.easyschedule;

import com.people.Contact;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public class Appointment {
    private int appointmentId, userId, customerId, contactId;
    private String title, description, location, type;
    private ZonedDateTime startDate, endDate;

    public Appointment(int userId, int customerId, int contactId,
                       String title, String description, String location,
                       String type, ZonedDateTime startDate, ZonedDateTime endDate) {
        setUserId(userId);
        setCustomerId(customerId);
        setTitle(title);
        setDescription(description);
        setLocation(location);
        setType(type);
        setContactId(contactId);
        setStartDate(startDate);
        setEndDate(endDate);
    }
    public Appointment(int appointmentId, int userId, int customerId, int contactId,
                       String title, String description, String location,
                       String type, ZonedDateTime startDate, ZonedDateTime endDate) {
        this(userId, customerId, contactId, title, description, location, type, startDate, endDate);
        setAppointmentId(appointmentId);
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int id) {
        this.contactId = id;
    }

    public ZonedDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(ZonedDateTime startDate) {
        this.startDate = startDate;
    }

    public ZonedDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(ZonedDateTime endDate) {
        this.endDate = endDate;
    }
}
