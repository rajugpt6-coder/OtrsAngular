package com.techment.OtrsSystem.Repository;

import com.techment.OtrsSystem.domain.CustomerServiceRepresentative;
import com.techment.OtrsSystem.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(exported = false)
public interface CustomerServiceRepresentativeRepository extends CrudRepository<CustomerServiceRepresentative, Long> {
    List<CustomerServiceRepresentative> findByDepartment(String department);
    Optional<CustomerServiceRepresentative> findByUser(User user);
    User findByUserId(long id);
}
