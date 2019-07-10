package com.techment.OtrsSystem.Service;

import com.techment.OtrsSystem.Repository.TicketRepository;
import com.techment.OtrsSystem.Repository.UserRepository;
import com.techment.OtrsSystem.domain.CustomerServiceRepresentative;
import com.techment.OtrsSystem.domain.Ticket;
import com.techment.OtrsSystem.domain.User;
import jdk.nashorn.internal.runtime.options.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import javax.xml.ws.http.HTTPException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class TicketService {

    public static final long MAX_DAYS = 8;
    private static final Logger LOGGER = LoggerFactory.getLogger(TicketService.class);

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;


    public Ticket createTicket(String category, String description, String status, String title, Long id){
        LOGGER.info("New user attempting raise ticket");
        Ticket ticket = null;
        if(userRepository.existsById(id) && userRepository.findById(id).get().getActivationStatus().equalsIgnoreCase("active")) {
            ticket = ticketRepository.save(new Ticket(category, title, description, Timestamp.valueOf(LocalDateTime.now()), Timestamp.valueOf(LocalDateTime.now().plusDays(MAX_DAYS)), status, userRepository.findById(id).get()));
        }
        return ticket;

    }

    public Page<Ticket> findTicketsByUserId (Long id, Pageable pageable) {
        Page<Ticket> ticket = null;
        if(userRepository.existsById(id) && userRepository.findById(id).get().getActivationStatus().equalsIgnoreCase("active")) {
            return ticketRepository.findByUser(userRepository.findById(id), pageable);
        }
        return ticket;

    }



    public Ticket findTicketById(long ticketId) throws NoSuchElementException{
            return ticketRepository.findById(ticketId).orElseThrow(()-> new NoSuchElementException("No tickets found"));
    }

    public Page<Ticket> getTicketsByCategory(String category, String status, Pageable pageable) {
        return ticketRepository.findByCategoryAndStatus(category, status, pageable);
    }

    public void resolveIssue(long id, long csrId) {
        CustomerServiceRepresentative customerServiceRepresentative = userRepository.findCustomerServiceRepresentativeById(csrId);
        Ticket ticket = ticketRepository.findById(id).get();
        ticket.setCustomerServiceRepresentative(customerServiceRepresentative);
        ticketRepository.save(ticket);
    }

    public void updateStatus(String status, long id) {
        ticketRepository.updateTicketStatus(status, id);
    }

    public Page<Ticket> getAllTickets(Pageable pageable) throws NoSuchElementException {
        return ticketRepository.findAll(pageable);
    }

    public Page<Ticket> getClaimedTickets(long csrId,Pageable pageable) {
        return ticketRepository.findByCustomerServiceRepresentative(userRepository.findCustomerServiceRepresentativeById(csrId), pageable);
    }

    //Analytics part

    public long countAllTickets () {
        return userRepository.count();
    }

    public long countTicketByCategory (String category) {
        return ticketRepository.countTicketByCategory(category);
    }

    public long countTicketByCategoryAndStatus (String category, String status){
        return ticketRepository.countTicketByCategoryAndStatus(category, status);
    }

}
