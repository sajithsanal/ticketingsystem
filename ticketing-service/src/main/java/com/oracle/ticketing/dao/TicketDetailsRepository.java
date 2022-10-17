package com.oracle.ticketing.dao;

import com.oracle.ticketing.entity.TicketDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketDetailsRepository extends JpaRepository<TicketDetailsEntity, Long>{

    TicketDetailsEntity findByTicketId(Long ticketId);
    List<TicketDetailsEntity> findAllByCustomerIdOrderByLastUpdatedDateDesc(Long customerId);

}
