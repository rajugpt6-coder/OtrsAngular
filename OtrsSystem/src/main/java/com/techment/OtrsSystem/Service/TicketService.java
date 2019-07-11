package com.techment.OtrsSystem.Service;

import com.techment.OtrsSystem.Repository.TicketRepository;
import com.techment.OtrsSystem.Repository.UserRepository;
import com.techment.OtrsSystem.Security.JwtProvider;
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

    @Autowired
    private UserService userService;

    @Autowired
    private JwtProvider jwtProvider;


    public Ticket createTicket(String category, String description, String status, String title, long id, String token){
        LOGGER.info("New user attempting raise ticket");
        Ticket ticket = null;
        if(userRepository.existsById(id) &&
               userRepository.findById(id).get().getEmail().equalsIgnoreCase(jwtProvider.getUsername(userService.filterToken(token))) &&
        userRepository.findById(id).get().getActivationStatus().equalsIgnoreCase("active")) {
            ticket = ticketRepository.save(new Ticket(category, title, description, Timestamp.valueOf(LocalDateTime.now()), Timestamp.valueOf(LocalDateTime.now().plusDays(MAX_DAYS)), status, userRepository.findById(id).get()));
        }
        return ticket;

    }

    public Page<Ticket> findTicketsByUserId (Long id, Pageable pageable, String token) {
        LOGGER.info("New user attempting to retieve tickets ");
        Page<Ticket> ticket = null;
        token = userService.filterToken(token);

        if(userRepository.existsById(id) && userRepository.findById(id).get().getActivationStatus().equalsIgnoreCase("active")
           && userRepository.findById(id).get().getEmail().equalsIgnoreCase(jwtProvider.getUsername(token)) ) {
            return ticketRepository.findByUser(userRepository.findById(id).get(), pageable);
        }
        return ticket;

    }



    public Ticket findTicketById(long ticketId, long userId, String token) throws NoSuchElementException{
        token = userService.filterToken(token);
        if(userRepository.findById(userId).get().getEmail().equalsIgnoreCase(jwtProvider.getUsername(token)) &&
            ticketRepository.findUserById(ticketId).equals(userRepository.findByEmail(jwtProvider.getUsername(token)))) {
            return ticketRepository.findById(ticketId).orElseThrow(() -> new NoSuchElementException("No tickets found"));
        }
        return null;
    }

    public Page<Ticket> getTicketsByCategory(long id, String category, String status, Pageable pageable, String token) {
        token = userService.filterToken(token);
        if(userRepository.findById(id).get().getEmail().equalsIgnoreCase(jwtProvider.getUsername(token))) {
            return ticketRepository.findByCategoryAndStatus(category, status, pageable);
        }
        return null;
    }

    public void resolveIssue(long ticketId, long csrId, String token) {
        LOGGER.info("resolver fetching raised issues");
        token = userService.filterToken(token);
        if(userRepository.findById(csrId).get().getEmail().equalsIgnoreCase(jwtProvider.getUsername(token))) {
            CustomerServiceRepresentative customerServiceRepresentative = userRepository.findCustomerServiceRepresentativeById(csrId);
            Ticket ticket = ticketRepository.findById(ticketId).get();
            ticket.setCustomerServiceRepresentative(customerServiceRepresentative);
            ticketRepository.save(ticket);
        }

    }

    public void updateStatus(String status, long ticketId, String token, long id) {
        token = userService.filterToken(token);
        if(userRepository.findById(id).get().getEmail().equalsIgnoreCase(jwtProvider.getUsername(token))){
            ticketRepository.updateTicketStatus(status, ticketId);
        }

    }

    public Page<Ticket> getAllTickets(Pageable pageable) throws NoSuchElementException {
        LOGGER.info("Admin attempting to retieve all tickets ");
        return ticketRepository.findAll(pageable);
    }

    public Page<Ticket> getClaimedTickets(long csrId,Pageable pageable, String token) {
        token = userService.filterToken(token);
        if(userRepository.findById(csrId).get().getEmail().equalsIgnoreCase(jwtProvider.getUsername(token))) {
            return ticketRepository.findByCustomerServiceRepresentative(userRepository.findCustomerServiceRepresentativeById(csrId), pageable);
        }
        return null;
    }

    //searching
    public Page<Ticket> findTicketsByTitle(String title, Pageable pageable){
        return ticketRepository.findByTitle(title, pageable);
    }

    public Page<Ticket> findTicketsByTitleAndUser(String title, long userId, Pageable pageable, String token){
        token = userService.filterToken(token);
        if(userRepository.findById(userId).get().getEmail().equalsIgnoreCase(jwtProvider.getUsername(token))) {
            return ticketRepository.findByTitleAndUser(title, userRepository.findById(userId).get(), pageable);
        }
        return null;
    }

    public Page<Ticket> findTicketsByTitleAndStatus(String status, long userId, Pageable pageable, String token){
        token = userService.filterToken(token);
        if(userRepository.findById(userId).get().getEmail().equalsIgnoreCase(jwtProvider.getUsername(token))) {
            return ticketRepository.findByStatusAndUser(status, userRepository.findById(userId).get(), pageable);
        }
        return null;
    }

    public Page<Ticket> findTicketsByStatus(String status, Pageable pageable){
        return ticketRepository.findByStatus(status, pageable);
    }

    public Page<Ticket> findTicketsByCategory(String category, Pageable pageable){
        return ticketRepository.findByCategory(category, pageable);
    }

    public Page<Ticket> findTicketsByDueDate(Timestamp dueDate, Pageable pageable){
        return ticketRepository.findByDueDate(dueDate, pageable);
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
