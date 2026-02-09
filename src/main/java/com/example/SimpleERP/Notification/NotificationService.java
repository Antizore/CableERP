package com.example.SimpleERP.Notification;


import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository){
        this.notificationRepository = notificationRepository;
    }

    public List<Notification> returnNotifications(boolean unreadOnly){
        return unreadOnly
                ? notificationRepository.findUnreadNotifications(NotificationStatus.UNREAD)
                : notificationRepository.findAll();
    }

    public void createAlert(String message) {
        String safeMessage = (message.length() > 500) ? message.substring(0, 500) : message;
        Notification notification = new Notification(
                NotificationCategory.OPTIMIZATION,
                NotificationStatus.UNREAD,
                Timestamp.from(Instant.now()),
                safeMessage
        );
        notificationRepository.save(notification);
        System.out.println(">>> [ALERT] " + safeMessage);
    }
}
