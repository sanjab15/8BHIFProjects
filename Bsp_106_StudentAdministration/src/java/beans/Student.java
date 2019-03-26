/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Jan
 */
public class Student implements Serializable {

    private String attendingClass;
    private String lastname;
    private String firstname;
    private LocalDate dateOfBirth;
    private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private String credentials;

    public Student() {
    }

    public Student(String[] tokens) {
        attendingClass = tokens[0];
        lastname = tokens[1];
        firstname = tokens[2];
        credentials = this.firstname + " " + this.lastname + "," + attendingClass;
        dateOfBirth = LocalDate.parse(tokens[3], dateTimeFormatter);
    }

    public String getCredentials() {
        return credentials;
    }

    public void setCredentials(String credentials) {
        this.credentials = credentials;
    }

    public String getFullname() {
        return firstname + " " + lastname;
    }

    public String getAttendingClass() {
        return attendingClass;
    }

    public void setAttendingClass(String attendingClass) {
        this.attendingClass = attendingClass;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public DateTimeFormatter getDateTimeFormatter() {
        return dateTimeFormatter;
    }

    public void setDateTimeFormatter(DateTimeFormatter dateTimeFormatter) {
        this.dateTimeFormatter = dateTimeFormatter;
    }

}
