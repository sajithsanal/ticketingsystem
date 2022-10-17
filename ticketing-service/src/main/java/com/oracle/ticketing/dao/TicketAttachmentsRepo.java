package com.oracle.ticketing.dao;

import com.oracle.ticketing.entity.TicketAttachmentsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketAttachmentsRepo extends JpaRepository<TicketAttachmentsEntity, Long> {
}
