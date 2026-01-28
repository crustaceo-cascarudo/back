package com.fpmislata.back.domain.model;

import java.util.Date;
import java.util.List;

public class Reservation {
    Long id;
    String name;
    String email;
    Date reservation_date;
    Integer phone_number;
    String message;

    public Reservation(Long id, String name, String email, Date reservation_date, Integer phone_number, String message) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.reservation_date = reservation_date;
        this.phone_number = phone_number;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getReservation_date() {
        return reservation_date;
    }

    public void setReservation_date(Date reservation_date) {
        this.reservation_date = reservation_date;
    }

    public Integer getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(Integer phone_number) {
        this.phone_number = phone_number;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}


