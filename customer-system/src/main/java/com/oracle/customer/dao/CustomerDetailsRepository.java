package com.oracle.customer.dao;

import com.oracle.customer.entity.CustomerDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerDetailsRepository extends JpaRepository<CustomerDetailsEntity, Long> {

    CustomerDetailsEntity findByEmail(String email);

}
