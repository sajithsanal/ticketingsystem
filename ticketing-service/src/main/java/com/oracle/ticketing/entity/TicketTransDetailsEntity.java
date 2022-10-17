package com.oracle.ticketing.entity;


import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Audited
@Table(name = "ticket_trans_details", schema = "ticketing_schema")
public class TicketTransDetailsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "ticket_id")
    private Long ticketId;

    @Column(name = "notes")
    private String notes;

    @Column(name = "status")
    private String status;

    @Column(name = "updated_date")
    private Timestamp lastUpdatedDate;

    @Column(name = "updated_by")
    private Long lastUpdatedById;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", referencedColumnName = "ticket_id", insertable = false, updatable = false)
    private TicketDetailsEntity ticketDetailsEntity;

    public TicketDetailsEntity getTicketDetailsEntity() {
        return ticketDetailsEntity;
    }

    public void setTicketDetailsEntity(TicketDetailsEntity ticketDetailsEntity) {
        this.ticketDetailsEntity = ticketDetailsEntity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTicketId() {
        return ticketId;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(Timestamp lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public Long getLastUpdatedById() {
        return lastUpdatedById;
    }

    public void setLastUpdatedById(Long lastUpdatedById) {
        this.lastUpdatedById = lastUpdatedById;
    }
}