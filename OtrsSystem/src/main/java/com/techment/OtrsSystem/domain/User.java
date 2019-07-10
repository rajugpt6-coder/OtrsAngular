package com.techment.OtrsSystem.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
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
    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "activation_status")
    private String activationStatus;

    @Column(name = "phone_no")
    @Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$",
            message="{invalid.phonenumber}")
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

    @OneToMany
    @JoinColumn(name = "issue_id")
    private List<Ticket> tickets;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinTable(name = "tbl_user_csr")
    private CustomerServiceRepresentative customerServiceRepresentative;

    public User(String email, String password, String firstName, String middleName, String lastName, String phoneNo, List<Role> roles, List<Ticket> tickets, CustomerServiceRepresentative customerServiceRepresentative) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.phoneNo = phoneNo;
        this.roles = roles;
        this.tickets = tickets;
        this.customerServiceRepresentative = customerServiceRepresentative;
    }

    public User(String email, String password, String firstName, String middleName, String lastName, String phoneNo, List<Role> roles) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.phoneNo = phoneNo;
        this.roles = roles;
        this.tickets = tickets;
        this.customerServiceRepresentative = customerServiceRepresentative;
    }

    public User (String email, String password, String firstName,String middleName, String lastName, String phoneNo, Role role) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.phoneNo = phoneNo;
        this.roles = Arrays.asList(role);
    }

    public User (String email, String password, String firstName,String middleName, String lastName, String phoneNo, Role role, String activationStatus) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.phoneNo = phoneNo;
        this.roles = Arrays.asList(role);
        this.activationStatus = activationStatus;
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
}
