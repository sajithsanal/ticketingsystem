package com.oracle.ticketing.entity;


import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "ticket_attachments", schema = "ticketing_schema")
public class TicketAttachmentsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "ticket_id")
    private Long ticketId;

    @Column(name = "trans_id")
    private Long transId;

    @Column(name = "attachment_name")
    private String attachmentName;


    @Lob
    @Column(name = "attachment")
    private byte[] attachment;

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

    public Long getTransId() {
        return transId;
    }

    public void setTransId(Long transId) {
        this.transId = transId;
    }

    public byte[] getAttachment() {
        return attachment;
    }

    public void setAttachment(byte[] attachment) {
        this.attachment = attachment;
    }
}