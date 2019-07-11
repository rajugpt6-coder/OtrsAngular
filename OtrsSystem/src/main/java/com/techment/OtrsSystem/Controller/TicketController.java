package com.techment.OtrsSystem.Controller;

import com.techment.OtrsSystem.Security.JwtTokenFilter;
import com.techment.OtrsSystem.Service.CustomerServiceRepresentativeService;
import com.techment.OtrsSystem.Service.TicketService;
import com.techment.OtrsSystem.Service.UserService;
import com.techment.OtrsSystem.domain.Ticket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/users/{id}")
public class TicketController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    public static final String INITIAL_STATUS = "pending";

    @Autowired
    CustomerServiceRepresentativeService customerServiceRepresentativeService;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private UserService userService;



    @PostMapping("/ticket")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CSR') or hasRole('ROLE_USER')")
    @ResponseStatus(HttpStatus.CREATED)
    public Optional<Ticket> raiseTicket(@RequestBody @Validated TicketDto ticketDto, @PathVariable("id") Long id,
                                        @RequestHeader(value = "Authorization") String token){

        return Optional.ofNullable(ticketService.createTicket(ticketDto.getCategory(), ticketDto.getDescription(), INITIAL_STATUS,
                ticketDto.getTitle(), id, token));

    }

    @GetMapping("/tickets")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CSR') or hasRole('ROLE_USER')")
    public Page<Ticket> getTickets(@PathVariable("id") Long id,  Pageable pageable, @RequestHeader(value = "Authorization") String token){
            return ticketService.findTicketsByUserId(id, pageable, token);
//            PagedResources<Ticket> result =  pagedResourcesAssembler.toResource(tickets, assembler);
//            return ticketService.findTicketsByUserId(id, pageable) ;
//        }
//        return null;
    }

    @GetMapping("/tickets/{ticketId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CSR') or hasRole('ROLE_USER')")
    public Ticket getTicketDetails (@PathVariable("ticketId") Long ticketId, @PathVariable("id") long userId,
                                    @RequestHeader(value = "Authorization") String token) {

            return ticketService.findTicketById(ticketId, userId, token);

    }

    @GetMapping("/csr/assignTickets")
    @PreAuthorize("hasRole('ROLE_CSR')")
    public Page<Ticket> getTicketsByDepartment(@PathVariable("id") Long id, Pageable pageable,
                                               @RequestHeader(value = "Authorization") String token){
        return ticketService.getTicketsByCategory(id, userService.getCustomerServiceRepresentative(id).getDepartment(), INITIAL_STATUS, pageable,
                token);
    }

    @PatchMapping("/tickets/{ticketId}/resolveTicket")
    @PreAuthorize("hasRole('ROLE_CSR')")
    @ResponseStatus(HttpStatus.CREATED)
    public void resolveIssue(@PathVariable("ticketId") long ticketId, @PathVariable("id") long csrId,
                             @RequestHeader(value = "Authorization") String token) {

        ticketService.resolveIssue(ticketId, csrId, token);
    }

    @PatchMapping("/tickets/{ticketId}/status/{status}")
    @PreAuthorize("hasRole('ROLE_CSR')")
    @ResponseStatus(HttpStatus.CREATED)
    public void updateStatus(@PathVariable("ticketId") long ticketId, @PathVariable("status") String status, @PathVariable("id") long id,
                             @RequestHeader(value = "Authorization") String token){
        ticketService.updateStatus(status, ticketId, token, id);

    }

    @GetMapping("/csr/claimedTickets")
    @PreAuthorize("hasRole('ROLE_CSR')")
    public Page<Ticket> getClaimedTickets(@PathVariable("id") long csrId, Pageable pageable, @RequestHeader(value = "Authorization") String token){
        return ticketService.getClaimedTickets(csrId, pageable, token);
    }

    @GetMapping("/tickets/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Page<Ticket> getAllTickets(Pageable pageable) {
        return ticketService.getAllTickets(pageable);
    }

    //searching code

    @GetMapping("/tickets/search/title/{title}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CSR')")
    public Page<Ticket> getTicketsByTitle(@PathVariable("title") String title, Pageable pageable) {
        return ticketService.findTicketsByTitle(title, pageable);
    }

    @GetMapping("/tickets/search/status/{status}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CSR')")
    public Page<Ticket> getTicketsByStatus(@PathVariable("status") String status, Pageable pageable) {
        return ticketService.findTicketsByStatus(status, pageable);
    }

    @GetMapping("/tickets/search/user/title/{title}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN') or hasRole('ROLE_CSR')")
    public Page<Ticket> getTicketsByTitleAndUser(@PathVariable("id") long id, @PathVariable("title") String title,
                                                 @RequestHeader(value = "Authorization") String token, Pageable pageable){
        return ticketService.findTicketsByTitleAndUser(title, id, pageable, token);
    }

    @GetMapping("/tickets/search/user/status/{status}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN') or hasRole('ROLE_CSR')")
    public Page<Ticket> getTicketsByStatusAndUser(@PathVariable("id") long id, @PathVariable("status") String status,
                                                 @RequestHeader(value = "Authorization") String token, Pageable pageable){
        return ticketService.findTicketsByTitleAndUser(status, id, pageable, token);
    }

    @GetMapping("/tickets/search/dueDate/{dueDate}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CSR') or hasRole('ROLE_USER')")
    public Page<Ticket> getTicketsByDueDate(@PathVariable("dueDate") Timestamp dueDate, Pageable pageable) {
        return ticketService.findTicketsByDueDate(dueDate, pageable);
    }

    @GetMapping("/tickets/search/category/{category}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Page<Ticket> getTicketsByCategory(@PathVariable("category") String category, Pageable pageable) {
        return ticketService.findTicketsByCategory(category, pageable);
    }


    // Analytics code below

    @GetMapping("/ticktes/count")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public long getIsuueCount() {
        return ticketService.countAllTickets();
    }

    @GetMapping("/tickets/{category}/count")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public long getIssueByCategory(@PathVariable("category") String category ) {
        return ticketService.countTicketByCategory(category);
    }

    @GetMapping("/tickets/{category}/{status}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public long getIssueBycategoryAndStatus (@PathVariable("category") String category, @PathVariable("status") String status ) {
        return ticketService.countTicketByCategoryAndStatus(category, status);
    }
}
