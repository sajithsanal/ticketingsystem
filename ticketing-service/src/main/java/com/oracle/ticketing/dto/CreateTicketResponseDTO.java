package com.oracle.ticketing.dto;

public class CreateTicketResponseDTO {

    private String status;
    private Long ticketId;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getTicketId() {
        return ticketId;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }
}
