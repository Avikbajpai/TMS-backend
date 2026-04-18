package com.nexustms.repository;

import com.nexustms.model.Ticket;
import com.nexustms.model.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, String> {
    List<Ticket> findByStatus(TicketStatus status);
}
