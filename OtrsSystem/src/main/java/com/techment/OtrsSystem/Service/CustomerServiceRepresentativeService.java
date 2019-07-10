package com.techment.OtrsSystem.Service;

import com.techment.OtrsSystem.Repository.CustomerServiceRepresentativeRepository;
import com.techment.OtrsSystem.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceRepresentativeService {

    @Autowired
    private CustomerServiceRepresentativeRepository customerServiceRepresentativeRepository;
    public User getUserDetails (long id){
        return customerServiceRepresentativeRepository.findByUserId(id);
    }
}
