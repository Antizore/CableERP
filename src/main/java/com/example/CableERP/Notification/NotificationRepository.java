package com.example.CableERP.Notification;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query(
            "SELECT a FROM Notification a WHERE a.status = :status"
    )
    List<Notification> findUnreadNotifications(@Param("status") NotificationStatus status);

}
