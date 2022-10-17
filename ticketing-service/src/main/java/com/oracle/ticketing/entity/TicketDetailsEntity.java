package com.oracle.ticketing.entity;


import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Audited
@Table(name = "ticket_details", schema = "ticketing_schema")
public class TicketDetailsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_id")
    private Long ticketId;


    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "complaint_details")
    private String complaintDetails;

    @Column(name = "status")
    private String status;

    @Column(name = "created_date")
    private Timestamp createdDate;

    @Column(name = "updated_date")
    private Timestamp lastUpdatedDate;

    @Column(name = "created_by")
    private Long createdById;

    @Column(name = "updated_by")
    private Long lastUpdatedById;

    @Column(name = "assigned_to")
    private String assignedTo;


    @Column(name = "creator_type")
    private String creatorType;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id")
    private List<TicketTransDetailsEntity> ticketTransDetailsEntityList = new ArrayList<>();


    public String getCreatorType() {
        return creatorType;
    }

    public void setCreatorType(String creatorType) {
        this.creatorType = creatorType;
    }

    public List<TicketTransDetailsEntity> getTicketTransDetailsEntityList() {
        return ticketTransDetailsEntityList;
    }

    public void setTicketTransDetailsEntityList(List<TicketTransDetailsEntity> ticketTransDetailsEntityList) {
        this.ticketTransDetailsEntityList = ticketTransDetailsEntityList;
    }

    public Long getTicketId() {
        return ticketId;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getComplaintDetails() {
        return complaintDetails;
    }

    public void setComplaintDetails(String complaintDetails) {
        this.complaintDetails = complaintDetails;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public Timestamp getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(Timestamp lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public Long getCreatedById() {
        return createdById;
    }

    public void setCreatedById(Long createdById) {
        this.createdById = createdById;
    }

    public Long getLastUpdatedById() {
        return lastUpdatedById;
    }

    public void setLastUpdatedById(Long lastUpdatedById) {
        this.lastUpdatedById = lastUpdatedById;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }
}