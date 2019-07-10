package com.techment.OtrsSystem.domain;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "tbl_csr")
public class CustomerServiceRepresentative {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @Column(name = "department")
    private String department;

    @OneToOne
    @JoinTable(name = "tbl_user_csr")
    private User user;

    @OneToMany
    @JoinColumn(name = "issue_id")
    private List<Ticket> assignedTickets;

    protected CustomerServiceRepresentative() {}

    public CustomerServiceRepresentative(String department, User user, List<Ticket> tickets) {
        this.department = department;
        this.user = user;
        this.assignedTickets = tickets;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Ticket> getTicket() {
        return assignedTickets;
    }

    public void setTicket(List<Ticket> tickets) {
        this.assignedTickets = tickets;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


}
