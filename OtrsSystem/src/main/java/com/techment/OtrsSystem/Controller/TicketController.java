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
    public Optional<Ticket> raiseTicket(@RequestBody @Valid TicketDto ticketDto, @PathVariable("id") Long id){

        return Optional.ofNullable(ticketService.createTicket(ticketDto.getCategory(), ticketDto.getDescription(), INITIAL_STATUS, ticketDto.getTitle(), id));

    }

    @GetMapping("/{email}/tickets")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CSR') or hasRole('ROLE_USER')")
    public Page<Ticket> getTickets(@PathVariable("id") Long id, @PathVariable("email") String email, Pageable pageable
                                             ){
        if(userService.findUserById(id).get().getEmail().equalsIgnoreCase(email)) {
//            Page<Ticket> tickets = ticketService.findTicketsByUserId(id, pageable);
//            PagedResources<Ticket> result =  pagedResourcesAssembler.toResource(tickets, assembler);
            return ticketService.findTicketsByUserId(id, pageable) ;
        }
        return null;
    }

    @GetMapping("/{email}/tickets/{ticketId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CSR') or hasRole('ROLE_USER')")
    public Ticket getTicketDetails (@PathVariable("ticketId") Long ticketId, @PathVariable("id") long id, @PathVariable("email") String email) {
        if(userService.findUserById(id).get().getEmail().equalsIgnoreCase(email)) {
            return ticketService.findTicketById(ticketId);
        }
        return null;
    }

    @GetMapping("/csr/assignTickets")
    @PreAuthorize("hasRole('ROLE_CSR')")
    public Page<Ticket> getTicketsByDepartment(@PathVariable("id") Long id, Pageable pageable){
        return ticketService.getTicketsByCategory(userService.getCustomerServiceRepresentative(id).getDepartment(), INITIAL_STATUS, pageable);
    }

    @PatchMapping("/tickets/{ticketId}/resolveTicket")
    @PreAuthorize("hasRole('ROLE_CSR')")
    @ResponseStatus(HttpStatus.CREATED)
    public void resolveIssue(@PathVariable("ticketId") Long id, @RequestBody @Validated TicketDto ticketDto) {
        ticketService.resolveIssue(id, ticketDto.getUserId());
    }

    @PatchMapping("/tickets/{ticketId}/status/{status}")
    @PreAuthorize("hasRole('ROLE_CSR')")
    @ResponseStatus(HttpStatus.CREATED)
    public void updateStatus(@PathVariable("ticketId") long id, @PathVariable("status") String status){
        ticketService.updateStatus(status, id);

    }

    @GetMapping("/csr/claimedTickets")
    @PreAuthorize("hasRole('ROLE_CSR')")
    public Page<Ticket> getClaimedTickets(@PathVariable("id") long csrId, Pageable pageable){
        return ticketService.getClaimedTickets(csrId, pageable);
    }

    @GetMapping("/tickets/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Page<Ticket> getAllTickets(Pageable pageable) {
        return ticketService.getAllTickets(pageable);
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
