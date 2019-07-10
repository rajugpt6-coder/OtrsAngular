package com.techment.OtrsSystem.Controller;

import javax.validation.constraints.NotNull;

public class LoginDto {
    @NotNull
    private String username;

    @NotNull
    private String password;


    private String workingNumber;


    private String landline;


    private String gender;


    private String firstName;


    private String lastName;


    private String middleName;



    private String phoneNo;



    private String employeeId;

//    private String activationStatus;


    /**
     * Default constructor
     */
    protected LoginDto() {
    }

    /**
     * Partial constructor
     * @param username
     * @param password
     */
    public LoginDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public LoginDto(@NotNull String username, @NotNull String password, String workingNumber, String landline, String gender, String firstName,  String lastName, String middleName, String phoneNo, String employeeId) {
        this.username = username;
        this.password = password;
        this.workingNumber = workingNumber;
        this.landline = landline;
        this.gender = gender;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.phoneNo = phoneNo;
        this.employeeId = employeeId;

    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getWorkingNumber() {
        return workingNumber;
    }

    public String getLandline() {
        return landline;
    }

    public String getGender() {
        return gender;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public String getEmployeeId() {
        return employeeId;
    }


}
