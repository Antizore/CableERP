package com.example.CableERP.WorkOrder;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface WorkOrderRepository extends JpaRepository<WorkOrder, Long> {

    List<WorkOrder> findByStatus(WorkOrderStatus status);

    @Query(
            "SELECT MAX(w.plannedEndAt) FROM WorkOrder w"
    )
    Timestamp findLastPlanned();

}
