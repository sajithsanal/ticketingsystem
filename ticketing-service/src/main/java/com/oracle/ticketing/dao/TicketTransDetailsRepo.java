package com.oracle.ticketing.dao;

import com.oracle.ticketing.entity.TicketTransDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketTransDetailsRepo extends JpaRepository<TicketTransDetailsEntity, Long> {

    List<TicketTransDetailsEntity> findAllByTicketIdOrderByLastUpdatedDateDesc(Long ticketId);

}
