package com.example.CableERP.Customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    /*
     In JPQL, you must use the entity name (Customer) as defined in your @Entity class, not the database table name (customer).
     */

    @Query("SELECT c FROM Customer c WHERE c.email = :email ")
    Customer findCustomerByEmail(@Param("email") String email);


}
