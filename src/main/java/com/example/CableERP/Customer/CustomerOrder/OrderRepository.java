package com.example.CableERP.Customer.CustomerOrder;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Order findFirstByPlannedEndAtIsNotNullOrderByPlannedEndAtDesc();

}
