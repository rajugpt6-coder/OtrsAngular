package com.techment.OtrsSystem.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Entity
@Table(name = "tbl_issue")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @Column(name = "category")
    @NotNull
    private String category;

    @Column(name = "title")
    @NotNull
    private String title;

    @Column(name = "issue_description")
    @NotNull
    private String issueDescription;

    @Column(name = "cr_date")
    private Timestamp createdDate;

    @Column(name = "due_date")
    private Timestamp dueDate;

    @Column(name = "status")
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_user")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_csr")
    private CustomerServiceRepresentative customerServiceRepresentative;

    protected Ticket() {}



    public Ticket(@NotNull String category, @NotNull String title, @NotNull String issueDescription, Timestamp createdDate, Timestamp dueDate, String status, User user) {
        this.category = category;
        this.title = title;
        this.issueDescription = issueDescription;
        this.createdDate = createdDate;
        this.dueDate = dueDate;
        this.status = status;
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIssueDescription() {
        return issueDescription;
    }

    public void setIssueDescription(String issueDescription) {
        this.issueDescription = issueDescription;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public Timestamp getDueDate() {
        return dueDate;
    }

    public void setDueDate(Timestamp dueDate) {
        this.dueDate = dueDate;
    }

    public CustomerServiceRepresentative getCustomerServiceRepresentative() {
        return customerServiceRepresentative;
    }

    public void setCustomerServiceRepresentative(CustomerServiceRepresentative customerServiceRepresentative) {
        this.customerServiceRepresentative = customerServiceRepresentative;
    }
}
