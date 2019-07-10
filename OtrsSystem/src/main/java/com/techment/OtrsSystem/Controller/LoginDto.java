package com.techment.OtrsSystem.Controller;

import javax.validation.constraints.NotNull;

public class LoginDto {
    @NotNull
    private String username;

    @NotNull
    private String password;

    private String firstName;

    private String lastName;

    private String middleName;

    private String phoneNo;

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
    /**
     * Full constructor
     * @param username
     * @param password
     */
    public LoginDto(String username, String password, String firstName, String lastName, String middleName, String phoneNo) {
        this(username, password);
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.phoneNo = phoneNo;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
