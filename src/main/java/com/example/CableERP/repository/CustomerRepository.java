package com.example.CableERP.repository;

import com.example.CableERP.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("SELECT c FROM Customer c WHERE c.email = :email ")
    Customer findCustomerByEmail(@Param("email") String email);


}
