package com.techment.OtrsSystem.Repository;

import com.techment.OtrsSystem.domain.CustomerServiceRepresentative;
import com.techment.OtrsSystem.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(exported = false)
public interface UserRepository extends PagingAndSortingRepository<User, Long> {
    Optional<User> findByEmail(String email);
    CustomerServiceRepresentative findCustomerServiceRepresentativeById(long id);

    Page<User> findByEmployeeId(String employeeId, Pageable pageable);

    Page<User> findByFirstName(String firstName, Pageable pageable);

    Page<User> findByActivationStatus(String activationStatus, Pageable pageable);
}
