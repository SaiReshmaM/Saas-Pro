package com.saas.backend.dto;

public class InvoiceDTO {

    private Double amount;
    private String status;

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}