package simpleerp.customer.co;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findFirstByPlannedEndAtIsNotNullOrderByPlannedEndAtDesc();

    Order findFirstByStatusInAndPlannedStartAtAfterOrderByPlannedStartAtAsc(
            List<OrderStatus> statuses,
            Timestamp now
    );

    Order findFirstByStatus(OrderStatus orderStatus);

}
