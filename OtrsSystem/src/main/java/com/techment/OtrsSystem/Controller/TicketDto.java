package com.techment.OtrsSystem.Controller;

import com.techment.OtrsSystem.domain.CustomerServiceRepresentative;

import javax.validation.constraints.NotNull;

public class TicketDto {


    @NotNull
    private String title;

    @NotNull
    private String description;


    private String status;

    @NotNull
    private String category;


    private CustomerServiceRepresentative customerServiceRepresentative;



    public TicketDto(String title, String description,  String category) {
        this.title = title;
        this.description = description;
        this.category = category;

    }


    protected TicketDto() {}

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public CustomerServiceRepresentative getCustomerServiceRepresentative() {
        return customerServiceRepresentative;
    }

    public void setCustomerServiceRepresentative(CustomerServiceRepresentative customerServiceRepresentative) {
        this.customerServiceRepresentative = customerServiceRepresentative;
    }
}
