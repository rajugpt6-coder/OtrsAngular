package com.techment.OtrsSystem.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Entity
@Table( name = "tbl_user" )
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @Column(name = "email")
    @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\."
            +"[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@"
            +"(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?",
            message="{invalid.email}")
    private String email;

    @Column(name = "password")
    @JsonIgnore
    @NotNull
    private String password;

    @Column(name = "first_name")
    @NotNull
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "last_name")
    @NotNull
    private String lastName;

    @NotNull
    @Column(name = "emp_id")
    private String employeeId;

    @Column(name = "activation_status")
    private String activationStatus;

    @Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$",
            message="{invalid.phonenumber}")
    @Column(name = "working_no")
    @NotNull
    private String workingNumber;


    @Column(name = "landline_no")
    private String landline;

    @Column(name = "phone_no")
    @Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$",
            message="{invalid.phonenumber}")
    @NotNull
    private String phoneNo;

    /**
     * Default Constructor.
     */
    protected User() {
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "tbl_user_role", joinColumns
            = @JoinColumn(name = "user_id",
            referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id",
                    referencedColumnName = "id"))

    private List<Role> roles;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_gender")
    private Gender gender;

    @OneToMany
    private List<Ticket> tickets;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "fk_csr")
    private CustomerServiceRepresentative customerServiceRepresentative;

    public User(@Pattern(regexp = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\."
            + "[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@"
            + "(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?",
            message = "{invalid.email}") @NotNull String email, @NotNull String password, String firstName,  String middleName, @NotNull String lastName, @NotNull String employeeId, String activationStatus,
            @NotNull String workingNumber,
             String landline,
            @NotNull String phoneNo, @NotNull List<Role> roles, @NotNull Gender gender) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.employeeId = employeeId;
        this.activationStatus = activationStatus;
        this.workingNumber = workingNumber;
        this.landline = landline;
        this.phoneNo = phoneNo;
        this.roles = roles;
        this.gender = gender;
    }

    public User(long id, @Pattern(regexp = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\."
            + "[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@"
            + "(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?",
            message = "{invalid.email}") String email, @NotNull String password, @NotNull String firstName, String middleName, @NotNull String lastName, @NotNull String employeeId, String activationStatus, @Pattern(regexp = "^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$",
            message = "{invalid.phonenumber}") @NotNull String workingNumber, String landline, @Pattern(regexp = "^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$",
            message = "{invalid.phonenumber}") @NotNull String phoneNo, List<Role> roles, Gender gender, List<Ticket> tickets, CustomerServiceRepresentative customerServiceRepresentative) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.employeeId = employeeId;
        this.activationStatus = activationStatus;
        this.workingNumber = workingNumber;
        this.landline = landline;
        this.phoneNo = phoneNo;
        this.roles = roles;
        this.gender = gender;
        this.tickets = tickets;
        this.customerServiceRepresentative = customerServiceRepresentative;
    }

    public User(@Pattern(regexp = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\."
            + "[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@"
            + "(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?",
            message = "{invalid.email}") String email, @NotNull String password, @NotNull String firstName, String middleName, @NotNull String lastName, @NotNull String employeeId, String activationStatus, @Pattern(regexp = "^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$",
            message = "{invalid.phonenumber}") @NotNull String workingNumber, String landline, @Pattern(regexp = "^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$",
            message = "{invalid.phonenumber}") @NotNull String phoneNo, List<Role> roles, Gender gender, CustomerServiceRepresentative customerServiceRepresentative) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.employeeId = employeeId;
        this.activationStatus = activationStatus;
        this.workingNumber = workingNumber;
        this.landline = landline;
        this.phoneNo = phoneNo;
        this.roles = roles;
        this.gender = gender;

        this.customerServiceRepresentative = customerServiceRepresentative;
    }

    public String getActivationStatus() {
        return activationStatus;
    }

    public void setActivationStatus(String activationStatus) {
        this.activationStatus = activationStatus;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    public CustomerServiceRepresentative getCustomerServiceRepresentative() {
        return customerServiceRepresentative;
    }

    public void setCustomerServiceRepresentative(CustomerServiceRepresentative customerServiceRepresentative) {
        this.customerServiceRepresentative = customerServiceRepresentative;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getWorkingNumber() {
        return workingNumber;
    }

    public void setWorkingNumber(String workingNumber) {
        this.workingNumber = workingNumber;
    }

    public String getLandline() {
        return landline;
    }

    public void setLandline(String landline) {
        this.landline = landline;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }
}
