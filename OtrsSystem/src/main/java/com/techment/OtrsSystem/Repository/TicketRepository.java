package com.techment.OtrsSystem.Repository;

import com.techment.OtrsSystem.domain.CustomerServiceRepresentative;
import com.techment.OtrsSystem.domain.Ticket;
import com.techment.OtrsSystem.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(exported = false)
public interface TicketRepository extends PagingAndSortingRepository<Ticket, Long> {
    Page<Ticket> findByUser(Optional<User> user, Pageable pageable);
    Page<Ticket> findByCategoryAndStatus(String category, String status, Pageable pageable);

    @Modifying
    @Query(value = "update Ticket ticket set ticket.status =:status where ticket.id =:id")
    void updateTicketStatus(@Param("status") String status, @Param("id") long id);

    Page<Ticket> findByCustomerServiceRepresentative(CustomerServiceRepresentative customerServiceRepresentative, Pageable pageable);

    long countTicketByCategory(String category);

    long countTicketByCategoryAndStatus(String category, String status);
}
